CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

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
	entity_type VARCHAR (30) NOT NULL,
    kills integer NOT NUll DEFAULT 1,
    UNIQUE (user_id, entity_type)
);

CREATE TABLE IF NOT EXISTS death_stats (
	id serial PRIMARY KEY,
	user_id VARCHAR(150) references users (id) ON DELETE CASCADE ON UPDATE CASCADE,
	reason VARCHAR (150) NOT NULL,
    deaths integer NOT NUll DEFAULT 1,
    UNIQUE (user_id, reason)
);

CREATE TABLE IF NOT EXISTS job_type (
    id INTEGER PRIMARY KEY,
	name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_job (
	user_id VARCHAR(150) references users (id) ON DELETE CASCADE ON UPDATE CASCADE,
	job_id INTEGER references job_type (id) ON DELETE CASCADE ON UPDATE CASCADE,
	level INTEGER DEFAULT 1,
	PRIMARY KEY (user_id, job_id)
);

CREATE TABLE IF NOT EXISTS restricted_item (
    id INTEGER PRIMARY KEY,
	type text NOT NULL
);

CREATE TABLE IF NOT EXISTS job_item_allowance (
	id SERIAL PRIMARY KEY,
    job_id INTEGER references job_type (id) ON DELETE CASCADE ON UPDATE CASCADE,
	item_id INTEGER references restricted_item (id) ON DELETE CASCADE ON UPDATE CASCADE,
	level INTEGER NOT NULL DEFAULT 1,
	UNIQUE (job_id, item_id)
);

CREATE TABLE IF NOT EXISTS role_item_allowance (
	id SERIAL PRIMARY KEY,
    role_id INTEGER references role (id) ON DELETE CASCADE ON UPDATE CASCADE,
	item_id INTEGER references restricted_item (id) ON DELETE CASCADE ON UPDATE CASCADE,
	UNIQUE (role_id, item_id)
);

CREATE TABLE IF NOT EXISTS departments
(
	id int PRIMARY KEY,
	name text NOT NULL,
	description text
);

CREATE TABLE IF NOT EXISTS product_types
(
	id int PRIMARY KEY,
	name text NOT NULL,
	department int references departments(id)
);

CREATE TABLE IF NOT EXISTS products
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	name text NOT NULL,
	description text NOT NULL,
	type int references product_types(id),
	price numeric NOT NULL DEFAULT 0,
	availability bool NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_items
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	user_id varchar(150) references users(id) ON DELETE CASCADE,
	item_id uuid references products(id) NOT NULL,
	received bool NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS user_perks
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	user_id varchar(150) references users(id) ON DELETE CASCADE,
	perk_id uuid references products(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS bundles
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	name text NOT NULL,
	discount numeric NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS bundle_products
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	bundle_id uuid references bundles(id) ON DELETE CASCADE,
	product_id uuid references products(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions
(
	id uuid PRIMARY KEY DEFAULT uuid_generate_v1 (),
	user_id varchar(150),
	user_email text,
	product_id uuid,
	product_name text,
	amount numeric
);

--CREATE UNIQUE INDEX vol_block_idx ON volume_block (volume_id, x, y, z);

INSERT INTO role (id, name, description)
VALUES (1, 'Admin', 'Most privileged role'),
(2, 'Moder', 'Game server moderator'),
(3, 'Player', 'Default player');

INSERT INTO struct_type (id, name, description) VALUES
(1, 'main', 'Main lore structure'),
(2, 'agronomic', 'Food structure'),
(3, 'private', 'player`s private structure'),
(4, 'wave abode', 'Structure - adobe of monsters'),
(5, 'pressure abode', 'Structure - adobe of monsters'),
(6, 'farmers guild', 'farmers guild'),
(7, 'craft guild', 'craft guild'),
(8, 'defence guild', 'defence guild'),
(9, 'wizard guild', 'wizard guild');


INSERT INTO npc_role (id, name, description) VALUES
(2, 'warden', 'The main person in the village'),
(3, 'builder', 'A villager who builds and repairs structures');

INSERT INTO struct_block_type (id, name, description) VALUES
(1, 'Default', 'block without special behavior'),
(2, 'Safe location', 'Safe location for npc'),
(3, 'Abode spawn', 'Block where monsters spawn in the abode');

INSERT INTO job_type (id, name) VALUES
(1, 'Warrior'),
(2, 'Farmer'),
(3, 'Wizard');

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
