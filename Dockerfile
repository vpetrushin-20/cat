# Базовый образ
FROM openjdk:11-slim

# Копирование файлов
COPY src /app/src
COPY pom.xml /app

# Установка Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Установка зависимостей
WORKDIR /app
RUN mvn dependency:resolve

# Сборка проекта
RUN mvn package

# Запуск тестов (пример)
CMD ["mvn", "test"]