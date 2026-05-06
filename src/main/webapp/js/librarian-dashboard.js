const BOOKS_API = 'http://localhost:8081/api/books';
const MEMBERS_API = 'http://localhost:8081/api/members';

document.addEventListener('DOMContentLoaded', function() {
    loadDashboardStats();
});

async function loadDashboardStats() {
    try {
        const [booksResponse, membersResponse] = await Promise.all([
            fetch(BOOKS_API),
            fetch(MEMBERS_API)
        ]);

        const booksData = await booksResponse.json();
        const membersData = await membersResponse.json();

        if (booksData.success) {
            const books = booksData.data;
            const availableBooks = books.filter(book => !book.isBorrowed && !book.isDeleted);
            const borrowedBooks = books.filter(book => book.isBorrowed && !book.isDeleted);

            document.getElementById('totalBooks').textContent = books.length;
            document.getElementById('availableBooks').textContent = availableBooks.length;
            document.getElementById('borrowedBooks').textContent = borrowedBooks.length;
        }

        if (membersData.success) {
            document.getElementById('totalMembers').textContent = membersData.count;
        }
    } catch (error) {
        console.error('Failed to load dashboard stats:', error);
    }
}