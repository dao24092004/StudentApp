# Stage 1: Build ứng dụng
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app

# Cài đặt Python và các phụ thuộc cần thiết
RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    python3-dev \
    build-essential \
    libpq-dev \
    && rm -rf /var/lib/apt/lists/*

# Cài đặt psycopg2 qua pip
RUN pip3 install psycopg2-binary

# Copy file JAR từ stage build
COPY --from=builder /app/target/TruongHoc-0.0.1-SNAPSHOT.jar app.jar

# Copy file schedule.py vào container
COPY src/main/resources/scripts/schedule.py /app/schedule.py

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]