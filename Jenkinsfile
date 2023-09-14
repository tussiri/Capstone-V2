pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Get the code from the version control system.
                checkout scm
            }
        }

        stage('Build and Unit Test') {
            steps {
                // Navigate to the backend directory
                dir('backend') {
                    // Use Gradle to build the project and run unit tests
                    sh './gradlew clean build test'
                }
            }
        }

        stage('Docker Build') {
            steps {
                // Navigate to the backend directory
                dir('backend') {
                    // Build the Docker image
                    sh 'docker build -t capstone-backend .'
                }
            }
        }

        stage('Docker Run') {
            steps {
                // Use Jenkins credentials for sensitive information
                withCredentials([
                    string(credentialsId: 'springDatasourceUrl', variable: 'SPRING_DATASOURCE_URL'),
                    string(credentialsId: 'springDatasourceUsername', variable: 'SPRING_DATASOURCE_USERNAME'),
                    string(credentialsId: 'springDatasourcePassword', variable: 'SPRING_DATASOURCE_PASSWORD'),
                    string(credentialsId: 'appJwtSecret', variable: 'APP_JWTSECRET'),
                    string(credentialsId: 'appJwtExpirationInMs', variable: 'APP_JWTEXPIRATIONINMS')
                ]) {
                    // Run the Docker container with environment variables
                    sh '''docker run -d -p 8080:8080 \
                        --env SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
                        --env SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME \
                        --env SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD \
                        --env APP_JWTSECRET=$APP_JWTSECRET \
                        --env APP_JWTEXPIRATIONINMS=$APP_JWTEXPIRATIONINMS \
                        capstone-backend'''
                }
            }
        }
    }

}
