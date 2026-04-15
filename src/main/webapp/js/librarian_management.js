const API_BASE_URL = 'http://localhost:8081/api/staff';

let librarians = [];
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadLibrarians();
    setupEventListeners();
});

function setupEventListeners() {
    const createBtn = document.getElementById('createLibrarianBtn');
    const exportBtn = document.getElementById('exportPdfBtn');
    const searchInput = document.getElementById('searchInput');
    const createForm = document.getElementById('createForm');
    const editForm = document.getElementById('editForm');
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    const cancelDeleteBtn = document.getElementById('cancelDeleteBtn');

    if (createBtn) createBtn.addEventListener('click', openCreateModal);
    if (exportBtn) exportBtn.addEventListener('click', exportToPdf);
    if (searchInput) searchInput.addEventListener('input', filterLibrarians);
    if (createForm) createForm.addEventListener('submit', handleCreate);
    if (editForm) editForm.addEventListener('submit', handleEdit);
    if (confirmDeleteBtn) confirmDeleteBtn.addEventListener('click', handleDelete);
    if (cancelDeleteBtn) cancelDeleteBtn.addEventListener('click', closeDeleteModal);

    document.querySelectorAll('.close-modal').forEach(btn => {
        btn.addEventListener('click', function() {
            closeAllModals();
        });
    });
}

async function loadLibrarians() {
    showLoading();
    try {
        console.log('Fetching librarians from:', API_BASE_URL);
        const response = await fetch(API_BASE_URL);
        console.log('Response status:', response.status);
        
        const data = await response.json();
        console.log('Response data:', data);

        if (data.success) {
            librarians = data.data;
            console.log('Loaded librarians:', librarians);
            renderTable(librarians);
            hideLoading();
        } else {
            console.error('Error from API:', data.message);
            showError(data.message);
            hideLoading();
        }
    } catch (error) {
        console.error('Failed to load librarians:', error);
        showError('Failed to load librarians: ' + error.message);
        hideLoading();
    }
}

function renderTable(data) {
    const tbody = document.getElementById('librarianTableBody');
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center py-8 text-gray-500">No librarians found</td></tr>';
        return;
    }

    data.forEach(librarian => {
        const row = document.createElement('tr');
        row.className = 'border-b border-gray-200 hover:bg-gray-50';
        row.innerHTML = `
            <td class="py-3 px-4">${librarian.id}</td>
            <td class="py-3 px-4">${librarian.name}</td>
            <td class="py-3 px-4">${librarian.email}</td>
            <td class="py-3 px-4">${librarian.gender}</td>
            <td class="py-3 px-4">${librarian.role}</td>
            <td class="py-3 px-4">
                ${librarian.isDeleted ?
            '<span class="px-2 py-1 bg-red-100 text-red-700 rounded text-xs">Deleted</span>' :
            '<span class="px-2 py-1 bg-green-100 text-green-700 rounded text-xs">Active</span>'}
            </td>
            <td class="py-3 px-4 text-center">
                <button onclick="openEditModal(${librarian.id})" class="text-blue-600 hover:text-blue-800 mr-3">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button onclick="openDeleteModal(${librarian.id})" class="text-red-600 hover:text-red-800">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterLibrarians() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const filtered = librarians.filter(librarian => {
        return librarian.name.toLowerCase().includes(searchTerm) ||
            librarian.email.toLowerCase().includes(searchTerm) ||
            librarian.role.toLowerCase().includes(searchTerm) ||
            librarian.gender.toLowerCase().includes(searchTerm);
    });
    renderTable(filtered);
}

function openCreateModal() {
    const modal = document.getElementById('createModal');
    const form = document.getElementById('createForm');
    
    if (modal) modal.classList.add('active');
    if (form) form.reset();
}

function openEditModal(id) {
    const librarian = librarians.find(l => l.id === id);
    if (!librarian) {
        console.error('Librarian not found with ID:', id);
        return;
    }

    const modal = document.getElementById('editModal');
    const editId = document.getElementById('editId');
    const editName = document.getElementById('editName');
    const editEmail = document.getElementById('editEmail');
    const editGender = document.getElementById('editGender');
    const editRole = document.getElementById('editRole');

    if (editId) editId.value = librarian.id;
    if (editName) editName.value = librarian.name;
    if (editEmail) editEmail.value = librarian.email;
    if (editGender) editGender.value = librarian.gender;
    if (editRole) editRole.value = librarian.role;

    if (modal) modal.classList.add('active');
}

function openDeleteModal(id) {
    deleteTargetId = id;
    const modal = document.getElementById('deleteModal');
    if (modal) modal.classList.add('active');
}

function closeDeleteModal() {
    deleteTargetId = null;
    const modal = document.getElementById('deleteModal');
    if (modal) modal.classList.remove('active');
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
        email: document.getElementById('createEmail').value,
        password: document.getElementById('createPassword').value,
        gender: document.getElementById('createGender').value,
        role: document.getElementById('createRole').value
    };

    console.log('Creating librarian with data:', data);

    try {
        const response = await fetch(API_BASE_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        console.log('Create response status:', response.status);
        const result = await response.json();
        console.log('Create response data:', result);

        if (result.success) {
            showSuccess('Librarian created successfully');
            closeAllModals();
            await loadLibrarians();
        } else {
            showError(result.message || 'Failed to create librarian');
        }
    } catch (error) {
        console.error('Create error:', error);
        showError('Failed to create librarian: ' + error.message);
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
        email: document.getElementById('editEmail').value,
        gender: document.getElementById('editGender').value,
        role: document.getElementById('editRole').value
    };

    console.log('Updating librarian ID:', id, 'with data:', data);

    try {
        const response = await fetch(`${API_BASE_URL}/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        console.log('Update response status:', response.status);
        const result = await response.json();
        console.log('Update response data:', result);

        if (result.success) {
            showSuccess('Librarian updated successfully');
            closeAllModals();
            await loadLibrarians();
        } else {
            showError(result.message || 'Failed to update librarian');
        }
    } catch (error) {
        console.error('Update error:', error);
        showError('Failed to update librarian: ' + error.message);
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

    console.log('Deleting librarian ID:', deleteTargetId);

    try {
        const response = await fetch(`${API_BASE_URL}/${deleteTargetId}`, {
            method: 'DELETE'
        });

        console.log('Delete response status:', response.status);
        const result = await response.json();
        console.log('Delete response data:', result);

        if (result.success) {
            showSuccess('Librarian deleted successfully');
            closeDeleteModal();
            await loadLibrarians();
        } else {
            showError(result.message || 'Failed to delete librarian');
        }
    } catch (error) {
        console.error('Delete error:', error);
        showError('Failed to delete librarian: ' + error.message);
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
    doc.text('Librarian Management Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = librarians.map(librarian => [
        librarian.id,
        librarian.name,
        librarian.email,
        librarian.gender,
        librarian.role,
        librarian.isDeleted ? 'Deleted' : 'Active'
    ]);

    doc.autoTable({
        head: [['ID', 'Name', 'Email', 'Gender', 'Role', 'Status']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 9 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] }
    });

    doc.save('librarians-report.pdf');
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
    const loadingSpinner = document.getElementById('loadingSpinner');
    const tableContainer = document.getElementById('tableContainer');
    
    if (loadingSpinner) loadingSpinner.classList.remove('hidden');
    if (tableContainer) tableContainer.classList.add('hidden');
}

function hideLoading() {
    const loadingSpinner = document.getElementById('loadingSpinner');
    const tableContainer = document.getElementById('tableContainer');
    
    if (loadingSpinner) loadingSpinner.classList.add('hidden');
    if (tableContainer) tableContainer.classList.remove('hidden');
}