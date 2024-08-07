# Wholesaler
> - The goal of the project is to facilitate the management of the wholesalers.   
> - The project allows for browsing, adding, editing, and deleting data related to wholesale trade.

## Technologies Used
- Java 11
- Spring Boot version 2.7.5
- AngularJS version v1.2.32
- Bootstrap version v3.4.1
- MySQL version 8.0.17


## Features
Functionality for handling Users, Roles, Companies, Contact Persons, and Trade Notes, offering:

- **Addition:** Easily add new entries to the system.
- **Editing:** Modify existing data seamlessly.
- **Deletion:** Remove unnecessary records for better data management.
- **Browsing:** Navigate through data efficiently, now with built-in support for pagination and column sorting.

## Screenshots
![Example screenshot](./img/companies.png)
![Example screenshot](./img/userOne.png)
![Example screenshot](./img/contactPersonEdit.png)
![Example screenshot](./img/tradeNoteAdd.png)

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
