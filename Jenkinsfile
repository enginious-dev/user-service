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
                sh 'cp ./target/*.jar /opt/user-service'
            }
        }
    }
}
