const API_BASE_URL = 'http://localhost:8081/api/members';

let memberId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadProfile();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('profileForm').addEventListener('submit', handleUpdate);
    document.getElementById('deleteBtn').addEventListener('click', openDeleteModal);
    document.getElementById('logoutBtn').addEventListener('click', openLogoutModal);
    document.getElementById('cancelDeleteBtn').addEventListener('click', closeDeleteModal);
    document.getElementById('confirmDeleteBtn').addEventListener('click', handleDelete);
    document.getElementById('cancelLogoutBtn').addEventListener('click', closeLogoutModal);
    document.getElementById('confirmLogoutBtn').addEventListener('click', handleLogout);
}

function loadProfile() {
    memberId = localStorage.getItem("userId");

    fetch(`${API_BASE_URL}/${memberId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const member = data.data;
                document.getElementById('name').value = member.name;
                document.getElementById('email').value = member.email;
                document.getElementById('age').value = member.age;
                document.getElementById('gender').value = member.gender;
                document.getElementById('city').value = member.city;
            } else {
                showError(data.message);
            }
        })
        .catch(error => {
            showError('Failed to load profile: ' + error.message);
        });
}

function handleUpdate(e) {
    e.preventDefault();

    const updateBtn = document.getElementById('updateBtn');
    const btnText = document.getElementById('updateBtnText');
    const btnLoader = document.getElementById('updateBtnLoader');

    updateBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    const data = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        age: parseInt(document.getElementById('age').value),
        gender: document.getElementById('gender').value,
        city: document.getElementById('city').value
    };

    const password = document.getElementById('password').value;
    if (password) {
        data.password = password;
    }

    fetch(`${API_BASE_URL}/${memberId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                showSuccess('Profile updated successfully');
                document.getElementById('password').value = '';
            } else {
                showError(result.message);
            }
        })
        .catch(error => {
            showError('Failed to update profile: ' + error.message);
        })
        .finally(() => {
            updateBtn.disabled = false;
            btnText.classList.remove('hidden');
            btnLoader.classList.add('hidden');
        });
}

function openDeleteModal() {
    document.getElementById('deleteModal').classList.add('active');
}

function closeDeleteModal() {
    document.getElementById('deleteModal').classList.remove('active');
}

function handleDelete() {
    const confirmBtn = document.getElementById('confirmDeleteBtn');
    const btnText = document.getElementById('deleteBtnText');
    const btnLoader = document.getElementById('deleteBtnLoader');

    confirmBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    fetch(`${API_BASE_URL}/${memberId}`, {
        method: 'DELETE'
    })
        .then(response => response.json())
        .then(result => {
            if (result.success) {
                showSuccess('Account deleted successfully');
                setTimeout(() => {
                    window.location.href = '/';
                }, 1500);
            } else {
                showError(result.message);
                confirmBtn.disabled = false;
                btnText.classList.remove('hidden');
                btnLoader.classList.add('hidden');
            }
        })
        .catch(error => {
            showError('Failed to delete account: ' + error.message);
            confirmBtn.disabled = false;
            btnText.classList.remove('hidden');
            btnLoader.classList.add('hidden');
        });
}

function openLogoutModal() {
    document.getElementById('logoutModal').classList.add('active');
}

function closeLogoutModal() {
    document.getElementById('logoutModal').classList.remove('active');
}

function handleLogout() {
    localStorage.clear()
    setTimeout(()=>{
        window.location.href = '/';
    },2000)
}

function showSuccess(message) {
    const alert = document.getElementById('successAlert');
    const messageEl = document.getElementById('successMessage');
    messageEl.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => {
        alert.classList.add('hidden');
    }, 5000);
}

function showError(message) {
    const alert = document.getElementById('errorAlert');
    const messageEl = document.getElementById('errorMessage');
    messageEl.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => {
        alert.classList.add('hidden');
    }, 5000);
}