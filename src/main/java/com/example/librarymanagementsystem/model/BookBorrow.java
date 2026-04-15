package com.example.librarymanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "book_borrows")
public class BookBorrow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    @NotNull(message = "Member is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @NotNull(message = "Borrowed date is required")
    @Column(name = "borrowed_date", nullable = false)
    private LocalDate borrowedDate;
    
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @Size(max = 500, message = "Additional notes cannot exceed 500 characters")
    @Column(name = "additional_notes", length = 500)
    private String additionalNotes;
    
    @Column(name = "returned_date")
    private LocalDate returnedDate;
    
    @Column(name = "is_returned", nullable = false)
    private Boolean isReturned = false;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    // Default constructor
    public BookBorrow() {}
    
    // Constructor with required fields
    public BookBorrow(Book book, Member member, LocalDate borrowedDate, LocalDate endDate) {
        this.book = book;
        this.member = member;
        this.borrowedDate = borrowedDate;
        this.endDate = endDate;
        this.isReturned = false;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public BookBorrow(Book book, Member member, LocalDate borrowedDate, LocalDate endDate, 
                     String additionalNotes, LocalDate returnedDate, Boolean isReturned, Boolean isDeleted) {
        this.book = book;
        this.member = member;
        this.borrowedDate = borrowedDate;
        this.endDate = endDate;
        this.additionalNotes = additionalNotes;
        this.returnedDate = returnedDate;
        this.isReturned = isReturned != null ? isReturned : false;
        this.isDeleted = isDeleted != null ? isDeleted : false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }
    
    public void setBorrowedDate(LocalDate borrowedDate) {
        this.borrowedDate = borrowedDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getAdditionalNotes() {
        return additionalNotes;
    }
    
    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
    
    public LocalDate getReturnedDate() {
        return returnedDate;
    }
    
    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }
    
    public Boolean getIsReturned() {
        return isReturned;
    }
    
    public void setIsReturned(Boolean isReturned) {
        this.isReturned = isReturned;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    // Helper method to check if borrow is overdue
    public boolean isOverdue() {
        return !isReturned && LocalDate.now().isAfter(endDate);
    }
    
    // Helper method to get days until due
    public long getDaysUntilDue() {
        if (isReturned) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), endDate);
    }
    
    // Helper method to get days overdue
    public long getDaysOverdue() {
        if (isReturned || !isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(endDate, LocalDate.now());
    }
    
    @Override
    public String toString() {
        return "BookBorrow{" +
                "id=" + id +
                ", book=" + (book != null ? book.getName() : "null") +
                ", member=" + (member != null ? member.getName() : "null") +
                ", borrowedDate=" + borrowedDate +
                ", endDate=" + endDate +
                ", additionalNotes='" + additionalNotes + '\'' +
                ", returnedDate=" + returnedDate +
                ", isReturned=" + isReturned +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
