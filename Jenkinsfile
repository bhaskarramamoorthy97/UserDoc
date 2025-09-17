pipeline {
    agent any

    environment {
        APP_EC2_IP = 'moviedatahub.online'
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
                sh """
        # Save Docker image and load on remote server
        docker save ${DOCKER_IMAGE} | ssh -i /root/MoviedataHub.pem -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} 'docker load'

        # Stop existing container, remove it, and run new one
        ssh -i /root/MoviedataHub.pem -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} '
            docker stop myapp-container || true
            docker rm myapp-container || true
            docker run -d --network mysql_default --name myapp-container -p 8080:8080 ${DOCKER_IMAGE}
        '
        """
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
