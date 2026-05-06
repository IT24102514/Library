document.getElementById('signupForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const name = document.getElementById('name').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const age = parseInt(document.getElementById('age').value);
    const gender = document.getElementById('gender').value;
    const city = document.getElementById('city').value;

    const errorDiv = document.getElementById('error');
    const successDiv = document.getElementById('success');
    const errorMessage = document.getElementById('errorMessage');
    const successMessage = document.getElementById('successMessage');
    const signupBtn = document.getElementById('signupBtn');
    const btnText = document.getElementById('btnText');
    const btnLoader = document.getElementById('btnLoader');

    errorDiv.classList.add('hidden');
    successDiv.classList.add('hidden');
    signupBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    const data = {
        name,
        email,
        password,
        age,
        gender,
        city
    };

    fetch('http://localhost:8081/api/members', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                successMessage.textContent = 'Account created successfully! Redirecting to login...';
                successDiv.classList.remove('hidden');
                document.getElementById('signupForm').reset();
                setTimeout(() => {
                    window.location.href = '/member-login';
                }, 2000);
            } else {
                errorMessage.textContent = result.message || 'Failed to create account';
                errorDiv.classList.remove('hidden');
                signupBtn.disabled = false;
                btnText.classList.remove('hidden');
                btnLoader.classList.add('hidden');
            }
        })
        .catch(error => {
            errorMessage.textContent = 'Signup failed: ' + error.message;
            errorDiv.classList.remove('hidden');
            signupBtn.disabled = false;
            btnText.classList.remove('hidden');
            btnLoader.classList.add('hidden');
        });
});