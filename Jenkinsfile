pipeline {
    agent any

    tools {
        jdk 'Java17'
        maven 'Maven'
    }

    stages {
        stage('Checkout Code from Github') {
            steps {
                echo 'Pulling from Github'
                git branch: 'master', credentialsId: 'gitcred', url: 'https://github.com/ramesh734/capstoneproject.git'
            }
        }

        stage('Build project') {
            steps {
                echo 'Building project to create JAR'
                bat 'mvn clean package'
            }
        }

        stage('Create Docker Image') {
            steps {
                echo 'Creating Docker Image'
                bat 'docker build -t asset-backend:latest .'
            }
        }

        stage('Start Minikube') {
            steps {
                echo 'Starting Minikube'
                bat 'minikube start'
            }
        }

        stage('Load Image to Minikube') {
            steps {
                echo 'Loading Image to Minikube'
                bat 'minikube image load asset-backend:latest'
            }
        }

        stage('Deploy Application to k8') {
            steps {
                echo 'Deploying Application'
                bat 'kubectl apply -f k8s/'

                echo 'Checking the pods status'
                bat 'kubectl get pods'

                echo 'Checking the service status'
                bat 'kubectl get svc'
            }
        }

        stage('Wait for Pods') {
            steps {
                echo 'Waiting for the pods to be ready'
                bat 'kubectl wait --for=condition=Ready pod --all --timeout=120s'
            }
        }

        stage('Access the Application in k8') {
            steps {
                echo 'Getting Application URL'
                bat 'minikube service asset-backend --url'
            }
        }

        stage('Open k8 Dashboard') {
            steps {
                echo 'Enabling metrics-server'
                bat 'minikube addons enable metrics-server'

                echo 'Opening K8s Dashboard'
                bat 'minikube dashboard'
            }
        }
    }

    post {
        always {
            echo 'Build and Run is successful!'
        }
        failure {
            echo 'The Pipeline failed :('
        }
    }
}
