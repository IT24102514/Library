<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Feedback Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js" integrity="sha384-JcnsjUPPylna1s1fvi1u12X5qjY5OL56iySh75FdtrwhO/SWXgMjoVqcKyIIWOLk" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.31/jspdf.plugin.autotable.min.js" integrity="sha384-vuyrTV5nkscLp1knFvt+FIHfKKzmROBq5reruhMRslauj54mW+l2B8b6szMN6lCL" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/feedback-management.css">
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
            <a href="/book-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-book"></i>
                <span>Book Management</span>
            </a>

            <a href="/member-management" class="flex items-center gap-3 px-4 py-3 text-gray-700 hover:bg-gray-100 rounded-md mb-1">
                <i class="fa-solid fa-user-group"></i>
                <span>Member Management</span>
            </a>
            <a href="/feedback-management" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
                <i class="fa-solid fa-comments"></i>
                <span>Feedback Management</span>
            </a>
        </nav>
    </aside>

    <main class="flex-1 p-8">
        <div class="mb-8 flex justify-between items-center">
            <div>
                <h2 class="text-3xl font-bold">Feedback Management</h2>
                <p class="text-gray-600 mt-2">View user feedbacks and reviews</p>
            </div>
            <div class="flex gap-3">
                <button id="exportPdfBtn" class="bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800">
                    <i class="fa-solid fa-file-pdf mr-2"></i>Export PDF
                </button>
            </div>
        </div>

        <div class="bg-white border border-gray-200 rounded-lg p-6">
            <div class="mb-4 flex gap-4">
                <input type="text" id="searchInput" placeholder="Search by book name or member name..." class="flex-1 px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                <select id="ratingFilter" class="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
                    <option value="">All Ratings</option>
                    <option value="5">5 Stars</option>
                    <option value="4">4 Stars</option>
                    <option value="3">3 Stars</option>
                    <option value="2">2 Stars</option>
                    <option value="1">1 Star</option>
                </select>
            </div>

            <div id="loadingSpinner" class="text-center py-8">
                <i class="fa-solid fa-spinner fa-spin text-4xl text-gray-400"></i>
            </div>

            <div id="feedbackContainer" class="hidden">
            </div>
        </div>
    </main>
</div>

<script src="/js/feedback-management.js"></script>
</body>
</html>