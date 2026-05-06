package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.Feedback;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // Find all non-deleted feedbacks
    @Query("SELECT f FROM Feedback f WHERE f.isDeleted = false")
    List<Feedback> findAllActive();
    
    // Find feedbacks by book (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.book = :book AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByBookAndIsDeletedFalseOrderByCreatedAtDesc(@Param("book") Book book);
    
    // Find feedbacks by book ID (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.book.id = :bookId AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByBookIdAndIsDeletedFalseOrderByCreatedAtDesc(@Param("bookId") Long bookId);
    
    // Find feedbacks by member (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.member = :member AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByMemberAndIsDeletedFalseOrderByCreatedAtDesc(@Param("member") Member member);
    
    // Find feedbacks by member ID (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.member.id = :memberId AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByMemberIdAndIsDeletedFalseOrderByCreatedAtDesc(@Param("memberId") Long memberId);
    
    // Find feedbacks by rating (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.rating = :rating AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByRatingAndIsDeletedFalseOrderByCreatedAtDesc(@Param("rating") Integer rating);
    
    // Find feedbacks by rating range (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.rating BETWEEN :minRating AND :maxRating AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByRatingBetweenAndIsDeletedFalseOrderByCreatedAtDesc(@Param("minRating") Integer minRating, @Param("maxRating") Integer maxRating);
    
    // Find feedbacks by book and member (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.book.id = :bookId AND f.member.id = :memberId AND f.isDeleted = false")
    Optional<Feedback> findByBookIdAndMemberIdAndIsDeletedFalse(@Param("bookId") Long bookId, @Param("memberId") Long memberId);
    
    // Check if member has already given feedback for a book
    @Query("SELECT COUNT(f) > 0 FROM Feedback f WHERE f.book.id = :bookId AND f.member.id = :memberId AND f.isDeleted = false")
    boolean existsByBookIdAndMemberIdAndIsDeletedFalse(@Param("bookId") Long bookId, @Param("memberId") Long memberId);
    
    // Find feedbacks created between dates (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.createdAt BETWEEN :startDate AND :endDate AND f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findByCreatedAtBetweenAndIsDeletedFalseOrderByCreatedAtDesc(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Count feedbacks by book (non-deleted)
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.book.id = :bookId AND f.isDeleted = false")
    long countByBookIdAndIsDeletedFalse(@Param("bookId") Long bookId);
    
    // Count feedbacks by member (non-deleted)
    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.member.id = :memberId AND f.isDeleted = false")
    long countByMemberIdAndIsDeletedFalse(@Param("memberId") Long memberId);
    
    // Calculate average rating by book (non-deleted)
    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.book.id = :bookId AND f.isDeleted = false")
    Double getAverageRatingByBookIdAndIsDeletedFalse(@Param("bookId") Long bookId);
    
    // Find feedbacks by multiple criteria (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE " +
           "(:bookId IS NULL OR f.book.id = :bookId) AND " +
           "(:memberId IS NULL OR f.member.id = :memberId) AND " +
           "(:minRating IS NULL OR f.rating >= :minRating) AND " +
           "(:maxRating IS NULL OR f.rating <= :maxRating) AND " +
           "(:startDate IS NULL OR f.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR f.createdAt <= :endDate) AND " +
           "f.isDeleted = false " +
           "ORDER BY f.createdAt DESC")
    List<Feedback> findByMultipleCriteria(@Param("bookId") Long bookId,
                                         @Param("memberId") Long memberId,
                                         @Param("minRating") Integer minRating,
                                         @Param("maxRating") Integer maxRating,
                                         @Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
    
    // Find recent feedbacks (non-deleted)
    @Query("SELECT f FROM Feedback f WHERE f.isDeleted = false ORDER BY f.createdAt DESC")
    List<Feedback> findRecentFeedbacksOrderByCreatedAtDesc();
}
