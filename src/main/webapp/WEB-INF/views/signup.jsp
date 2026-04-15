<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Member Signup</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        * {
            font-family: 'Ubuntu', sans-serif;
        }
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
    </style>
</head>
<body class="min-h-screen flex items-center justify-center p-4">
<div class="w-full max-w-md">
    <div class="bg-white rounded-lg shadow-xl p-8">
        <div class="text-center mb-8">
            <div class="inline-block p-4 bg-black rounded-full mb-4">
                <i class="fa-solid fa-user-plus text-white text-3xl"></i>
            </div>
            <h1 class="text-3xl font-bold text-gray-800">Join Our Library</h1>
            <p class="text-gray-600 mt-2">Create your member account</p>
        </div>

        <form id="signupForm" class="space-y-4">
            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">Full Name</label>
                <div class="relative">
                    <i class="fa-solid fa-user absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="text" id="name" placeholder="Enter your full name" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">Email Address</label>
                <div class="relative">
                    <i class="fa-solid fa-envelope absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="email" id="email" placeholder="Enter your email" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">Password</label>
                <div class="relative">
                    <i class="fa-solid fa-lock absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="password" id="password" placeholder="Enter your password" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                </div>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium mb-2 text-gray-700">Age</label>
                    <input type="number" id="age" min="1" max="120" placeholder="Age" class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                </div>

                <div>
                    <label class="block text-sm font-medium mb-2 text-gray-700">Gender</label>
                    <select id="gender" class="w-full px-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                        <option value="">Select</option>
                        <option value="Male">Male</option>
                        <option value="Female">Female</option>
                        <option value="Other">Other</option>
                    </select>
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium mb-2 text-gray-700">City</label>
                <div class="relative">
                    <i class="fa-solid fa-location-dot absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                    <input type="text" id="city" placeholder="Enter your city" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500" required>
                </div>
            </div>

            <div id="error" class="hidden bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
                <i class="fa-solid fa-circle-exclamation mr-2"></i>
                <span id="errorMessage"></span>
            </div>

            <div id="success" class="hidden bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-md text-sm">
                <i class="fa-solid fa-circle-check mr-2"></i>
                <span id="successMessage"></span>
            </div>

            <button type="submit" id="signupBtn" class="w-full bg-black text-white py-3 px-4 rounded-md hover:bg-gray-800 transition-colors font-medium">
                <span id="btnText">Create Account</span>
                <i id="btnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
            </button>

            <div class="text-center mt-4">
                <span class="text-sm text-gray-600">Already have an account? </span>
                <a href="/login" class="text-sm text-purple-600 hover:text-purple-800 font-medium">Sign In</a>
            </div>
        </form>
    </div>

    <div class="text-center mt-6 text-white">
        <p class="text-sm">© 2025 Library Management System. All rights reserved.</p>
    </div>
</div>

<script src="/js/signup.js"></script>
</body>
</html>