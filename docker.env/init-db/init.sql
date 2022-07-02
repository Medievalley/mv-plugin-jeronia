CREATE TABLE users (
	id SERIAL PRIMARY KEY,
	username VARCHAR (20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR (30) NOT NULL,
    last_ip VARCHAR (20) NOT NUll,
    confirmed BOOLEAN NOT NULL
);

CREATE TABLE roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR (20) NOT NULL,
    description TEXT,
);

CREATE TABLE user_data (
	user_id INTEGER references users (id) UNIQUE,
	role_id INTEGER references roles (id) NOT NULL,
    lives_number INTEGER
);

INSERT INTO roles VALUES (1, 'Admin', 'Most privileged role');