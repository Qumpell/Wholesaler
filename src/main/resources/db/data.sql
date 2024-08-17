INSERT INTO industries (name)
VALUES ('IT'),
       ('Healthcare'),
       ('Education'),
       ('Finance');


INSERT INTO roles (name)
VALUES ('Admin'),
       ('User'),
       ('Manager');



INSERT INTO users (firstname, surname, date_of_birth, login, password, role_name, is_deleted)
VALUES ('John', 'Doe', '1985-01-15', 'johndoe', 'password123', 'Admin', FALSE),
       ('Jane', 'Smith', '1990-06-30', 'janesmith', 'password456', 'User', FALSE),
       ('Alice', 'Johnson', '1982-11-25', 'alicejohnson', 'password789', 'Manager', FALSE),
       ('Robert', 'Brown', '1987-03-12', 'robertbrown', 'password321', 'Admin', FALSE),
       ('Emily', 'Davis', '1992-07-19', 'emilydavis', 'password654', 'User', FALSE),
       ('Michael', 'Wilson', '1989-10-05', 'michaelwilson', 'password987', 'Manager', FALSE),
       ('Sophia', 'Taylor', '1994-02-22', 'sophiataylor', 'password147', 'User', FALSE),
       ('James', 'Anderson', '1981-08-14', 'jamesanderson', 'password258', 'Admin', FALSE),
       ('Olivia', 'Thomas', '1988-11-11', 'oliviathomas', 'password369', 'Manager', FALSE),
       ('William', 'Jackson', '1993-05-27', 'williamjackson', 'password741', 'User', FALSE);



INSERT INTO companies (name, nip, city, address, industry_name, owner_id, is_deleted)
VALUES ('Tech Innovations Ltd.', '1234567890', 'New York', '123 Tech Lane', 'IT', 1, FALSE),
       ('Health Solutions Inc.', '0987654321', 'Los Angeles', '456 Health Ave', 'Healthcare', 2, FALSE),
       ('EduTech Co.', '1112131415', 'San Francisco', '789 Education Blvd', 'Education', 3, FALSE),
       ('FinServices Group', '1617181920', 'Chicago', '101 Finance St', 'Finance', 4, FALSE),
       ('IT Solutions LLC', '2122232425', 'Seattle', '202 IT Park', 'IT', 5, FALSE),
       ('MedTech Innovations', '2627282930', 'Boston', '303 Health Street', 'Healthcare', 6, FALSE),
       ('EduCare Services', '3132333435', 'Austin', '404 Learning Road', 'Education', 7, FALSE),
       ('FinTech Enterprises', '3637383940', 'Miami', '505 Finance Ave', 'Finance', 8, FALSE),
       ('TechGrowth Partners', '4142434445', 'Denver', '606 Innovation Drive', 'IT', 9, FALSE),
       ('Global Health Inc.', '4647484950', 'Philadelphia', '707 Wellness Way', 'Healthcare', 10, FALSE);


INSERT INTO trade_notes (content, company_name, owner_id, is_deleted)
VALUES ('Quarterly report on IT infrastructure.', 'Tech Innovations Ltd.', 1, FALSE),
       ('Annual review of healthcare services.', 'Health Solutions Inc.', 2, FALSE),
       ('New educational software deployment.', 'EduTech Co.', 3, FALSE),
       ('Financial audit results for Q1.', 'FinServices Group', 4, FALSE),
       ('IT system upgrade and maintenance.', 'IT Solutions LLC', 5, FALSE),
       ('Research on latest health technologies.', 'MedTech Innovations', 6, FALSE),
       ('Student enrollment statistics report.', 'EduCare Services', 7, FALSE),
       ('Financial projections for the next quarter.', 'FinTech Enterprises', 8, FALSE),
       ('Tech innovation strategy for the upcoming year.', 'TechGrowth Partners', 9, FALSE),
       ('Health sector market analysis.', 'Global Health Inc.', 10, FALSE),
       ('IT support tickets resolution summary.', 'Tech Innovations Ltd.', 1, FALSE),
       ('Healthcare compliance and regulations update.', 'Health Solutions Inc.', 2, FALSE),
       ('Education program feedback summary.', 'EduTech Co.', 3, FALSE),
       ('Annual finance department performance review.', 'FinServices Group', 4, FALSE),
       ('IT disaster recovery plan review.', 'IT Solutions LLC', 5, FALSE);


INSERT INTO contact_persons (firstname, surname, phone_number, mail, position, company_name, owner_id, is_deleted)
VALUES ('Alice', 'Walker', '555-1234', 'alice.walker@example.com', 'IT Manager', 'Tech Innovations Ltd.', 1, FALSE),
       ('Bob', 'Martin', '555-5678', 'bob.martin@example.com', 'Healthcare Specialist', 'Health Solutions Inc.', 2,
        FALSE),
       ('Charlie', 'Davis', '555-8765', 'charlie.davis@example.com', 'Education Coordinator', 'EduTech Co.', 3, FALSE),
       ('Dana', 'Johnson', '555-4321', 'dana.johnson@example.com', 'Finance Analyst', 'FinServices Group', 4, FALSE),
       ('Ella', 'Wilson', '555-6789', 'ella.wilson@example.com', 'IT Support', 'IT Solutions LLC', 5, FALSE),
       ('Frank', 'Brown', '555-1357', 'frank.brown@example.com', 'Healthcare Consultant', 'MedTech Innovations', 6,
        FALSE),
       ('Grace', 'Smith', '555-2468', 'grace.smith@example.com', 'Education Advisor', 'EduCare Services', 7, FALSE),
       ('Henry', 'White', '555-3579', 'henry.white@example.com', 'Financial Planner', 'FinTech Enterprises', 8, FALSE),
       ('Ivy', 'Taylor', '555-4680', 'ivy.taylor@example.com', 'Innovation Specialist', 'TechGrowth Partners', 9,
        FALSE),
       ('Jack', 'Young', '555-5791', 'jack.young@example.com', 'Market Analyst', 'Global Health Inc.', 10, FALSE),
       ('Kathy', 'Williams', '555-6802', 'kathy.williams@example.com', 'Project Manager', 'Tech Innovations Ltd.', 1,
        FALSE),
       ('Liam', 'Lee', '555-7913', 'liam.lee@example.com', 'Product Manager', 'Health Solutions Inc.', 2, FALSE);
