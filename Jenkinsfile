pipeline {
    agent any

    environment {
        // Define environment variables if needed
    }

    stages {
        stage('Checkout') {
            steps {
                // Get the code from the version control system.
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                // Navigate to the backend directory and build
                dir('backend') {
                    sh './gradlew clean build'  // Gradle build
                }
            }
        }


        stage('Run Tests') {
            steps {
                // Navigate to the backend directory and run tests
                dir('backend') {
                    sh './gradlew test'  // Gradle test command
                }
            }
        }

        stage('Deploy') {
            steps {
                // Add your deployment steps here
            }
        }
    }

    post {
        always {
            // Actions to perform after the pipeline finishes, like sending notifications
        }
    }
}
