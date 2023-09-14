pipeline {
             stage('Build with Gradle') {
                 steps {
                     checkout scm
                 }
             }

             stage('Test with Gradle') {
                 steps {
                     sh './gradlew test'
                 }
             }
         }
     }