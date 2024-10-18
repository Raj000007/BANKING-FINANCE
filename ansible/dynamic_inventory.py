#!/usr/bin/env python

import json

def main():
    # Manually set the IP addresses for the servers
    inventory = {
        'test': {
            'hosts': ['54.198.212.8'],  # Test server IP
        },
        'production': {
            'hosts': ['54.237.251.49'],  # Production server IP
        },
    }

    # Print the inventory in JSON format
    print(json.dumps(inventory))

if __name__ == '__main__':
    main()
