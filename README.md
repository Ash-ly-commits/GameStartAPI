# E-Commerce Backend API

This project is a **Spring Boot RESTful API** that powers the backend of an e-commerce application. It provides endpoints for authentication, product browsing, category management, shopping cart functionality, user profiles, and order checkout, backed by a **MySQL database** and secured with **JWT-based authentication**.

---

## ✨ Features

* **Authentication & Authorization**

  * User registration and login
  * JWT token-based security
  * Role-based access control (Admin vs User)

* **Products & Categories**

  * Browse and search products
  * Filter by category, price range, and subcategory
  * Admin-only CRUD operations

* **Shopping Cart**

  * Add, update, and remove items
  * Persistent cart per authenticated user

* **Orders & Checkout**

  * Create orders from shopping cart
  * Order line items generated automatically

* **User Profiles**

  * View and update authenticated user profile

---

## 🛠️ Tech Stack

* **Java 17+**
* **Spring Boot**

  * Spring Web (REST controllers)
  * Spring Security (JWT + role-based auth)
* **MySQL**
* **JDBC / DAO Pattern**
* **Maven**

---

## 📂 Project Structure

```
org
├── controllers        # REST controllers
├── data               # DAO interfaces
│   └── mysql          # MySQL DAO implementations
├── models             # Domain models
├── security            # JWT & Spring Security config
```

---

## 🔐 Security Overview

* **JWT Authentication**

  * Tokens issued on `/login`
  * Sent via `Authorization: Bearer <token>` header

* **Access Rules**

  * Public endpoints: browse products & categories
  * Authenticated users: shopping cart, profile, checkout
  * Admin-only: create/update/delete products & categories

---

## 🚀 API Endpoints

### Authentication

| Method | Endpoint    | Description                      |
| ------ | ----------- | -------------------------------- |
| POST   | `/login`    | Authenticate user and return JWT |
| POST   | `/register` | Register a new user              |

---

### Categories

| Method | Endpoint                    | Access | Description              |
| ------ | --------------------------- | ------ | ------------------------ |
| GET    | `/categories`               | Public | Get all categories       |
| GET    | `/categories/{id}`          | Public | Get category by ID       |
| GET    | `/categories/{id}/products` | Public | Get products in category |
| POST   | `/categories`               | Admin  | Create category          |
| PUT    | `/categories/{id}`          | Admin  | Update category          |
| DELETE | `/categories/{id}`          | Admin  | Delete category          |

---

### Products

| Method | Endpoint         | Access | Description       |
| ------ | ---------------- | ------ | ----------------- |
| GET    | `/products`      | Public | Search products   |
| GET    | `/products/{id}` | Public | Get product by ID |
| POST   | `/products`      | Admin  | Create product    |
| PUT    | `/products/{id}` | Admin  | Update product    |
| DELETE | `/products/{id}` | Admin  | Delete product    |

**Search Parameters:**

* `cat` – category ID
* `minPrice` / `maxPrice`
* `subCategory`

---

### Shopping Cart (Authenticated)

| Method | Endpoint                              | Description      |
| ------ | ------------------------------------- | ---------------- |
| GET    | `/shopping_cart`                      | Get current cart |
| POST   | `/shopping_cart/products/{productId}` | Add product      |
| PUT    | `/shopping_cart/products/{productId}` | Update quantity  |
| DELETE | `/shopping_cart`                      | Clear cart       |

---

### Orders (Authenticated)

| Method | Endpoint  | Description               |
| ------ | --------- | ------------------------- |
| POST   | `/orders` | Checkout and create order |

---

### Profile (Authenticated)

| Method | Endpoint    | Description         |
| ------ | ----------- | ------------------- |
| GET    | `/profiles` | Get user profile    |
| PUT    | `/profiles` | Update user profile |

---

## 🗄️ Database Design (High-Level)

* **users** – authentication & roles
* **profiles** – user profile details
* **categories** – product categories
* **products** – product catalog
* **shopping_cart** – user cart items
* **orders** – completed orders
* **order_line_items** – items per order

---

## ⚙️ Setup Instructions

1. **Clone the repository**

   ```bash
   git clone <repo-url>
   ```

2. **Configure MySQL**

   * Create a database
   * Update `application.properties` with:

     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/your_db
     spring.datasource.username=your_user
     spring.datasource.password=your_password
     ```

3. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

4. **Access API**

   * Base URL: `http://localhost:8080`

---

## 🧪 Error Handling

* Uses `ResponseStatusException` for consistent HTTP responses
* Common responses:

  * `400 Bad Request` – invalid input or empty cart
  * `401 Unauthorized` – missing/invalid JWT
  * `403 Forbidden` – insufficient role
  * `404 Not Found` – resource not found
  * `500 Internal Server Error` – unexpected errors

---

## 🧩 Design Notes

* DAO pattern cleanly separates database logic
* Controllers focus on request handling and validation
* JDBC used directly for learning and control
* Passwords stored using **BCrypt hashing**

---

## 📌 Future Enhancements

* Pagination & sorting for products
* Order history endpoint
* Inventory validation on checkout
* Global exception handler (`@ControllerAdvice`)
* Swagger / OpenAPI documentation

---

## 👤 Author

Developed as part of a **Year Up** backend e-commerce project.

---

If you'd like, I can also:

* Add **Swagger docs**
* Generate **sample JSON requests/responses**
* Create a **database schema diagram**
* Tailor the README for GitHub portfolio use
