provider "aws" {
  region = "us-east-1"  # Change this to your preferred AWS region
}

resource "aws_instance" "finance_app" {
  ami           = "ami-005fc0f236362e99f"  # Change this to a valid AMI ID for your region
  instance_type = "t2.micro"               # Change instance type as needed

  tags = {
    Name = "FinanceAppInstance"
  }

  # Optional: Add key_name if you want to access the instance via SSH
  key_name = "finance"  # Replace with your actual key name

  # Add a security group
  vpc_security_group_ids = [aws_security_group.finance_app_sg.id]
}

resource "aws_security_group" "finance_app_sg" {
  name        = "finance_app_sg"
  description = "Allow all inbound traffic for finance application"

  ingress {
    from_port   = 0          # Allow all inbound ports
    to_port     = 0          # Allow all inbound ports
    protocol    = "-1"       # Allow all protocols
    cidr_blocks = ["0.0.0.0/0"]  # Allow from anywhere (0.0.0.0/0)
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"  # Allow all outbound traffic
    cidr_blocks = ["0.0.0.0/0"]
  }
}

output "instance_ip" {
  value = aws_instance.finance_app.public_ip
}
