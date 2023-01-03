CREATE TABLE IF NOT EXISTS users (
	id SERIAL PRIMARY KEY,
	username VARCHAR (20) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR (30) NOT NULL,
    ip VARCHAR (30) NOT NUll,
    verified BOOLEAN DEFAULT false NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY,
    name VARCHAR (30) UNIQUE NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS player_data (
	user_id INTEGER references users (id) ON DELETE CASCADE PRIMARY KEY,
	role_id INTEGER references role (id) ON DELETE SET NULL,
    lives INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS volume (
    id SERIAL PRIMARY KEY,
    size_x INTEGER NOT NULL,
    size_y INTEGER NOT NULL,
    size_z INTEGER NOT NULL,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS volume_block (
    id SERIAL PRIMARY KEY,
    volume_id INTEGER REFERENCES volume(id) ON DELETE CASCADE ON UPDATE CASCADE,
    type VARCHAR(255) NOT NULL,
    block_data TEXT NOT NULL,
    x INTEGER,
    y INTEGER,
    z INTEGER
);

CREATE UNIQUE INDEX vol_block_idx ON volume_block (volume_id, x, y, z);

CREATE TABLE IF NOT EXISTS struct_type (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS struct (
    id SERIAL PRIMARY KEY,
    type_id INTEGER references struct_type(id) ON DELETE SET NULL ON UPDATE CASCADE,
    destructible BOOLEAN DEFAULT false NOT NULL,
    world VARCHAR(255) NOT NULL,
    x1 INTEGER NOT NULL,
    y1 INTEGER NOT NULL,
    z1 INTEGER NOT NULL,
    x2 INTEGER NOT NULL,
    y2 INTEGER NOT NULL,
    z2 INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS lore_struct (
    id SERIAL PRIMARY KEY,
    struct_id INTEGER references struct(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    volume_id integer references volume(id) ON DELETE SET NULL ON UPDATE CASCADE,
    name varchar(100) NOT NULL,
    stock INTEGER DEFAULT 0 NOT NULL,
    priority INTEGER UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS private_struct (
    id SERIAL PRIMARY KEY,
    struct_id INTEGER references struct(id) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL,
    volume_id integer references volume(id) ON DELETE SET NULL ON UPDATE CASCADE,
    owner_id INTEGER references users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS struct_block (
    id SERIAL PRIMARY KEY,
    struct_id INTEGER references struct (id) ON DELETE CASCADE ON UPDATE CASCADE,
    volume_block_id INTEGER references volume_block (id) ON DELETE CASCADE ON UPDATE CASCADE,
    trigger_destruction BOOLEAN DEFAULT true,
    broken BOOLEAN DEFAULT false NOT NULL
);

CREATE TABLE IF NOT EXISTS location (
    id SERIAL PRIMARY KEY,
    struct_id INTEGER references struct (id) ON DELETE CASCADE ON UPDATE CASCADE,
    x integer NOT NULL,
    y integer NOT NULL,
    z integer NOT NULL
);

CREATE TABLE IF NOT EXISTS npc_role (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT NOT NULL
);

CREATE table IF NOT EXISTS struct_npc (
    id SERIAL PRIMARY KEY,
    role_id INTEGER references npc_role(id) ON DELETE SET NULL ON UPDATE CASCADE NOT NULL,
    spawn INTEGER references location(id) ON DELETE SET NULL ON UPDATE CASCADE NOT NULL,
    work INTEGER references location(id) ON DELETE SET NULL ON UPDATE CASCADE NOT NULL,
    name VARCHAR(30) NOT NULL,
    struct_id INTEGER references struct (id) ON DELETE CASCADE ON UPDATE CASCADE,
    alive BOOLEAN DEFAULT true NOT NULL
);

CREATE TABLE IF NOT EXISTS storage (
    id SERIAL PRIMARY KEY,
    deposit INTEGER DEFAULT 0 NOT NULL,
    resources INTEGER DEFAULT 0 NOT NULL
);

INSERT INTO role VALUES (1, 'Admin', 'Most privileged role');

INSERT INTO struct_type (id, name, description) VALUES
(1, 'private', 'Private territory that each user can own'),
(2, 'lore', 'Lore structure that normally cannot be destroyed');
(3, 'abode', 'Structure - adobe of monsters');

INSERT INTO npc_role (id, name, description) VALUES
(1, 'harvester', 'A villager who harvests'),
(2, 'warden', 'The main person in the village'),
(3, 'builder', 'A villager who builds and repairs structures');

INSERT INTO storage (deposit, resources) VALUES (100, 3);

-- MOCK SCRIPT
 with rows as (
     insert into users(
         username,
         email,
         password,
         ip,
         verified)
     values(
         'Smarkatch',
         'shrigorevich@gmail.com',
         'password',
         '127.0.0.1',
         true
     )
     returning id
 )
 insert into player_data (user_id, role_id, lives) select id, 1, 4 from rows;
