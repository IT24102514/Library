<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/profile.css">
</head>
<body>
<div class="flex min-h-screen">
    <aside class="w-64 bg-white border-r border-gray-200">
        <div class="p-6">
            <h1 class="text-2xl font-bold">Member Dashboard</h1>
        </div>
        <nav class="px-4">
            <a href="/" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Browse Books</span>
            </a>
            <a href="/member-borrowings" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-arrow-right-arrow-left"></i>
                <span>My Borrowings</span>
            </a>
            <a href="/member-profile" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
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
            <p class="text-gray-600 mt-2">Manage your account information</p>
        </div>

        <div id="errorAlert" class="hidden bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md mb-4">
            <i class="fa-solid fa-circle-exclamation mr-2"></i>
            <span id="errorMessage"></span>
        </div>

        <div id="successAlert" class="hidden bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-md mb-4">
            <i class="fa-solid fa-circle-check mr-2"></i>
            <span id="successMessage"></span>
        </div>

        <div class="bg-white border border-gray-200 rounded-lg p-8 max-w-2xl">
            <form id="profileForm">
                <div class="mb-6">
                    <label class="block text-sm font-medium mb-2">Full Name</label>
                    <input type="text" id="name" required class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                </div>

                <div class="mb-6">
                    <label class="block text-sm font-medium mb-2">Email Address</label>
                    <input type="email" id="email" required class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                </div>

                <div class="grid grid-cols-2 gap-4 mb-6">
                    <div>
                        <label class="block text-sm font-medium mb-2">Age</label>
                        <input type="number" id="age" min="1" max="120" required class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                    </div>

                    <div>
                        <label class="block text-sm font-medium mb-2">Gender</label>
                        <select id="gender" required class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                            <option value="Male">Male</option>
                            <option value="Female">Female</option>
                            <option value="Other">Other</option>
                        </select>
                    </div>
                </div>

                <div class="mb-6">
                    <label class="block text-sm font-medium mb-2">City</label>
                    <input type="text" id="city" required class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                </div>

                <div class="mb-6">
                    <label class="block text-sm font-medium mb-2">Password (leave empty to keep current)</label>
                    <input type="password" id="password" class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                </div>

                <div class="flex gap-4">
                    <button type="submit" id="updateBtn" class="flex-1 bg-black text-white py-2 px-4 rounded-md hover:bg-gray-800">
                        <span id="updateBtnText">Update Profile</span>
                        <i id="updateBtnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
                    </button>
                    <button type="button" id="deleteBtn" class="flex-1 bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700">
                        Delete Account
                    </button>
                </div>
            </form>
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

<div id="deleteModal" class="modal">
    <div class="modal-content-small">
        <div class="mb-6">
            <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <i class="fa-solid fa-trash text-red-600 text-xl"></i>
            </div>
            <h3 class="text-xl font-bold text-center mb-2">Delete Account</h3>
            <p class="text-gray-600 text-center">Are you sure you want to delete your account? This action cannot be undone.</p>
        </div>
        <div class="flex gap-3">
            <button id="cancelDeleteBtn" class="flex-1 px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50">Cancel</button>
            <button id="confirmDeleteBtn" class="flex-1 bg-red-600 text-white px-4 py-2 rounded-md hover:bg-red-700">
                <span id="deleteBtnText">Delete</span>
                <i id="deleteBtnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
            </button>
        </div>
    </div>
</div>

<script src="/js/profile.js"></script>
</body>
</html>