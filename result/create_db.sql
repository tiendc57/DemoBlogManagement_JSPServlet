CREATE DATABASE BlogManagement;

use BlogManagement;

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE blogs (
    id VARCHAR(36) PRIMARY KEY,
    category VARCHAR(100),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(36) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (id, name, username, password, email) VALUES
(UUID(), 'John Doe', 'johndoe', 'password123', 'john.doe@example.com'),
(UUID(), 'Jane Smith', 'janesmith', 'password456', 'jane.smith@example.com'),
(UUID(), 'Alice Johnson', 'alicej', 'password789', 'alice.johnson@example.com');

INSERT INTO blogs (id, category, title, content, author_id, created_at) VALUES
(UUID(), 'EDU', 'The Rise of AI', 'Content about AI...', 
    (SELECT id FROM users WHERE username = 'johndoe'), NOW()),
(UUID(), 'EDU', '10 Tips for a Healthy Life', 'Content about health tips...', 
    (SELECT id FROM users WHERE username = 'janesmith'), NOW()),
(UUID(), 'EDU', 'Traveling the World', 'Content about traveling...', 
    (SELECT id FROM users WHERE username = 'alicej'), NOW()),
(UUID(), 'SPORT', 'The Importance of Learning', 'Content about education...', 
    (SELECT id FROM users WHERE username = 'johndoe'), NOW());
