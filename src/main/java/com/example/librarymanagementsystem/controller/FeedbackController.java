package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.FeedbackDTO;
import com.example.librarymanagementsystem.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * Get all active feedbacks
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllFeedbacks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacks();
            response.put("success", true);
            response.put("message", "Feedbacks retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedbacks: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all feedbacks including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllFeedbacksIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getAllFeedbacksIncludingDeleted();
            response.put("success", true);
            response.put("message", "All feedbacks retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all feedbacks: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedback by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getFeedbackById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<FeedbackDTO> feedback = feedbackService.getFeedbackById(id);
            if (feedback.isPresent()) {
                response.put("success", true);
                response.put("message", "Feedback retrieved successfully");
                response.put("data", feedback.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Feedback with ID " + id + " not found");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedbacks by book ID
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Map<String, Object>> getFeedbacksByBookId(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByBookId(bookId);
            response.put("success", true);
            response.put("message", "Feedbacks for book retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedbacks for book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedbacks by member ID
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Map<String, Object>> getFeedbacksByMemberId(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByMemberId(memberId);
            response.put("success", true);
            response.put("message", "Feedbacks for member retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedbacks for member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new feedback
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            FeedbackDTO createdFeedback = feedbackService.createFeedback(feedbackDTO);
            response.put("success", true);
            response.put("message", "Feedback created successfully");
            response.put("data", createdFeedback);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing feedback
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateFeedback(@PathVariable Long id, @Valid @RequestBody FeedbackDTO feedbackDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            FeedbackDTO updatedFeedback = feedbackService.updateFeedback(id, feedbackDTO);
            response.put("success", true);
            response.put("message", "Feedback updated successfully");
            response.put("data", updatedFeedback);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a feedback
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteFeedback(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = feedbackService.deleteFeedback(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Feedback deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete feedback");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a feedback (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteFeedback(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = feedbackService.hardDeleteFeedback(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Feedback permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete feedback");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error permanently deleting feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted feedback
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreFeedback(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = feedbackService.restoreFeedback(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Feedback restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore feedback");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error restoring feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedbacks by rating
     */
    @GetMapping("/rating/{rating}")
    public ResponseEntity<Map<String, Object>> getFeedbacksByRating(@PathVariable Integer rating) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByRating(rating);
            response.put("success", true);
            response.put("message", "Feedbacks by rating retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedbacks by rating: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedbacks by rating range
     */
    @GetMapping("/rating-range")
    public ResponseEntity<Map<String, Object>> getFeedbacksByRatingRange(@RequestParam Integer minRating, @RequestParam Integer maxRating) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByRatingRange(minRating, maxRating);
            response.put("success", true);
            response.put("message", "Feedbacks by rating range retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving feedbacks by rating range: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get recent feedbacks
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentFeedbacks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getRecentFeedbacks();
            response.put("success", true);
            response.put("message", "Recent feedbacks retrieved successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving recent feedbacks: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search feedbacks by multiple criteria
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchFeedbacksByMultipleCriteria(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<FeedbackDTO> feedbacks = feedbackService.searchFeedbacksByMultipleCriteria(bookId, memberId, minRating, maxRating, startDate, endDate);
            response.put("success", true);
            response.put("message", "Search by multiple criteria completed successfully");
            response.put("data", feedbacks);
            response.put("count", feedbacks.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching feedbacks by multiple criteria: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get average rating for a book
     */
    @GetMapping("/book/{bookId}/average-rating")
    public ResponseEntity<Map<String, Object>> getAverageRatingForBook(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Double averageRating = feedbackService.getAverageRatingForBook(bookId);
            response.put("success", true);
            response.put("message", "Average rating retrieved successfully");
            response.put("data", averageRating);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting average rating: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedback count for a book
     */
    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<Map<String, Object>> getFeedbackCountForBook(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = feedbackService.getFeedbackCountForBook(bookId);
            response.put("success", true);
            response.put("message", "Feedback count retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting feedback count: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get feedback count for a member
     */
    @GetMapping("/member/{memberId}/count")
    public ResponseEntity<Map<String, Object>> getFeedbackCountForMember(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = feedbackService.getFeedbackCountForMember(memberId);
            response.put("success", true);
            response.put("message", "Feedback count retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting feedback count: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if member has given feedback for a book
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkMemberFeedbackForBook(@RequestParam Long bookId, @RequestParam Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean hasGivenFeedback = feedbackService.hasMemberGivenFeedbackForBook(bookId, memberId);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", hasGivenFeedback);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking member feedback: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
