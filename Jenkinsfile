pipeline {
    agent {
        docker {
            image 'gradle:5.5.1-jdk8'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew clean bootJar'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
//        stage('Test') {
//            steps {
//                sh './gradlew test'
//                echo 'step added to stage via Blue Ocean'
//            }
//            post {
//                always {
//                    junit 'target/surefire-reports/*.xml'
//                }
//            }
//        }
    }
}
