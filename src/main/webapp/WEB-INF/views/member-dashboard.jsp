<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Dashboard</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/member-borrowings.css">
</head>
<body>
<div class="flex min-h-screen">
    <aside class="w-64 bg-white border-r border-gray-200">
        <div class="p-6">
            <h1 class="text-2xl font-bold">Member Dashboard</h1>
        </div>
        <nav class="px-4">
            <a href="/member-dashboard" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
                <i class="fa-solid fa-home"></i>
                <span>Dashboard</span>
            </a>
            <a href="/" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Browse Books</span>
            </a>
            <a href="/member-borrowings" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-arrow-right-arrow-left"></i>
                <span>My Borrowings</span>
            </a>
            <a href="/member-profile" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-user"></i>
                <span>My Profile</span>
            </a>
            <a href="/" class="flex items-center gap-3 px-4 py-3 text-red-600 hover:bg-red-50 rounded-md mb-1">
                <i class="fa-solid fa-sign-out-alt"></i>
                <span>Logout</span>
            </a>
        </nav>
    </aside>

    <main class="flex-1 p-8 bg-gray-50">
        <div class="mb-8">
            <h2 class="text-3xl font-bold">Welcome Back!</h2>
            <p class="text-gray-600 mt-2" id="welcomeMessage">Your library dashboard</p>
        </div>

        <!-- Stats Cards -->
        <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center justify-between">
                    <div>
                        <p class="text-gray-600 text-sm">Active Borrowings</p>
                        <p class="text-3xl font-bold text-blue-600" id="activeBorrowings">-</p>
                    </div>
                    <div class="bg-blue-100 p-4 rounded-full">
                        <i class="fa-solid fa-book-open text-blue-600 text-2xl"></i>
                    </div>
                </div>
            </div>

            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center justify-between">
                    <div>
                        <p class="text-gray-600 text-sm">Overdue Books</p>
                        <p class="text-3xl font-bold text-red-600" id="overdueBooks">-</p>
                    </div>
                    <div class="bg-red-100 p-4 rounded-full">
                        <i class="fa-solid fa-exclamation-triangle text-red-600 text-2xl"></i>
                    </div>
                </div>
            </div>

            <div class="bg-white rounded-lg shadow p-6">
                <div class="flex items-center justify-between">
                    <div>
                        <p class="text-gray-600 text-sm">Total Books Read</p>
                        <p class="text-3xl font-bold text-green-600" id="totalReturned">-</p>
                    </div>
                    <div class="bg-green-100 p-4 rounded-full">
                        <i class="fa-solid fa-check-circle text-green-600 text-2xl"></i>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions -->
        <div class="bg-white rounded-lg shadow p-6 mb-8">
            <h3 class="text-xl font-bold mb-4">Quick Actions</h3>
            <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
                <a href="/" class="flex flex-col items-center justify-center p-6 bg-gradient-to-br from-purple-500 to-purple-600 text-white rounded-lg hover:shadow-lg transition-all">
                    <i class="fa-solid fa-search text-3xl mb-2"></i>
                    <span class="font-semibold">Browse Books</span>
                </a>
                <a href="/member-borrowings" class="flex flex-col items-center justify-center p-6 bg-gradient-to-br from-blue-500 to-blue-600 text-white rounded-lg hover:shadow-lg transition-all">
                    <i class="fa-solid fa-list text-3xl mb-2"></i>
                    <span class="font-semibold">My Borrowings</span>
                </a>
                <a href="/member-profile" class="flex flex-col items-center justify-center p-6 bg-gradient-to-br from-green-500 to-green-600 text-white rounded-lg hover:shadow-lg transition-all">
                    <i class="fa-solid fa-user text-3xl mb-2"></i>
                    <span class="font-semibold">My Profile</span>
                </a>
                <a href="/" class="flex flex-col items-center justify-center p-6 bg-gradient-to-br from-orange-500 to-orange-600 text-white rounded-lg hover:shadow-lg transition-all">
                    <i class="fa-solid fa-star text-3xl mb-2"></i>
                    <span class="font-semibold">Leave Review</span>
                </a>
            </div>
        </div>

        <!-- Current Borrowings -->
        <div class="bg-white rounded-lg shadow p-6">
            <div class="flex justify-between items-center mb-4">
                <h3 class="text-xl font-bold">Current Borrowings</h3>
                <a href="/member-borrowings" class="text-blue-600 hover:text-blue-700 font-semibold">View All →</a>
            </div>
            <div id="loadingSpinner" class="text-center py-8">
                <i class="fa-solid fa-spinner fa-spin text-4xl text-gray-400"></i>
            </div>
            <div id="borrowingsContainer" class="hidden">
                <!-- Will be populated by JavaScript -->
            </div>
            <div id="noBorrowings" class="hidden text-center py-8 text-gray-500">
                <i class="fa-solid fa-book-open text-4xl mb-2"></i>
                <p>You have no active borrowings</p>
                <a href="/" class="text-blue-600 hover:text-blue-700 mt-2 inline-block">Browse Books</a>
            </div>
        </div>
    </main>
</div>

<script src="/js/member-dashboard.js"></script>
</body>
</html>
