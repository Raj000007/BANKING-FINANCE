-- Preload account data
INSERT INTO account (account_no, account_holder_name, account_type, balance) 
VALUES (1001, 'John Doe', 'Savings', 5000.00),
       (1002, 'Jane Smith', 'Checking', 3000.00),
       (1003, 'Alice Brown', 'Savings', 12000.00);

-- Preload policy data
INSERT INTO policy (policy_no, account_no, policy_name, policy_details) 
VALUES (2001, 1001, 'Life Insurance', 'Covers life term insurance for 10 years'),
       (2002, 1002, 'Health Insurance', 'Covers health insurance for family'),
       (2003, 1003, 'Car Insurance', 'Covers full damage for 5 years');
