pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker' // Docker credentials ID
        GIT_REPO_URL = 'https://github.com/Raj000007/BANKING-FINANCE.git' // GitHub repository URL
        AWS_CREDENTIALS_ID = 'aws-credentials' // AWS credentials ID
        ANSIBLE_CREDENTIALS_ID = 'Ansible'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: "${GIT_REPO_URL}"
            }
        }

        stage('Build') {
            steps {
                script {
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
                        sh "docker build -t rajesh159951/finance:${version} ."
                        sh "docker push rajesh159951/finance:${version}"
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

        stage('Create Ansible Inventory') {
            steps {
                script {
                    def instance_ip = sh(
                        script: 'terraform output -raw instance_ip',
                        returnStdout: true
                    ).trim()
                    env.INSTANCE_IP = instance_ip
                    
                    // Create the inventory.yml file dynamically
                    writeFile file: 'inventory.yml', text: """
                    all:
                      hosts:
                        ${env.INSTANCE_IP}:
                          ansible_user: ubuntu
                          ansible_ssh_private_key_file: ~/.ssh/finance.pem
                    """
                }
            }
        }

        stage('Ansible Configuration') {
            steps {
                script {
                    withCredentials([sshUserPrivateKey(credentialsId: "${ANSIBLE_CREDENTIALS_ID}", keyFileVariable: 'SSH_KEY')]) {
                        sh '''
                            ansible-playbook -i inventory.yml --private-key ${SSH_KEY} ansible/ansible-playbook.yml
                        '''
                    }
                }
            }
        }
    }
}
