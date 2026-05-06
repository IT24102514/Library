# Library Management System

A full-stack web application for managing library operations built with **Spring Boot** and **JSP/CSS/JavaScript**.

## Features

- **Admin Dashboard** – Manage librarians, books, categories, members, and feedback
- **Librarian Portal** – Manage book borrowings, view books, manage profile
- **Member Portal** – Browse books, view borrowings, submit feedback, manage profile
- **Book Management** – CRUD operations with categories and availability tracking
- **Borrowing System** – Track book checkouts, returns, and overdue items
- **Member Management** – Registration, authentication, and profile management
- **Feedback System** – Members can rate and review books

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2, Spring Data JPA, Hibernate
- **Frontend**: JSP, HTML5, CSS3, JavaScript (Vanilla)
- **Database**: H2 (embedded, file-based) – zero configuration needed
- **Build Tool**: Apache Maven
- **Authentication**: BCrypt password hashing

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+ (or use the included Maven wrapper)

### Run the Application

```bash
# Clone the repository
git clone <repository-url>
cd Library

# Build and run
./mvnw spring-boot:run
```

The application starts at **http://localhost:8081**

### Default Accounts

| Role      | Email                  | Password  |
|-----------|------------------------|-----------|
| Admin     | admin@library.com      | admin123  |
| Librarian | librarian@library.com  | lib123    |

Members can register via the **Sign Up** page.

### H2 Database Console

Access the database console at **http://localhost:8081/h2-console**

- JDBC URL: `jdbc:h2:file:./data/librarydb`
- Username: `sa`
- Password: *(empty)*

## Project Structure

```
src/main/
├── java/com/example/librarymanagementsystem/
│   ├── config/          # WebConfig, DataInitializer
│   ├── controller/      # REST + View controllers
│   ├── dto/             # Data Transfer Objects
│   ├── model/           # JPA Entities
│   ├── repository/      # Spring Data JPA Repositories
│   └── service/         # Business logic
├── resources/
│   ├── application.properties
│   └── data.sql         # Seed data
└── webapp/
    ├── css/             # Stylesheets
    ├── js/              # Client-side JavaScript
    └── WEB-INF/views/   # JSP templates
```

## API Endpoints

| Module      | Base Path          |
|-------------|--------------------|
| Books       | `/api/books`       |
| Categories  | `/api/categories`  |
| Members     | `/api/members`     |
| Staff       | `/api/staff`       |
| Borrowings  | `/api/borrows`     |
| Feedback    | `/api/feedbacks`   |

## License

This project is for educational purposes.
