<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Librarian Dashboard</title>
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
<body>
<div class="flex min-h-screen">
    <aside class="w-64 bg-white border-r border-gray-200">
        <div class="p-6">
            <h1 class="text-2xl font-bold">Librarian Dashboard</h1>
        </div>
        <nav class="px-4">
            <a href="/librarian-dashboard" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
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
            <a href="/librarian-profile" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-user"></i>
                <span>My Profile</span>
            </a>
        </nav>
    </aside>

    <main class="flex-1 p-8">
        <div class="mb-8">
            <h2 class="text-3xl font-bold">Welcome to Librarian Dashboard</h2>
            <p class="text-gray-600 mt-2">Manage books and borrowings</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-black text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-book text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Total Books</p>
                        <p class="text-2xl font-bold" id="totalBooks">0</p>
                    </div>
                </div>
            </div>

            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-green-600 text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-circle-check text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Available Books</p>
                        <p class="text-2xl font-bold" id="availableBooks">0</p>
                    </div>
                </div>
            </div>

            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-yellow-600 text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-arrow-right-arrow-left text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Borrowed Books</p>
                        <p class="text-2xl font-bold" id="borrowedBooks">0</p>
                    </div>
                </div>
            </div>

            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-blue-600 text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-user-group text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Total Members</p>
                        <p class="text-2xl font-bold" id="totalMembers">0</p>
                    </div>
                </div>
            </div>
        </div>

        <div class="mt-8">
            <h3 class="text-xl font-bold mb-4">Quick Actions</h3>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <a href="/librarian-books" class="p-6 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
                    <i class="fa-solid fa-book text-2xl text-black mb-3"></i>
                    <h4 class="font-semibold mb-2">Manage Books</h4>
                    <p class="text-sm text-gray-600">Add, edit, or remove books from the library</p>
                </a>

                <a href="/librarian-borrowings" class="p-6 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
                    <i class="fa-solid fa-arrow-right-arrow-left text-2xl text-black mb-3"></i>
                    <h4 class="font-semibold mb-2">Handle Borrowings</h4>
                    <p class="text-sm text-gray-600">Process book borrowings and returns</p>
                </a>

                <a href="/librarian-profile" class="p-6 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors">
                    <i class="fa-solid fa-user text-2xl text-black mb-3"></i>
                    <h4 class="font-semibold mb-2">My Profile</h4>
                    <p class="text-sm text-gray-600">View your account information</p>
                </a>
            </div>
        </div>
    </main>
</div>

<script src="/js/librarian-dashboard.js"></script>
</body>
</html>