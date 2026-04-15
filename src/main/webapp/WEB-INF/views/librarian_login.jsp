<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Librarian Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            font-family: 'Ubuntu', sans-serif;
        }
        body {
            background: linear-gradient(135deg, #2563eb 0%, #1e40af 100%);
        }
    </style>
</head>
<body class="min-h-screen flex items-center justify-center p-4">
<div class="w-full max-w-md">
    <div class="bg-white rounded-lg shadow-xl p-8">
        <div class="text-center mb-8">
            <div class="inline-block p-4 bg-black rounded-full mb-4">
                <i class="fa-solid fa-user-tie text-white text-3xl"></i>
            </div>
            <h1 class="text-3xl font-bold text-gray-800">Library System</h1>
            <p class="text-gray-600 mt-2">Librarian Login Portal</p>
        </div>

        <form id="loginForm" class="space-y-4">
            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">Email Address</label>
                <div class="relative">
                    <i class="fa-solid fa-envelope absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="email" id="email" placeholder="Enter your email" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">Password</label>
                <div class="relative">
                    <i class="fa-solid fa-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="password" id="password" placeholder="Enter your password" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                </div>
            </div>

            <div id="error" class="hidden bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
                <i class="fa-solid fa-circle-exclamation mr-2"></i>
                <span id="errorMessage"></span>
            </div>

            <button type="submit" id="loginBtn" class="w-full bg-black text-white py-3 px-4 rounded-md hover:bg-gray-800 transition-colors font-medium">
                <span id="btnText">Sign In</span>
                <i id="btnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
            </button>

            <div class="text-center mt-4">
                <a href="/admin-login" class="text-sm text-gray-600 hover:text-gray-800">Admin Login</a>
                <span class="text-gray-400 mx-2">|</span>
                <a href="/login" class="text-sm text-gray-600 hover:text-gray-800">Member Login</a>
            </div>
        </form>
    </div>

    <div class="text-center mt-6 text-white">
        <p class="text-sm">© 2025 Library Management System. All rights reserved.</p>
    </div>
</div>

<script src="/js/librarian_login.js"></script>
</body>
</html>