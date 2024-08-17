CREATE TABLE IF NOT EXISTS industries (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        firstname VARCHAR(255),
                        surname VARCHAR(255),
                        date_of_birth DATE,
                        login VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255),
                        role_name VARCHAR(255),
                        is_deleted BOOLEAN DEFAULT FALSE,
                        CONSTRAINT FOREIGN KEY (role_name) REFERENCES roles(name) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS companies (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE,
                        nip VARCHAR(255) NOT NULL UNIQUE,
                        city VARCHAR(255),
                        address VARCHAR(255),
                        industry_name VARCHAR(255),
                        owner_id BIGINT,
                        is_deleted BOOLEAN DEFAULT FALSE,
                        CONSTRAINT FOREIGN KEY (industry_name) REFERENCES industries(name) ON DELETE SET NULL,
                        CONSTRAINT FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS trade_notes (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        content TEXT,
                        company_name VARCHAR(255),
                        owner_id BIGINT,
                        is_deleted BOOLEAN DEFAULT FALSE,
                        CONSTRAINT FOREIGN KEY (company_name) REFERENCES companies(name) ON DELETE SET NULL,
                        CONSTRAINT FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL

);

CREATE TABLE IF NOT EXISTS contact_persons (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        firstname VARCHAR(255),
                        surname VARCHAR(255),
                        phone_number VARCHAR(255),
                        mail VARCHAR(255),
                        position VARCHAR(255),
                        company_name VARCHAR(255),
                        owner_id BIGINT,
                        is_deleted BOOLEAN DEFAULT FALSE,
                        FOREIGN KEY (company_name) REFERENCES companies(name) ON DELETE SET NULL,
                        FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE SET NULL
);