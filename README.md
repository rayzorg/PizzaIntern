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

---

## application-dev.properties

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


