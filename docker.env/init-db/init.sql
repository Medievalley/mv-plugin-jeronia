CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	username VARCHAR (20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR (30) NOT NULL,
    lastIp VARCHAR (20) NOT NUll,
    confirmed BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR (20) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS user_data (
	userId INTEGER references users (id) UNIQUE,
	roleId INTEGER references roles (id) NOT NULL,
    livesNumber INTEGER
);

INSERT INTO roles VALUES (1, 'Admin', 'Most privileged role');