package com.example.librarymanagementsystem.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public class FeedbackDTO {
    
    private Long id;
    
    @NotBlank(message = "Feedback content is required")
    @Size(min = 10, max = 1000, message = "Feedback content must be between 10 and 1000 characters")
    private String content;
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    private Integer rating;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    private MemberFeedbackDTO member;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Boolean isDeleted;
    
    // Default constructor
    public FeedbackDTO() {}
    
    // Constructor with required fields
    public FeedbackDTO(String content, Integer rating, Long bookId, Long memberId) {
        this.content = content;
        this.rating = rating;
        this.bookId = bookId;
        this.memberId = memberId;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public FeedbackDTO(Long id, String content, Integer rating, Long bookId, Long memberId, 
                       MemberFeedbackDTO member, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.bookId = bookId;
        this.memberId = memberId;
        this.member = member;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
    }
    
    // Constructor for updates (without member details)
    public FeedbackDTO(Long id, String content, Integer rating, Long bookId, Long memberId) {
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.bookId = bookId;
        this.memberId = memberId;
        this.isDeleted = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Long getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public MemberFeedbackDTO getMember() {
        return member;
    }
    
    public void setMember(MemberFeedbackDTO member) {
        this.member = member;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", rating=" + rating +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", member=" + member +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
