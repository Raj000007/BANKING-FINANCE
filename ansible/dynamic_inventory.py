#!/usr/bin/env python

import json
import sys

def main():
    # Define your inventory
    inventory = {
        "test": {  # Define the 'test' group
            "hosts": ["54.198.212.8"],  # Your target host
            "vars": {
                "ansible_ssh_user": "ubuntu",  # Set SSH user to 'ubuntu'
                "ansible_ssh_private_key_file": "root/.ssh/id_rsa"  # Specify your private key
            }
        },
        "_meta": {
            "hostvars": {
                "54.198.212.8": {
                    "ansible_host": "54.198.212.8",
                    "ansible_ssh_user": "ubuntu",  # Ensure this is also set here
                    "ansible_ssh_private_key_file": "root/.ssh/id_rsa"  # Specify your private key here too
                }
            }
        }
    }
    
    if len(sys.argv) > 1:
        if sys.argv[1] == "--list":
            print(json.dumps(inventory))
        elif sys.argv[1] == "--host":
            print(json.dumps(inventory["_meta"]["hostvars"][sys.argv[2]]))
    else:
        print("Usage: {} --list|--host <hostname>".format(sys.argv[0]))

if __name__ == '__main__':
    main()
