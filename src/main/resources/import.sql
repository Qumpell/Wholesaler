
INSERT INTO industries (id, name) VALUES (1, 'Technology');
INSERT INTO industries (id, name) VALUES (2, 'Finance');
INSERT INTO industries (id, name) VALUES (3, 'Retail');
INSERT INTO industries (id, name) VALUES (4, 'Healthcare');
INSERT INTO industries (id, name) VALUES (5, 'Construction');


INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_MODERATOR');

INSERT INTO users (firstname, surname, email, date_of_birth, username, password, is_deleted)
VALUES ('John', 'Doe', 'john.doe@example.com', '1985-01-15', 'johndoe', 'password1', false),
       ('Jane', 'Doe', 'jane.doe@example.com', '1990-04-25', 'janedoe', 'password2', false),
       ('Michael', 'Smith', 'michael.smith@example.com', '1982-03-12', 'michaelsmith', 'password3', false),
       ('Emily', 'Johnson', 'emily.johnson@example.com', '1988-07-23', 'emilyjohnson', 'password4', false),
       ('Chris', 'Brown', 'chris.brown@example.com', '1995-09-11', 'chrisbrown', 'password5', false),
       ('Jessica', 'Davis', 'jessica.davis@example.com', '1983-11-09', 'jessicadavis', 'password6', false),
       ('David', 'Miller', 'david.miller@example.com', '1987-02-19', 'davidmiller', 'password7', false),
       ('Sarah', 'Wilson', 'sarah.wilson@example.com', '1991-06-14', 'sarahwilson', 'password8', false),
       ('Daniel', 'Moore', 'daniel.moore@example.com', '1986-12-22', 'danielmoore', 'password9', false),
       ('Olivia', 'Taylor', 'olivia.taylor@example.com', '1992-05-30', 'oliviataylor', 'password10', false),
       ('Ethan', 'Anderson', 'ethan.anderson@example.com', '1989-08-07', 'ethananderson', 'password11', false),
       ('Sophia', 'Thomas', 'sophia.thomas@example.com', '1993-01-17', 'sophiathomas', 'password12', false),
       ('Matthew', 'Jackson', 'matthew.jackson@example.com', '1984-10-29', 'matthewjackson', 'password13', false),
       ('Emma', 'White', 'emma.white@example.com', '1996-04-03', 'emmawhite', 'password14', false),
       ('James', 'Harris', 'james.harris@example.com', '1981-09-18', 'jamesharris', 'password15', false),
    ('Admin', 'Admin', 'admin@example.com', '1985-01-15', 'admin', '$2a$10$uFUle8nQVHyBsSuwR286uubAxVZYF4qsZpvb6RAkbQvJ6AlRU2NGy', false);

INSERT INTO user_roles (user_id, role_id)
VALUES
    (1, 2),
    (2, 3),
    (3, 2),
    (4, 1),
    (5, 3),
    (6, 2),
    (7, 3),
    (8, 2),
    (9, 3),
    (10, 2),
    (11, 3),
    (12, 2),
    (13, 3),
    (14, 1),
    (15, 3),
    (16, 1);

INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (1, 'PL1234567890', '987654321', 'TechCorp', 'Warsaw', '123 Nowy Świat', 1, 1, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (2, 'DE2234567890', NULL, 'FinCorp', 'Berlin', '456 Unter den Linden', 2, 2, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (3, 'FR3234567890', NULL, 'RetailMart', 'Paris', '789 Champs-Élysées', 3, 3, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (4, 'IT4234567890', NULL, 'HealthPlus', 'Rome', '101 Via del Corso', 4, 4, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (5, 'ES5234567890', NULL, 'ConstructBuild', 'Madrid', '202 Gran Vía', 5, 5, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (6, 'PL6234567890', '987654326', 'TechInnovators', 'Kraków', '303 Floriańska', 1, 6, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (7, 'DE7234567890', NULL, 'FinancePros', 'Munich', '404 Maximilianstraße', 2, 7, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (8, 'FR8234567890', NULL, 'RetailHub', 'Lyon', '505 Rue de la République', 3, 8, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (9, 'IT9234567890', NULL, 'MediCare', 'Milan', '606 Corso Buenos Aires', 4, 9, false);
INSERT INTO companies (id, nip, regon, name, city, address, industry_id, user_id, is_deleted)
VALUES (10, 'ES1023456789', NULL, 'BuilderPros', 'Barcelona', '707 Passeig de Gràcia', 5, 10, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (1, 'First trade note for TechCorp', 1, 1, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (2, 'Second trade note for TechCorp', 1, 2, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (3, 'Trade discussion on new product for FinCorp', 2, 2, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (4, 'New investment plan for FinCorp', 2, 3, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (5, 'Expansion strategy for RetailMart', 3, 3, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (6, 'RetailMart new supplier agreement', 3, 4, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (7, 'HealthPlus new partnership deal', 4, 4, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (8, 'HealthPlus healthcare reform impact', 4, 5, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (9, 'New construction project for ConstructBuild', 5, 5, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (10, 'Innovative design in ConstructBuild', 5, 6, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (11, 'TechInnovators introduces AI-based product', 6, 6, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (12, 'TechInnovators expands research team', 6, 7, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (13, 'FinancePros reports strong Q2 earnings', 7, 7, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (14, 'New partnership for FinancePros', 7, 8, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (15, 'RetailHub sees record sales in Q3', 8, 8, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (16, 'New product line for RetailHub', 8, 9, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (17, 'MediCare expands into telehealth services', 9, 9, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (18, 'New government contract for MediCare', 9, 10, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (19, 'BuilderPros wins major infrastructure project', 10, 10, false);
INSERT INTO trade_notes (id, content, company_id, user_id, is_deleted)
VALUES (20, 'BuilderPros expands construction team', 10, 1, false);


INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (1, 'John', 'Doe', '123456789', 'john.doe@techcorp.com', 'Manager', 1, 1, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (2, 'Jane', 'Smith', '987654321', 'jane.smith@fincorp.com', 'Financial Analyst', 2, 2, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (3, 'Michael', 'Johnson', '555123456', 'michael.johnson@retailmart.com', 'Store Manager', 3, 3, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (4, 'Emily', 'Davis', '444987654', 'emily.davis@healthplus.com', 'Healthcare Specialist', 4, 4, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (5, 'Chris', 'Brown', '333555123', 'chris.brown@constructbuild.com', 'Project Manager', 5, 5, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (6, 'Sarah', 'Wilson', '222444987', 'sarah.wilson@techinnovators.com', 'Research Lead', 6, 6, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (7, 'David', 'Moore', '111333555', 'david.moore@financepros.com', 'CFO', 7, 7, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (8, 'Jessica', 'Taylor', '999111333', 'jessica.taylor@retailhub.com', 'Marketing Lead', 8, 8, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (9, 'Mark', 'Anderson', '888999111', 'mark.anderson@medicare.com', 'Telehealth Director', 9, 9, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (10, 'Anna', 'Thomas', '777888999', 'anna.thomas@builderpros.com', 'Site Supervisor', 10, 10, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (11, 'Robert', 'Jackson', '666777888', 'robert.jackson@techcorp.com', 'Technical Lead', 1, 1, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (12, 'Patricia', 'White', '555666777', 'patricia.white@fincorp.com', 'HR Manager', 2, 2, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (13, 'James', 'Harris', '444555666', 'james.harris@retailmart.com', 'Logistics Coordinator', 3, 3, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (14, 'Linda', 'Clark', '333444555', 'linda.clark@healthplus.com', 'Nurse Practitioner', 4, 4, false);
INSERT INTO contact_persons (id, firstname, surname, phone_number, mail, position, company_id, user_id, is_deleted)
VALUES (15, 'Daniel', 'Lewis', '222333444', 'daniel.lewis@constructbuild.com', 'Architect', 5, 5, false);
