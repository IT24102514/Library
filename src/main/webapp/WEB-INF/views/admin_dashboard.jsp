<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
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
            <h1 class="text-2xl font-bold">Admin Dashboard</h1>
        </div>
        <nav class="px-4">
            <a href="/librarian-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-users"></i>
                <span>Librarian Management</span>
            </a>
            <a href="/category-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Category Management</span>
            </a>
            <a href="/book-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Book Management</span>
            </a>

            <a href="/member-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-user-group"></i>
                <span>Member Management</span>
            </a>
            <a href="/feedback-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-comments"></i>
                <span>Feedback Management</span>
            </a>
        </nav>
    </aside>

    <main class="flex-1 p-8">
        <div class="mb-8">
            <h2 class="text-3xl font-bold">Welcome to Admin Dashboard</h2>
            <p class="text-gray-600 mt-2">Manage your library system</p>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-black text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-users text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Total Librarians</p>
                        <p class="text-2xl font-bold">0</p>
                    </div>
                </div>
            </div>

            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-black text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-book text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Total Books</p>
                        <p class="text-2xl font-bold">0</p>
                    </div>
                </div>
            </div>

            <div class="p-6 border border-gray-200 rounded-lg">
                <div class="flex items-center gap-4 mb-4">
                    <div class="w-12 h-12 bg-black text-white rounded-lg flex items-center justify-center">
                        <i class="fa-solid fa-user-group text-xl"></i>
                    </div>
                    <div>
                        <p class="text-sm text-gray-600">Total Members</p>
                        <p class="text-2xl font-bold">0</p>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>

</body>
</html>