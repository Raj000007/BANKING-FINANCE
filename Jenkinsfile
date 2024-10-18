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
                    // Run Ansible playbook to configure the test server
                    dir(ANSIBLE_DIR) {
                        // Ensure dynamic_inventory.py has executable permissions
                        sh 'chmod +x dynamic_inventory.py'
                        // Run Ansible playbook
                        sh 'ansible-playbook -i dynamic_inventory.py deploy.yml'
                    }
                }
            }
        }
        stage('Deploy to Test Server') {
            steps {
                script {
                    // Retrieve the test server IP from Terraform output
                    def serverIp = sh(script: "terraform output -raw test_server_ip", returnStdout: true).trim()
                    echo "Test Server IP: ${serverIp}" // Debugging line
                    
                    // Check if the serverIp is empty or contains invalid characters
                    if (serverIp && serverIp.contains(".")) { // Ensure it contains a dot for a valid IP
                        // Deploy Docker image to the test server
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}'"
                    } else {
                        error "Failed to retrieve a valid test server IP: ${serverIp}."
                    }
                }
            }
        }
        
        stage('Promote to Production') {
            steps {
                input message: 'Deploy to Production?', ok: 'Yes, deploy!'
                script {
                    // Run Ansible playbook to configure the production server
                    dir(ANSIBLE_DIR) {
                        // Ensure dynamic_inventory.py has executable permissions
                        sh 'chmod +x dynamic_inventory.py'
                        // Run Ansible playbook for production
                        sh 'ansible-playbook -i dynamic_inventory.py production.yml'
                    }
                    // Retrieve the production server IP from Terraform output
                    def prodIp = sh(script: "terraform output -raw prod_server_ip", returnStdout: true).trim()
                    echo "Production Server IP: ${prodIp}" // Debugging line
                    
                    // Check if the prodIp is empty or contains invalid characters
                    if (prodIp && prodIp.contains(".")) { // Ensure it contains a dot for a valid IP
                        // Deploy Docker image to the production server
                        sh "ssh -o StrictHostKeyChecking=no ubuntu@${prodIp} 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}'"
                    } else {
                        error "Failed to retrieve a valid production server IP: ${prodIp}."
                    }
                }
            }
        }
        
    }
}
