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

### Database

spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=
spring.jpa.properties.hibernate.format_sql=
spring.jpa.database-platform=

### Database

spring.mail.host=
spring.mail.port=
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=
spring.mail.properties.mail.smtp.starttls.enable=
spring.mail.properties.mail.smtp.starttls.required=

### JWT

jwt.secret=
jwt.expiration=

