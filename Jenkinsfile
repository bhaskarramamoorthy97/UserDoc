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

        stage('Debug Info') {
    steps {
        sh """
        whoami
        pwd
        ls -la /root/
        ls -la /var/jenkins_home/
        """
    }
}

        stage('Deploy to App Server') {
            steps {
                withCredentials([file(credentialsId: 'myapp-creds-ec2user', variable: 'PEM_FILE')]) {
                    sh """
                    # Save Docker image and load on remote server
                    docker save ${DOCKER_IMAGE} | ssh -i ${PEM_FILE} -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} 'docker load'

                    # Stop existing container, remove it, and run new one
                    ssh -i ${PEM_FILE} -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} '
                        docker stop myapp-container || true
                        docker rm myapp-container || true
                        docker run -d --network mysql_default --name myapp-container -p 8080:8080 ${DOCKER_IMAGE}
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
