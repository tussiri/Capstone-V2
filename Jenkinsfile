pipeline {
    agent any

    stages {
        stage('Print Workspace Before Checkout') {
            steps {
                sh 'ls -al'
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Print Workspace After Checkout') {
            steps {
                sh 'ls -al'
            }
        }

        stage('Debug: Print Directory Structure') {
            steps {
                sh 'find . -type d'
            }
        }

        stage('Build and Test') {
            steps {
                sh './backend/gradlew clean build test'
            }
        }
    }

    post {
        always {
            junit '**/build/test-results/**/*.xml'
        }
    }
}
