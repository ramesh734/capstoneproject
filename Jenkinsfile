pipeline {
    agent any

    tools {
        maven 'Maven'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Code pulled from GitHub'
            }
        }

        stage('Build') {
            steps {
                echo 'Building Spring Boot project'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running tests'
                sh 'mvn test'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image'
                sh 'docker build -t asset-app .'
            }
        }
    }

    post {
        success {
            echo 'SUCCESS 🚀'
        }
        failure {
            echo 'FAILED ❌'
        }
    }
}
