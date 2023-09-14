pipeline {
    agent any

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

}
