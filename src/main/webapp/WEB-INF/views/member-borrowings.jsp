<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Borrowings</title>
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
            <a href="/member-dashboard" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-home"></i>
                <span>Dashboard</span>
            </a>
            <a href="/" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Browse Books</span>
            </a>
            <a href="/member-borrowings" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
                <i class="fa-solid fa-arrow-right-arrow-left"></i>
                <span>My Borrowings</span>
            </a>
            <a href="/member-profile" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-user"></i>
                <span>My Profile</span>
            </a>
        </nav>
    </aside>

    <main class="flex-1 p-8">
        <div class="mb-8">
            <h2 class="text-3xl font-bold">My Borrowings</h2>
            <p class="text-gray-600 mt-2">View your borrowed books and return history</p>
        </div>

        <div class="mb-6 flex gap-4">
            <button id="filterAll" class="filter-btn active">All</button>
            <button id="filterActive" class="filter-btn">Active</button>
            <button id="filterReturned" class="filter-btn">Returned</button>
            <button id="filterOverdue" class="filter-btn">Overdue</button>
        </div>

        <div id="loadingSpinner" class="text-center py-8">
            <i class="fa-solid fa-spinner fa-spin text-4xl text-gray-400"></i>
        </div>

        <div id="borrowingsContainer" class="hidden">
        </div>

        <div id="noBorrowings" class="hidden text-center py-12">
            <i class="fa-solid fa-book-open text-6xl text-gray-400 mb-4"></i>
            <p class="text-gray-600 text-lg">No borrowings found</p>
        </div>
    </main>
</div>

<script src="/js/member-borrowings.js"></script>
</body>
</html>