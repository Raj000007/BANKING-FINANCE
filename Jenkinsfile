pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME' // The name configured in Jenkins for Maven installation
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker' // Your Docker credentials ID
        GIT_REPO_URL = 'https://github.com/Raj000007/BANKING-FINANCE.git' // Your GitHub repository URL
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
                    def version = "v${env.BUILD_NUMBER}" // Or use timestamp: "v${new Date().format('yyyyMMddHHmmss')}"
                    sh "mvn clean package -Dversion=${version}"
                }
            }
        }

        stage('Docker Build and Push') {
            steps {
                script {
                    // Use the Jenkins build number as the version for the Docker image
                    def version = "v${env.BUILD_NUMBER}"
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        // Build the Docker image with versioning
                        sh "docker build -t rajesh159951/finance:${version} ."
                        // Push the Docker image to the repository
                        sh "docker push rajesh159951/finance:${version}"
                    }
                }
            }
        }
    }
}
       