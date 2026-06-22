# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# Copy Maven wrapper and pom first for better cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build WAR/JAR
RUN ./mvnw clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV TZ=Asia/Thimphu
ENV SPRING_PROFILES_ACTIVE=dev

# Copy generated WAR/JAR from build stage
COPY --from=builder /app/target/*.war /app/app.war

EXPOSE 8081

ENTRYPOINT ["java", \
  "-Duser.timezone=Asia/Thimphu", \
  "-jar", \
  "/app/app.war"]
