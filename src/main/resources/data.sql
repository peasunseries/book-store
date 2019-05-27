DROP TABLE IF EXISTS USERS;
CREATE TABLE USERS (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  password CHAR(100) NOT NULL,
  date_of_birth DATE NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX (username)
);

DROP TABLE IF EXISTS ORDERS;
CREATE TABLE ORDERS (
  id INT NOT NULL AUTO_INCREMENT,
  user_id INT NOT NULL,
  book_id INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (user_id, book_id),
  FOREIGN KEY (user_id) REFERENCES USERS(id)
);

INSERT INTO USERS (id, first_name, last_name, username, password, date_of_birth) VALUES (1, 'Chiwa','Kantawong', 'chiwa','$2a$10$D02XjIOKVZOFV5kL6aezXubWwkseex9uQZAiYLUFcUCZ3CPfiGS32', '1978-01-16');
INSERT INTO USERS (id, first_name, last_name, username, password, date_of_birth)  VALUES (2, 'Jirapa', 'Kantawong', 'jirapa', '$2a$10$D02XjIOKVZOFV5kL6aezXubWwkseex9uQZAiYLUFcUCZ3CPfiGS32', '1978-01-16');
INSERT INTO USERS (id, first_name, last_name, username, password, date_of_birth)  VALUES (3, 'Sura', 'Jiranan', 'sura', '$2a$10$D02XjIOKVZOFV5kL6aezXubWwkseex9uQZAiYLUFcUCZ3CPfiGS32', '1978-01-16');

INSERT INTO ORDERS (id, user_id, book_id) VALUES (1, 1, 1);
INSERT INTO ORDERS (id, user_id, book_id) VALUES (2, 1, 2);
INSERT INTO ORDERS (id, user_id, book_id) VALUES (3, 1, 3);