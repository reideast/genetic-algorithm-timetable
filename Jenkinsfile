pipeline {
    agent {
        docker {
            image 'andreweast2/build-openjdk-node:latest'
            args '-p 5000:5000' + // TODO: make a _second_ container for launching the app, and set that up with port pass-through. Also, Server_Port should be parameterised?? Or, not (and hard-code that to the image)
                    ' --user root:root' // Note: This overrides jenkins hard-coded `--user 998:996`, and is one of the recognized workarounds
        }
    }

    parameters {
        booleanParam(name: 'Run_Server', defaultValue: true, description: 'Start up the app in a local container using Spring\'s Tomcat bootJar')
        string(name: 'Server_Port', defaultValue: '5000', description: 'HTTP port for Tomcat')
    }

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean cleanNodeModules bootJar'
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
//
//                stash includes: 'build/libs/*.jar', name: 'built-jar'
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
            when {
//                beforeAgent true
                expression {
                    params.Run_Server
                }
            }
//            agent {
//                docker {
//                    image 'andreweast2/build-openjdk-node:latest'
//                    args '-p 5000:5000'
//                }
//            }
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
//                        unstash 'built-jar'
                        sh './gradlew bootRun'
                    }
                }
            }
        }
    }
//    post {
//        always {
//            cleanWs()
//        }
//    }
}
