<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Library Management System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="/css/nav.css">
    <link rel="stylesheet" href="/css/index.css">
</head>
<body>
<!-- 3D Animated Background -->
<div class="animated-background">
    <div class="sphere sphere-1"></div>
    <div class="sphere sphere-2"></div>
    <div class="sphere sphere-3"></div>
    <div class="sphere sphere-4"></div>
    <div class="sphere sphere-5"></div>
    
    <!-- Floating Particles -->
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
    <div class="particle"></div>
</div>
<div class="glass-overlay"></div>

<nav class="navbar">
    <div class="navbar-container">
        <a href="/" class="navbar-brand">
            <i class="fa-solid fa-book-open"></i>
            <span>Library Management System</span>
        </a>
        <div class="navbar-menu" id="navbarMenu">
            <!-- Will be dynamically populated by nav.js -->
        </div>
    </div>
</nav>

<main class="container">
    <div class="header">
        <h1 class="title">Browse Our Collection</h1>
        <p class="subtitle">Discover and borrow books from our library</p>
    </div>

    <div class="search-section">
        <div class="search-box">
            <i class="fa-solid fa-search search-icon"></i>
            <input type="text" id="searchInput" placeholder="Search by book name or author..." class="search-input">
        </div>
    </div>

    <div id="loadingSpinner" class="loading">
        <i class="fa-solid fa-spinner fa-spin spinner"></i>
    </div>

    <div id="booksContainer" class="books-grid hidden">
    </div>

    <div id="noBooks" class="no-books hidden">
        <i class="fa-solid fa-book-open"></i>
        <p>No books found</p>
    </div>
</main>

<!-- Book Details Modal -->
<div id="bookModal" class="modal">
    <div class="modal-content-large">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <h3 style="text-align: left; font-size: 1.75rem; font-weight: bold; color: #1e293b;">Book Details</h3>
            <button class="close-modal" style="background: #f1f5f9; border: none; width: 35px; height: 35px; border-radius: 50%; cursor: pointer; display: flex; align-items: center; justify-content: center; color: #64748b; font-size: 1.25rem;">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>

        <div class="book-details" style="margin-bottom: 2rem;">
            <div style="display: flex; gap: 1.5rem; flex-wrap: wrap;">
                <div id="modalBookImage" style="width: 200px; height: 280px; background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%); border-radius: 12px; flex-shrink: 0; display: flex; align-items: center; justify-content: center; overflow: hidden;">
                </div>
                <div style="flex: 1; min-width: 250px;">
                    <h4 id="modalBookTitle" style="font-size: 1.5rem; font-weight: bold; color: #1e293b; margin-bottom: 0.5rem;"></h4>
                    <p id="modalBookAuthor" style="color: #64748b; margin-bottom: 0.75rem; display: flex; align-items: center; gap: 0.5rem;"></p>
                    <div id="modalBookCategory" style="margin-bottom: 1rem;"></div>
                    <p id="modalBookDescription" style="color: #334155; line-height: 1.6; margin-bottom: 1rem; padding: 1rem; background: #f8fafc; border-radius: 8px;"></p>
                    <div id="modalBookStatus" style="margin-bottom: 1rem;"></div>
                    <div id="borrowBookSection" class="hidden" style="margin-top: 1.5rem;">
                        <button id="borrowBookBtn" style="padding: 0.875rem 2rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 12px; font-weight: 600; cursor: pointer; display: flex; align-items: center; gap: 0.5rem; transition: all 0.3s ease;">
                            <i class="fa-solid fa-book-open"></i>
                            <span>Borrow This Book</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div style="border-top: 2px solid #e2e8f0; padding-top: 1.5rem;">
            <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
                <h4 style="font-size: 1.25rem; font-weight: bold; color: #1e293b;">Reviews</h4>
                <button id="addReviewBtn" class="hidden" style="background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 0.75rem 1.5rem; border: none; border-radius: 10px; cursor: pointer; font-weight: 600; display: flex; align-items: center; gap: 0.5rem;">
                    <i class="fa-solid fa-plus"></i>Add Review
                </button>
            </div>

            <div id="reviewsContainer"></div>
        </div>
    </div>
</div>

<!-- Review Modal -->
<div id="reviewModal" class="modal">
    <div class="modal-content">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem;">
            <h3 style="font-size: 1.5rem; font-weight: bold;" id="reviewModalTitle">Add Review</h3>
            <button class="close-modal" style="background: #f1f5f9; border: none; width: 35px; height: 35px; border-radius: 50%; cursor: pointer; display: flex; align-items: center; justify-content: center; color: #64748b;">
                <i class="fa-solid fa-xmark"></i>
            </button>
        </div>
        <form id="reviewForm">
            <input type="hidden" id="reviewId">
            <input type="hidden" id="reviewBookId">
            <div style="margin-bottom: 1.5rem;">
                <label style="display: block; font-weight: 600; margin-bottom: 0.5rem; color: #334155;">Rating</label>
                <div style="display: flex; gap: 0.5rem;" id="starRating">
                    <button type="button" class="star-btn" data-rating="1"><i class="fa-solid fa-star" style="font-size: 1.5rem; color: #e2e8f0;"></i></button>
                    <button type="button" class="star-btn" data-rating="2"><i class="fa-solid fa-star" style="font-size: 1.5rem; color: #e2e8f0;"></i></button>
                    <button type="button" class="star-btn" data-rating="3"><i class="fa-solid fa-star" style="font-size: 1.5rem; color: #e2e8f0;"></i></button>
                    <button type="button" class="star-btn" data-rating="4"><i class="fa-solid fa-star" style="font-size: 1.5rem; color: #e2e8f0;"></i></button>
                    <button type="button" class="star-btn" data-rating="5"><i class="fa-solid fa-star" style="font-size: 1.5rem; color: #e2e8f0;"></i></button>
                </div>
                <input type="hidden" id="rating" required>
            </div>
            <div style="margin-bottom: 1.5rem;">
                <label style="display: block; font-weight: 600; margin-bottom: 0.5rem; color: #334155;">Comment</label>
                <textarea id="comment" rows="4" style="width: 100%; padding: 0.875rem 1rem; border: 2px solid #e2e8f0; border-radius: 10px; font-size: 1rem; outline: none; resize: vertical; min-height: 100px; font-family: 'Ubuntu', sans-serif;" required></textarea>
            </div>
            <div style="display: flex; gap: 1rem; justify-content: flex-end;">
                <button type="button" class="close-modal" style="padding: 0.875rem 1.5rem; background: #e2e8f0; color: #1e293b; border: none; border-radius: 10px; cursor: pointer; font-weight: 600;">Cancel</button>
                <button type="submit" style="padding: 0.875rem 1.5rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 10px; cursor: pointer; font-weight: 600;">Submit Review</button>
            </div>
        </form>
    </div>
</div>

<script src="/js/nav.js"></script>
<script src="/js/index.js"></script>
</body>
</html>