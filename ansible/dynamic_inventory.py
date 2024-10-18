#!/usr/bin/env python3
import subprocess
import json

def get_tf_output():
    output = subprocess.check_output(['terraform', 'output', '-json'])
    return json.loads(output)

def main():
    tf_output = get_tf_output()

    inventory = {
        'test': {
            'hosts': [tf_output['test_server_ip']['value']],
            'vars': {
                'ansible_ssh_user': 'ubuntu'
            }
        },
        'production': {
            'hosts': [tf_output['prod_server_ip']['value']],
            'vars': {
                'ansible_ssh_user': 'ubuntu'
            }
        }
    }

    print(json.dumps(inventory))

if __name__ == '__main__':
    main()
