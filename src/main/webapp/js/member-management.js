const API_BASE_URL = 'http://localhost:8081/api/members';

let members = [];
let deleteTargetId = null;

document.addEventListener('DOMContentLoaded', function() {
    loadMembers();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('exportPdfBtn').addEventListener('click', exportToPdf);
    document.getElementById('searchInput').addEventListener('input', filterMembers);
    document.getElementById('confirmDeleteBtn').addEventListener('click', handleDelete);
    document.getElementById('cancelDeleteBtn').addEventListener('click', closeDeleteModal);
}

async function loadMembers() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        if (data.success) {
            members = data.data;
            renderTable(members);
            hideLoading();
        } else {
            showError(data.message);
            hideLoading();
        }
    } catch (error) {
        showError('Failed to load members: ' + error.message);
        hideLoading();
    }
}

function renderTable(data) {
    const tbody = document.getElementById('memberTableBody');
    tbody.innerHTML = '';

    if (data.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center py-8 text-gray-500">No members found</td></tr>';
        return;
    }

    data.forEach(member => {
        const row = document.createElement('tr');
        row.className = 'border-b border-gray-200 hover:bg-gray-50';
        row.innerHTML = `
            <td class="py-3 px-4">${member.id}</td>
            <td class="py-3 px-4">${member.name}</td>
            <td class="py-3 px-4">${member.email}</td>
            <td class="py-3 px-4">${member.age}</td>
            <td class="py-3 px-4">${member.gender}</td>
            <td class="py-3 px-4">${member.city}</td>
            <td class="py-3 px-4">
                ${member.isDeleted ?
            '<span class="px-2 py-1 bg-red-100 text-red-700 rounded text-xs">Deleted</span>' :
            '<span class="px-2 py-1 bg-green-100 text-green-700 rounded text-xs">Active</span>'}
            </td>
            <td class="py-3 px-4 text-center">
                <button onclick="openDeleteModal(${member.id})" class="text-red-600 hover:text-red-800">
                    <i class="fa-solid fa-trash"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

function filterMembers() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const filtered = members.filter(member => {
        return member.name.toLowerCase().includes(searchTerm) ||
            member.email.toLowerCase().includes(searchTerm) ||
            member.city.toLowerCase().includes(searchTerm) ||
            member.gender.toLowerCase().includes(searchTerm);
    });
    renderTable(filtered);
}

function openDeleteModal(id) {
    deleteTargetId = id;
    document.getElementById('deleteModal').classList.add('active');
}

function closeDeleteModal() {
    deleteTargetId = null;
    document.getElementById('deleteModal').classList.remove('active');
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
            showSuccess('Member deleted successfully');
            closeDeleteModal();
            loadMembers();
        } else {
            showError(result.message);
        }
    } catch (error) {
        showError('Failed to delete member: ' + error.message);
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
    doc.text('Member Management Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = members.map(member => [
        member.id,
        member.name,
        member.email,
        member.age,
        member.gender,
        member.city,
        member.isDeleted ? 'Deleted' : 'Active'
    ]);

    doc.autoTable({
        head: [['ID', 'Name', 'Email', 'Age', 'Gender', 'City', 'Status']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 9 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] }
    });

    doc.save('members-report.pdf');
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