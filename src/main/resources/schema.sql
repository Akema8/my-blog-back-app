CREATE TABLE if not exists posts (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       text TEXT,
                       tags TEXT,
                       likesCount INT DEFAULT 0,
                       commentsCount INT DEFAULT 0
);

INSERT INTO posts (title, text, tags, likesCount, commentsCount) VALUES
( 'Название поста 1', 'Текст поста1...', 'tag_1,tag_2', 5, 3),
( 'Название поста 2', 'Текст поста2...', '', 1, 1);




CREATE TABLE if not exists comments (
    id INT PRIMARY KEY AUTO_INCREMENT,
    text TEXT,
    postId INT NOT NULL,
    CONSTRAINT fk_post FOREIGN KEY (postId) REFERENCES posts(id)
);

INSERT INTO comments (text, postId) VALUES
( 'комментарий 1', 1),
( 'комментарий 2', 1),
( 'комментарий 3', 1),
( 'комментарий 1', 2);