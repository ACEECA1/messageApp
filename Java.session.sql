-- @block
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    displayname VARCHAR(100) NOT NULL,
    Username VARCHAR(50) NOT NULL UNIQUE,
    Email VARCHAR(100) NOT NULL UNIQUE,
    Password VARCHAR(255) NOT NULL,
    profilePic VARCHAR(255),
);

--@block
SELECT * FROM Users;

-- @block
INSERT INTO Users (displayname, Username, Email, Password) VALUES
('John Doe', 'johndoe', 'john.doe@example.com', 'password123'),
('Jane Smith', 'janesmith', 'jane.smith@example.com', 'password456'),
('Alice Johnson', 'alicej', 'alice.johnson@example.com', 'password789');

-- @block
DROP TABLE Users;
-- @block
DELETE FROM Users WHERE NOT (UserID = -1);


--@block
CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(50) NOT NULL,          -- username of sender
    receiver VARCHAR(50) NOT NULL,        -- username of receiver
    message TEXT NOT NULL,                 -- the actual message
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- when the message was sent
    is_read BOOLEAN DEFAULT FALSE          -- optional: whether the receiver has read it
);
-- @block
CREATE INDEX idx_sender_receiver ON messages(sender, receiver);
CREATE INDEX idx_receiver_timestamp ON messages(receiver, timestamp);

--@block
DELETE FROM messages WHERE NOT (id = -1);

-- @block
SELECT * FROM messages;