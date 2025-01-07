CREATE DATABASE BlogManagement;

use BlogManagement;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE blogs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    author_id INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL,
    FOREIGN KEY (author_id) REFERENCES User(id) ON DELETE CASCADE
);

INSERT INTO users (name, username, password, email) VALUES
('John Doe', 'johndoe', 'password123', 'john.doe@example.com'),
('Jane Smith', 'janesmith', 'password456', 'jane.smith@example.com'),
('Alice Johnson', 'alicej', 'password789', 'alice.johnson@example.com');

INSERT INTO blogs (category, title, content, author_id, created_at) VALUES
('Technology', 'The Rise of AI', 'Content about AI...', 1, NOW()),
('Health', '10 Tips for a Healthy Life', 'Content about health tips...', 2, NOW()),
('Lifestyle', 'Traveling the World', 'Content about traveling...', 3, NOW()),
('Education', 'The Importance of Learning', 'Content about education...', 1, NOW());
