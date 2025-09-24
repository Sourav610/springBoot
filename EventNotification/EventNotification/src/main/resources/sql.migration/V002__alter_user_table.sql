ALTER TABLE user_details_entity ADD CONSTRAINT uq_email UNIQUE (email);
ALTER TABLE user_details_entity MODIFY COLUMN is_online BOOLEAN;
