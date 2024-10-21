# Wholesaler
> - The goal of the project is to facilitate the management of the wholesalers.   
> - The project is an API that allows users to
browse, add, edit, and delete data related to wholesale trade.

### Frontend Application (in progress)
The frontend application, built with **Angular**, can be found [here](https://github.com/your-username/frontend-repo).
## Technologies Used
- Java 17
- Spring Boot version 3.3.2
- AngularJS version v1.2.32 
- Bootstrap version v3.4.1
- MySQL version 9.0.1


## Features
Functionality for handling users, roles, companies, contact people, 
and trade notes, offering the following features:

### 1. Authentication and Authorization: 
This project implements secure user authentication using **JWT (JSON Web Token)** 
and **refresh tokens**. Additionally, the system enforces **role-based access 
control (RBAC)** and ensures that users have fine-grained permissions based on their roles.
### 2. Resource Ownership
Users can create, edit, and delete resources. **However, only the user who created a specific resource 
(trade note or contact person) can edit or delete it, ensuring ownership-based access control.**
For example, if a user creates a trade note, only that user 
(or an admin or a moderator) will be able to modify or delete it.
### 3. Addition:
Easily add new entries to the system.
### 4. Editing: 
Modify existing data seamlessly.
### 5. Deletion:
Remove unnecessary records for better data management.
### 6. Browsing:
Navigate through data efficiently, now with built-in support for pagination and column sorting.


## Setup with docker
**1. Clone the application**

```bash
git clone https://github.com/Qumpell/Wholesaler.git
```

**2. Run docker-compose file in project directory**
```bash
docker-compose -up
```
The app will start running at <http://localhost:8080>.

**3. Additional information** 
 - To stop app
  ```bash
  docker-compose -down
  ```
   - To rebuild app
   ```bash
    docker-compose -up --build
  ```



## Setup without docker
**1. Clone the application**

```bash
git clone https://github.com/Qumpell/Wholesaler.git
```
**2. Create Mysql database**
```bash
create database wholesaler
```
**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.yaml`

+ change as per your mysql installation:
   - spring:  
     -  datasource:  
        -   password: here
        -   username: here
        
**4. Build and run the app using maven**

```bash
mvn spring-boot:run
```
The app will start running at <http://localhost:8080>.

## Project Status
Project is: _in progress_

## For the previous version without security functionality
### Screenshots
![Example screenshot](./img/companies.png)
![Example screenshot](./img/userOne.png)
![Example screenshot](./img/contactPersonEdit.png)
![Example screenshot](./img/tradeNoteAdd.png)
