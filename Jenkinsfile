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
        stage('deploy') {
            steps {
                sh 'sudo systemctl stop user-service.service'
                sh 'rm -rf /opt/user-service/*.jar'
                sh 'cp ./target/*.jar /opt/user-service'
                sh 'sudo systemctl start user-service.service'
            }
        }
    }
}
