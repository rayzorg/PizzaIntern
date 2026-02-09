# pizza-app – Spring Boot Application

The application uses **Spring Profiles** to manage configuration securely and to avoid committing sensitive information (such as passwords and secrets) to the repository.

---

## ⚙️ Configuration Management

The application uses the standard Spring Boot configuration mechanism:

- `application.properties`  
  Contains **all required configuration keys**, but **no sensitive values**.  
  This file **is committed** to the repository.

- `application-<profile>.properties` (e.g. `application-dev.properties`)  
  Contains **environment-specific and sensitive values** (database credentials, mail passwords, JWT secret, …).  
  These files **must NOT be committed** and should be added to `.gitignore`.

  Spring will always:
1. Load `application.properties`
2. Then override/extend it with `application-<profile>.properties` if a profile is active

---

## 📄 application.properties (committed)

The following configuration keys are required by the application:

```properties

### Database

spring.datasource.url=        # JDBC URL of the database (host, port, database name)
spring.datasource.username=   # Username used to connect to the database
spring.datasource.password=   # Password used to connect to the database

spring.jpa.hibernate.ddl-auto=update   # Defines how Hibernate handles database schema updates
spring.jpa.properties.hibernate.format_sql=true  # Formats SQL queries in the logs for readability
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect  # Hibernate dialect for MySQL


### Email (sending an email when placing an order and when contacting)

spring.mail.host=smtp.gmail.com        # SMTP server used for sending emails
spring.mail.port=587                   # SMTP port (587 for TLS)
spring.mail.username=                  # Email account username used for authentication
spring.mail.password=                  # Email account password or app-specific password
spring.mail.properties.mail.smtp.auth=true              # Enables SMTP authentication
spring.mail.properties.mail.smtp.starttls.enable=true   # Enables STARTTLS encryption
spring.mail.properties.mail.smtp.starttls.required=true # Requires STARTTLS to be used

### JWT (for authenticaten and authorization)

jwt.secret=            # Secret key used to sign and validate JWT tokens
jwt.expiration=3600000 # JWT expiration time in milliseconds (e.g. 1 hour)

``` 

## 📄 application-dev.properties (non-committed)

For the active **`dev`** Spring profile, the following properties **must be defined with actual values**
in `application-dev.properties`.

These properties are intentionally **not set** in `application.properties` because they contain
**environment-specific or sensitive information**.

```properties
# Database
spring.datasource.url=        # JDBC URL of the development database
spring.datasource.username=   # Database username for the dev environment
spring.datasource.password=   # Database password for the dev environment

# Email (SMTP)
spring.mail.username=         # Email account username used to send emails
spring.mail.password=         # Email account password or app-specific password

# JWT (Security)
jwt.secret=                   # Secret key used to sign and validate JWT tokens

```
---
## Activating the `dev` Spring Profile in Eclipse

To run the application with the **`dev`** profile in **Eclipse**, follow these steps:

1. Right-click the project in Eclipse
2. Select **Run As → Run Configurations…**
3. In the left menu, select **Spring Boot App**
4. Choose your application configuration
5. Open the **Arguments** tab
6. In the **VM arguments** field, add: -Dspring.profiles.active=dev
7. Click **Apply**
8. Click **Run**

When the application starts, Spring will:
1. Load `application.properties`
2. Then load `application-dev.properties`

You can verify this in the console output: The following profiles are active: dev

## Configuration Guide

This section explains how to configure the application components that require
environment-specific or sensitive values.  
All sensitive configuration must be placed in the active Spring profile
(e.g. `application-dev.properties`) and must **not** be committed to the repository.

In the dev profile, spring.jpa.hibernate.ddl-auto=update is used
Seed data is automatically inserted on first startup

---

## Database Configuration

The application uses a relational database (MySQL).

### Required steps:
1. Create a local MySQL database (e.g. `internproject`)
2. Create a database user with access to this database
3. Configure the following properties in `application-dev.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/internproject
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password





