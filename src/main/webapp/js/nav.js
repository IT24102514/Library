// Navigation handler for showing logged-in user info
document.addEventListener('DOMContentLoaded', function() {
    updateNavigation();
});

function updateNavigation() {
    const navbarMenu = document.getElementById('navbarMenu');
    const userId = localStorage.getItem('userId');
    const userName = localStorage.getItem('userName');

    if (!navbarMenu) return;

    if (userId) {
        // User is logged in - show member menu
        navbarMenu.innerHTML = `
            <span style="color: #e2e8f0; padding: 0.75rem 1.25rem; display: flex; align-items: center; gap: 0.5rem; font-weight: 500;">
                <i class="fa-solid fa-user-circle" style="font-size: 1.5rem;"></i>
                <span>Welcome, ${userName || 'Member'}</span>
            </span>
            <a href="/" class="navbar-link">
                <i class="fa-solid fa-book"></i>
                <span>Browse Books</span>
            </a>
            <a href="/member-borrowings" class="navbar-link">
                <i class="fa-solid fa-list"></i>
                <span>My Borrowings</span>
            </a>
            <a href="/member-profile" class="navbar-link">
                <i class="fa-solid fa-user"></i>
                <span>My Profile</span>
            </a>
            <button onclick="handleLogout()" class="navbar-btn" style="background: linear-gradient(135deg, #fa709a 0%, #f76b1c 100%);">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Logout</span>
            </button>
        `;
    } else {
        // User is not logged in - show login options
        navbarMenu.innerHTML = `
            <a href="/login" class="navbar-link">
                <i class="fa-solid fa-user"></i>
                <span>Member Login</span>
            </a>
            <a href="/library-login" class="navbar-link">
                <i class="fa-solid fa-user-tie"></i>
                <span>Librarian Login</span>
            </a>
            <a href="/admin-login" class="navbar-link">
                <i class="fa-solid fa-user-shield"></i>
                <span>Admin Login</span>
            </a>
            <a href="/signup" class="navbar-btn">
                <i class="fa-solid fa-user-plus"></i>
                <span>Sign Up</span>
            </a>
        `;
    }
}

function handleLogout() {
    localStorage.removeItem('userId');
    localStorage.removeItem('userName');
    window.location.href = '/';
}

