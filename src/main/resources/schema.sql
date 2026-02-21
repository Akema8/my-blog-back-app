CREATE TABLE if not exists posts (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       text TEXT,
                       tags TEXT,
                       likesCount INT DEFAULT 0,
                       commentsCount INT DEFAULT 0
);

INSERT INTO posts (title, text, tags, likesCount, commentsCount) VALUES
( 'Название поста 1', 'Текст поста1...', 'tag_1,tag_2', 5, 1),
( 'Название поста 2', 'Текст поста2...', '', 1, 5);