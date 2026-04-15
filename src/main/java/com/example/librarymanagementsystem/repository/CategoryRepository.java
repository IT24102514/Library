package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    // Find all non-deleted categories
    @Query("SELECT c FROM Category c WHERE c.isDeleted = false")
    List<Category> findAllActive();
    
    // Find category by name (case-insensitive)
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<Category> findByNameIgnoreCase(@Param("name") String name);
    
    // Find active category by name (case-insensitive)
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.isDeleted = false")
    Optional<Category> findActiveByNameIgnoreCase(@Param("name") String name);
    
    // Check if category name exists (excluding current category for updates)
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name) AND c.id != :id")
    boolean existsByNameIgnoreCaseAndIdNot(@Param("name") String name, @Param("id") Long id);
    
    // Check if category name exists (for new categories)
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE LOWER(c.name) = LOWER(:name)")
    boolean existsByNameIgnoreCase(@Param("name") String name);
    
    // Find categories by name containing (case-insensitive)
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) AND c.isDeleted = false")
    List<Category> findByNameContainingIgnoreCase(@Param("name") String name);
}
