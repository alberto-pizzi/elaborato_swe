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