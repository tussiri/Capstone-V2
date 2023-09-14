pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Get the code from the version control system.
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                // Run Gradle build and test
                sh './gradlew clean build test'
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