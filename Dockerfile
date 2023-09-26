# --- Frontend Build Stage ---
FROM node:18 AS frontend-build
LABEL authors="tumaussiri"
WORKDIR /app/frontend
COPY frontend/ .
RUN npm install
RUN npm run build

# --- Backend Build Stage ---
FROM openjdk:19-jdk-slim AS backend-build
WORKDIR /app/backend
COPY backend/ .

# Install xargs and other utilities if needed
RUN apt-get update && apt-get install -y findutils

# Set ARG and ENV for backend build stage
ARG SPRING_DATASOURCE_URL
ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL

ARG SPRING_DATASOURCE_USERNAME
ENV SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME

ARG SPRING_DATASOURCE_PASSWORD
ENV SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD

ARG APP_JWTSECRET
ENV APP_JWTSECRET=$APP_JWTSECRET

ARG APP_JWTEXPIRATIONINMS
ENV APP_JWTEXPIRATIONINMS=$APP_JWTEXPIRATIONINMS

RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# --- Final Stage ---
FROM openjdk:19-jdk
WORKDIR /app

# Set ARG and ENV for final stage
ARG SPRING_DATASOURCE_URL
ENV SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL

ARG SPRING_DATASOURCE_USERNAME
ENV SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME

ARG SPRING_DATASOURCE_PASSWORD
ENV SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD

ARG APP_JWTSECRET
ENV APP_JWTSECRET=$APP_JWTSECRET

ARG APP_JWTEXPIRATIONINMS
ENV APP_JWTEXPIRATIONINMS=$APP_JWTEXPIRATIONINMS

# Copy the Spring Boot JAR file from the backend build stage
COPY --from=backend-build /app/backend/build/libs/*.jar ./app.jar
# Copy the built frontend files from the frontend build stage
COPY --from=frontend-build /app/frontend/build/ ./static/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
