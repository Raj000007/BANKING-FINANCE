provider "aws" {
    region = "us-east-1" // Change as needed
}

resource "aws_instance" "test" {
    ami           = "ami-005fc0f236362e99f" // Change to your Ubuntu AMI
    instance_type = "t2.micro"
    key_name      = "finance" // Change to your key pair

    tags = {
        Name = "TestServer"
    }
}

output "test_server_ip" {
    value = aws_instance.test.public_ip
}

resource "aws_instance" "prod" {
    ami           = "ami-005fc0f236362e99f" // Change to your Ubuntu AMI
    instance_type = "t2.micro"
    key_name      = "finance" // Change to your key pair

    tags = {
        Name = "ProdServer"
    }
}

output "prod_server_ip" {
    value = aws_instance.prod.public_ip
}
