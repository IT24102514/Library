package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BookBorrowDTO;
import com.example.librarymanagementsystem.dto.BookDTO;
import com.example.librarymanagementsystem.dto.MemberDTO;
import com.example.librarymanagementsystem.model.BookBorrow;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Member;
import com.example.librarymanagementsystem.repository.BookBorrowRepository;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookBorrowService {
    
    @Autowired
    private BookBorrowRepository bookBorrowRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * Convert BookBorrow entity to BookBorrowDTO
     */
    private BookBorrowDTO convertToDTO(BookBorrow bookBorrow) {
        if (bookBorrow == null) {
            return null;
        }
        
        BookDTO bookDTO = null;
        if (bookBorrow.getBook() != null) {
            bookDTO = new BookDTO(
                bookBorrow.getBook().getId(),
                bookBorrow.getBook().getName(),
                bookBorrow.getBook().getAuthorName(),
                bookBorrow.getBook().getDescription(),
                bookBorrow.getBook().getImageUrl(),
                null, // CategoryDTO will be set separately if needed
                bookBorrow.getBook().getIsBorrowed(),
                bookBorrow.getBook().getIsDeleted()
            );
        }
        
        MemberDTO memberDTO = null;
        if (bookBorrow.getMember() != null) {
            memberDTO = new MemberDTO(
                bookBorrow.getMember().getId(),
                bookBorrow.getMember().getName(),
                bookBorrow.getMember().getEmail(),
                null, // Password not included
                bookBorrow.getMember().getAge(),
                bookBorrow.getMember().getGender(),
                bookBorrow.getMember().getCity(),
                bookBorrow.getMember().getIsDeleted()
            );
        }
        
        BookBorrowDTO dto = new BookBorrowDTO(
            bookBorrow.getId(),
            bookBorrow.getBook() != null ? bookBorrow.getBook().getId() : null,
            bookBorrow.getMember() != null ? bookBorrow.getMember().getId() : null,
            bookBorrow.getBorrowedDate(),
            bookBorrow.getEndDate(),
            bookBorrow.getAdditionalNotes(),
            bookBorrow.getReturnedDate(),
            bookBorrow.getIsReturned(),
            bookBorrow.getIsDeleted(),
            bookDTO,
            memberDTO
        );
        
        // Set computed fields
        dto.setIsOverdue(bookBorrow.isOverdue());
        dto.setDaysUntilDue(bookBorrow.getDaysUntilDue());
        dto.setDaysOverdue(bookBorrow.getDaysOverdue());
        
        return dto;
    }
    
    /**
     * Convert BookBorrowDTO to BookBorrow entity
     */
    private BookBorrow convertToEntity(BookBorrowDTO bookBorrowDTO) {
        if (bookBorrowDTO == null) {
            return null;
        }
        
        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setId(bookBorrowDTO.getId());
        bookBorrow.setBorrowedDate(bookBorrowDTO.getBorrowedDate());
        bookBorrow.setEndDate(bookBorrowDTO.getEndDate());
        bookBorrow.setAdditionalNotes(bookBorrowDTO.getAdditionalNotes());
        bookBorrow.setReturnedDate(bookBorrowDTO.getReturnedDate());
        bookBorrow.setIsReturned(bookBorrowDTO.getIsReturned() != null ? bookBorrowDTO.getIsReturned() : false);
        bookBorrow.setIsDeleted(bookBorrowDTO.getIsDeleted() != null ? bookBorrowDTO.getIsDeleted() : false);
        
        // Set book if provided
        if (bookBorrowDTO.getBookId() != null) {
            Optional<Book> book = bookRepository.findById(bookBorrowDTO.getBookId());
            if (book.isPresent()) {
                bookBorrow.setBook(book.get());
            }
        }
        
        // Set member if provided
        if (bookBorrowDTO.getMemberId() != null) {
            Optional<Member> member = memberRepository.findById(bookBorrowDTO.getMemberId());
            if (member.isPresent()) {
                bookBorrow.setMember(member.get());
            }
        }
        
        return bookBorrow;
    }
    
    /**
     * Get all active borrows
     */
    public List<BookBorrowDTO> getAllBorrows() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findAllActive();
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all borrows including deleted ones
     */
    public List<BookBorrowDTO> getAllBorrowsIncludingDeleted() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findAll();
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrow by ID
     */
    public Optional<BookBorrowDTO> getBorrowById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            Optional<BookBorrow> borrow = bookBorrowRepository.findById(id);
            if (borrow.isPresent() && borrow.get().getIsDeleted()) {
                return Optional.empty();
            }
            return borrow.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrows by book ID
     */
    public List<BookBorrowDTO> getBorrowsByBookId(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            // Verify book exists
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
            
            List<BookBorrow> borrows = bookBorrowRepository.findByBookIdAndIsDeletedFalseOrderByBorrowedDateDesc(bookId);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows for book " + bookId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrows by member ID
     */
    public List<BookBorrowDTO> getBorrowsByMemberId(Long memberId) {
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            // Verify member exists
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("Member with ID " + memberId + " not found");
            }
            
            List<BookBorrow> borrows = bookBorrowRepository.findByMemberIdAndIsDeletedFalseOrderByBorrowedDateDesc(memberId);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows for member " + memberId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get active borrows (not returned)
     */
    public List<BookBorrowDTO> getActiveBorrows() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findActiveBorrowsOrderByBorrowedDateDesc();
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get returned borrows
     */
    public List<BookBorrowDTO> getReturnedBorrows() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findReturnedBorrowsOrderByReturnedDateDesc();
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving returned borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get overdue borrows
     */
    public List<BookBorrowDTO> getOverdueBorrows() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findOverdueBorrowsOrderByEndDateAsc(LocalDate.now());
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving overdue borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new borrow
     */
    public BookBorrowDTO createBorrow(BookBorrowDTO bookBorrowDTO) {
        try {
            if (bookBorrowDTO == null) {
                throw new IllegalArgumentException("Borrow cannot be null");
            }
            if (bookBorrowDTO.getBookId() == null) {
                throw new IllegalArgumentException("Book ID is required");
            }
            if (bookBorrowDTO.getMemberId() == null) {
                throw new IllegalArgumentException("Member ID is required");
            }
            if (bookBorrowDTO.getBorrowedDate() == null) {
                throw new IllegalArgumentException("Borrowed date is required");
            }
            if (bookBorrowDTO.getEndDate() == null) {
                throw new IllegalArgumentException("End date is required");
            }
            
            // Verify book exists
            Optional<Book> book = bookRepository.findById(bookBorrowDTO.getBookId());
            if (book.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + bookBorrowDTO.getBookId() + " not found");
            }
            
            // Check if book has available copies
            Book bookEntity = book.get();
            int availableCopies = bookEntity.getTotalCopies() - bookEntity.getBorrowedCopies();
            if (availableCopies <= 0) {
                throw new IllegalArgumentException("No copies available for this book");
            }
            
            // Verify member exists
            Optional<Member> member = memberRepository.findById(bookBorrowDTO.getMemberId());
            if (member.isEmpty()) {
                throw new IllegalArgumentException("Member with ID" + bookBorrowDTO.getMemberId() + " not found");
            }
            
            BookBorrow borrow = convertToEntity(bookBorrowDTO);
            borrow.setIsReturned(false);
            borrow.setIsDeleted(false);
            
            // Update book borrowed copies count
            bookEntity.setBorrowedCopies(bookEntity.getBorrowedCopies() + 1);
            if (bookEntity.getBorrowedCopies() >= bookEntity.getTotalCopies()) {
                bookEntity.setIsBorrowed(true); // All copies borrowed
            }
            bookRepository.save(bookEntity);
            
            BookBorrow savedBorrow = bookBorrowRepository.save(borrow);
            return convertToDTO(savedBorrow);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating borrow: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing borrow
     */
    public BookBorrowDTO updateBorrow(Long id, BookBorrowDTO bookBorrowDTO) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            if (bookBorrowDTO == null) {
                throw new IllegalArgumentException("Borrow details cannot be null");
            }
            if (bookBorrowDTO.getBorrowedDate() == null) {
                throw new IllegalArgumentException("Borrowed date is required");
            }
            if (bookBorrowDTO.getEndDate() == null) {
                throw new IllegalArgumentException("End date is required");
            }
            
            Optional<BookBorrow> optionalBorrow = bookBorrowRepository.findById(id);
            if (optionalBorrow.isEmpty()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " not found");
            }
            
            BookBorrow borrow = optionalBorrow.get();
            
            // Update fields
            borrow.setBorrowedDate(bookBorrowDTO.getBorrowedDate());
            borrow.setEndDate(bookBorrowDTO.getEndDate());
            borrow.setAdditionalNotes(bookBorrowDTO.getAdditionalNotes() != null ? 
                bookBorrowDTO.getAdditionalNotes().trim() : null);
            
            BookBorrow savedBorrow = bookBorrowRepository.save(borrow);
            return convertToDTO(savedBorrow);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Return a borrowed book
     */
    public BookBorrowDTO returnBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            
            Optional<BookBorrow> optionalBorrow = bookBorrowRepository.findById(id);
            if (optionalBorrow.isEmpty()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " not found");
            }
            
            BookBorrow borrow = optionalBorrow.get();
            if (borrow.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot return deleted borrow");
            }
            if (borrow.getIsReturned()) {
                throw new IllegalArgumentException("Book is already returned");
            }
            
            borrow.setIsReturned(true);
            borrow.setReturnedDate(LocalDate.now());
            
            // Update book borrowed copies count
            Book book = borrow.getBook();
            if (book.getBorrowedCopies() > 0) {
                book.setBorrowedCopies(book.getBorrowedCopies() - 1);
            }
            if (book.getBorrowedCopies() < book.getTotalCopies()) {
                book.setIsBorrowed(false); // At least one copy available
            }
            bookRepository.save(book);
            
            BookBorrow savedBorrow = bookBorrowRepository.save(borrow);
            return convertToDTO(savedBorrow);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error returning book for borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a borrow
     */
    public boolean deleteBorrow(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            
            Optional<BookBorrow> optionalBorrow = bookBorrowRepository.findById(id);
            if (optionalBorrow.isEmpty()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " not found");
            }
            
            BookBorrow borrow = optionalBorrow.get();
            if (borrow.getIsDeleted()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " is already deleted");
            }
            
            borrow.setIsDeleted(true);
            bookBorrowRepository.save(borrow);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a borrow (permanently remove from database)
     */
    public boolean hardDeleteBorrow(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            
            if (!bookBorrowRepository.existsById(id)) {
                throw new IllegalArgumentException("Borrow with ID " + id + " not found");
            }
            
            bookBorrowRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted borrow
     */
    public boolean restoreBorrow(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Borrow ID cannot be null");
            }
            
            Optional<BookBorrow> optionalBorrow = bookBorrowRepository.findById(id);
            if (optionalBorrow.isEmpty()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " not found");
            }
            
            BookBorrow borrow = optionalBorrow.get();
            if (!borrow.getIsDeleted()) {
                throw new IllegalArgumentException("Borrow with ID " + id + " is not deleted");
            }
            
            borrow.setIsDeleted(false);
            bookBorrowRepository.save(borrow);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring borrow with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrows by date range
     */
    public List<BookBorrowDTO> getBorrowsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate == null || endDate == null) {
                throw new IllegalArgumentException("Both start date and end date are required");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            List<BookBorrow> borrows = bookBorrowRepository.findByBorrowedDateBetweenAndIsDeletedFalseOrderByBorrowedDateDesc(startDate, endDate);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows by date range: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrows due on specific date
     */
    public List<BookBorrowDTO> getBorrowsDueOnDate(LocalDate dueDate) {
        try {
            if (dueDate == null) {
                throw new IllegalArgumentException("Due date cannot be null");
            }
            List<BookBorrow> borrows = bookBorrowRepository.findDueOnDateAndIsReturnedFalseAndIsDeletedFalseOrderByBorrowedDateAsc(dueDate);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows due on date: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrows due within days
     */
    public List<BookBorrowDTO> getBorrowsDueWithinDays(int days) {
        try {
            if (days < 0) {
                throw new IllegalArgumentException("Days cannot be negative");
            }
            LocalDate currentDate = LocalDate.now();
            LocalDate futureDate = currentDate.plusDays(days);
            List<BookBorrow> borrows = bookBorrowRepository.findDueWithinDaysAndIsReturnedFalseAndIsDeletedFalseOrderByEndDateAsc(currentDate, futureDate);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrows due within days: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search borrows by multiple criteria
     */
    public List<BookBorrowDTO> searchBorrowsByMultipleCriteria(Long bookId, Long memberId, Boolean isReturned, 
                                                              LocalDate startBorrowDate, LocalDate endBorrowDate,
                                                              LocalDate startReturnDate, LocalDate endReturnDate) {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findByMultipleCriteria(bookId, memberId, isReturned, 
                    startBorrowDate, endBorrowDate, startReturnDate, endReturnDate);
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching borrows by multiple criteria: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get recent borrows
     */
    public List<BookBorrowDTO> getRecentBorrows() {
        try {
            List<BookBorrow> borrows = bookBorrowRepository.findRecentBorrowsOrderByBorrowedDateDesc();
            return borrows.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving recent borrows: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if book is currently borrowed
     */
    public boolean isBookCurrentlyBorrowed(Long bookId) {
        try {
            if (bookId == null) {
                return false;
            }
            return bookBorrowRepository.isBookCurrentlyBorrowed(bookId);
        } catch (Exception e) {
            throw new RuntimeException("Error checking if book is currently borrowed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get current borrow for a book
     */
    public Optional<BookBorrowDTO> getCurrentBorrowForBook(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            Optional<BookBorrow> borrow = bookBorrowRepository.findCurrentBorrowForBook(bookId);
            return borrow.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting current borrow for book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get active borrow count for member
     */
    public long getActiveBorrowCountForMember(Long memberId) {
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("Member with ID " + memberId + " not found");
            }
            
            return bookBorrowRepository.countActiveBorrowsByMemberId(memberId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting active borrow count for member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get total borrow count for member
     */
    public long getTotalBorrowCountForMember(Long memberId) {
        try {
            if (memberId == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            if (!memberRepository.existsById(memberId)) {
                throw new IllegalArgumentException("Member with ID " + memberId + " not found");
            }
            
            return bookBorrowRepository.countTotalBorrowsByMemberId(memberId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting total borrow count for member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get total borrow count for book
     */
    public long getTotalBorrowCountForBook(Long bookId) {
        try {
            if (bookId == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            if (!bookRepository.existsById(bookId)) {
                throw new IllegalArgumentException("Book with ID " + bookId + " not found");
            }
            
            return bookBorrowRepository.countTotalBorrowsByBookId(bookId);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting total borrow count for book: " + e.getMessage(), e);
        }
    }
}
