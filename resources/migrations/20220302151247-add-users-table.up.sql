CREATE TABLE users
(user_id SERIAL PRIMARY KEY,
 email VARCHAR(100) not null,
 password VARCHAR(100) not null,
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
--;;
INSERT INTO users
(email,password,first_name,last_name)
VALUES ('a@b.com','pass','Amanda','Hugnkiss')
