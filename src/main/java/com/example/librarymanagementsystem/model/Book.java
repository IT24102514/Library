package com.example.librarymanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Book name is required")
    @Size(min = 2, max = 200, message = "Book name must be between 2 and 200 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Author name must be between 2 and 100 characters")
    @Column(name = "author_name", nullable = false)
    private String authorName;
    
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;
    
    @Size(max = 500, message = "Image URL cannot exceed 500 characters")
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "total_copies", nullable = false)
    private Integer totalCopies = 1;
    
    @Column(name = "borrowed_copies", nullable = false)
    private Integer borrowedCopies = 0;
    
    @Column(name = "is_borrowed", nullable = false)
    private Boolean isBorrowed = false;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    // Default constructor
    public Book() {}
    
    // Constructor with required fields
    public Book(String name, String authorName, Category category) {
        this.name = name;
        this.authorName = authorName;
        this.category = category;
        this.isBorrowed = false;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public Book(String name, String authorName, String description, String imageUrl, Category category) {
        this.name = name;
        this.authorName = authorName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
        this.isBorrowed = false;
        this.isDeleted = false;
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
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
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
    
    public Integer getTotalCopies() {
        return totalCopies;
    }
    
    public void setTotalCopies(Integer totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public Integer getBorrowedCopies() {
        return borrowedCopies;
    }
    
    public void setBorrowedCopies(Integer borrowedCopies) {
        this.borrowedCopies = borrowedCopies;
    }
    
    public Integer getAvailableCopies() {
        return totalCopies - borrowedCopies;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorName='" + authorName + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", category=" + (category != null ? category.getName() : "null") +
                ", isBorrowed=" + isBorrowed +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
