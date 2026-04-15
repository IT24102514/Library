package com.example.librarymanagementsystem.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class BookBorrowDTO {
    
    private Long id;
    
    @NotNull(message = "Book ID is required")
    private Long bookId;
    
    @NotNull(message = "Member ID is required")
    private Long memberId;
    
    @NotNull(message = "Borrowed date is required")
    private LocalDate borrowedDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @Size(max = 500, message = "Additional notes cannot exceed 500 characters")
    private String additionalNotes;
    
    private LocalDate returnedDate;
    
    private Boolean isReturned;
    
    private Boolean isDeleted;
    
    // Include book and member details
    private BookDTO book;
    private MemberDTO member;
    
    // Additional computed fields
    private Boolean isOverdue;
    private Long daysUntilDue;
    private Long daysOverdue;
    
    // Default constructor
    public BookBorrowDTO() {}
    
    // Constructor with required fields
    public BookBorrowDTO(Long bookId, Long memberId, LocalDate borrowedDate, LocalDate endDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedDate = borrowedDate;
        this.endDate = endDate;
        this.isReturned = false;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public BookBorrowDTO(Long id, Long bookId, Long memberId, LocalDate borrowedDate, LocalDate endDate, 
                        String additionalNotes, LocalDate returnedDate, Boolean isReturned, Boolean isDeleted,
                        BookDTO book, MemberDTO member) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedDate = borrowedDate;
        this.endDate = endDate;
        this.additionalNotes = additionalNotes;
        this.returnedDate = returnedDate;
        this.isReturned = isReturned;
        this.isDeleted = isDeleted;
        this.book = book;
        this.member = member;
    }
    
    // Constructor for updates
    public BookBorrowDTO(Long id, Long bookId, Long memberId, LocalDate borrowedDate, LocalDate endDate, 
                        String additionalNotes) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowedDate = borrowedDate;
        this.endDate = endDate;
        this.additionalNotes = additionalNotes;
        this.isReturned = false;
        this.isDeleted = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public BookDTO getBook() {
        return book;
    }
    
    public void setBook(BookDTO book) {
        this.book = book;
    }
    
    public MemberDTO getMember() {
        return member;
    }
    
    public void setMember(MemberDTO member) {
        this.member = member;
    }
    
    public Boolean getIsOverdue() {
        return isOverdue;
    }
    
    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    
    public Long getDaysUntilDue() {
        return daysUntilDue;
    }
    
    public void setDaysUntilDue(Long daysUntilDue) {
        this.daysUntilDue = daysUntilDue;
    }
    
    public Long getDaysOverdue() {
        return daysOverdue;
    }
    
    public void setDaysOverdue(Long daysOverdue) {
        this.daysOverdue = daysOverdue;
    }
    
    @Override
    public String toString() {
        return "BookBorrowDTO{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", memberId=" + memberId +
                ", borrowedDate=" + borrowedDate +
                ", endDate=" + endDate +
                ", additionalNotes='" + additionalNotes + '\'' +
                ", returnedDate=" + returnedDate +
                ", isReturned=" + isReturned +
                ", isDeleted=" + isDeleted +
                ", isOverdue=" + isOverdue +
                ", daysUntilDue=" + daysUntilDue +
                ", daysOverdue=" + daysOverdue +
                '}';
    }
}
