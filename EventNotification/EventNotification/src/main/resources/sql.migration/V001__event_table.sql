CREATE TABLE IF NOT EXISTS event_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    event_date DATE,
    person_name VARCHAR(256),
    event_type VARCHAR(128),
    mobile_number VARCHAR(15),
    created_on DATETIME,
    updated_on DATETIME,
    email VARCHAR(255),
    user_id INT,
    event_title VARCHAR(128),
    event_message MEDIUMTEXT
);

CREATE TABLE IF NOT EXISTS notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    created_on DATETIME,
    created_by VARCHAR(64),
    updated_on DATETIME,
    alert_limit INT,
    event_details_id INT,
    FOREIGN KEY (event_details_id) REFERENCES event_details(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_details_entity(
    id INT PRIMARY KEY AUTO_INCREMENT,
    created_on DATETIME,
    created_by VARCHAR(64),
    updated_on DATETIME,
    full_name VARCHAR(255),
    user_email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    login_time DATETIME,
    logout_time DATETIME
);
