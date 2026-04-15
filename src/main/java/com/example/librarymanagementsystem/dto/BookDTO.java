package com.example.librarymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class BookDTO {
    
    private Long id;
    
    @NotBlank(message = "Book name is required")
    @Size(min = 2, max = 200, message = "Book name must be between 2 and 200 characters")
    private String name;
    
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    private String authorName;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    private String imageUrl;
    
    @NotNull(message = "Category is required")
    private CategoryDTO category;
    
    private Boolean isBorrowed;
    
    private Boolean isDeleted;
    
    private List<FeedbackDTO> feedbacks;
    
    private Double averageRating;
    
    private Long feedbackCount;
    
    // Default constructor
    public BookDTO() {}
    
    // Constructor with required fields
    public BookDTO(String name, String authorName, CategoryDTO category) {
        this.name = name;
        this.authorName = authorName;
        this.category = category;
        this.isBorrowed = false;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public BookDTO(Long id, String name, String authorName, String description, String imageUrl, 
                   CategoryDTO category, Boolean isBorrowed, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isBorrowed = isBorrowed;
        this.isDeleted = isDeleted;
    }
    
    // Constructor with feedbacks
    public BookDTO(Long id, String name, String authorName, String description, String imageUrl, 
                   CategoryDTO category, Boolean isBorrowed, Boolean isDeleted, 
                   List<FeedbackDTO> feedbacks, Double averageRating, Long feedbackCount) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isBorrowed = isBorrowed;
        this.isDeleted = isDeleted;
        this.feedbacks = feedbacks;
        this.averageRating = averageRating;
        this.feedbackCount = feedbackCount;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAuthorName() {
        return authorName;
    }
    
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public CategoryDTO getCategory() {
        return category;
    }
    
    public void setCategory(CategoryDTO category) {
        this.category = category;
    }
    
    public Boolean getIsBorrowed() {
        return isBorrowed;
    }
    
    public void setIsBorrowed(Boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public List<FeedbackDTO> getFeedbacks() {
        return feedbacks;
    }
    
    public void setFeedbacks(List<FeedbackDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }
    
    public Double getAverageRating() {
        return averageRating;
    }
    
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
    
    public Long getFeedbackCount() {
        return feedbackCount;
    }
    
    public void setFeedbackCount(Long feedbackCount) {
        this.feedbackCount = feedbackCount;
    }
    
    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorName='" + authorName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + category +
                ", isBorrowed=" + isBorrowed +
                ", isDeleted=" + isDeleted +
                ", feedbacks=" + (feedbacks != null ? feedbacks.size() : 0) +
                ", averageRating=" + averageRating +
                ", feedbackCount=" + feedbackCount +
                '}';
    }
}
