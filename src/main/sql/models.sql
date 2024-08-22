CREATE TABLE IF NOT EXISTS "User" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100),
    province VARCHAR(100),
    zip VARCHAR(20),
    country VARCHAR(100),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Owner" (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(100) NOT NULL UNIQUE,
    city VARCHAR(100),
    province VARCHAR(100),
    zip VARCHAR(20),
    country VARCHAR(100),
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS "Facility" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    province VARCHAR(100) NOT NULL,
    zip VARCHAR(20),
    country VARCHAR(100) NOT NULL,
    n_managers INT NOT NULL,
    n_fields INT NOT NULL,
    telephone VARCHAR(20),
    image TEXT,
    WH_Mon VARCHAR(20),
    WH_Tue VARCHAR(20),
    WH_Wed VARCHAR(20),
    WH_Thu VARCHAR(20),
    WH_Fri VARCHAR(20),
    WH_Sat VARCHAR(20),
    WH_Sun VARCHAR(20),
    id_owner INTEGER UNIQUE,
    FOREIGN KEY (id_owner) REFERENCES Owner(id)
);

CREATE TABLE IF NOT EXISTS "Reservation" (
    id SERIAL PRIMARY KEY,
    res_date VARCHAR(255) NOT NULL,
    event_date VARCHAR(255) NOT NULL,
    res_time VARCHAR(255) NOT NULL,
    event_time_start VARCHAR(255) NOT NULL,
    event_time_end VARCHAR(255) NOT NULL;
    id_field INTEGER NOT NULL,
    n_partecipants INT NOT NULL,
    is_confirmed BIT NOT NULL
    is_matched BIT NOT NULL
    id_user INTEGER NOT NULL,
    FOREIGN KEY (id_user) REFERENCES User(id)
    FOREIGN KEY (id_field) REFERENCES Field(id)
);

CREATE TABLE IF NOT EXISTS "Invite" (
    id SERIAL PRIMARY KEY,
    id_group INTEGER NOT NULL UNIQUE,
    id_user INTEGER NOT NULL,
    FOREIGN KEY (id_group) REFERENCES Group(id)
    FOREIGN KEY (id_user) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS "IsPart" (
    id_group INTEGER NOT NULL UNIQUE,
    id_user INTEGER NOT NULL,
    PRIMARY KEY(id_group, id_user)
    FOREIGN KEY (id_group) REFERENCES Group(id)
    FOREIGN KEY (id_user) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS "Group" (
    id SERIAL PRIMARY KEY,
    group_head INTEGER NOT NULL,
    id_reservation INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (id_reservation) REFERENCES Reservation(id)
    FOREIGN KEY (group_head) REFERENCES User(id)
);

CREATE TABLE IF NOT EXISTS "Field" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    id_sport INTEGER NOT NULL,
    description VARCHAR(255) NOT NULL,
    price Float(3) NOT NULL,
    image VARCHAR(100) NOT NULL,
    id_facility INTEGER UNIQUE,
    FOREIGN KEY (id_facility) REFERENCES Facility(id)
);

CREATE TABLE IF NOT EXISTS "Sport" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    players_required INTEGER NOT NULL,
);

CREATE TABLE IF NOT EXISTS "Manages" (
    id_facility INTEGER NOT NULL,
    id_user INTEGER NOT NULL,
    PRIMARY KEY(id_facility, id_user)
    FOREIGN KEY (id_facility) REFERENCES Facility(id)
    FOREIGN KEY (id_user) REFERENCES User(id)
);