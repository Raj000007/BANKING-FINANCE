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
        stage('Print Current User') {
            steps {
                sh 'whoami'
            }
        }
        stage('Build and Test') {
            steps {
                sh 'mvn clean package'  // Build the Maven project
                sh 'mvn test'  // Run unit tests
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
                        // Make sure to use the new inventory file
                        sh 'chmod +x dynamic_inventory.py'
                        // Run Ansible playbook using the inventory file
                        sh 'ansible-playbook -i dynamic_inventory.py deploy.yml'
                    }
                }
            }
        }
        stage('Deploy to Test Server') {
            steps {
                script {
                    // Manually retrieve the test server IP address
                    def serverIp = "54.198.212.8"  // Test server IP
                    // Deploy Docker image to the test server
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} 'docker run -d -p 8825:8080 ${DOCKER_IMAGE}'"
                }
            }
        }
       
    }
}
