# Stage 1: Build dự án bằng Maven
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

# Copy file cấu hình Maven và tải thư viện trước để cache (tăng tốc build)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy toàn bộ code và build ra file JAR
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng với JRE nhẹ
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy file JAR từ stage build qua
COPY --from=build /app/target/*.jar app.jar

# Khai báo các biến môi trường (Triều có thể ghi đè lúc chạy)
ENV DBMS_CONNECTION=jdbc:postgresql://localhost:5432/edu_sentinel_db
ENV DBMS_USERNAME=postgres
ENV DBMS_PASSWORD=postgres

EXPOSE 8085

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]