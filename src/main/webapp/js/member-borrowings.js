const BORROW_API_URL = 'http://localhost:8081/api/borrows';

let allBorrowings = [];
let currentFilter = 'all';
let currentUserId = null;

document.addEventListener('DOMContentLoaded', function() {
    currentUserId = localStorage.getItem('userId');
    
    if (!currentUserId) {
        window.location.href = '/login';
        return;
    }
    
    loadBorrowings();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('filterAll').addEventListener('click', () => filterBorrowings('all'));
    document.getElementById('filterActive').addEventListener('click', () => filterBorrowings('active'));
    document.getElementById('filterReturned').addEventListener('click', () => filterBorrowings('returned'));
    document.getElementById('filterOverdue').addEventListener('click', () => filterBorrowings('overdue'));
}

async function loadBorrowings() {
    showLoading();
    try {
        const response = await fetch(`${BORROW_API_URL}/member/${currentUserId}`);
        const data = await response.json();
        
        if (data.success) {
            allBorrowings = data.data;
            filterBorrowings(currentFilter);
            hideLoading();
        } else {
            hideLoading();
            showNoBorrowings();
        }
    } catch (error) {
        console.error('Failed to load borrowings:', error);
        hideLoading();
        showNoBorrowings();
    }
}

function filterBorrowings(filter) {
    currentFilter = filter;
    
    // Update active button
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById(`filter${filter.charAt(0).toUpperCase() + filter.slice(1)}`).classList.add('active');
    
    let filtered = allBorrowings;
    const today = new Date();
    
    if (filter === 'active') {
        filtered = allBorrowings.filter(b => !b.returned);
    } else if (filter === 'returned') {
        filtered = allBorrowings.filter(b => b.returned);
    } else if (filter === 'overdue') {
        filtered = allBorrowings.filter(b => !b.returned && new Date(b.dueDate) < today);
    }
    
    renderBorrowings(filtered);
}

function renderBorrowings(borrowings) {
    const container = document.getElementById('borrowingsContainer');
    const noBorrowings = document.getElementById('noBorrowings');
    
    if (borrowings.length === 0) {
        container.classList.add('hidden');
        noBorrowings.classList.remove('hidden');
        return;
    }
    
    container.classList.remove('hidden');
    noBorrowings.classList.add('hidden');
    
    const today = new Date();
    
    container.innerHTML = borrowings.map(borrow => {
        const dueDate = new Date(borrow.dueDate);
        const isOverdue = !borrow.returned && dueDate < today;
        const statusClass = borrow.returned ? 'bg-green-100 text-green-800' : 
                           isOverdue ? 'bg-red-100 text-red-800' : 
                           'bg-yellow-100 text-yellow-800';
        const statusText = borrow.returned ? 'Returned' : 
                          isOverdue ? 'Overdue' : 
                          'Active';
        
        const bookImage = borrow.book && borrow.book.imageUrl 
            ? borrow.book.imageUrl 
            : `https://via.placeholder.com/100x140/667eea/ffffff?text=${encodeURIComponent(borrow.book?.name || 'Book')}`;
        
        return `
            <div class="bg-white rounded-lg shadow-md p-6 mb-4">
                <div class="flex gap-4">
                    <img src="${bookImage}" alt="${borrow.book?.name || 'Book'}" 
                         class="w-24 h-32 object-cover rounded"
                         onerror="this.src='https://via.placeholder.com/100x140/667eea/ffffff?text=Book'">
                    <div class="flex-1">
                        <div class="flex justify-between items-start">
                            <div>
                                <h3 class="text-xl font-bold">${borrow.book?.name || 'Unknown Book'}</h3>
                                <p class="text-gray-600">by ${borrow.book?.authorName || 'Unknown Author'}</p>
                            </div>
                            <span class="px-3 py-1 rounded-full text-sm font-semibold ${statusClass}">
                                ${statusText}
                            </span>
                        </div>
                        <div class="mt-4 grid grid-cols-3 gap-4 text-sm">
                            <div>
                                <p class="text-gray-500">Borrowed Date</p>
                                <p class="font-semibold">${new Date(borrow.borrowDate).toLocaleDateString()}</p>
                            </div>
                            <div>
                                <p class="text-gray-500">Due Date</p>
                                <p class="font-semibold ${isOverdue ? 'text-red-600' : ''}">${dueDate.toLocaleDateString()}</p>
                            </div>
                            <div>
                                <p class="text-gray-500">${borrow.returned ? 'Returned Date' : 'Days Remaining'}</p>
                                <p class="font-semibold">
                                    ${borrow.returned 
                                        ? new Date(borrow.returnDate).toLocaleDateString() 
                                        : Math.ceil((dueDate - today) / (1000 * 60 * 60 * 24)) + ' days'}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }).join('');
}

function showLoading() {
    document.getElementById('loadingSpinner').classList.remove('hidden');
    document.getElementById('borrowingsContainer').classList.add('hidden');
    document.getElementById('noBorrowings').classList.add('hidden');
}

function hideLoading() {
    document.getElementById('loadingSpinner').classList.add('hidden');
}

function showNoBorrowings() {
    document.getElementById('borrowingsContainer').classList.add('hidden');
    document.getElementById('noBorrowings').classList.remove('hidden');
}
