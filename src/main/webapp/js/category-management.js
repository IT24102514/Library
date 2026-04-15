const API_BASE_URL = 'http://localhost:8081/api/categories';

let categories = [];
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('createCategoryBtn').addEventListener('click', openCreateModal);
    document.getElementById('exportPdfBtn').addEventListener('click', exportToPdf);
    document.getElementById('searchInput').addEventListener('input', filterCategories);
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
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        if (data.success) {
            categories = data.data;
            renderTable(categories);
            hideLoading();
        } else {
            showError(data.message);
            hideLoading();
        }
    } catch (error) {
        showError('Failed to load categories: ' + error.message);
        hideLoading();
    }
}

function renderTable(data) {
    const tbody = document.getElementById('categoryTableBody');
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="text-center py-8 text-gray-500">No categories found</td></tr>';
        return;
    }

    data.forEach(category => {
        const row = document.createElement('tr');
        row.className = 'border-b border-gray-200 hover:bg-gray-50';
        row.innerHTML = `
            <td class="py-3 px-4">${category.id}</td>
            <td class="py-3 px-4">${category.name}</td>
            <td class="py-3 px-4">${category.description || 'N/A'}</td>
            <td class="py-3 px-4">
                ${category.isDeleted ?
            '<span class="px-2 py-1 bg-red-100 text-red-700 rounded text-xs">Deleted</span>' :
            '<span class="px-2 py-1 bg-green-100 text-green-700 rounded text-xs">Active</span>'}
            </td>
            <td class="py-3 px-4 text-center">
                <button onclick="openEditModal(${category.id})" class="text-blue-600 hover:text-blue-800 mr-3">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button onclick="openDeleteModal(${category.id})" class="text-red-600 hover:text-red-800">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterCategories() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const filtered = categories.filter(category => {
        return category.name.toLowerCase().includes(searchTerm) ||
            (category.description && category.description.toLowerCase().includes(searchTerm));
    });
    renderTable(filtered);
}

function openCreateModal() {
    document.getElementById('createModal').classList.add('active');
    document.getElementById('createForm').reset();
}

function openEditModal(id) {
    const category = categories.find(c => c.id === id);
    if (!category) return;

    document.getElementById('editId').value = category.id;
    document.getElementById('editName').value = category.name;
    document.getElementById('editDescription').value = category.description || '';

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

    const data = {
        name: document.getElementById('createName').value,
        description: document.getElementById('createDescription').value
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
            showSuccess('Category created successfully');
            closeAllModals();
            loadCategories();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to create category: ' + error.message);
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
    const data = {
        name: document.getElementById('editName').value,
        description: document.getElementById('editDescription').value
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
            showSuccess('Category updated successfully');
            closeAllModals();
            loadCategories();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to update category: ' + error.message);
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
            showSuccess('Category deleted successfully');
            closeDeleteModal();
            loadCategories();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to delete category: ' + error.message);
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
    doc.text('Category Management Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = categories.map(category => [
        category.id,
        category.name,
        category.description || 'N/A',
        category.isDeleted ? 'Deleted' : 'Active'
    ]);

    doc.autoTable({
        head: [['ID', 'Name', 'Description', 'Status']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 9 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] }
    });

    doc.save('categories-report.pdf');
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