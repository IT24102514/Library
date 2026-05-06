package com.example.librarymanagementsystem.repository;

import com.example.librarymanagementsystem.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    // Find all non-deleted members
    @Query("SELECT m FROM Member m WHERE m.isDeleted = false")
    List<Member> findAllActive();
    
    // Find member by email (case-insensitive)
    @Query("SELECT m FROM Member m WHERE LOWER(m.email) = LOWER(:email)")
    Optional<Member> findByEmailIgnoreCase(@Param("email") String email);
    
    // Find active member by email (case-insensitive)
    @Query("SELECT m FROM Member m WHERE LOWER(m.email) = LOWER(:email) AND m.isDeleted = false")
    Optional<Member> findActiveByEmailIgnoreCase(@Param("email") String email);
    
    // Check if email exists (excluding current member for updates)
    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE LOWER(m.email) = LOWER(:email) AND m.id != :id")
    boolean existsByEmailIgnoreCaseAndIdNot(@Param("email") String email, @Param("id") Long id);
    
    // Check if email exists (for new members)
    @Query("SELECT COUNT(m) > 0 FROM Member m WHERE LOWER(m.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
    
    // Find members by name (case-insensitive, non-deleted)
    @Query("SELECT m FROM Member m WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')) AND m.isDeleted = false")
    List<Member> findByNameContainingIgnoreCaseAndIsDeletedFalse(@Param("name") String name);
    
    // Find members by city (case-insensitive, non-deleted)
    @Query("SELECT m FROM Member m WHERE LOWER(m.city) LIKE LOWER(CONCAT('%', :city, '%')) AND m.isDeleted = false")
    List<Member> findByCityContainingIgnoreCaseAndIsDeletedFalse(@Param("city") String city);
    
    // Find members by gender (non-deleted)
    @Query("SELECT m FROM Member m WHERE m.gender = :gender AND m.isDeleted = false")
    List<Member> findByGenderAndIsDeletedFalse(@Param("gender") String gender);
    
    // Find members by age range (non-deleted)
    @Query("SELECT m FROM Member m WHERE m.age BETWEEN :minAge AND :maxAge AND m.isDeleted = false")
    List<Member> findByAgeBetweenAndIsDeletedFalse(@Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);
    
    // Find members by age (non-deleted)
    @Query("SELECT m FROM Member m WHERE m.age = :age AND m.isDeleted = false")
    List<Member> findByAgeAndIsDeletedFalse(@Param("age") Integer age);
    
    // Count members by city (non-deleted)
    @Query("SELECT COUNT(m) FROM Member m WHERE LOWER(m.city) = LOWER(:city) AND m.isDeleted = false")
    long countByCityIgnoreCaseAndIsDeletedFalse(@Param("city") String city);
    
    // Count members by gender (non-deleted)
    @Query("SELECT COUNT(m) FROM Member m WHERE m.gender = :gender AND m.isDeleted = false")
    long countByGenderAndIsDeletedFalse(@Param("gender") String gender);
    
    // Find members by multiple criteria (non-deleted)
    @Query("SELECT m FROM Member m WHERE " +
           "(:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:city IS NULL OR LOWER(m.city) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:gender IS NULL OR m.gender = :gender) AND " +
           "(:minAge IS NULL OR m.age >= :minAge) AND " +
           "(:maxAge IS NULL OR m.age <= :maxAge) AND " +
           "m.isDeleted = false")
    List<Member> findByMultipleCriteria(@Param("name") String name, 
                                       @Param("city") String city, 
                                       @Param("gender") String gender,
                                       @Param("minAge") Integer minAge, 
                                       @Param("maxAge") Integer maxAge);
}
