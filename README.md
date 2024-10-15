# tasks
Full Stack Developer Take-Home Exercise


# Task Manager Application

## Backend
- Java Spring Boot, H2 Database
- CRUD operations for Tasks

## Frontend
- Angular, Bootstrap
- Display and manage tasks via forms

### Testing the App
1. **Backend:**
   ```bash
   cd task-backend
   mvn test

2. **Frontend:**
   ```bash
   cd task-frontend
   npm install --save-dev jasmine karma @angular/core/testing @angular/common/http/testing
   ng test


### Running the App
1. **Backend:**
   ```bash
   cd task-backend
   mvn spring-boot:run

2. **Frontend:**
   ```bash
   cd task-frontend
   npm install
   npm run serve
