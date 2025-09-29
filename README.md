# 🛒 E-commerce Application (Spring Boot)

A fully functional backend system for an E-commerce platform built with **Spring Boot**.  
It includes authentication, role-based access, product/store management, purchases, and daily sales reports.

---

## 🚀 Features
- ✅ User registration & login with **JWT Authentication**
- ✅ Role-based access (**ADMIN**, **USER**)
- ✅ Manage Stores & Products (CRUD)
- ✅ Purchase products and track stock
- ✅ Generate daily sales reports
- ✅ Custom logging with **AOP (@LogMethod)**
- ✅ Exception handling with meaningful responses
- ✅ API documentation via **Swagger UI**

---

## 🏗️ Project Architecture
- **Controllers** → REST endpoints
- **Services** → Business logic
- **Repositories** → Database access (Spring Data JPA)
- **Models** → Entities (User, Store, Product, Purchase, Report)
- **Security** → JWT + Spring Security
- **DTOs** → Request/Response objects

---

## 🛡️ Security
- JWT token is required for accessing protected endpoints.
- Role-based security:
  - `ADMIN` → Can manage stores/products
  - `USER` → Can purchase products

---

## 📊 Database Schema (simplified)
- `users` → stores registered users
- `stores` → registered stores
- `products` → available products
- `store_products` → mapping of products per store with stock/price
- `user_purchases` → purchase history
- `store_sales_reports` → daily reports of sales

---

## 📖 API Documentation
Swagger UI available at:
http://localhost:8080/swagger-ui/index.html

---

## 🧪 Testing
- **Unit Tests** with JUnit 5 & Mockito
- Example: `AuthServiceTest` validates registration & login functionality

---

## 🛠️ Technologies Used
- Java 17+
- Spring Boot
- Spring Security + JWT
- Spring Data JPA + Hibernate
- Jakarta Validation
- Swagger (OpenAPI 3)
- Lombok
- JUnit 5 + Mockito

---

## ▶️ How to Run
1. Clone repository
2. Configure database in `application.properties`
3. Run the application:
   ```bash
   mvn spring-boot:run
Access API docs: http://localhost:8080/swagger-ui/index.html
