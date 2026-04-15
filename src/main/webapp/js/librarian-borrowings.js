const API_BASE_URL = 'http://localhost:8081/api/borrows';
const BOOKS_API = 'http://localhost:8081/api/books';
const MEMBERS_API = 'http://localhost:8081/api/members';

let borrowings = [];
let books = [];
let activeBooks = [];
let members = [];
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadData();
    setupEventListeners();
    setTodayDate();
});

function setupEventListeners() {
    document.getElementById('createBorrowBtn').addEventListener('click', openCreateModal);
    document.getElementById('exportPdfBtn').addEventListener('click', exportToPdf);
    document.getElementById('searchInput').addEventListener('input', filterBorrowings);
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

function setTodayDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('createBorrowedDate').value = today;
    document.getElementById('createEndDate').setAttribute('min', today);
}

async function loadData() {
    await Promise.all([loadBorrowings(), loadBooks(), loadMembers()]);
}

async function loadBorrowings() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        if (data.success) {
            borrowings = data.data;
            renderTable(borrowings);
            hideLoading();
        } else {
            showError(data.message);
            hideLoading();
        }
    } catch (error) {
        showError('Failed to load borrowings: ' + error.message);
        hideLoading();
    }
}

async function loadBooks() {
    try {
        const response = await fetch(BOOKS_API);
        const data = await response.json();

        if (data.success) {
            books = data.data.filter(book => !book.isDeleted);
            activeBooks = data.data.filter(book => !book.isBorrowed && !book.isDeleted);
            populateBookDropdowns();
        }
    } catch (error) {
        showError('Failed to load books: ' + error.message);
    }
}

async function loadMembers() {
    try {
        const response = await fetch(MEMBERS_API);
        const data = await response.json();

        if (data.success) {
            members = data.data;
            populateMemberDropdowns();
        }
    } catch (error) {
        showError('Failed to load members: ' + error.message);
    }
}

function populateBookDropdowns() {
    const createSelect = document.getElementById('createBookId');
    const editSelect = document.getElementById('editBookId');

    createSelect.innerHTML = '<option value="">Select Book</option>';
    editSelect.innerHTML = '<option value="">Select Book</option>';

    (editSelect ? books : activeBooks).forEach(book => {
        const option1 = document.createElement('option');
        option1.value = book.id;
        option1.textContent = `${book.name} - ${book.authorName}`;
        createSelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = book.id;
        option2.textContent = `${book.name} - ${book.authorName}`;
        editSelect.appendChild(option2);
    });
}

function populateMemberDropdowns() {
    const createSelect = document.getElementById('createMemberId');
    const editSelect = document.getElementById('editMemberId');

    createSelect.innerHTML = '<option value="">Select Member</option>';
    editSelect.innerHTML = '<option value="">Select Member</option>';

    members.forEach(member => {
        const option1 = document.createElement('option');
        option1.value = member.id;
        option1.textContent = `${member.name} - ${member.email}`;
        createSelect.appendChild(option1);

        const option2 = document.createElement('option');
        option2.value = member.id;
        option2.textContent = `${member.name} - ${member.email}`;
        editSelect.appendChild(option2);
    });
}

function renderTable(data) {
    const tbody = document.getElementById('borrowTableBody');
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" class="text-center py-8 text-gray-500">No borrowings found</td></tr>';
        return;
    }

    data.forEach(borrow => {
        const row = document.createElement('tr');
        row.className = 'border-b border-gray-200 hover:bg-gray-50';

        let statusBadge;
        if (borrow.isReturned) {
            statusBadge = '<span class="px-2 py-1 bg-green-100 text-green-700 rounded text-xs">Returned</span>';
        } else if (borrow.isOverdue) {
            statusBadge = '<span class="px-2 py-1 bg-red-100 text-red-700 rounded text-xs">Overdue</span>';
        } else {
            statusBadge = '<span class="px-2 py-1 bg-yellow-100 text-yellow-700 rounded text-xs">Active</span>';
        }

        const returnButton = !borrow.isReturned
            ? `<button onclick="handleReturn(${borrow.id})" class="text-green-600 hover:text-green-800 mr-3">
                <i class="fa-solid fa-check-circle"></i>
               </button>`
            : '';

        row.innerHTML = `
            <td class="py-3 px-4">${borrow.id}</td>
            <td class="py-3 px-4">${borrow.book ? borrow.book.name : 'N/A'}</td>
            <td class="py-3 px-4">${borrow.member ? borrow.member.name : 'N/A'}</td>
            <td class="py-3 px-4">${borrow.borrowedDate}</td>
            <td class="py-3 px-4">${borrow.endDate}</td>
            <td class="py-3 px-4">${statusBadge}</td>
            <td class="py-3 px-4 text-center">
                ${returnButton}
                <button onclick="openEditModal(${borrow.id})" class="text-blue-600 hover:text-blue-800 mr-3">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button onclick="openDeleteModal(${borrow.id})" class="text-red-600 hover:text-red-800">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterBorrowings() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const filtered = borrowings.filter(borrow => {
        const bookName = borrow.book ? borrow.book.name.toLowerCase() : '';
        const memberName = borrow.member ? borrow.member.name.toLowerCase() : '';
        return bookName.includes(searchTerm) || memberName.includes(searchTerm);
    });
    renderTable(filtered);
}

function openCreateModal() {
    document.getElementById('createModal').classList.add('active');
    document.getElementById('createForm').reset();
    setTodayDate();
}

function openEditModal(id) {
    const borrow = borrowings.find(b => b.id === id);
    if (!borrow) return;

    document.getElementById('editId').value = borrow.id;
    document.getElementById('editMemberId').value = borrow.memberId;
    document.getElementById('editBookId').value = borrow.bookId;
    document.getElementById('editBorrowedDate').value = borrow.borrowedDate;
    document.getElementById('editEndDate').value = borrow.endDate;
    document.getElementById('editNotes').value = borrow.additionalNotes || '';

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('editEndDate').setAttribute('min', today);

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
        bookId: parseInt(document.getElementById('createBookId').value),
        memberId: parseInt(document.getElementById('createMemberId').value),
        borrowedDate: document.getElementById('createBorrowedDate').value,
        endDate: document.getElementById('createEndDate').value,
        additionalNotes: document.getElementById('createNotes').value
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
            showSuccess('Borrowing created successfully');
            closeAllModals();
            loadData();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to create borrowing: ' + error.message);
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
        bookId: parseInt(document.getElementById('editBookId').value),
        memberId: parseInt(document.getElementById('editMemberId').value),
        borrowedDate: document.getElementById('editBorrowedDate').value,
        endDate: document.getElementById('editEndDate').value,
        additionalNotes: document.getElementById('editNotes').value
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
            showSuccess('Borrowing updated successfully');
            closeAllModals();
            loadData();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to update borrowing: ' + error.message);
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
            showSuccess('Borrowing deleted successfully');
            closeDeleteModal();
            loadData();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to delete borrowing: ' + error.message);
    } finally {
        submitBtn.disabled = false;
        btnText.classList.remove('hidden');
        btnLoader.classList.add('hidden');
    }
}

async function handleReturn(id) {
    if (!confirm('Mark this book as returned?')) return;

    try {
        const response = await fetch(`${API_BASE_URL}/${id}/return`, {
            method: 'POST'
        });

        const result = await response.json();

        if (result.success) {
            showSuccess('Book returned successfully');
            loadData();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to return book: ' + error.message);
    }
}

function exportToPdf() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('Borrowings Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = borrowings.map(borrow => [
        borrow.id,
        borrow.book ? borrow.book.name : 'N/A',
        borrow.member ? borrow.member.name : 'N/A',
        borrow.borrowedDate,
        borrow.endDate,
        borrow.isReturned ? 'Returned' : borrow.isOverdue ? 'Overdue' : 'Active'
    ]);

    doc.autoTable({
        head: [['ID', 'Book', 'Member', 'Borrowed', 'Due', 'Status']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 9 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] }
    });

    doc.save('borrowings-report.pdf');
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