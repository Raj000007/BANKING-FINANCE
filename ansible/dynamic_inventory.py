#!/usr/bin/env python

import json
import subprocess

def main():
    # Get the output from Terraform
    tf_output = json.loads(subprocess.check_output(['terraform', 'output', '-json']).decode('utf-8'))

    # Construct the inventory structure
    inventory = {
        'test': {
            'hosts': [tf_output['test_server_ip']['value']],
        },
        'production': {
            'hosts': [tf_output['prod_server_ip']['value']],
        },
    }

    # Print the inventory in the required format
    print(json.dumps(inventory))

if __name__ == '__main__':
    main()
