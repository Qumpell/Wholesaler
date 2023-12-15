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
- addition
- editing
- deletion
- browsing


## Screenshots
![Example screenshot](./img/companies.png)
![Example screenshot](./img/userOne.png)
![Example screenshot](./img/contactPersonEdit.png)
![Example screenshot](./img/tradeNoteAdd.png)



## Setup
**1. Clone the application**

```bash
https://github.com/Qumpell/Wholesaler.git
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
