<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Member Management</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js" integrity="sha384-JcnsjUPPylna1s1fvi1u12X5qjY5OL56iySh75FdtrwhO/SWXgMjoVqcKyIIWOLk" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.31/jspdf.plugin.autotable.min.js" integrity="sha384-vuyrTV5nkscLp1knFvt+FIHfKKzmROBq5reruhMRslauj54mW+l2B8b6szMN6lCL" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="/css/member-management.css">
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

            <a href="/member-management" class="flex items-center gap-3 px-4 py-3 bg-gray-100 text-gray-900 rounded-md mb-1">
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
        <div class="mb-8 flex justify-between items-center">
            <div>
                <h2 class="text-3xl font-bold">Member Management</h2>
                <p class="text-gray-600 mt-2">View and manage library members</p>
            </div>
            <div class="flex gap-3">
                <button id="exportPdfBtn" class="bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800">
                    <i class="fa-solid fa-file-pdf mr-2"></i>Export PDF
                </button>
            </div>
        </div>

        <div id="errorAlert" class="hidden bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md mb-4">
            <i class="fa-solid fa-circle-exclamation mr-2"></i>
            <span id="errorMessage"></span>
        </div>

        <div id="successAlert" class="hidden bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded-md mb-4">
            <i class="fa-solid fa-circle-check mr-2"></i>
            <span id="successMessage"></span>
        </div>

        <div class="bg-white border border-gray-200 rounded-lg p-6">
            <div class="mb-4">
                <input type="text" id="searchInput" placeholder="Search by name, email, city, or gender..." class="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-black">
            </div>

            <div id="loadingSpinner" class="text-center py-8">
                <i class="fa-solid fa-spinner fa-spin text-4xl text-gray-400"></i>
            </div>

            <div id="tableContainer" class="hidden overflow-x-auto">
                <table class="w-full">
                    <thead>
                    <tr class="border-b border-gray-200">
                        <th class="text-left py-3 px-4">ID</th>
                        <th class="text-left py-3 px-4">Name</th>
                        <th class="text-left py-3 px-4">Email</th>
                        <th class="text-left py-3 px-4">Age</th>
                        <th class="text-left py-3 px-4">Gender</th>
                        <th class="text-left py-3 px-4">City</th>
                        <th class="text-left py-3 px-4">Status</th>
                        <th class="text-center py-3 px-4">Actions</th>
                    </tr>
                    </thead>
                    <tbody id="memberTableBody">
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>

<div id="deleteModal" class="modal">
    <div class="modal-content-small">
        <div class="mb-6">
            <div class="w-12 h-12 bg-red-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <i class="fa-solid fa-trash text-red-600 text-xl"></i>
            </div>
            <h3 class="text-xl font-bold text-center mb-2">Delete Member</h3>
            <p class="text-gray-600 text-center">Are you sure you want to delete this member? This action cannot be undone.</p>
        </div>
        <div class="flex gap-3">
            <button id="cancelDeleteBtn" class="flex-1 px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50">Cancel</button>
            <button id="confirmDeleteBtn" class="flex-1 bg-black text-white px-4 py-2 rounded-md hover:bg-gray-800">
                <span id="deleteBtnText">Delete</span>
                <i id="deleteBtnLoader" class="fa-solid fa-spinner fa-spin hidden"></i>
            </button>
        </div>
    </div>
</div>

<script src="/js/member-management.js"></script>
</body>
</html>