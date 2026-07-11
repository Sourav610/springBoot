package com.notification.EventNotification.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.EventNotification.dto.ParseRemainderDTO;
import com.notification.EventNotification.util.impl.RemainderParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

@Service
public class GeminiRemainderParser {

    private static final Logger log = LoggerFactory.getLogger(GeminiRemainderParser.class);

    private static final Set<String> VALID_CATEGORIES = Set.of("bills", "health", "work", "personal", "other");
    private static final Set<String> VALID_PRIORITIES = Set.of("low", "medium", "high");
    private static final Set<String> VALID_RECURRENCE = Set.of("daily", "weekly", "monthly", "yearly");
    private static final Set<String> VALID_CONFIDENCE = Set.of("high", "medium", "low");

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final PromptLoader promptLoader;

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.base-url}")
    private String baseUrl;


    public GeminiRemainderParser(WebClient.Builder webClient, ObjectMapper objectMapper, PromptLoader promptLoader) {
        this.webClient = webClient.build();
        this.objectMapper = objectMapper;
        this.promptLoader = promptLoader;
    }

    public ParseRemainderDTO parseReminder(String userInput) {
        if (userInput == null || userInput.isBlank()) {
            throw new RemainderParserException("Input text cannot be empty");
        }

        String prompt = buildPromptFromTemplate(userInput);
        String rawResponse = callGemini(prompt);
        String cleanedJson = stripMarkdownFences(rawResponse);
        ParseRemainderDTO parsed = parseJson(cleanedJson);
        validate(parsed);

        return parsed;
    }

    private String buildPromptFromTemplate(String userInput) {
        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String currentDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);

        return promptLoader.getTemplate()
                .replace("{{CURRENT_DATE}}", currentDate)
                .replace("{{DAY_OF_WEEK}}", dayOfWeek)
                .replace("{{USER_INPUT}}", userInput.replace("\"", "\\\"")); // escape quotes in user text
    }

    private String callGemini(String prompt) {
        String requestBody = buildGeminiRequestBody(prompt);

        try {
            JsonNode response = webClient.post()
                    .uri(baseUrl + "?key=" + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .timeout(Duration.ofSeconds(15))
                    .onErrorResume(e -> {
                        log.error("Gemini API call failed: {}", e.getMessage());
                        return Mono.error(new RemainderParserException("AI service unavailable, please try again", e));
                    })
                    .block();

            return extractTextFromResponse(response);

        } catch (RemainderParserException e) {
            throw e;
        } catch (Exception e) {
            throw new RemainderParserException("Unexpected error calling Gemini API", e);
        }
    }

    private String buildGeminiRequestBody(String prompt) {
        try {
            var root = objectMapper.createObjectNode();
            var contents = root.putArray("contents");
            var content = contents.addObject();
            var parts = content.putArray("parts");
            parts.addObject().put("text", prompt);

            var generationConfig = root.putObject("generationConfig");
            generationConfig.put("temperature", 0.1);
            generationConfig.put("maxOutputTokens", 500);

            return objectMapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new RemainderParserException("Failed to build Gemini request", e);
        }
    }

    private String extractTextFromResponse(JsonNode response) {
        try {
            return response
                    .path("candidates").get(0)
                    .path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            log.error("Unexpected Gemini response shape: {}", response);
            throw new RemainderParserException("Could not extract text from Gemini response", e);
        }
    }

    private String stripMarkdownFences(String raw) {
        String cleaned = raw.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceFirst("^```(json)?", "").trim();
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.lastIndexOf("```")).trim();
        }
        return cleaned;
    }

    private ParseRemainderDTO parseJson(String json) {
        try {
            return objectMapper.readValue(json, ParseRemainderDTO.class);
        } catch (Exception e) {
            log.error("Failed to parse Gemini JSON output: {}", json);
            throw new RemainderParserException("AI returned invalid JSON, please rephrase your reminder", e);
        }
    }

    private void validate(ParseRemainderDTO dto) {
        // Category
        if (dto.getCategory() == null || !VALID_CATEGORIES.contains(dto.getCategory().toLowerCase())) {
            dto.setCategory("other");
        }

        // Priority
        if (dto.getPriority() == null || !VALID_PRIORITIES.contains(dto.getPriority().toLowerCase())) {
            dto.setPriority("medium");
        }

        // Confidence
        if (dto.getConfidence() == null || !VALID_CONFIDENCE.contains(dto.getConfidence().toLowerCase())) {
            dto.setConfidence("low");
        }

        // Recurrence — nullable, but if present must be valid
        if (dto.getRecurrence() != null && !VALID_RECURRENCE.contains(dto.getRecurrence().toLowerCase())) {
            dto.setRecurrence(null);
        }

        // Date validation
        if (dto.getDate() != null) {
            try {
                LocalDate parsedDate = LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
                if (parsedDate.isBefore(LocalDate.now())) {
                    log.warn("Gemini returned a past date: {}. Forcing low confidence.", dto.getDate());
                    dto.setConfidence("low");
                    dto.setNotes(appendNote(dto.getNotes(), "AI suggested a past date — please confirm."));
                }
            } catch (Exception e) {
                log.warn("Gemini returned an unparseable date: {}", dto.getDate());
                dto.setDate(null);
                dto.setConfidence("low");
                dto.setNotes(appendNote(dto.getNotes(), "Could not parse date — please set manually."));
            }
        } else {
            // No date at all — force low confidence so UI blocks auto-save
            dto.setConfidence("low");
        }

        // Time format sanity check (HH:MM)
        if (dto.getTime() != null && !dto.getTime().matches("^([01]\\d|2[0-3]):[0-5]\\d$")) {
            dto.setTime(null);
        }

        // Title fallback
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            dto.setTitle("Untitled reminder");
        }
    }

    private String appendNote(String existing, String addition) {
        if (existing == null || existing.isBlank()) return addition;
        return existing + " " + addition;
    }
}