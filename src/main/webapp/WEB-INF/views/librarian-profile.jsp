<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Librarian Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/librarian-profile.css">
</head>
<body>
<div class="flex min-h-screen">
    <aside class="w-64 bg-white border-r border-gray-200">
        <div class="p-6">
            <h1 class="text-2xl font-bold">Librarian Dashboard</h1>
        </div>
        <nav class="px-4">
            <a href="/librarian-dashboard" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-home"></i>
                <span>Dashboard</span>
            </a>
            <a href="/librarian-books" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Manage Books</span>
            </a>
            <a href="/librarian-borrowings" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-arrow-right-arrow-left"></i>
                <span>Borrowings</span>
            </a>
            <a href="/librarian-profile" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
                <i class="fa-solid fa-user"></i>
                <span>My Profile</span>
            </a>
            <button id="logoutBtn" class="w-full flex items-center gap-3 px-4 py-3 text-red-600 hover:bg-red-50 rounded-md mb-1">
                <i class="fa-solid fa-right-from-bracket"></i>
                <span>Logout</span>
            </button>
        </nav>
    </aside>

    <main class="flex-1 p-8">
        <div class="mb-8">
            <h2 class="text-3xl font-bold">My Profile</h2>
            <p class="text-gray-600 mt-2">View your account information</p>
        </div>

        <div class="bg-white border border-gray-200 rounded-lg p-8 max-w-2xl">
            <div class="profile-info">
                <div class="info-group">
                    <label class="info-label">Full Name</label>
                    <div class="info-value" id="name">Loading...</div>
                </div>

                <div class="info-group">
                    <label class="info-label">Email Address</label>
                    <div class="info-value" id="email">Loading...</div>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div class="info-group">
                        <label class="info-label">Gender</label>
                        <div class="info-value" id="gender">Loading...</div>
                    </div>

                    <div class="info-group">
                        <label class="info-label">Role</label>
                        <div class="info-value" id="role">Loading...</div>
                    </div>
                </div>

                <div class="info-group">
                    <label class="info-label">Account Status</label>
                    <div id="status">Loading...</div>
                </div>
            </div>
        </div>
    </main>
</div>

<div id="logoutModal" class="modal">
    <div class="modal-content-small">
        <div class="mb-6">
            <div class="w-12 h-12 bg-yellow-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <i class="fa-solid fa-right-from-bracket text-yellow-600 text-xl"></i>
            </div>
            <h3 class="text-xl font-bold text-center mb-2">Logout</h3>
            <p class="text-gray-600 text-center">Are you sure you want to logout?</p>
        </div>
        <div class="flex gap-3">
            <button id="cancelLogoutBtn" class="flex-1 px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50">Cancel</button>
            <button id="confirmLogoutBtn" class="flex-1 bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800">Logout</button>
        </div>
    </div>
</div>

<script src="/js/librarian_profile.js"></script>
</body>
</html>