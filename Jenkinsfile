pipeline {
    agent any

    environment {
        APP_EC2_IP = 'ec2-52-90-147-94.compute-1.amazonaws.com' // Your EC2 public IP
        DOCKER_IMAGE = 'myfirstapp:latest'                   // Docker image name
        JAR_NAME = 'target/my-first-app.jar'      // Adjust to your actual JAR
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/bhaskarramamoorthy97/UserDoc.git', credentialsId: 'myapp-creds'
            }
        }

       stage('Build Jar (Docker Maven with Java 17)') {
            steps {
                sh """
                docker run --rm \
                  -v \$PWD:/workspace -w /workspace \
                  maven:3.9.0-openjdk-17 mvn clean package -DskipTests
                """
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Deploy to App Server') {
            steps {
                sshagent(['myapp-creds-ec2user']) {   // Use your Jenkins SSH credential ID
                    sh """
                    # Copy files to EC2 server
                    scp -o StrictHostKeyChecking=no ${JAR_NAME} ec2-user@${APP_EC2_IP}:/home/ec2-user/
                    scp -o StrictHostKeyChecking=no Dockerfile ec2-user@${APP_EC2_IP}:/home/ec2-user/

                    # Connect to EC2 and deploy Docker container
                    ssh -o StrictHostKeyChecking=no ec2-user@${APP_EC2_IP} '
                        docker stop myapp-container || true
                        docker rm myapp-container || true
                        docker build -t ${DOCKER_IMAGE} /home/ec2-user/
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
            echo "Deployment failed. Check the logs!"
        }
    }
}
