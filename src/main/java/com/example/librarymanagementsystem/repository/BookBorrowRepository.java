package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.BookBorrow;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookBorrowRepository extends JpaRepository<BookBorrow, Long> {
    
    // Find all non-deleted borrows
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.isDeleted = false")
    List<BookBorrow> findAllActive();
    
    // Find borrows by book (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.book = :book AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByBookAndIsDeletedFalseOrderByBorrowedDateDesc(@Param("book") Book book);
    
    // Find borrows by book ID (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.book.id = :bookId AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByBookIdAndIsDeletedFalseOrderByBorrowedDateDesc(@Param("bookId") Long bookId);
    
    // Find borrows by member (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.member = :member AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByMemberAndIsDeletedFalseOrderByBorrowedDateDesc(@Param("member") Member member);
    
    // Find borrows by member ID (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.member.id = :memberId AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByMemberIdAndIsDeletedFalseOrderByBorrowedDateDesc(@Param("memberId") Long memberId);
    
    // Find active borrows (not returned and not deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.isReturned = false AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findActiveBorrowsOrderByBorrowedDateDesc();
    
    // Find returned borrows (returned and not deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.isReturned = true AND bb.isDeleted = false ORDER BY bb.returnedDate DESC")
    List<BookBorrow> findReturnedBorrowsOrderByReturnedDateDesc();
    
    // Find overdue borrows (not returned, past end date, not deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.isReturned = false AND bb.endDate < :currentDate AND bb.isDeleted = false ORDER BY bb.endDate ASC")
    List<BookBorrow> findOverdueBorrowsOrderByEndDateAsc(@Param("currentDate") LocalDate currentDate);
    
    // Find borrows by date range (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.borrowedDate BETWEEN :startDate AND :endDate AND bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByBorrowedDateBetweenAndIsDeletedFalseOrderByBorrowedDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find borrows by return date range (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.returnedDate BETWEEN :startDate AND :endDate AND bb.isDeleted = false ORDER BY bb.returnedDate DESC")
    List<BookBorrow> findByReturnedDateBetweenAndIsDeletedFalseOrderByReturnedDateDesc(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    // Find borrows due on specific date (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.endDate = :dueDate AND bb.isReturned = false AND bb.isDeleted = false ORDER BY bb.borrowedDate ASC")
    List<BookBorrow> findDueOnDateAndIsReturnedFalseAndIsDeletedFalseOrderByBorrowedDateAsc(@Param("dueDate") LocalDate dueDate);
    
    // Find borrows due within days (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.endDate BETWEEN :currentDate AND :futureDate AND bb.isReturned = false AND bb.isDeleted = false ORDER BY bb.endDate ASC")
    List<BookBorrow> findDueWithinDaysAndIsReturnedFalseAndIsDeletedFalseOrderByEndDateAsc(@Param("currentDate") LocalDate currentDate, @Param("futureDate") LocalDate futureDate);
    
    // Check if book is currently borrowed (not returned and not deleted)
    @Query("SELECT COUNT(bb) > 0 FROM BookBorrow bb WHERE bb.book.id = :bookId AND bb.isReturned = false AND bb.isDeleted = false")
    boolean isBookCurrentlyBorrowed(@Param("bookId") Long bookId);
    
    // Find current borrow for a book (not returned and not deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.book.id = :bookId AND bb.isReturned = false AND bb.isDeleted = false")
    Optional<BookBorrow> findCurrentBorrowForBook(@Param("bookId") Long bookId);
    
    // Count active borrows by member (not returned and not deleted)
    @Query("SELECT COUNT(bb) FROM BookBorrow bb WHERE bb.member.id = :memberId AND bb.isReturned = false AND bb.isDeleted = false")
    long countActiveBorrowsByMemberId(@Param("memberId") Long memberId);
    
    // Count total borrows by member (non-deleted)
    @Query("SELECT COUNT(bb) FROM BookBorrow bb WHERE bb.member.id = :memberId AND bb.isDeleted = false")
    long countTotalBorrowsByMemberId(@Param("memberId") Long memberId);
    
    // Count total borrows by book (non-deleted)
    @Query("SELECT COUNT(bb) FROM BookBorrow bb WHERE bb.book.id = :bookId AND bb.isDeleted = false")
    long countTotalBorrowsByBookId(@Param("bookId") Long bookId);
    
    // Find borrows by multiple criteria (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE " +
           "(:bookId IS NULL OR bb.book.id = :bookId) AND " +
           "(:memberId IS NULL OR bb.member.id = :memberId) AND " +
           "(:isReturned IS NULL OR bb.isReturned = :isReturned) AND " +
           "(:startBorrowDate IS NULL OR bb.borrowedDate >= :startBorrowDate) AND " +
           "(:endBorrowDate IS NULL OR bb.borrowedDate <= :endBorrowDate) AND " +
           "(:startReturnDate IS NULL OR bb.returnedDate >= :startReturnDate) AND " +
           "(:endReturnDate IS NULL OR bb.returnedDate <= :endReturnDate) AND " +
           "bb.isDeleted = false " +
           "ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findByMultipleCriteria(@Param("bookId") Long bookId,
                                           @Param("memberId") Long memberId,
                                           @Param("isReturned") Boolean isReturned,
                                           @Param("startBorrowDate") LocalDate startBorrowDate,
                                           @Param("endBorrowDate") LocalDate endBorrowDate,
                                           @Param("startReturnDate") LocalDate startReturnDate,
                                           @Param("endReturnDate") LocalDate endReturnDate);
    
    // Find recent borrows (non-deleted)
    @Query("SELECT bb FROM BookBorrow bb WHERE bb.isDeleted = false ORDER BY bb.borrowedDate DESC")
    List<BookBorrow> findRecentBorrowsOrderByBorrowedDateDesc();
}
