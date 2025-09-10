pipeline {
    agent any

    environment {
        APP_EC2_IP = 'ec2-54-167-15-63.compute-1.amazonaws.com'
        DOCKER_IMAGE = 'myfirstapp:latest'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bhaskarramamoorthy97/UserDoc.git', credentialsId: 'myapp-creds'
            }
        }

        stage('Build & Dockerize') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Deploy to App Server') {
            steps {
                sshagent(['myapp-creds-ec2user']) {
                    sh """
                    # Copy Docker image to EC2 server (optional: you can push to a registry instead)
                    docker save ${DOCKER_IMAGE} | bzip2 | ssh -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} 'bunzip2 | docker load'

                    # Run the container on EC2
                    ssh -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} '
                        docker stop myapp-container || true
                        docker rm myapp-container || true
                        docker run -d -p 8080:8080 --name myapp-container ${DOCKER_IMAGE}
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo "Deployment successful!"
        }
        failure {
            echo "Deployment failed. Check logs!"
        }
    }
}
