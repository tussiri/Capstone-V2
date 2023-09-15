pipeline {
    agent any
     environment {
    JAVA_HOME = '/Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home'
        }


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
            environment {
                JAVA_HOME = tool name: 'JDK 19', type: 'jdk'
                PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
            }
            steps {
                dir('backend') {
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
