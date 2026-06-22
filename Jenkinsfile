pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'your-registry.example.com'
        DOCKER_IMAGE_BACKEND = "${DOCKER_REGISTRY}/asset-management-backend"
        DOCKER_TAG = "${BUILD_NUMBER}"
        K8S_NAMESPACE = 'default'
        K8S_DEPLOYMENT = 'asset-backend'
    }

    parameters {
        choice(name: 'ACTION', choices: ['build-test', 'build-test-push', 'build-test-push-deploy'], description: 'Pipeline stage to run')
        string(name: 'DOCKER_TAG_OVERRIDE', defaultValue: '', description: 'Override Docker image tag (empty = build number)')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    def tag = params.DOCKER_TAG_OVERRIDE ?: env.DOCKER_TAG
                    env.FINAL_TAG = tag
                }
                echo "Building with Maven (tag: ${env.FINAL_TAG})..."
                bat 'mvn clean package'
            }
            post {
                success {
                    junit '**/target/surefire-reports/*.xml'
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build & Push') {
            when {
                expression { params.ACTION in ['build-test-push', 'build-test-push-deploy'] }
            }
            steps {
                script {
                    docker.withRegistry("https://${DOCKER_REGISTRY}", 'docker-registry-credentials') {
                        def img = docker.build("${DOCKER_IMAGE_BACKEND}:${env.FINAL_TAG}", '.')
                        img.push()
                        img.push('latest')
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            when {
                expression { params.ACTION == 'build-test-push-deploy' }
            }
            steps {
                script {
                    dir('k8s') {
                        sh """
                            sed -i 's|image:.*asset-management-backend.*|image: ${DOCKER_IMAGE_BACKEND}:${env.FINAL_TAG}|g' backend-deployment.yaml
                        """
                    }
                }
                withKubeConfig(cache: false, contextName: '', credentialsId: 'kubeconfig-credentials', namespace: K8S_NAMESPACE) {
                    sh 'kubectl apply -f k8s/'
                    sh "kubectl rollout status deployment/${K8S_DEPLOYMENT} -n ${K8S_NAMESPACE} --timeout=300s"
                }
            }
            post {
                success {
                    echo "Deployment ${K8S_DEPLOYMENT} rolled out successfully."
                }
                failure {
                    echo "Rollback triggered — deployment failed."
                    sh "kubectl rollout undo deployment/${K8S_DEPLOYMENT} -n ${K8S_NAMESPACE}"
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
        failure {
            emailext(
                subject: "[Jenkins] ${env.JOB_NAME} #${env.BUILD_NUMBER} - FAILED",
                body: "Pipeline ${env.BUILD_URL} failed.",
                to: 'team@example.com'
            )
        }
    }
}
