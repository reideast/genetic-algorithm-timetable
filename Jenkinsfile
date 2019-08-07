pipeline {
    agent {
        docker {
            image 'gradle:5.5.1-jdk8'
        }
    }
    stages {
        stage('Init') {
            steps {
                sh 'cp /home/jenkins_user/environmentvariables.config .ebextensions/environmentvariables.config'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew clean bootJar'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit 'build/test-results/**/*.xml'
                }
            }
        }
        stage('Launch') {
            steps {
                sh './gradlew bootRun'
            }
        }
    }
}
