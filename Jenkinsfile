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
                    def r = sh script: 'sudo systemctl stop user-service.service', returnStdout: true
                    return (r == 0);
                }
                waitUntil {
                    def r = sh script: 'rm -rf /opt/user-service/*.jar', returnStdout: true
                    return (r == 0);
                }
                waitUntil {
                    def r = sh script: 'cp ./target/*.jar /opt/user-service', returnStdout: true
                    return (r == 0);
                }
                waitUntil {
                    def r = sh script: 'sudo systemctl start user-service.service', returnStdout: true
                    return (r == 0);
                }
            }
        }
    }
}
