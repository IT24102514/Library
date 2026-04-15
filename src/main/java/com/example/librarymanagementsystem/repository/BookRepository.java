package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    // Find all non-deleted books
    @Query("SELECT b FROM Book b WHERE b.isDeleted = false")
    List<Book> findAllActive();
    
    // Find books by category (non-deleted)
    @Query("SELECT b FROM Book b WHERE b.category = :category AND b.isDeleted = false")
    List<Book> findByCategoryAndIsDeletedFalse(@Param("category") Category category);
    
    // Find books by category ID (non-deleted)
    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.isDeleted = false")
    List<Book> findByCategoryIdAndIsDeletedFalse(@Param("categoryId") Long categoryId);
    
    // Find available books (not borrowed and not deleted)
    @Query("SELECT b FROM Book b WHERE b.isBorrowed = false AND b.isDeleted = false")
    List<Book> findAvailableBooks();
    
    // Find borrowed books (borrowed and not deleted)
    @Query("SELECT b FROM Book b WHERE b.isBorrowed = true AND b.isDeleted = false")
    List<Book> findBorrowedBooks();
    
    // Find books by name (case-insensitive, non-deleted)
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%')) AND b.isDeleted = false")
    List<Book> findByNameContainingIgnoreCaseAndIsDeletedFalse(@Param("name") String name);
    
    // Find books by author name (case-insensitive, non-deleted)
    @Query("SELECT b FROM Book b WHERE LOWER(b.authorName) LIKE LOWER(CONCAT('%', :authorName, '%')) AND b.isDeleted = false")
    List<Book> findByAuthorNameContainingIgnoreCaseAndIsDeletedFalse(@Param("authorName") String authorName);
    
    // Find book by name and author (case-insensitive, non-deleted)
    @Query("SELECT b FROM Book b WHERE LOWER(b.name) = LOWER(:name) AND LOWER(b.authorName) = LOWER(:authorName) AND b.isDeleted = false")
    Optional<Book> findByNameAndAuthorNameIgnoreCaseAndIsDeletedFalse(@Param("name") String name, @Param("authorName") String authorName);
    
    // Check if book exists by name and author (excluding current book for updates)
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE LOWER(b.name) = LOWER(:name) AND LOWER(b.authorName) = LOWER(:authorName) AND b.id != :id AND b.isDeleted = false")
    boolean existsByNameAndAuthorNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("authorName") String authorName, @Param("id") Long id);
    
    // Check if book exists by name and author (for new books)
    @Query("SELECT COUNT(b) > 0 FROM Book b WHERE LOWER(b.name) = LOWER(:name) AND LOWER(b.authorName) = LOWER(:authorName) AND b.isDeleted = false")
    boolean existsByNameAndAuthorNameIgnoreCase(@Param("name") String name, @Param("authorName") String authorName);
    
    // Find books by category and availability status
    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.isBorrowed = :isBorrowed AND b.isDeleted = false")
    List<Book> findByCategoryIdAndIsBorrowedAndIsDeletedFalse(@Param("categoryId") Long categoryId, @Param("isBorrowed") Boolean isBorrowed);
    
    // Count books by category (non-deleted)
    @Query("SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId AND b.isDeleted = false")
    long countByCategoryIdAndIsDeletedFalse(@Param("categoryId") Long categoryId);
    
    // Count available books by category
    @Query("SELECT COUNT(b) FROM Book b WHERE b.category.id = :categoryId AND b.isBorrowed = false AND b.isDeleted = false")
    long countAvailableBooksByCategoryId(@Param("categoryId") Long categoryId);
}
