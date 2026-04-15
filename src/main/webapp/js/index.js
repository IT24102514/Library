const API_BASE_URL = 'http://localhost:8081/api/books';
const FEEDBACK_API_URL = 'http://localhost:8081/api/feedbacks';
const BORROW_API_URL = 'http://localhost:8081/api/borrows';

let allBooks = [];
let currentBookId = null;
let currentUserId = null;
let selectedRating = 0;
let refreshInterval = null;

document.addEventListener('DOMContentLoaded', function() {
    currentUserId = localStorage.getItem('userId');
    loadBooks();
    setupEventListeners();
    startRealTimeUpdates();
});

function startRealTimeUpdates() {
    // Refresh books every 10 seconds for real-time updates
    refreshInterval = setInterval(() => {
        console.log('Refreshing books for real-time updates...');
        loadBooks();
    }, 10000); // 10 seconds
}

// Clear interval when page is unloaded
window.addEventListener('beforeunload', () => {
    if (refreshInterval) {
        clearInterval(refreshInterval);
    }
});

function setupEventListeners() {
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('input', filterBooks);
    }

    const addReviewBtn = document.getElementById('addReviewBtn');
    if (addReviewBtn) {
        addReviewBtn.addEventListener('click', openReviewModal);
    }

    const borrowBookBtn = document.getElementById('borrowBookBtn');
    if (borrowBookBtn) {
        borrowBookBtn.addEventListener('click', handleBorrowBook);
    }

    const reviewForm = document.getElementById('reviewForm');
    if (reviewForm) {
        reviewForm.addEventListener('submit', handleReviewSubmit);
    }

    document.querySelectorAll('.close-modal').forEach(btn => {
        btn.addEventListener('click', closeAllModals);
    });

    document.querySelectorAll('.star-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            selectRating(parseInt(this.dataset.rating));
        });
    });
}

async function loadBooks() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        console.log('Books API Response:', data);

        if (data.success) {
            allBooks = data.data.filter(book => !book.isDeleted);
            console.log('Loaded books:', allBooks);
            renderBooks(allBooks);
            hideLoading();
        } else {
            hideLoading();
            showNoBooks();
        }
    } catch (error) {
        console.error('Failed to load books:', error);
        hideLoading();
        showNoBooks();
    }
}

function renderBooks(books) {
    const container = document.getElementById('booksContainer');
    const noBooks = document.getElementById('noBooks');

    container.innerHTML = '';

    if (books.length === 0) {
        container.classList.add('hidden');
        noBooks.classList.remove('hidden');
        return;
    }

    container.classList.remove('hidden');
    noBooks.classList.add('hidden');

    books.forEach(book => {
        const bookCard = document.createElement('div');
        bookCard.className = 'book-card';
        bookCard.onclick = () => openBookModal(book.id);

        // Use placeholder if no image
        const imageUrl = book.imageUrl && book.imageUrl.trim() !== ''
            ? book.imageUrl
            : `https://via.placeholder.com/280x350/667eea/ffffff?text=${encodeURIComponent(book.name || 'Book')}`;

        const availableCopies = (book.totalCopies || 0) - (book.borrowedCopies || 0);
        const statusClass = availableCopies > 0 ? 'status-available' : 'status-unavailable';
        const statusText = availableCopies > 0 ? 'Available' : 'Borrowed';

        console.log(`Book "${book.name}": Total=${book.totalCopies}, Borrowed=${book.borrowedCopies}, Available=${availableCopies}`);

        bookCard.innerHTML = `
            <img src="${imageUrl}" alt="${book.name || 'Book'}" class="book-image" onerror="this.src='https://via.placeholder.com/280x350/667eea/ffffff?text=Book'">
            <div class="book-info">
                <h3 class="book-title">${book.name || 'Untitled'}</h3>
                <p class="book-author">
                    <i class="fa-solid fa-user"></i>
                    <span>by ${book.authorName || 'Unknown Author'}</span>
                </p>
                <div class="book-details">
                    <span class="book-category">${book.category && book.category.name ? book.category.name : 'Uncategorized'}</span>
                    <span class="book-status ${statusClass}">${statusText}</span>
                </div>
            </div>
        `;

        container.appendChild(bookCard);
    });
}

function filterBooks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();

    const filtered = allBooks.filter(book => {
        return book.name.toLowerCase().includes(searchTerm) ||
            book.authorName.toLowerCase().includes(searchTerm);
    });

    renderBooks(filtered);
}

async function openBookModal(bookId) {
    currentBookId = bookId;
    const book = allBooks.find(b => b.id === bookId);
    if (!book) return;

    const imageUrl = book.imageUrl && book.imageUrl.trim() !== ''
        ? book.imageUrl
        : `https://via.placeholder.com/200x280/667eea/ffffff?text=${encodeURIComponent(book.name || 'Book')}`;

    document.getElementById('modalBookImage').innerHTML = `
        <img src="${imageUrl}" alt="${book.name || 'Book'}" style="width: 100%; height: 100%; object-fit: cover; border-radius: 12px;" onerror="this.src='https://via.placeholder.com/200x280/667eea/ffffff?text=Book'">
    `;

    document.getElementById('modalBookTitle').textContent = book.name || 'Untitled';

    const authorSpan = document.getElementById('modalBookAuthor');
    authorSpan.innerHTML = `
        <i class="fa-solid fa-user"></i>
        <span>by ${book.authorName || 'Unknown Author'}</span>
    `;

    const categorySpan = document.getElementById('modalBookCategory');
    categorySpan.innerHTML = `<span class="book-category-tag" style="display: inline-block; padding: 0.35rem 0.75rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border-radius: 15px; font-size: 0.8rem; font-weight: 600;">${book.category && book.category.name ? book.category.name : 'Uncategorized'}</span>`;

    document.getElementById('modalBookDescription').textContent = book.description || 'No description available.';

    const availableCopies = (book.totalCopies || 0) - (book.borrowedCopies || 0);
    const statusBadge = availableCopies > 0
        ? `<span class="status-badge status-available" style="display: inline-block; padding: 0.5rem 1rem; background: linear-gradient(135deg, #d1fae5 0%, #a7f3d0 100%); color: #065f46; border-radius: 20px; font-size: 0.9rem; font-weight: 600;"><i class="fa-solid fa-circle-check"></i> Available - ${availableCopies} of ${book.totalCopies || 0} copies</span>`
        : `<span class="status-badge status-unavailable" style="display: inline-block; padding: 0.5rem 1rem; background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%); color: #991b1b; border-radius: 20px; font-size: 0.9rem; font-weight: 600;"><i class="fa-solid fa-circle-xmark"></i> Not Available - All copies borrowed</span>`;

    document.getElementById('modalBookStatus').innerHTML = statusBadge;

    // Show/hide member features based on login status
    const borrowSection = document.getElementById('borrowBookSection');
    const addReviewBtn = document.getElementById('addReviewBtn');

    if (currentUserId) {
        addReviewBtn.classList.remove('hidden');

        // Show borrow button only if book is available
        if (availableCopies > 0) {
            borrowSection.classList.remove('hidden');
        } else {
            borrowSection.classList.add('hidden');
        }
    } else {
        addReviewBtn.classList.add('hidden');
        borrowSection.classList.add('hidden');
    }

    await loadReviews(bookId);

    document.getElementById('bookModal').classList.add('active');
}

async function loadReviews(bookId) {
    try {
        const response = await fetch(`${FEEDBACK_API_URL}/book/${bookId}`);
        const data = await response.json();

        if (data.success) {
            renderReviews(data.data);
        } else {
            renderReviews([]);
        }
    } catch (error) {
        console.error('Failed to load reviews:', error);
        renderReviews([]);
    }
}

function renderReviews(reviews) {
    const container = document.getElementById('reviewsContainer');

    if (reviews.length === 0) {
        container.innerHTML = '<p class="text-gray-500 text-center py-4">No reviews yet. Be the first to review!</p>';
        return;
    }

    container.innerHTML = reviews.map(review => {
        const stars = '<i class="fa-solid fa-star review-stars"></i>'.repeat(review.rating);
        const isOwner = currentUserId && parseInt(currentUserId) === review.memberId;

        const actions = isOwner ? `
            <div class="flex gap-2 mt-2">
                <button onclick="editReview(${review.id})" class="text-blue-600 hover:text-blue-800 text-sm">
                    <i class="fa-solid fa-pen-to-square mr-1"></i>Edit
                </button>
                <button onclick="deleteReview(${review.id})" class="text-red-600 hover:text-red-800 text-sm">
                    <i class="fa-solid fa-trash mr-1"></i>Delete
                </button>
            </div>
        ` : '';

        return `
            <div class="review-card">
                <div class="flex justify-between items-start mb-2">
                    <div>
                        <div class="font-semibold">${review.member ? review.member.name : 'Anonymous'}</div>
                        <div class="text-sm">${stars}</div>
                    </div>
                    <div class="text-xs text-gray-500">${new Date(review.createdAt).toLocaleDateString()}</div>
                </div>
                <p class="text-gray-700">${review.content}</p>
                ${actions}
            </div>
        `;
    }).join('');
}

function openReviewModal(isEdit = false) {
    if (!currentUserId) {
        alert('Please login to add a review');
        return;
    }

    document.getElementById('reviewModalTitle').textContent = isEdit ? 'Edit Review' : 'Add Review';
    document.getElementById('reviewForm').reset();
    document.getElementById('reviewId').value = '';
    document.getElementById('reviewBookId').value = currentBookId;
    selectRating(0);

    document.getElementById('reviewModal').classList.add('active');
}

async function editReview(reviewId) {
    try {
        const response = await fetch(`${FEEDBACK_API_URL}/${reviewId}`);
        const data = await response.json();

        if (data.success) {
            const review = data.data;
            document.getElementById('reviewModalTitle').textContent = 'Edit Review';
            document.getElementById('reviewId').value = review.id;
            document.getElementById('reviewBookId').value = review.bookId;
            document.getElementById('comment').value = review.content;
            selectRating(review.rating);

            document.getElementById('reviewModal').classList.add('active');
        }
    } catch (error) {
        alert('Failed to load review: ' + error.message);
    }
}

async function deleteReview(reviewId) {
    if (!confirm('Are you sure you want to delete this review?')) return;

    try {
        const response = await fetch(`${FEEDBACK_API_URL}/${reviewId}`, {
            method: 'DELETE'
        });

        const result = await response.json();

        if (result.success) {
            await loadReviews(currentBookId);
        } else {
            alert(result.message);
        }
    } catch (error) {
        alert('Failed to delete review: ' + error.message);
    }
}

async function handleReviewSubmit(e) {
    e.preventDefault();

    if (selectedRating === 0) {
        alert('Please select a rating');
        return;
    }

    const reviewId = document.getElementById('reviewId').value;
    const data = {
        content: document.getElementById('comment').value,
        rating: selectedRating,
        bookId: parseInt(document.getElementById('reviewBookId').value),
        memberId: parseInt(currentUserId)
    };

    try {
        const url = reviewId ? `${FEEDBACK_API_URL}/${reviewId}` : FEEDBACK_API_URL;
        const method = reviewId ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();

        if (result.success) {
            closeAllModals();
            await loadReviews(currentBookId);
        } else {
            alert(result.message);
        }
    } catch (error) {
        alert('Failed to submit review: ' + error.message);
    }
}

function selectRating(rating) {
    selectedRating = rating;
    const ratingInput = document.getElementById('rating');
    if (ratingInput) {
        ratingInput.value = rating;
    }

    document.querySelectorAll('.star-btn').forEach((btn, index) => {
        const star = btn.querySelector('i');
        if (star) {
            if (index < rating) {
                star.style.color = '#fbbf24';
            } else {
                star.style.color = '#e2e8f0';
            }
        }
    });
}

async function handleBorrowBook() {
    if (!currentUserId) {
        alert('Please login to borrow books');
        return;
    }

    if (!currentBookId) {
        alert('No book selected');
        return;
    }

    const borrowBtn = document.getElementById('borrowBookBtn');
    const originalContent = borrowBtn.innerHTML;
    
    borrowBtn.disabled = true;
    borrowBtn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i><span>Borrowing...</span>';

    try {
        // Set due date to 14 days from now
        const borrowDate = new Date();
        const dueDate = new Date();
        dueDate.setDate(dueDate.getDate() + 14);

        const borrowData = {
            bookId: parseInt(currentBookId),
            memberId: parseInt(currentUserId),
            borrowDate: borrowDate.toISOString().split('T')[0],
            dueDate: dueDate.toISOString().split('T')[0],
            returned: false
        };

        const response = await fetch(BORROW_API_URL, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(borrowData)
        });

        const result = await response.json();

        if (result.success) {
            alert('Book borrowed successfully! Due date: ' + dueDate.toLocaleDateString());
            closeAllModals();
            await loadBooks(); // Reload books to update availability
        } else {
            alert(result.message || 'Failed to borrow book');
        }
    } catch (error) {
        console.error('Error borrowing book:', error);
        alert('Failed to borrow book: ' + error.message);
    } finally {
        borrowBtn.disabled = false;
        borrowBtn.innerHTML = originalContent;
    }
}

function closeAllModals() {
    const modals = document.querySelectorAll('.modal');
    if (modals) {
        modals.forEach(modal => {
            modal.classList.remove('active');
        });
    }
}

function showLoading() {
    const loadingSpinner = document.getElementById('loadingSpinner');
    const booksContainer = document.getElementById('booksContainer');
    const noBooks = document.getElementById('noBooks');
    
    if (loadingSpinner) loadingSpinner.classList.remove('hidden');
    if (booksContainer) booksContainer.classList.add('hidden');
    if (noBooks) noBooks.classList.add('hidden');
}

function hideLoading() {
    const loadingSpinner = document.getElementById('loadingSpinner');
    if (loadingSpinner) loadingSpinner.classList.add('hidden');
}

function showNoBooks() {
    const booksContainer = document.getElementById('booksContainer');
    const noBooks = document.getElementById('noBooks');
    
    if (booksContainer) booksContainer.classList.add('hidden');
    if (noBooks) noBooks.classList.remove('hidden');
}