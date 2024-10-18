document.getElementById('balanceForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const accountNumber = document.getElementById('accountNumber').value;
    const accountHolderName = document.getElementById('accountHolderName').value;

    // Send request to backend to get balance
    fetch(`/api/accounts/balance?accountNumber=${accountNumber}&accountHolderName=${accountHolderName}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Account not found or mismatch!');
            }
            return response.json();
        })
        .then(data => {
            document.getElementById('balanceResult').innerText = `Balance: $${data.balance}`;
        })
        .catch(error => {
            document.getElementById('balanceResult').innerText = error.message;
        });
});
