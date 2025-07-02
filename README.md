# 🔐 MiniBank Security — Spring Security Learning Project

Welcome to **MiniBank Security**, a personal Spring Boot project focused on learning **Spring Security** in depth.

---

## 🎯 Purpose

This project was built as a hands-on learning tool to explore and understand key concepts of **Spring Security**,
including:

- Authentication using custom `UserDetailsService`
- Authorization with role-based access control
- Secure password encoding with `BCryptPasswordEncoder`
- Form login & HTTP Basic authentication
- Auditing with `@CreatedBy`, `@LastModifiedBy` using `AuditorAware`

---

## 🚫 What This Project Intentionally Avoids

To keep things simple and reduce boilerplate, this project **does NOT** use:

- `@Service` layer classes
- DTO (Data Transfer Objects)
- Mapper libraries (e.g., MapStruct)
- Clean architecture or layered structure

Instead, everything is written **directly in the controller and config classes** for clarity and focus.

🧠 **Why?**  
Because the goal is to **understand Spring Security**, not to build a perfect production-ready application architecture.

---

## 🛠️ Technologies Used

- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **MySQL Database**
- **BCrypt** password hashing
- **JPA Auditing** with `@CreatedBy` and `@LastModifiedBy`

---

## 📂 Project Structure

```text
src/
├── config/                      → Spring Security configuration
├── controller/                  → API endpoints (login, registration, etc.)
├── entity/                      → JPA entities (e.g., AppUser)
├── repository/                  → Spring Data JPA repositories
├── audit/                       → Custom AuditorAware for tracking changes
└── MiniBankBackendApplication   → Main application class
