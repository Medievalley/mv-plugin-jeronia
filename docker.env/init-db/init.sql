-- DROP TABLE IF EXISTS users CASCADE;
-- DROP TABLE IF EXISTS player_data;
-- DROP TABLE IF EXISTS roles;
-- DROP TABLE IF EXISTS structures;
-- DROP TABLE IF EXISTS structure_types;

CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	username VARCHAR (20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR (30) NOT NULL,
    lastip VARCHAR (30) NOT NUll,
    verified BOOLEAN DEFAULT false NOT NULL
);

CREATE TABLE IF NOT EXISTS roles (
    id INTEGER PRIMARY KEY,
    name VARCHAR (30) UNIQUE NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS player_data (
	userid INTEGER references users (id) ON DELETE CASCADE PRIMARY KEY,
	roleid INTEGER references roles (id) ON DELETE SET NULL,
    lives INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS struct_types (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS structures (
    id SERIAL PRIMARY KEY,
    ownerid INTEGER references users(id) ON DELETE SET NULL,
    typeid INTEGER references struct_types(id) ON DELETE SET NULL ON UPDATE CASCADE,
    destructible BOOLEAN DEFAULT false NOT NULL,
    world VARCHAR(255) NOT NULL,
    x1 INTEGER NOT NULL,
    y1 INTEGER NOT NULL,
    z1 INTEGER NOT NULL,
    x2 INTEGER NOT NULL,
    y2 INTEGER NOT NULL,
    z2 INTEGER NOT NULL
);

-- MOCK SCRIPT

-- INSERT INTO roles VALUES (1, 'Admin', 'Most privileged role');
-- with rows as (
--     insert into users(
--         username, 
--         email, 
--         password, 
--         lastip) 
--     values(
--         'Smarkatch',
--         'shrigorevich@gmail.com',
--         'password',
--         '127.0.0.1'
--     )
--     returning id
-- )
-- insert into player_data (userid, roleid) select id, 1 from rows;