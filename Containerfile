FROM eclipse-temurin:25-jdk-jammy

# Set working directory
WORKDIR /app

# Copy Maven wrapper and config first (for layer caching)
COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x mvnw
# Download dependencies
RUN ./mvnw dependency:resolve

# Copy source code
COPY src ./src

# Expose Spring Boot port
EXPOSE 8080

# Run Spring Boot
CMD ["./mvnw", "spring-boot:run"]
