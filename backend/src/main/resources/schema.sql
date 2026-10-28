DROP TABLE IF EXISTS repair_logs;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS repair_orders;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS equipment;
DROP TABLE IF EXISTS labs;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE labs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(200),
    description VARCHAR(500)
);

CREATE TABLE equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lab_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    model VARCHAR(100),
    category VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    purchase_date DATE
);

CREATE TABLE reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    purpose VARCHAR(300),
    status VARCHAR(20) NOT NULL,
    reject_reason VARCHAR(300),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE repair_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    equipment_id BIGINT NOT NULL,
    technician_id BIGINT,
    urgency VARCHAR(20) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    status VARCHAR(20) NOT NULL,
    result VARCHAR(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100),
    content VARCHAR(500),
    is_read BOOLEAN DEFAULT FALSE,
    related_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE repair_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    repair_id BIGINT NOT NULL,
    from_status VARCHAR(20),
    to_status VARCHAR(20),
    operator_id BIGINT,
    note VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
