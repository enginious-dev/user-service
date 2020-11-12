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
                sh 'sudo systemctl stop user-service.service'
                sh 'sleep 10'
                sh 'rm -rf /opt/user-service/*.jar'
                sh 'sleep 10'
                sh 'cp ./target/*.jar /opt/user-service'
                sh 'sleep 10'
                sh 'sudo systemctl start user-service.service'
            }
        }
    }
}
