pipeline {
    agent any
    environment {
        // Update JAVA_HOME to the correct path
        JAVA_HOME = '/Users/tumaussiri/Library/Java/JavaVirtualMachines/corretto-19.0.2/Contents/Home'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Set Permissions') {
            steps {
                // Ensure backend/gradlew exists before setting permissions
                sh 'if [ -f "backend/gradlew" ]; then chmod +x backend/gradlew; fi'
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
            environment {
                // Make sure 'JDK 19' is configured in Jenkins
                JAVA_HOME = tool name: 'JDK 19', type: 'jdk'
                PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
            }
            steps {
                dir('backend') {
                    // Ensure the directory exists before running gradlew
                    sh 'if [ -d "backend" ]; then ./gradlew clean build test; fi'
                }
            }
        }
    }

    post {
        always {
            echo 'This will always run'
        }
        success {
            echo 'Pipeline succeeded! Well Done'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
