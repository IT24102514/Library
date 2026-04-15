<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Login</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            font-family: 'Ubuntu', sans-serif;
        }
        body {
            background-color: white;
        }
    </style>
</head>
<body class="min-h-screen flex items-center justify-center">
<div class="w-full max-w-md p-8 border border-gray-200 rounded-lg shadow-sm">
    <h1 class="text-2xl font-bold text-center mb-6">Admin Login</h1>

    <form id="loginForm" class="space-y-4">
        <div>
            <label class="block text-sm font-medium mb-2">Username</label>
            <div class="relative">
                <i class="fa-solid fa-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                <input type="text" id="username" class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black" required>
            </div>
        </div>

        <div>
            <label class="block text-sm font-medium mb-2">Password</label>
            <div class="relative">
                <i class="fa-solid fa-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                <input type="password" id="password" class="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black" required>
            </div>
        </div>

        <div id="error" class="hidden bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
            <i class="fa-solid fa-circle-exclamation mr-2"></i>
            <span id="errorMessage"></span>
        </div>

        <button type="submit" id="loginBtn" class="w-full bg-black text-white py-2 px-4 rounded-md hover:bg-gray-800 transition-colors">
            <span id="btnText">Login</span>
            <i id="btnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
        </button>
    </form>
</div>

<script src="/js/admin_login.js"></script>
</body>
</html>