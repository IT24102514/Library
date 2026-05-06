package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.FeedbackDTO;
import com.example.librarymanagementsystem.dto.MemberFeedbackDTO;
import com.example.librarymanagementsystem.model.Feedback;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Member;
import com.example.librarymanagementsystem.repository.FeedbackRepository;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * Convert Feedback entity to FeedbackDTO
     */
    private FeedbackDTO convertToDTO(Feedback feedback) {
        if (feedback == null) {
            return null;
        }
        
        MemberFeedbackDTO memberDTO = null;
        if (feedback.getMember() != null) {
            memberDTO = new MemberFeedbackDTO(
                feedback.getMember().getId(),
                feedback.getMember().getName(),
                feedback.getMember().getEmail(),
                feedback.getMember().getAge(),
                feedback.getMember().getGender(),
                feedback.getMember().getCity()
            );
        }
        
        return new FeedbackDTO(
            feedback.getId(),
            feedback.getContent(),
            feedback.getRating(),
            feedback.getBook() != null ? feedback.getBook().getId() : null,
            feedback.getMember() != null ? feedback.getMember().getId() : null,
            memberDTO,
            feedback.getCreatedAt(),
            feedback.getUpdatedAt(),
            feedback.getIsDeleted()
        );
    }
    
    /**
     * Convert FeedbackDTO to Feedback entity
     */
    private Feedback convertToEntity(FeedbackDTO feedbackDTO) {
        if (feedbackDTO == null) {
            return null;
        }
        
        Feedback feedback = new Feedback();
        feedback.setId(feedbackDTO.getId());
        feedback.setContent(feedbackDTO.getContent());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setIsDeleted(feedbackDTO.getIsDeleted() != null ? feedbackDTO.getIsDeleted() : false);
        
        // Set book if provided
        if (feedbackDTO.getBookId() != null) {
            Optional<Book> book = bookRepository.findById(feedbackDTO.getBookId());
            if (book.isPresent()) {
                feedback.setBook(book.get());
            }
        }
        
        // Set member if provided
        if (feedbackDTO.getMemberId() != null) {
            Optional<Member> member = memberRepository.findById(feedbackDTO.getMemberId());
            if (member.isPresent()) {
                feedback.setMember(member.get());
            }
        }
        
        return feedback;
    }
    
    /**
     * Get all active feedbacks
     */
    public List<FeedbackDTO> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAllActive();
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedbacks: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all feedbacks including deleted ones
     */
    public List<FeedbackDTO> getAllFeedbacksIncludingDeleted() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all feedbacks: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedback by ID
     */
    public Optional<FeedbackDTO> getFeedbackById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Feedback ID cannot be null");
            }
            Optional<Feedback> feedback = feedbackRepository.findById(id);
            if (feedback.isPresent() && feedback.get().getIsDeleted()) {
                return Optional.empty();
            }
            return feedback.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedback with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedbacks by book ID
     */
    public List<FeedbackDTO> getFeedbacksByBookId(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            // Verify book exists
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
            
            List<Feedback> feedbacks = feedbackRepository.findByBookIdAndIsDeletedFalseOrderByCreatedAtDesc(bookId);
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedbacks for book " + bookId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedbacks by member ID
     */
    public List<FeedbackDTO> getFeedbacksByMemberId(Long memberId) {
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            // Verify member exists
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("Member with ID " + memberId + " not found");
            }
            
            List<Feedback> feedbacks = feedbackRepository.findByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(memberId);
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedbacks for member " + memberId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new feedback
     */
    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        try {
            if (feedbackDTO == null) {
                throw new IllegalArgumentException("Feedback cannot be null");
            }
            if (feedbackDTO.getContent() == null || feedbackDTO.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Feedback content is required");
            }
            if (feedbackDTO.getRating() == null) {
                throw new IllegalArgumentException("Rating is required");
            }
            if (feedbackDTO.getBookId() == null) {
                throw new IllegalArgumentException("Book ID is required");
            }
            if (feedbackDTO.getMemberId() == null) {
                throw new IllegalArgumentException("Member ID is required");
            }
            
            // Check if member has already given feedback for this book
            if (feedbackRepository.existsByBookIdAndMemberIdAndIsDeletedFalse(feedbackDTO.getBookId(), feedbackDTO.getMemberId())) {
                throw new IllegalArgumentException("Member has already given feedback for this book");
            }
            
            // Verify book exists
            Optional<Book> book = bookRepository.findById(feedbackDTO.getBookId());
            if (book.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + feedbackDTO.getBookId() + " not found");
            }
            
            // Verify member exists
            Optional<Member> member = memberRepository.findById(feedbackDTO.getMemberId());
            if (member.isEmpty()) {
                throw new IllegalArgumentException("Member with ID " + feedbackDTO.getMemberId() + " not found");
            }
            
            Feedback feedback = convertToEntity(feedbackDTO);
            feedback.setContent(feedback.getContent().trim());
            feedback.setIsDeleted(false);
            
            Feedback savedFeedback = feedbackRepository.save(feedback);
            return convertToDTO(savedFeedback);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating feedback: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing feedback
     */
    public FeedbackDTO updateFeedback(Long id, FeedbackDTO feedbackDTO) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Feedback ID cannot be null");
            }
            if (feedbackDTO == null) {
                throw new IllegalArgumentException("Feedback details cannot be null");
            }
            if (feedbackDTO.getContent() == null || feedbackDTO.getContent().trim().isEmpty()) {
                throw new IllegalArgumentException("Feedback content is required");
            }
            if (feedbackDTO.getRating() == null) {
                throw new IllegalArgumentException("Rating is required");
            }
            
            Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
            if (optionalFeedback.isEmpty()) {
                throw new IllegalArgumentException("Feedback with ID " + id + " not found");
            }
            
            Feedback feedback = optionalFeedback.get();
            
            // Update fields
            feedback.setContent(feedbackDTO.getContent().trim());
            feedback.setRating(feedbackDTO.getRating());
            
            Feedback savedFeedback = feedbackRepository.save(feedback);
            return convertToDTO(savedFeedback);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating feedback with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a feedback
     */
    public boolean deleteFeedback(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Feedback ID cannot be null");
            }
            
            Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
            if (optionalFeedback.isEmpty()) {
                throw new IllegalArgumentException("Feedback with ID " + id + " not found");
            }
            
            Feedback feedback = optionalFeedback.get();
            if (feedback.getIsDeleted()) {
                throw new IllegalArgumentException("Feedback with ID " + id + " is already deleted");
            }
            
            feedback.setIsDeleted(true);
            feedbackRepository.save(feedback);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting feedback with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a feedback (permanently remove from database)
     */
    public boolean hardDeleteFeedback(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Feedback ID cannot be null");
            }
            
            if (!feedbackRepository.existsById(id)) {
                throw new IllegalArgumentException("Feedback with ID " + id + " not found");
            }
            
            feedbackRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting feedback with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted feedback
     */
    public boolean restoreFeedback(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Feedback ID cannot be null");
            }
            
            Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
            if (optionalFeedback.isEmpty()) {
                throw new IllegalArgumentException("Feedback with ID " + id + " not found");
            }
            
            Feedback feedback = optionalFeedback.get();
            if (!feedback.getIsDeleted()) {
                throw new IllegalArgumentException("Feedback with ID " + id + " is not deleted");
            }
            
            feedback.setIsDeleted(false);
            feedbackRepository.save(feedback);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring feedback with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedbacks by rating
     */
    public List<FeedbackDTO> getFeedbacksByRating(Integer rating) {
        try {
            if (rating == null) {
                throw new IllegalArgumentException("Rating cannot be null");
            }
            List<Feedback> feedbacks = feedbackRepository.findByRatingAndIsDeletedFalseOrderByCreatedAtDesc(rating);
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedbacks by rating: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedbacks by rating range
     */
    public List<FeedbackDTO> getFeedbacksByRatingRange(Integer minRating, Integer maxRating) {
        try {
            if (minRating == null || maxRating == null) {
                throw new IllegalArgumentException("Both minRating and maxRating are required");
            }
            if (minRating > maxRating) {
                throw new IllegalArgumentException("minRating cannot be greater than maxRating");
            }
            List<Feedback> feedbacks = feedbackRepository.findByRatingBetweenAndIsDeletedFalseOrderByCreatedAtDesc(minRating, maxRating);
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving feedbacks by rating range: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get recent feedbacks
     */
    public List<FeedbackDTO> getRecentFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findRecentFeedbacksOrderByCreatedAtDesc();
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recent feedbacks: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search feedbacks by multiple criteria
     */
    public List<FeedbackDTO> searchFeedbacksByMultipleCriteria(Long bookId, Long memberId, Integer minRating, Integer maxRating, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            List<Feedback> feedbacks = feedbackRepository.findByMultipleCriteria(bookId, memberId, minRating, maxRating, startDate, endDate);
            return feedbacks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching feedbacks by multiple criteria: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get average rating for a book
     */
    public Double getAverageRatingForBook(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
            
            Double averageRating = feedbackRepository.getAverageRatingByBookIdAndIsDeletedFalse(bookId);
            return averageRating != null ? averageRating : 0.0;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting average rating for book " + bookId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedback count for a book
     */
    public long getFeedbackCountForBook(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
            
            return feedbackRepository.countByBookIdAndIsDeletedFalse(bookId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting feedback count for book " + bookId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get feedback count for a member
     */
    public long getFeedbackCountForMember(Long memberId) {
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("Member with ID " + memberId + " not found");
            }
            
            return feedbackRepository.countByMemberIdAndIsDeletedFalse(memberId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting feedback count for member " + memberId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if member has given feedback for a book
     */
    public boolean hasMemberGivenFeedbackForBook(Long bookId, Long memberId) {
        try {
            if (bookId == null || memberId == null) {
                return false;
            }
            return feedbackRepository.existsByBookIdAndMemberIdAndIsDeletedFalse(bookId, memberId);
        } catch (Exception e) {
            throw new RuntimeException("Error checking if member has given feedback for book: " + e.getMessage(), e);
        }
    }
}
