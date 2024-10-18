pipeline {
    agent any
    stages {
        stage('Checkout Code') {
            steps {
                git url: 'https://github.com/Raj000007/BANKING-FINANCE.git', branch: 'main'
            }
        }
        stage('Build and Test') {
            steps {
                sh 'mvn clean package'  
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    def imageName = "rajesh159951/finance:${env.BUILD_ID}"
                    sh "docker build -t ${imageName} ."
                }
            }
        }
        stage('Provision Test Server') {
            steps {
                script {
                    sh 'terraform init'
                    sh 'terraform apply -auto-approve'
                }
            }
        }
        stage('Configure Test Server') {
            steps {
                script {
                    sh 'ansible-playbook -i dynamic_inventory.py deploy.yml'
                }
            }
        }
        stage('Deploy to Test Server') {
            steps {
                script {
                    def serverIp = sh(script: "terraform output test_server_ip", returnStdout: true).trim()
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${serverIp} 'docker run -d -p 8080:8080 your-image-name:${env.BUILD_ID}'"
                }
            }
        }
        stage('Run Automated Tests') {
            steps {
                // Add commands to run your automated tests here
            }
        }
        stage('Promote to Production') {
            steps {
                input 'Deploy to Production?'
                script {
                    sh 'ansible-playbook -i dynamic_inventory.py production.yml'
                    def prodIp = sh(script: "terraform output prod_server_ip", returnStdout: true).trim()
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@${prodIp} 'docker run -d -p 8080:8080 your-image-name:${env.BUILD_ID}'"
                }
            }
        }
        stage('Setup Monitoring') {
            steps {
                // Run Prometheus and Grafana setup scripts or commands
            }
        }
    }
}
