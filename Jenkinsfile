pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Deploy') {
            steps {
                waitUntil {
                    sh 'sudo systemctl stop user-service.service'
                }
                waitUntil {
                    sh 'rm -rf /opt/user-service/*.jar'
                }
                waitUntil {
                    sh 'cp ./target/*.jar /opt/user-service'
                }
                waitUntil {
                    sh 'sudo systemctl start user-service.service'
                }
            }
        }
    }
}
