-- Create Database
CREATE DATABASE spring_restful_api;
USE spring_restful_api;

-- Table Users
CREATE TABLE users (
    id                      VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    token                   VARCHAR(255),
    token_expired_at        BIGINT,
    PRIMARY KEY (id),
    UNIQUE (token)
) ENGINE InnoDB;

SELECT * FROM users;
DESC users;

-- Table Contacts
CREATE TABLE contacts (
    id                      VARCHAR(255) NOT NULL,
    firstname               VARCHAR(255) NOT NULL,
    lastname                VARCHAR(255),
    email                   VARCHAR(255),
    phone                   VARCHAR(15),
    user_id                 VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_users_contacts (user_id) REFERENCES users (id)
) ENGINE InnoDB;

SELECT * FROM contacts;
DESC contacts;

-- Table Addresses
CREATE TABLE addresses (
    id                      VARCHAR(255) NOT NULL,
    street                  VARCHAR(255),
    city                    VARCHAR(255),
    province                VARCHAR(255),
    country                 VARCHAR(255) NOT NULL,
    postal_code             VARCHAR(5) NOT NULL,
    contact_id              VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_contacts_contacts (contact_id) REFERENCES contacts (id)
) ENGINE InnoDB;

SELECT * FROM addresses;
DESC addresses;

-- Table Categories
CREATE TABLE categories (
    id                      VARCHAR(255) NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
) ENGINE InnoDB;

SELECT * FROM categories;
DESC categories;

-- Table Products
CREATE TABLE products (
    id                      VARCHAR(255) NOT NULL,
    name                    VARCHAR(255) NOT NULL,
    price_buy               DOUBLE(100, 2) NOT NULL,
    price_sell              DOUBLE(100, 2) NOT NULL,
    stock                   INT(100) NOT NULL,
    description             TEXT(255),
    category_id             VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_categories_products (user_id) REFERENCES categories (id)
) ENGINE InnoDB;

SELECT * FROM products;
DESC products;
