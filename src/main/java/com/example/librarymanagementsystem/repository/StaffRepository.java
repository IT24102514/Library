package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
    
    // Find all non-deleted staff
    @Query("SELECT s FROM Staff s WHERE s.isDeleted = false")
    List<Staff> findAllActive();
    
    // Find staff by email (case-insensitive)
    @Query("SELECT s FROM Staff s WHERE LOWER(s.email) = LOWER(:email)")
    Optional<Staff> findByEmailIgnoreCase(@Param("email") String email);
    
    // Find active staff by email (case-insensitive)
    @Query("SELECT s FROM Staff s WHERE LOWER(s.email) = LOWER(:email) AND s.isDeleted = false")
    Optional<Staff> findActiveByEmailIgnoreCase(@Param("email") String email);
    
    // Check if email exists (excluding current staff for updates)
    @Query("SELECT COUNT(s) > 0 FROM Staff s WHERE LOWER(s.email) = LOWER(:email) AND s.id != :id")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    // Check if email exists (for new staff)
    @Query("SELECT COUNT(s) > 0 FROM Staff s WHERE LOWER(s.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
    
    // Find staff by name (case-insensitive, non-deleted)
    @Query("SELECT s FROM Staff s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.isDeleted = false")
    List<Staff> findByNameContainingIgnoreCaseAndIsDeletedFalse(@Param("name") String name);
    
    // Find staff by role (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.role = :role AND s.isDeleted = false")
    List<Staff> findByRoleAndIsDeletedFalse(@Param("role") String role);
    
    // Find staff by gender (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.gender = :gender AND s.isDeleted = false")
    List<Staff> findByGenderAndIsDeletedFalse(@Param("gender") String gender);
    
    // Find staff by role and gender (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.role = :role AND s.gender = :gender AND s.isDeleted = false")
    List<Staff> findByRoleAndGenderAndIsDeletedFalse(@Param("role") String role, @Param("gender") String gender);
    
    // Count staff by role (non-deleted)
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.role = :role AND s.isDeleted = false")
    long countByRoleAndIsDeletedFalse(@Param("role") String role);
    
    // Count staff by gender (non-deleted)
    @Query("SELECT COUNT(s) FROM Staff s WHERE s.gender = :gender AND s.isDeleted = false")
    long countByGenderAndIsDeletedFalse(@Param("gender") String gender);
    
    // Find staff by multiple criteria (non-deleted)
    @Query("SELECT s FROM Staff s WHERE " +
           "(:name IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:role IS NULL OR s.role = :role) AND " +
           "(:gender IS NULL OR s.gender = :gender) AND " +
           "s.isDeleted = false")
    List<Staff> findByMultipleCriteria(@Param("name") String name, 
                                       @Param("role") String role, 
                                       @Param("gender") String gender);
    
    // Find all admins (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.role = 'ADMIN' AND s.isDeleted = false")
    List<Staff> findAllAdmins();
    
    // Find all librarians (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.role = 'LIBRARIAN' AND s.isDeleted = false")
    List<Staff> findAllLibrarians();
    
    // Find all managers (non-deleted)
    @Query("SELECT s FROM Staff s WHERE s.role = 'MANAGER' AND s.isDeleted = false")
    List<Staff> findAllManagers();
}
