package com.notification.EventNotification.service.impl;


import com.notification.EventNotification.util.impl.RemainderParserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class PromptLoader {

    @Value("${gemini.prompt-path}")
    private String promptPath;

    private final ResourcePatternResolver resourceResolver;
    private String promptTemplate;

    public PromptLoader(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
    }

    @PostConstruct
    public void loadPrompt() {
        try {
            Resource resource = resourceResolver.getResource("classpath:" + promptPath);
            byte[] bytes = resource.getInputStream().readAllBytes();
            this.promptTemplate = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load prompt file at " + promptPath, e);
        }
    }

    public String getTemplate() {
        if (promptTemplate == null) {
            throw new RemainderParserException("Prompt template not loaded");
        }
        return promptTemplate;
    }
}
