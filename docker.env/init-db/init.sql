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
    name varchar(100) NOT NULL,
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

CREATE TABLE IF NOT EXISTS volumes (
    id SERIAL PRIMARY KEY,
    size_x INTEGER NOT NULL,
    size_y INTEGER NOT NULL,
    size_z INTEGER NOT NULL,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS volume_blocks (
    id SERIAL PRIMARY KEY,
    volume_id INTEGER REFERENCES volumes(id) ON DELETE CASCADE ON UPDATE CASCADE,
    type VARCHAR(255) NOT NULL,
    x INTEGER,
    y INTEGER,
    z INTEGER
);

CREATE UNIQUE INDEX vol_block_idx ON volume_blocks (volume_id, x, y, z);

INSERT INTO roles VALUES (1, 'Admin', 'Most privileged role');
INSERT INTO struct_types (id, name, description)
VALUES (0, 'default', 'default structure')
VALUES (1, 'private', 'Private territory that each user can own')
VALUES (2, 'lore', 'Lore structure that normally cannot be destroyed');

-- MOCK SCRIPT

-- with rows as (
--     insert into users(
--         username, 
--         email, 
--         password, 
--         lastip,
--         verified)
--     values(
--         'Smarkatch',
--         'shrigorevich@gmail.com',
--         'password',
--         '127.0.0.1',
--         false
--     )
--     returning id
-- )
-- insert into player_data (userid, roleid, lives) select id, 1, 4 from rows;
