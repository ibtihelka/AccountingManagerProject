# Accounting Manager Project Documentation

## Table of Contents
- [Introduction](#introduction)
- [Project Overview](#project-overview)
   - [Purpose](#purpose)
   - [Architecture](#architecture)
   - [Parent Project and Maven Multi-Module Structure](#parent-project-and-maven-multi-module-structure)
   - [Project Modules](#project-modules)
- [Discovery Microservice (Eureka Server)](#discovery-microservice-eureka-server)
- [Gateway Microservice](#gateway-microservice)
- [Accounting Auth Microservice](#accounting-auth-microservice)
- [Accounting Engine & Bank Statement Engine Microservices](#accounting-engine--bank-statement-engine-microservices)
  - [OCRs Integration Details](#ocrs-integration-details)
- [Accounting Management Microservice](#accounting-management-microservice)
- [Accounting Core Module](#accounting-core-module)
- [AccountingTest Module](#accountingtest-module)
- [GUI](#gui)
- [Database](#database)
  - [Pre-existing Accounts](#pre-existing-accounts)
- [Getting Started](#getting-started)
   - [How to Run it on Docker](#how-to-run-it-on-docker)
   - [How to Run it Locally](#how-to-run-it-locally)
- [Documentation](#documentation)
- [Frontend Documentation](#frontend-documentation)
- [Development Tools](#development-tools)
- [Conclusion](#conclusion)

## Introduction

Welcome to the **Accounting Manager** project! This in-depth guide provides detailed insights into the project's structure, modules, technologies, setup procedures for both local and Docker environments, and crucial developer commands. If you have questions or require assistance, please feel free to contact the project team.

## Project Overview

### Purpose

The Accounting Manager project is designed to revolutionize invoice management for accountants. Its primary objectives are to facilitate OCR-based invoice scanning, extract essential information (e.g., SIRET number, TVA), and efficiently manage invoices and folders. The platform caters to both individual clients (physical persons) and business clients (legal entities).
The project adopts a microservices architecture using Spring Boot 3.2.2 and Java 17.

### Architecture

### Parent Project and Maven Multi-Module Structure

The **Accounting Manager** project is organized as a Maven multi-module project. At its core, there is a parent project that plays a pivotal role in managing dependencies and ensuring cohesion among the various modules. The parent project streamlines the development process by providing shared configurations, plugins, and dependencies that are common across all modules.

This multi-module approach allows for modularization of functionalities, enhancing maintainability and scalability. Each module focuses on specific aspects of the application, promoting a clean and organized codebase.

### Project Modules

- **Discovery Microservice (Eureka Server):** Enables service registration and discovery for all microservices.

- **Gateway Microservice:** Acts as the API Gateway for the entire system.

- **Accounting Auth Microservice:** Handles authentication and authorization, including JWT token generation, password reset, account confirmation, login, and registration.

- **Accounting Engine Microservice & Bank Statement Engine:** Responsible for using OCRs, detecting crucial values from invoices, and overall processing of invoices/bank statements.

- **Accounting Management Microservice:** Manages the application, invoices, folders, types, client lists, and admin features.

- **Accounting Core Module:** A crucial library supporting multiple OCRs by transforming their outputs into a standardized format using XSLT 3.0. Currently integrated with Tesseract, and Asprise OCRs.

- **AccountingTest Module:** Responsible for test automation, developed using Java, Selenium, Cucumber, JUnit, and following the Page Object Model (POM) design pattern.

## Discovery Microservice (Eureka Server)

The **Discovery Microservice** functions as a Eureka Server, playing a pivotal role in enabling service registration and discovery for all microservices within the Accounting Manager project.

### Features:

1. **Service Registration:**
   - Allows microservices to register themselves, facilitating dynamic service discovery.

2. **Service Discovery:**
   - Enables microservices to discover and communicate with each other dynamically.

### Implementation:

Implemented with Spring Boot 3.2.2, the Discovery Microservice enhances the scalability and flexibility of the overall system architecture, enabling seamless communication between microservices.

## Gateway Microservice

The **Gateway Microservice** acts as the API Gateway for the entire Accounting Manager system. It serves as the entry point for external requests, directing traffic to the appropriate microservices and ensuring efficient communication.

### Features:

1. **Request Routing:**
   - Routes incoming requests to the corresponding microservices based on predefined rules.

2. **API Management:**
   - Manages and secures APIs, providing a unified and controlled access point for external entities.

### Implementation:

Implemented with Spring Boot 3.2.2, the Gateway Microservice enhances the overall system's security, performance, and manageability by acting as a centralized API Gateway.

## Accounting Auth Microservice

The **Accounting Auth Microservice** is a vital component responsible for handling authentication and authorization within the Accounting Manager project. This module incorporates various features essential for securing and managing user access. Let's delve into its functionalities:

### Features:

1. **JWT Token Generation:**
   - Generates JSON Web Tokens (JWT) for secure communication and authentication between microservices.

2. **Password Reset:**
   - Facilitates the process of resetting passwords for users, enhancing security and user convenience.

3. **Account Confirmation:**
   - Manages the confirmation of user accounts, ensuring that only verified users gain access to the system.

4. **Login:**
   - Handles user authentication during login, verifying credentials and granting access to authorized users.

5. **Registration:**
   - Provides a seamless user registration process, creating accounts with necessary information for future authentication.

### Implementation:

The microservice is implemented using Spring Boot 3.2.2 and Java 17, ensuring robust and efficient authentication mechanisms. The integration of JWT enhances security, and features like password reset and account confirmation contribute to a user-friendly experience.

## Accounting Engine & Bank Statement Engine Microservices

The **Accounting Engine Microservice** and the **Bank Statement Engine Microservice** are core modules designed to process invoices/bank statements efficiently.
They play a crucial role in utilizing OCR technology for scanning invoices/bank statements,
detecting key values, and streamlining the overall invoice/bank statements processing workflow.

### Features:

1. **OCR Integration:**
   - Integrates multiple OCR technologies, including ABBYY, Tesseract, and Asprise OCR, providing flexibility in OCR selection.

2. **Value Detection:**
   - Utilizes OCRs to detect essential values from documents, such as SIRET number, TVA, invoice numbers / RIB, IBAN, BIC...etc

3. **Processing Invoices:**
   - Manages the overall processing of documents, ensuring accuracy and efficiency in handling diverse documents formats.

### Implementation:

Built with Spring Boot 3.2.2 and Java 17,
the Accounting Engine and the Bank Statement Engine leverage OCR capabilities to enhance invoice processing.
The integration of multiple OCRs allows the system to adapt to different formats, ensuring versatility and scalability.

### OCRs Integration Details:

### _- ABBYY:_
We integrated ABBYY OCR using the ABBYY SDK. The SDK files are located inside the Ocr package. 
Within this package, there is a subpackage com.abbyy.orcsdk which contains the Client class responsible for configuring the OCR.

These files are used in the EngineService, the Client class and ProcessingSettings class to configure the OCR and process the images.

The ABBYY SDK files are organized in the following structure:

    Ocr/
    └── com/
        └── abbyy/
            └── orcsdk/
                ├── Client.java
                ├── ProcessingSettings.java
                └── ... (other ocr related classes)
    └── CmdLineOptions.java
    └── ... (other ocr related classes)


The Client class contains the configuration for the OCR, including the applicationId and password.

**Configuration Instructions and License Renewal:**
  1. Obtain Application ID and Password:
     - Visit this link: https://cloud.ocrsdk.com/Account/Welcome 
     - Request a trial license by creating a new application. (Click on the "ADD NEW APPLICATION" button).
     - If you don't have an account, sign up for a new account.
     - Fill in the required details and submit the form.


  2. Setting Up Application ID and Password:
     - Once you have a trial license, you'll receive an applicationId and a password on your email.
     - Open the Client.java file located in the Ocr/com/abbyy/orcsdk package.
     - Replace the placeholders with your applicationId and password.

**NOTE: The trial license is valid for 3 months. After this period, do these steps again to renew the license.**

### _- Tesseract:_

Tesseract OCR is integrated using the Tesseract4J library, it's included as a dependency in the pom.xml file.<br/>
But also we need the tessdata folder which contains the trained data for the OCR and language files
to be able to recognize the text and work properly.<br />
The tessdata folder is located in the resources/lib directory, and it's being used by the EngineService,
we just need to specify the path to the tessdata folder in the EngineService.java file.

There is no need for additional configuration for Tesseract OCR plus it is open-source.

### _- Asprise:_

Asprise OCR is integrated using the Asprise OCR library, it's included as a dependency in the pom.xml file.<br/>
Plus,
we need the aocr.jar file located in the resources/lib directory,
which contains the OCR engine and the trained data for the OCR to be able to recognize the text and work properly.

The aocr.jar file is being used by the EngineService,
we just need to import the OCR class from this library and use it to process the images.

No additional configuration is required for Asprise OCR.<br/>
**NOTE: It is not free, and its licensing is tied to the machine's MAC address.**

## Accounting Management Microservice

The **Accounting Management Microservice** serves as a centralized hub
for managing various aspects of the Accounting Manager application.
It encompasses features related to invoices, folders, client lists, administrative functionalities, statistics and more.

### Features:

1. **Invoice and Folder Management:**
   - Handles the creation, retrieval, and update of invoices and folders, ensuring organized data management.

2. **Accountants/Clients List Management:**
   - Provides functionalities for managing lists of accountants and their clients, distinguishing between individual and business clients.

3. **Admin Features:**
   - Implements administrative features, enabling authorized users to perform specific actions for system management.

4. **Statistics:**
   - Generates statistics and reports based on the data stored in the system, providing valuable insights for decision-making.

### Implementation:

Implemented using Spring Boot 3.2.2 and Java 17, the Accounting Management Microservice ensures efficient data management and comprehensive administrative capabilities within the Accounting Manager project.

## Accounting Core Module

A crucial dependency/library, the **Accounting Core** module is utilized by the Accounting Engine. Its purpose is to provide shared functionality for supporting multiple OCRs with different output formats. Initially designed for ABBYY OCR, the module allows seamless integration of other OCRs by transforming their specific XML outputs into a standardized format using XSLT 3.0.

Currently, integrated OCRs include ABBYY, Tesseract, and Asprise OCR (with licensing considerations). XSLT templates for each OCR are stored in the Core module under resources/xslt.

## AccountingTest Module

The AccountingTest module is responsible for automating tests within the Accounting Manager project. This module is developed using Java, Selenium, TestNG, and follows the Page Object Model (POM) design pattern to ensure scalable and maintainable test automation.

### Features:

1. **Automated UI Testing:**

   - Uses Selenium to simulate user interactions with the GUI, ensuring that the user interface behaves as expected.

2. **Assertions:**

   - Utilizes TestNG for assertions to verify the outcomes of the tests, ensuring correctness and reliability.

### Implementation:
The AccountingTest module is implemented with Java and integrates Selenium for UI testing and TestNG for assertions. The Page Object Model (POM) design pattern is employed to create an organized and reusable code structure, facilitating efficient test automation.

## GUI

The graphical user interface (GUI) is developed using Angular 15.2.9, employing a component-based architecture. Noteworthy GUI modules include the main user components, an admin module, and an authentication module. Angular Material 15.2.9 ensures a consistent and responsive UI.

## Database

The project uses MySQL as the database, with the database dump and class diagram available in the 'db' directory.

### Pre-existing Accounts

The database contains pre-existing accountants with the following details:


| Email Address         | Password     | Role       |
|-----------------------|--------------|------------|
| user@gmail.com        | 00000000     | ACCOUNTANT |
| admin@gmail.com       | 00000000     | ADMIN      |


## Sample Data

You can find some invoices and bank statements in the `Sample-Data` directory. These files can be used for testing the OCRs and the processing of invoices/bank statements.

# Getting Started

## How to Run it on Docker

### Prerequisites

- JDK 17
- Maven
- Docker and Docker Compose

### Docker Setup

#### Single click containerization:

To containerize the application with a single click, use this command `start containerize` or just click on the `containerize.bat` file in the File Explorer.

#### Or you can containerize it following these steps:

1. Open the project in IntelliJ IDEA for the backend and Visual Studio Code for the GUI.
2. In IntelliJ, run `mvn clean install` to generate JAR files.
3. Build and start the containers using `docker-compose up --build -d`.
4. Access the GUI at `http://localhost:4200`.

### Ports

- GUI: 4200
- Gateway: 8080
- Accounting Engine: 8081
- Accounting Auth: 8082
- Accounting Management: 8083
- Bank Statement Engine: 8086
- Discovery (Eureka Server): 8761

### Useful Commands

- To stop and remove containers: `docker-compose down`.
- To delete all images: `docker rmi -f $(docker images -aq)`.
- To prune Docker build cache: `docker buildx prune -f`.
- To containerize the application with a single click, use this command `start containerize` or just click on the `containerize.bat` file in the File Explorer.

## How to Run it Locally

### Prerequisites

- JDK 17
- Maven
- Node.js 18.16.1
- NPM 9.5.1
- XAMPP or any other suitable local database solution

### Local Development Setup

1. Open the project in IntelliJ IDEA for the backend and Visual Studio Code for the GUI.
2. Start your local database solution (e.g., XAMPP).
3. Update database configuration in the project accordingly. You can find the database dump in the `/db` directory.
4. Run the backend application in IntelliJ.
5. Navigate to the GUI directory. Ensure you are under `accountingmanager>gui`.
6. Run `npm i` to install dependencies.
7. Start the development server with `ng s`.
8. Access the GUI at `http://localhost:4200`.

### Ports

- GUI: 4200
- Gateway: 8080
- Accounting Engine: 8081
- Accounting Auth: 8082
- Accounting Management: 8083
- Bank Statement Engine: 8086
- Discovery (Eureka Server): 8761

## Documentation

### API Documentation

Access Swagger UI for API documentation by navigating to `http://localhost:{module_port}/swagger-ui/index.html#/` when the project is running.

You can also access API Documentation in offline mode
by opening the html/pdf files located in the `API-Documentation` directory.

### Frontend Documentation

For GUI documentation:

1. Navigate to the GUI directory.
2. Run `npm i` to install dependencies.
3. Start the development server with `ng s`.
4. Generate documentation using `compodoc -p tsconfig.doc.json -s -r 9000`.
5. Access the documentation at `http://127.0.0.1:9000`.

## Development Tools

- IntelliJ IDEA: For backend development.
- Visual Studio Code: For GUI development.
- XAMPP: For local database usage.

## Conclusion

The **Accounting Manager** project boasts a well-organized architecture, with each microservice serving a distinct purpose and contributing to the system's overall functionality. Whether it's authentication, invoice processing, data management, service discovery, or API gateway functionality, each module plays a crucial role in achieving the project's objectives. The implementation details and features of each microservice ensure a robust, scalable, and efficient system for managing accounting tasks.

Congratulations! You are now well-equipped to explore and contribute to the **Accounting Manager** project. Delve into the detailed README for a thorough understanding, and don't hesitate to reach out to the project team for any assistance. Happy coding! :)
