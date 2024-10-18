pipeline {
    agent any

    tools {
        maven 'MAVEN_HOME'
    }

    environment {
        DOCKER_CREDENTIALS_ID = 'Docker'
        GIT_REPO_URL = 'https://github.com/Raj000007/BANKING-FINANCE.git'
        AWS_CREDENTIALS_ID = 'aws-credentials'
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
                        ansiColor('xterm') {
                            sh '''
                                cd terraform/
                                terraform init
                                terraform apply -auto-approve -no-color
                            '''
                        }
                    }
                }
            }
        }

        stage('Get Instance IP') {
            steps {
                script {
                    // Capture the instance IP from Terraform output
                    def instance_ip = sh(
                        script: 'terraform output -raw instance_ip',
                        returnStdout: true
                    ).trim()
                    
                    if (!instance_ip) {
                        error 'Instance IP could not be retrieved from Terraform output! Ensure the instance is created and outputs are defined.'
                    }
                    
                    env.INSTANCE_IP = instance_ip
                }
            }
        }

        stage('Ansible Configuration') {
            steps {
                script {
                    if (env.INSTANCE_IP) {
                        // Run Ansible playbook using captured instance IP
                        sh """
                            ansible-playbook -i "${env.INSTANCE_IP}," --private-key ~/.ssh/finance.pem ansible/ansible-playbook.yml
                        """
                    } else {
                        error "No instance IP available for Ansible playbook run."
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs() // Clean workspace after pipeline run
        }
    }
}
