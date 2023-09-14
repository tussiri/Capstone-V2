pipeline {
    agent any

    stages {
        stage('Print Workspace') {
            steps {
                sh 'ls -al'
            }
        }

        stage('Checkout') {
            steps {
                // Get the code from the version control system.
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                // Run Gradle build and test
                sh './backend/gradlew clean build test'
            }
        }
    }

    post {
        always {
            // Archive the test results
            junit '**/build/test-results/**/*.xml'
        }
    }
}