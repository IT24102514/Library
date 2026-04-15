const API_BASE_URL = 'http://localhost:8081/api/books';
const CATEGORY_API_URL = 'http://localhost:8081/api/categories';

let books = [];
let categories = [];
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    loadBooks();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('createBookBtn').addEventListener('click', openCreateModal);
    document.getElementById('exportPdfBtn').addEventListener('click', exportToPdf);
    document.getElementById('searchInput').addEventListener('input', filterBooks);
    document.getElementById('createForm').addEventListener('submit', handleCreate);
    document.getElementById('editForm').addEventListener('submit', handleEdit);
    document.getElementById('confirmDeleteBtn').addEventListener('click', handleDelete);
    document.getElementById('cancelDeleteBtn').addEventListener('click', closeDeleteModal);

    document.querySelectorAll('.close-modal').forEach(btn => {
        btn.addEventListener('click', function() {
            closeAllModals();
        });
    });
}

async function loadCategories() {
    try {
        const response = await fetch(CATEGORY_API_URL);
        const data = await response.json();

        if (data.success) {
            categories = data.data;
            populateCategoryDropdowns();
        }
    } catch (error) {
        showError('Failed to load categories: ' + error.message);
    }
}

function populateCategoryDropdowns() {
    const createSelect = document.getElementById('createCategory');
    const editSelect = document.getElementById('editCategory');

    createSelect.innerHTML = '<option value="">Select Category</option>';
    editSelect.innerHTML = '<option value="">Select Category</option>';

    categories.forEach(category => {
        const option1 = document.createElement('option');
        option1.value = category.id;
        option1.textContent = category.name;
        createSelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = category.id;
        option2.textContent = category.name;
        editSelect.appendChild(option2);
    });
}

async function loadBooks() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        if (data.success) {
            books = data.data.filter((book)=>!book.isDeleted);
            renderTable(books);
            hideLoading();
        } else {
            showError(data.message);
            hideLoading();
        }
    } catch (error) {
        showError('Failed to load books: ' + error.message);
        hideLoading();
    }
}

function renderTable(data) {
    const tbody = document.getElementById('bookTableBody');
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center py-8 text-gray-500">No books found</td></tr>';
        return;
    }

    data.forEach(book => {
        const row = document.createElement('tr');
        row.className = 'border-b border-gray-200 hover:bg-gray-50';

        const imageCell = book.imageUrl
            ? `<img src="${book.imageUrl}" alt="${book.name}" class="w-12 h-16 object-cover rounded">`
            : `<div class="w-12 h-16 bg-gray-200 rounded flex items-center justify-center"><i class="fa-solid fa-book text-gray-400"></i></div>`;

        const statusBadge = book.isDeleted
            ? '<span class="px-2 py-1 bg-red-100 text-red-700 rounded text-xs">Deleted</span>'
            : book.isBorrowed
                ? '<span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded text-xs">Borrowed</span>'
                : '<span class="px-2 py-1 bg-green-100 text-green-700 rounded text-xs">Available</span>';

        row.innerHTML = `
            <td class="py-3 px-4">${book.id}</td>
            <td class="py-3 px-4">${imageCell}</td>
            <td class="py-3 px-4">${book.name}</td>
            <td class="py-3 px-4">${book.authorName}</td>
            <td class="py-3 px-4">${book.category ? book.category.name : 'N/A'}</td>
            <td class="py-3 px-4">${statusBadge}</td>
            <td class="py-3 px-4 text-center">
                <button onclick="openEditModal(${book.id})" class="text-blue-600 hover:text-blue-800 mr-3">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button onclick="openDeleteModal(${book.id})" class="text-red-600 hover:text-red-800">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const filtered = books.filter(book => {
        return book.name.toLowerCase().includes(searchTerm) ||
            book.authorName.toLowerCase().includes(searchTerm) ||
            (book.category && book.category.name.toLowerCase().includes(searchTerm));
    });
    renderTable(filtered);
}

function openCreateModal() {
    document.getElementById('createModal').classList.add('active');
    document.getElementById('createForm').reset();
}

function openEditModal(id) {
    const book = books.find(b => b.id === id);
    if (!book) return;

    document.getElementById('editId').value = book.id;
    document.getElementById('editName').value = book.name;
    document.getElementById('editAuthorName').value = book.authorName;
    document.getElementById('editDescription').value = book.description || '';
    document.getElementById('editImageUrl').value = book.imageUrl || '';
    document.getElementById('editCategory').value = book.category ? book.category.id : '';

    document.getElementById('editModal').classList.add('active');
}

function openDeleteModal(id) {
    deleteTargetId = id;
    document.getElementById('deleteModal').classList.add('active');
}

function closeDeleteModal() {
    deleteTargetId = null;
    document.getElementById('deleteModal').classList.remove('active');
}

function closeAllModals() {
    document.querySelectorAll('.modal').forEach(modal => {
        modal.classList.remove('active');
    });
}

async function handleCreate(e) {
    e.preventDefault();

    const submitBtn = document.getElementById('createSubmitBtn');
    const btnText = document.getElementById('createBtnText');
    const btnLoader = document.getElementById('createBtnLoader');

    submitBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    const categoryId = document.getElementById('createCategory').value;
    const category = categories.find(c => c.id == categoryId);

    const data = {
        name: document.getElementById('createName').value,
        authorName: document.getElementById('createAuthorName').value,
        description: document.getElementById('createDescription').value,
        imageUrl: document.getElementById('createImageUrl').value,
        category: {
            id: parseInt(categoryId),
            name: category.name
        }
    };

    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (result.success) {
            showSuccess('Book created successfully');
            closeAllModals();
            loadBooks();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to create book: ' + error.message);
    } finally {
        submitBtn.disabled = false;
        btnText.classList.remove('hidden');
        btnLoader.classList.add('hidden');
    }
}

async function handleEdit(e) {
    e.preventDefault();

    const submitBtn = document.getElementById('editSubmitBtn');
    const btnText = document.getElementById('editBtnText');
    const btnLoader = document.getElementById('editBtnLoader');

    submitBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    const id = document.getElementById('editId').value;
    const categoryId = document.getElementById('editCategory').value;
    const category = categories.find(c => c.id == categoryId);

    const data = {
        name: document.getElementById('editName').value,
        authorName: document.getElementById('editAuthorName').value,
        description: document.getElementById('editDescription').value,
        imageUrl: document.getElementById('editImageUrl').value,
        category: {
            id: parseInt(categoryId),
            name: category.name
        }
    };

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (result.success) {
            showSuccess('Book updated successfully');
            closeAllModals();
            loadBooks();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to update book: ' + error.message);
    } finally {
        submitBtn.disabled = false;
        btnText.classList.remove('hidden');
        btnLoader.classList.add('hidden');
    }
}

async function handleDelete() {
    if (!deleteTargetId) return;

    const submitBtn = document.getElementById('confirmDeleteBtn');
    const btnText = document.getElementById('deleteBtnText');
    const btnLoader = document.getElementById('deleteBtnLoader');

    submitBtn.disabled = true;
    btnText.classList.add('hidden');
    btnLoader.classList.remove('hidden');

    try {
        const response = await fetch(`${API_BASE_URL}/${deleteTargetId}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (result.success) {
            showSuccess('Book deleted successfully');
            closeDeleteModal();
            loadBooks();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to delete book: ' + error.message);
    } finally {
        submitBtn.disabled = false;
        btnText.classList.remove('hidden');
        btnLoader.classList.add('hidden');
    }
}

function exportToPdf() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('Book Management Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = books.map(book => [
        book.id,
        book.name,
        book.authorName,
        book.category ? book.category.name : 'N/A',
        book.isDeleted ? 'Deleted' : book.isBorrowed ? 'Borrowed' : 'Available'
    ]);

    doc.autoTable({
        head: [['ID', 'Name', 'Author', 'Category', 'Status']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 9 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] }
    });

    doc.save('books-report.pdf');
}

function showSuccess(message) {
    const alert = document.getElementById('successAlert');
    const messageEl = document.getElementById('successMessage');
    messageEl.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => {
        alert.classList.add('hidden');
    }, 5000);
}

function showError(message) {
    const alert = document.getElementById('errorAlert');
    const messageEl = document.getElementById('errorMessage');
    messageEl.textContent = message;
    alert.classList.remove('hidden');
    setTimeout(() => {
        alert.classList.add('hidden');
    }, 5000);
}

function showLoading() {
    document.getElementById('loadingSpinner').classList.remove('hidden');
    document.getElementById('tableContainer').classList.add('hidden');
}

function hideLoading() {
    document.getElementById('loadingSpinner').classList.add('hidden');
    document.getElementById('tableContainer').classList.remove('hidden');
}