pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the Git repository
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                // Use the Gradle Wrapper to build and test the project
                sh './gradlew clean build'
            }
        }
    }
}