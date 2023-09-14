pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Set Permissions') {
                    steps {
                        // Set execute permissions for gradlew
                        sh 'chmod +x backend/gradlew'
                    }
                }

        stage('Print Workspace Before Checkout') {
            steps {
                sh 'ls -al'
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
                dir('backend') { // Navigate to the backend directory
                    sh './gradlew clean build test'
                }
            }
        }
    }

    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
