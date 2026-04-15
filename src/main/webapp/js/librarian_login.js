document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const errorDiv = document.getElementById('error');
    const errorMessage = document.getElementById('errorMessage');
    const loginBtn = document.getElementById('loginBtn');
    const btnText = document.getElementById('btnText');
    const btnLoader = document.getElementById('btnLoader');

    errorDiv.classList.add('hidden');
    loginBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    fetch('http://localhost:8081/api/staff/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const staff = data.data;
localStorage.setItem("librarianId",staff.id);
                   window.location.href = '/librarian-dashboard';

            } else {
                errorMessage.textContent = data.message || 'Invalid email or password';
                errorDiv.classList.remove('hidden');
                loginBtn.disabled = false;
                btnText.classList.remove('hidden');
                btnLoader.classList.add('hidden');
            }
        })
        .catch(error => {
            errorMessage.textContent = 'Login failed: ' + error.message;
            errorDiv.classList.remove('hidden');
            loginBtn.disabled = false;
            btnText.classList.remove('hidden');
            btnLoader.classList.add('hidden');
        });
});