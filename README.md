# Andersen Test Application

## Overview
This project consists of two Spring Boot applications:
1. **Author Service** – Located in `author-service`, it provides mock of author-service like it's external microservice.
2. **Andersen Test Application** – The main application that depends on the Author Service.

## Requirements
- **Java 17+**
- **Maven 3.6+**
- **Git** (for cloning the repository)
- **An IDE with Java Support**

---

## Running the Applications
### 1. Clone repository with `git clone`

### **2.1 Run using Startup Scripts**

Make sure you are in the root folder of project in your terminal

#### **For Windows:**
Run the batch script:
```sh
run.bat
```

#### **For MacOS/Linux:**
Run the shell script:
```sh
chmod +x run.sh
./run.sh
```

### **2.2 (Optional) Running Manually in an IDE**
If you prefer to run the applications manually in an IDE, follow these steps:

1. **Open the project in your IDE.**
2. **Run the Author Service:**
    - Navigate to:  
      `author-service/src/main/java/com/andersenlab/test/authorservice/AuthorServiceApplication.java`
    - Run the `main` method.
3. **Run the Main Application:**
    - Navigate to:  
      `src/main/java/com/andersenlab/test/andersentest/AndersenTestApplication.java`
    - Run the `main` method.


## Database

Application uses in-memory H2 database, after running app it available at `http://localhost:8080/h2-console`. username: `sa`, password: `password`

