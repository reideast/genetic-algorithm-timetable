pipeline {
    agent {
        docker {
            image 'andreweast2/build-openjdk-node:latest'
            args '-p 5000:5000' +
                    ' -u root:root' // TODO: HACK to prevent `/.npm` not being write-allowed on container
        }
    }

    parameters {
        string(name: 'Server_Port', defaultValue: '5000', description: 'HTTP port for Tomcat')
    }

    stages {
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
                withCredentials([
                        usernamePassword(credentialsId: 'rds.login', usernameVariable: 'RDS_USERNAME', passwordVariable: 'RDS_PASSWORD'),
                        string(credentialsId: 'rds.hostname', variable: 'RDS_HOSTNAME'),
                        string(credentialsId: 'rds.port', variable: 'RDS_PORT'),
                        string(credentialsId: 'rds.db_name', variable: 'RDS_DB_NAME')
                ]) {
                    withEnv([
                            "SERVER_PORT=${params.Server_Port}"
                    ]) {
                        sh './gradlew bootRun'
                    }
                }
            }
        }
    }
}
