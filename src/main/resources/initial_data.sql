-- Dodawanie użytkowników
CREATE TABLE IF NOT EXISTS users (id SERIAL, username VARCHAR, email VARCHAR, password VARCHAR );
INSERT INTO users (username, email, password) VALUES
                                                  ('user1', 'user1@example.com', 'password1'),
                                                  ('user2', 'user2@example.com', 'password2'),
                                                  ('user3', 'user3@example.com', 'password3');

-- Dodawanie albumów ze zdjęciami
CREATE TABLE IF NOT EXISTS albums (user_id INT, title VARCHAR );
INSERT INTO albums (user_id, title) VALUES
                                        (1, 'Wakacje 2023'),
                                        (2, 'Impreza urodzinowa'),
                                        (3, 'Podróże po Europie');

-- Dodawanie zdjęć do albumów
CREATE TABLE IF NOT EXISTS photos (album_id INT, title VARCHAR, url VARCHAR );
INSERT INTO photos (album_id, title, url) VALUES
                                              (1, 'Plaża', 'https://example.com/beach.jpg'),
                                              (1, 'Góry', 'https://example.com/mountains.jpg'),
                                              (2, 'Tort urodzinowy', 'https://example.com/birthday_cake.jpg'),
                                              (3, 'Paryż', 'https://example.com/paris.jpg');

-- Dodawanie znajomych
CREATE TABLE IF NOT EXISTS friendships (user_id INT, friend_id INT );
INSERT INTO friendships (user_id, friend_id) VALUES
                                                 (1, 2),
                                                 (1, 3),
                                                 (2, 1),
                                                 (2, 3),
                                                 (3, 1),
                                                 (3, 2);

-- Dodawanie polubień zdjęć
CREATE TABLE IF NOT EXISTS likes (user_id INT, photo_id INT );
INSERT INTO likes (user_id, photo_id) VALUES
                                          (1, 1),
                                          (1, 2),
                                          (2, 3),
                                          (3, 4);

-- Usuwanie polubień zdjęć
DELETE FROM likes WHERE user_id = 1 AND photo_id = 1;

-- Usuwanie albumów ze zdjęciami
DELETE FROM photos WHERE album_id = 1;
DELETE FROM albums WHERE user_id = 1;
