provider "aws" {
  region = "us-east-1"  # Adjust to your preferred AWS region
}

resource "aws_instance" "finance_app" {
  ami           = "ami-005fc0f236362e99f"  # Adjust to a valid AMI ID for your region
  instance_type = "t2.micro"               # Adjust instance type as needed

  tags = {
    Name = "FinanceAppInstance"
  }

  # Optional: Add key_name if you want to access the instance via SSH
  key_name = "finance"  # Replace with your actual key name

  # Use the existing default security group
  vpc_security_group_ids = ["sg-08cf7d21ef5c6db4a"]
}

output "instance_ip" {
  value = aws_instance.finance_app.public_ip
}
