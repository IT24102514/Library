const API_BASE_URL = 'http://localhost:8081/api/staff';

let staffId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadProfile();
    setupEventListeners();
});

function setupEventListeners() {
    const logoutBtn = document.getElementById('logoutBtn');
    const cancelLogoutBtn = document.getElementById('cancelLogoutBtn');
    const confirmLogoutBtn = document.getElementById('confirmLogoutBtn');

    if (logoutBtn) logoutBtn.addEventListener('click', openLogoutModal);
    if (cancelLogoutBtn) cancelLogoutBtn.addEventListener('click', closeLogoutModal);
    if (confirmLogoutBtn) confirmLogoutBtn.addEventListener('click', handleLogout);
}

function loadProfile() {
    staffId = localStorage.getItem("librarianId");

    if (!staffId) {
        showError('No librarian session found. Please login again.');
        setTimeout(() => {
            window.location.href = '/library-login';
        }, 2000);
        return;
    }

    fetch(`${API_BASE_URL}/${staffId}`)
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                const staff = data.data;
                document.getElementById('name').textContent = staff.name || 'N/A';
                document.getElementById('email').textContent = staff.email || 'N/A';
                document.getElementById('gender').textContent = staff.gender || 'N/A';
                document.getElementById('role').textContent = staff.role || 'Librarian';

                const statusDiv = document.getElementById('status');
                if (staff.isDeleted) {
                    statusDiv.innerHTML = '<span style="display: inline-block; padding: 0.5rem 1rem; background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%); color: #991b1b; border-radius: 20px; font-size: 0.9rem; font-weight: 600;"><i class="fa-solid fa-circle-xmark"></i> Inactive</span>';
                } else {
                    statusDiv.innerHTML = '<span style="display: inline-block; padding: 0.5rem 1rem; background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%); color: #065f46; border-radius: 20px; font-size: 0.9rem; font-weight: 600;"><i class="fa-solid fa-circle-check"></i> Active</span>';
                }
            } else {
                showError(data.message || 'Failed to load profile');
            }
        })
        .catch(error => {
            console.error('Failed to load profile:', error);
            showError('Failed to load profile. Please try again.');
        });
}

function showError(message) {
    document.getElementById('name').textContent = 'Error loading data';
    document.getElementById('email').textContent = message;
    document.getElementById('gender').textContent = '-';
    document.getElementById('role').textContent = '-';
    document.getElementById('status').innerHTML = '<span style="color: #991b1b;">Error</span>';
}

function openLogoutModal() {
    const modal = document.getElementById('logoutModal');
    if (modal) modal.classList.add('active');
}

function closeLogoutModal() {
    const modal = document.getElementById('logoutModal');
    if (modal) modal.classList.remove('active');
}

function handleLogout() {
    localStorage.removeItem('librarianId');
    localStorage.removeItem('librarianName');
    window.location.href = '/library-login';
}