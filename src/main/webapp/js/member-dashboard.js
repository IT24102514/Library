// Member Dashboard JavaScript

document.addEventListener('DOMContentLoaded', function() {
    console.log('Member Dashboard loaded');
    
    // Check if user is logged in
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) {
        console.log('No user logged in, redirecting to login');
        window.location.href = '/login';
        return;
    }

    // Update welcome message
    const welcomeMessage = document.getElementById('welcomeMessage');
    if (welcomeMessage && user.name) {
        welcomeMessage.textContent = `Welcome back, ${user.name}!`;
    }

    // Load dashboard data
    loadDashboardStats();
    loadRecentBorrowings();
});

async function loadDashboardStats() {
    console.log('Loading dashboard statistics...');
    
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) return;

    try {
        const response = await fetch(`/api/borrows?userId=${user.id}`);
        console.log('Stats response status:', response.status);
        
        if (response.ok) {
            const data = await response.json();
            console.log('Stats data received:', data);
            
            if (data.success && data.data) {
                const borrowings = data.data;
                
                // Calculate stats
                const active = borrowings.filter(b => b.status === 'BORROWED').length;
                const overdue = borrowings.filter(b => {
                    if (b.status === 'BORROWED' && b.dueDate) {
                        return new Date(b.dueDate) < new Date();
                    }
                    return false;
                }).length;
                const returned = borrowings.filter(b => b.status === 'RETURNED').length;

                // Update stats
                document.getElementById('activeBorrowings').textContent = active;
                document.getElementById('overdueBooks').textContent = overdue;
                document.getElementById('totalReturned').textContent = returned;

                console.log('Stats updated:', { active, overdue, returned });
            }
        } else {
            console.error('Failed to load stats:', response.status);
            // Show default values
            document.getElementById('activeBorrowings').textContent = '0';
            document.getElementById('overdueBooks').textContent = '0';
            document.getElementById('totalReturned').textContent = '0';
        }
    } catch (error) {
        console.error('Error loading dashboard stats:', error);
        // Show default values on error
        document.getElementById('activeBorrowings').textContent = '0';
        document.getElementById('overdueBooks').textContent = '0';
        document.getElementById('totalReturned').textContent = '0';
    }
}

async function loadRecentBorrowings() {
    console.log('Loading recent borrowings...');
    
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    if (!user.id) return;

    const loadingSpinner = document.getElementById('loadingSpinner');
    const borrowingsContainer = document.getElementById('borrowingsContainer');
    const noBorrowings = document.getElementById('noBorrowings');

    try {
        const response = await fetch(`/api/borrows?userId=${user.id}`);
        console.log('Borrowings response status:', response.status);
        
        if (response.ok) {
            const data = await response.json();
            console.log('Borrowings data received:', data);
            
            if (data.success && data.data) {
                const borrowings = data.data;
                
                // Filter only active borrowings and take first 3
                const activeBorrowings = borrowings
                    .filter(b => b.status === 'BORROWED')
                    .slice(0, 3);

                loadingSpinner.classList.add('hidden');

                if (activeBorrowings.length === 0) {
                    noBorrowings.classList.remove('hidden');
                } else {
                    borrowingsContainer.classList.remove('hidden');
                    borrowingsContainer.innerHTML = renderBorrowingsTable(activeBorrowings);
                }
            } else {
                loadingSpinner.classList.add('hidden');
                noBorrowings.classList.remove('hidden');
            }
        } else {
            console.error('Failed to load borrowings:', response.status);
            loadingSpinner.classList.add('hidden');
            noBorrowings.classList.remove('hidden');
        }
    } catch (error) {
        console.error('Error loading recent borrowings:', error);
        loadingSpinner.classList.add('hidden');
        noBorrowings.classList.remove('hidden');
    }
}

function renderBorrowingsTable(borrowings) {
    return `
        <div class="overflow-x-auto">
            <table class="w-full">
                <thead>
                    <tr class="border-b">
                        <th class="text-left py-3 px-4 font-semibold text-gray-700">Book</th>
                        <th class="text-left py-3 px-4 font-semibold text-gray-700">Borrow Date</th>
                        <th class="text-left py-3 px-4 font-semibold text-gray-700">Due Date</th>
                        <th class="text-left py-3 px-4 font-semibold text-gray-700">Status</th>
                    </tr>
                </thead>
                <tbody>
                    ${borrowings.map(borrowing => {
                        const isOverdue = new Date(borrowing.dueDate) < new Date();
                        const statusClass = isOverdue ? 'bg-red-100 text-red-700' : 'bg-blue-100 text-blue-700';
                        const statusText = isOverdue ? 'Overdue' : 'Active';
                        
                        return `
                            <tr class="border-b hover:bg-gray-50">
                                <td class="py-3 px-4">
                                    <div class="font-semibold">${borrowing.book?.name || 'Unknown Book'}</div>
                                    <div class="text-sm text-gray-600">${borrowing.book?.author || 'Unknown Author'}</div>
                                </td>
                                <td class="py-3 px-4 text-gray-600">
                                    ${new Date(borrowing.borrowDate).toLocaleDateString()}
                                </td>
                                <td class="py-3 px-4 text-gray-600">
                                    ${new Date(borrowing.dueDate).toLocaleDateString()}
                                </td>
                                <td class="py-3 px-4">
                                    <span class="px-3 py-1 rounded-full text-sm font-semibold ${statusClass}">
                                        ${statusText}
                                    </span>
                                </td>
                            </tr>
                        `;
                    }).join('')}
                </tbody>
            </table>
        </div>
    `;
}
