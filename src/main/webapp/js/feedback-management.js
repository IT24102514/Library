const API_BASE_URL = 'http://localhost:8081/api/feedbacks';

let feedbacks = [];
let filteredFeedbacks = [];

document.addEventListener('DOMContentLoaded', function() {
    loadFeedbacks();
    setupEventListeners();
});

function setupEventListeners() {
    document.getElementById('exportPdfBtn').addEventListener('click', exportToPdf);
    document.getElementById('searchInput').addEventListener('input', filterFeedbacks);
    document.getElementById('ratingFilter').addEventListener('change', filterFeedbacks);
}

async function loadFeedbacks() {
    showLoading();
    try {
        const response = await fetch(API_BASE_URL);
        const data = await response.json();

        if (data.success) {
            feedbacks = data.data;
            filteredFeedbacks = feedbacks;
            renderFeedbacks(filteredFeedbacks);
            hideLoading();
        } else {
            hideLoading();
            showNoFeedback();
        }
    } catch (error) {
        console.error('Failed to load feedbacks:', error);
        hideLoading();
        showNoFeedback();
    }
}

function renderFeedbacks(data) {
    const container = document.getElementById('feedbackContainer');

    if (data.length === 0) {
        showNoFeedback();
        return;
    }

    container.classList.remove('hidden');
    container.innerHTML = data.map(feedback => {
        const stars = '<i class="fa-solid fa-star"></i>'.repeat(feedback.rating);
        const emptyStars = '<i class="fa-regular fa-star"></i>'.repeat(5 - feedback.rating);

        return `
            <div class="feedback-card">
                <div class="flex justify-between items-start mb-3">
                    <div>
                        <div class="font-semibold text-lg mb-1">
                            <i class="fa-solid fa-user text-gray-500 mr-2"></i>
                            ${feedback.member ? feedback.member.name : 'Anonymous'}
                        </div>
                        <div class="text-sm text-gray-600 mb-2">
                            <i class="fa-solid fa-envelope text-gray-500 mr-2"></i>
                            ${feedback.member ? feedback.member.email : 'N/A'}
                        </div>
                        <div class="star-rating">
                            ${stars}${emptyStars}
                            <span class="text-gray-600 ml-2">(${feedback.rating}/5)</span>
                        </div>
                    </div>
                    <div class="text-right">
                        <div class="text-sm text-gray-500 mb-1">
                            <i class="fa-solid fa-calendar mr-1"></i>
                            ${new Date(feedback.createdAt).toLocaleDateString()}
                        </div>
                        <div class="text-sm text-gray-500">
                            <i class="fa-solid fa-clock mr-1"></i>
                            ${new Date(feedback.createdAt).toLocaleTimeString()}
                        </div>
                    </div>
                </div>
                
                <div class="mb-3 p-3 bg-gray-50 rounded-md">
                    <p class="text-gray-700 leading-relaxed">${feedback.content}</p>
                </div>
                
                <div class="flex items-center gap-2 text-sm text-gray-600">
                    <i class="fa-solid fa-book text-gray-500"></i>
                    <span>Book ID: ${feedback.bookId}</span>
                </div>
            </div>
        `;
    }).join('');
}

function filterFeedbacks() {
    const searchTerm = document.getElementById('searchInput').value.toLowerCase();
    const ratingFilter = document.getElementById('ratingFilter').value;

    filteredFeedbacks = feedbacks.filter(feedback => {
        const memberName = feedback.member ? feedback.member.name.toLowerCase() : '';
        const memberEmail = feedback.member ? feedback.member.email.toLowerCase() : '';

        const matchesSearch = memberName.includes(searchTerm) ||
            memberEmail.includes(searchTerm) ||
            feedback.content.toLowerCase().includes(searchTerm);

        const matchesRating = !ratingFilter || feedback.rating === parseInt(ratingFilter);

        return matchesSearch && matchesRating;
    });

    renderFeedbacks(filteredFeedbacks);
}

function exportToPdf() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    doc.setFontSize(18);
    doc.text('Feedback Management Report', 14, 22);

    doc.setFontSize(11);
    doc.text('Generated on: ' + new Date().toLocaleString(), 14, 30);

    const tableData = filteredFeedbacks.map(feedback => [
        feedback.id,
        feedback.member ? feedback.member.name : 'Anonymous',
        feedback.member ? feedback.member.email : 'N/A',
        feedback.rating + '/5',
        feedback.content.substring(0, 50) + (feedback.content.length > 50 ? '...' : ''),
        new Date(feedback.createdAt).toLocaleDateString()
    ]);

    doc.autoTable({
        head: [['ID', 'Member', 'Email', 'Rating', 'Content', 'Date']],
        body: tableData,
        startY: 35,
        theme: 'grid',
        styles: { font: 'helvetica', fontSize: 8 },
        headStyles: { fillColor: [0, 0, 0], textColor: [255, 255, 255] },
        columnStyles: {
            4: { cellWidth: 60 }
        }
    });

    doc.save('feedbacks-report.pdf');
}

function showLoading() {
    document.getElementById('loadingSpinner').classList.remove('hidden');
    document.getElementById('feedbackContainer').classList.add('hidden');
}

function hideLoading() {
    document.getElementById('loadingSpinner').classList.add('hidden');
}

function showNoFeedback() {
    const container = document.getElementById('feedbackContainer');
    container.classList.remove('hidden');
    container.innerHTML = `
        <div class="no-feedback">
            <i class="fa-solid fa-comments"></i>
            <p>No feedbacks found</p>
        </div>
    `;
}