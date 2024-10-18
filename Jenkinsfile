pipeline {
    agent any
    tools {
        maven 'MAVEN_HOME' // Specify the Maven installation configured in Jenkins
    }
    environment {
        TF_DIR = 'terraform' // Directory where your main.tf is located
        ANSIBLE_DIR = 'ansible' // Directory where your Ansible files are located
        DOCKER_IMAGE = "rajesh159951/finance:${env.BUILD_ID}" // Docker image name
    }
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Raj000007/BANKING-FINANCE.git', branch: 'main'
            }
        }
        stage('Build and Test') {
            steps {
                script {
                    // Build the Maven project
                    sh 'mvn clean package'
                    // Run unit tests
                    sh 'mvn test'
                }
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    // Build the Docker image
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'Docker', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                    script {
                        // Login to Docker Hub using credentials stored in Jenkins
                        sh "docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
                        // Push the Docker image to Docker Hub
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }
        stage('Terraform Init & Apply') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials']]) {
                    script {
                        dir(TF_DIR) {
                            // Initialize Terraform and apply configuration to provision servers
                            sh 'terraform init'
                            sh 'terraform apply -auto-approve'
                        }
                    }
                }
            }
        }
        stage('Configure Test Server with Ansible') {
            steps {
                script {
                    dir(ANSIBLE_DIR) {
                        sh 'chmod +x dynamic_inventory.py'
                        // Retry running Ansible playbook if it fails
                        retry(3) {
                            sh 'ansible-playbook -i dynamic_inventory.py deploy.yml'
                        }
                    }
                }
            }
        }
        stage('Deploy to Test Server') {
            steps {
                script {
                    // Use the manually set IP instead of dynamic output
                    def serverIp = "54.198.212.8" // Set test server IP manually
                    // Deploy Docker image to the test server
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}'"
                }
            }
        }
        stage('Promote to Production') {
            steps {
                input message: 'Deploy to Production?', ok: 'Yes, deploy!'
                script {
                    dir(ANSIBLE_DIR) {
                        sh 'chmod +x dynamic_inventory.py'
                        // Retry running Ansible playbook if it fails
                        retry(3) {
                            sh 'ansible-playbook -i dynamic_inventory.py production.yml'
                        }
                    }
                    // Use the manually set IP instead of dynamic output
                    def prodIp = "54.237.251.49" // Set production server IP manually
                    // Deploy Docker image to the production server
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${prodIp} 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}'"
                }
            }
        }
    }
}
