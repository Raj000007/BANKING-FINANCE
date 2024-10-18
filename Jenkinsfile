pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME' // The name configured in Jenkins for Maven installation
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker' // Your Docker credentials ID
        GIT_REPO_URL = 'https://github.com/Raj000007/BANKING-FINANCE.git' // Your GitHub repository URL
        AWS_CREDENTIALS_ID = 'aws_credentials' // Your AWS credentials ID (for Terraform)
        ANSIBLE_CREDENTIALS_ID = 'ansible_ssh' // Ansible credentials ID
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout code from GitHub
                git branch: 'main', url: "${GIT_REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                script {
                    // Generate a version number based on the build number
                    def version = "v${env.BUILD_NUMBER}" 
                    sh "mvn clean package -Dversion=${version}"
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    def version = "v${env.BUILD_NUMBER}"
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        // Build the Docker image with versioning
                        sh "docker build -t rajesh159951/finance:${version} ."
                        // Push the Docker image to Docker Hub with version tag
                        sh "docker push rajesh159951/finance:${version}"
                        // Also tag as "latest"
                        sh "docker tag rajesh159951/finance:${version} rajesh159951/finance:latest"
                        sh "docker push rajesh159951/finance:latest"
                    }
                }
            }
        }

        stage('Terraform Init and Apply') {
            steps {
                script {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${AWS_CREDENTIALS_ID}"]]) {
                        sh '''
                            cd terraform/
                            terraform init
                            terraform apply -auto-approve
                        '''
                    }
                }
            }
        }

     
    }
}
