CREATE TABLE IF NOT EXISTS users (
	id VARCHAR(150) PRIMARY KEY,
	login VARCHAR (20) UNIQUE NOT NULL,
    ip VARCHAR (30) NOT NUll,
    verified BOOLEAN DEFAULT false NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY,
    name VARCHAR (30) UNIQUE NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS player_data (
    id SERIAL PRIMARY KEY,
	user_id VARCHAR(150) references users (id) ON DELETE CASCADE ON UPDATE CASCADE,
	role_id INTEGER references role (id) ON DELETE SET NULL,
    lives INTEGER DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS volume (
    id SERIAL PRIMARY KEY,
    size_x INTEGER NOT NULL,
    size_y INTEGER NOT NULL,
    size_z INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS volume_block (
    id SERIAL PRIMARY KEY,
    volume_id INTEGER REFERENCES volume(id) ON DELETE CASCADE ON UPDATE CASCADE,
    material VARCHAR(70) NOT NULL,
    block_data TEXT NOT NULL,
    x INTEGER,
    y INTEGER,
    z INTEGER
);

CREATE TABLE IF NOT EXISTS struct_type (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS location (
    id SERIAL PRIMARY KEY,
    x integer NOT NULL,
    y integer NOT NULL,
    z integer NOT NULL
);

CREATE TABLE IF NOT EXISTS struct (
    id SERIAL PRIMARY KEY,
    type_id INTEGER references struct_type(id) ON DELETE SET NULL ON UPDATE CASCADE,
    world VARCHAR(255) NOT NULL,
    volume_id integer references volume(id) ON DELETE SET NULL ON UPDATE CASCADE,
    name varchar(100),
    priority INTEGER UNIQUE,
    deposit INTEGER DEFAULT 0 NOT NULL,
    resources INTEGER DEFAULT 0 NOT NULL,
    x1 INTEGER NOT NULL,
    y1 INTEGER NOT NULL,
    z1 INTEGER NOT NULL,
    x2 INTEGER NOT NULL,
    y2 INTEGER NOT NULL,
    z2 INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS struct_block_type (
    id INTEGER PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS struct_block (
    id SERIAL PRIMARY KEY,
    struct_id INTEGER references struct (id) ON DELETE CASCADE ON UPDATE CASCADE,
    volume_block_id INTEGER references volume_block (id) ON DELETE CASCADE ON UPDATE CASCADE,
    type_id INTEGER references struct_block_type (id) ON DELETE SET NULL ON UPDATE CASCADE DEFAULT 1,
    hp_trigger BOOLEAN DEFAULT true,
    broken BOOLEAN DEFAULT false NOT NULL
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

CREATE TABLE IF NOT EXISTS kill_stats (
	id serial PRIMARY KEY,
	user_id VARCHAR(150) references users (id) ON DELETE CASCADE ON UPDATE CASCADE,
	entity_type VARCHAR (30) NULL,
    kills integer NOT NUll DEFAULT 1,
    UNIQUE (user_id, entity_type)
);

CREATE TABLE IF NOT EXISTS death_stats (
	id serial PRIMARY KEY,
	user_id VARCHAR(150) references users (id) ON DELETE CASCADE ON UPDATE CASCADE,
	reason VARCHAR (150) NULL,
    deaths integer NOT NUll DEFAULT 1,
    UNIQUE (user_id, reason)
);

--CREATE UNIQUE INDEX vol_block_idx ON volume_block (volume_id, x, y, z);

INSERT INTO role (id, name, description)
VALUES (1, 'Admin', 'Most privileged role'),
(2, 'Moder', 'Game server moderator'),
(3, 'Player', 'Default player');

INSERT INTO struct_type (id, name, description) VALUES
(1, 'main', 'Main lore structure'),
(2, 'agronomic', 'Food structure'),
(4, 'private', 'player`s private structure'),
(3, 'abode', 'Structure - adobe of monsters');

INSERT INTO npc_role (id, name, description) VALUES
(1, 'harvester', 'A villager who harvests'),
(2, 'warden', 'The main person in the village'),
(3, 'builder', 'A villager who builds and repairs structures');

INSERT INTO struct_block_type (id, name, description) VALUES
(1, 'Default', 'block without special behavior'),
(2, 'Safe location', 'Safe location for npc'),
(3, 'Abode spawn', 'Block where monsters spawn in the abode');

-- MOCK SCRIPT
 with rows as (
     insert into users(
         id,
         login,
         ip,
         verified)
     values(
         'lsadfsladfb',
         'Smarkatch',
         '127.0.0.1',
         true
     )
     returning id
 )
 insert into player_data (user_id, role_id, lives) select id, 1, 4 from rows;
