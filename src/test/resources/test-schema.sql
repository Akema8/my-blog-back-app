CREATE TABLE if not exists posts (
                                     id INT PRIMARY KEY AUTO_INCREMENT,
                                     title VARCHAR(255) NOT NULL,
    text TEXT,
    tags TEXT,
    likesCount INT DEFAULT 0,
    commentsCount INT DEFAULT 0
    );

CREATE TABLE if not exists comments (
                                        id INT PRIMARY KEY AUTO_INCREMENT,
                                        text TEXT,
                                        postId INT NOT NULL
                                        -- CONSTRAINT fk_post FOREIGN KEY (postId) REFERENCES posts(id)
    );