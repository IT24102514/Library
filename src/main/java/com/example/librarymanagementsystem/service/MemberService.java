package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.MemberDTO;
import com.example.librarymanagementsystem.model.Member;
import com.example.librarymanagementsystem.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    /**
     * Convert Member entity to MemberDTO
     */
    private MemberDTO convertToDTO(Member member) {
        if (member == null) {
            return null;
        }
        
        return new MemberDTO(
            member.getId(),
            member.getName(),
            member.getEmail(),
            null, // Don't expose password in DTO
            member.getAge(),
            member.getGender(),
            member.getCity(),
            member.getIsDeleted()
        );
    }
    
    /**
     * Convert MemberDTO to Member entity
     */
    private Member convertToEntity(MemberDTO memberDTO) {
        if (memberDTO == null) {
            return null;
        }
        
        Member member = new Member();
        member.setId(memberDTO.getId());
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAge(memberDTO.getAge());
        member.setGender(memberDTO.getGender());
        member.setCity(memberDTO.getCity());
        member.setIsDeleted(memberDTO.getIsDeleted() != null ? memberDTO.getIsDeleted() : false);
        
        // Handle password separately
        if (memberDTO.getPassword() != null && !memberDTO.getPassword().trim().isEmpty()) {
            member.setPasswordHashed(memberDTO.getPassword());
        }
        
        return member;
    }
    
    /**
     * Get all active members
     */
    public List<MemberDTO> getAllMembers() {
        try {
            List<Member> members = memberRepository.findAllActive();
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all members including deleted ones
     */
    public List<MemberDTO> getAllMembersIncludingDeleted() {
        try {
            List<Member> members = memberRepository.findAll();
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all members: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get member by ID
     */
    public Optional<MemberDTO> getMemberById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            Optional<Member> member = memberRepository.findById(id);
            if (member.isPresent() && member.get().getIsDeleted()) {
                return Optional.empty();
            }
            return member.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving member with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get member by email
     */
    public Optional<MemberDTO> getMemberByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            Optional<Member> member = memberRepository.findActiveByEmailIgnoreCase(email.trim());
            return member.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving member with email " + email + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new member
     */
    public MemberDTO createMember(MemberDTO memberDTO) {
        try {
            if (memberDTO == null) {
                throw new IllegalArgumentException("Member cannot be null");
            }
            if (memberDTO.getName() == null || memberDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Member name is required");
            }
            if (memberDTO.getEmail() == null || memberDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (memberDTO.getPassword() == null || memberDTO.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (memberDTO.getAge() == null) {
                throw new IllegalArgumentException("Age is required");
            }
            if (memberDTO.getGender() == null || memberDTO.getGender().trim().isEmpty()) {
                throw new IllegalArgumentException("Gender is required");
            }
            if (memberDTO.getCity() == null || memberDTO.getCity().trim().isEmpty()) {
                throw new IllegalArgumentException("City is required");
            }
            
            // Check if email already exists
            if (memberRepository.existsByEmailIgnoreCase(memberDTO.getEmail().trim())) {
                throw new IllegalArgumentException("Member with email '" + memberDTO.getEmail() + "' already exists");
            }
            
            Member member = convertToEntity(memberDTO);
            member.setName(member.getName().trim());
            member.setEmail(member.getEmail().trim().toLowerCase());
            member.setGender(member.getGender().trim());
            member.setCity(member.getCity().trim());
            member.setIsDeleted(false);
            
            Member savedMember = memberRepository.save(member);
            return convertToDTO(savedMember);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing member
     */
    public MemberDTO updateMember(Long id, MemberDTO memberDTO) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            if (memberDTO == null) {
                throw new IllegalArgumentException("Member details cannot be null");
            }
            if (memberDTO.getName() == null || memberDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Member name is required");
            }
            if (memberDTO.getEmail() == null || memberDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (memberDTO.getAge() == null) {
                throw new IllegalArgumentException("Age is required");
            }
            if (memberDTO.getGender() == null || memberDTO.getGender().trim().isEmpty()) {
                throw new IllegalArgumentException("Gender is required");
            }
            if (memberDTO.getCity() == null || memberDTO.getCity().trim().isEmpty()) {
                throw new IllegalArgumentException("City is required");
            }
            
            Optional<Member> optionalMember = memberRepository.findById(id);
            if (optionalMember.isEmpty()) {
                throw new IllegalArgumentException("Member with ID " + id + " not found");
            }
            
            Member member = optionalMember.get();
            
            // Check if the new email conflicts with existing members
            if (!member.getEmail().equalsIgnoreCase(memberDTO.getEmail().trim())) {
                if (memberRepository.existsByEmailIgnoreCaseAndIdNot(memberDTO.getEmail().trim(), id)) {
                    throw new IllegalArgumentException("Member with email '" + memberDTO.getEmail() + "' already exists");
                }
            }
            
            // Update fields
            member.setName(memberDTO.getName().trim());
            member.setEmail(memberDTO.getEmail().trim().toLowerCase());
            member.setAge(memberDTO.getAge());
            member.setGender(memberDTO.getGender().trim());
            member.setCity(memberDTO.getCity().trim());
            
            // Update password only if provided
            if (memberDTO.getPassword() != null && !memberDTO.getPassword().trim().isEmpty()) {
                member.setPasswordHashed(memberDTO.getPassword());
            }
            
            Member savedMember = memberRepository.save(member);
            return convertToDTO(savedMember);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating member with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a member
     */
    public boolean deleteMember(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            Optional<Member> optionalMember = memberRepository.findById(id);
            if (optionalMember.isEmpty()) {
                throw new IllegalArgumentException("Member with ID " + id + " not found");
            }
            
            Member member = optionalMember.get();
            if (member.getIsDeleted()) {
                throw new IllegalArgumentException("Member with ID " + id + " is already deleted");
            }
            
            member.setIsDeleted(true);
            memberRepository.save(member);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting member with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a member (permanently remove from database)
     */
    public boolean hardDeleteMember(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            if (!memberRepository.existsById(id)) {
                throw new IllegalArgumentException("Member with ID " + id + " not found");
            }
            
            memberRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting member with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted member
     */
    public boolean restoreMember(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            
            Optional<Member> optionalMember = memberRepository.findById(id);
            if (optionalMember.isEmpty()) {
                throw new IllegalArgumentException("Member with ID " + id + " not found");
            }
            
            Member member = optionalMember.get();
            if (!member.getIsDeleted()) {
                throw new IllegalArgumentException("Member with ID " + id + " is not deleted");
            }
            
            member.setIsDeleted(false);
            memberRepository.save(member);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring member with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Search members by name
     */
    public List<MemberDTO> searchMembersByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return getAllMembers();
            }
            List<Member> members = memberRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name.trim());
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching members by name: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search members by city
     */
    public List<MemberDTO> searchMembersByCity(String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                return getAllMembers();
            }
            List<Member> members = memberRepository.findByCityContainingIgnoreCaseAndIsDeletedFalse(city.trim());
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching members by city: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search members by gender
     */
    public List<MemberDTO> searchMembersByGender(String gender) {
        try {
            if (gender == null || gender.trim().isEmpty()) {
                return getAllMembers();
            }
            List<Member> members = memberRepository.findByGenderAndIsDeletedFalse(gender.trim());
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching members by gender: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search members by age range
     */
    public List<MemberDTO> searchMembersByAgeRange(Integer minAge, Integer maxAge) {
        try {
            if (minAge == null || maxAge == null) {
                throw new IllegalArgumentException("Both minAge and maxAge are required");
            }
            if (minAge > maxAge) {
                throw new IllegalArgumentException("minAge cannot be greater than maxAge");
            }
            List<Member> members = memberRepository.findByAgeBetweenAndIsDeletedFalse(minAge, maxAge);
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error searching members by age range: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search members by multiple criteria
     */
    public List<MemberDTO> searchMembersByMultipleCriteria(String name, String city, String gender, Integer minAge, Integer maxAge) {
        try {
            List<Member> members = memberRepository.findByMultipleCriteria(name, city, gender, minAge, maxAge);
            return members.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching members by multiple criteria: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if member exists by email
     */
    public boolean memberExistsByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            return memberRepository.existsByEmailIgnoreCase(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if member exists by email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Authenticate member (check email and password)
     */
    public Optional<MemberDTO> authenticateMember(String email, String password) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            
            Optional<Member> member = memberRepository.findActiveByEmailIgnoreCase(email.trim());
            if (member.isPresent() && member.get().checkPassword(password)) {
                return Optional.of(convertToDTO(member.get()));
            }
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error authenticating member: " + e.getMessage(), e);
        }
    }
    
    /**
     * Change member password
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Member ID cannot be null");
            }
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Old password is required");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("New password is required");
            }
            
            Optional<Member> optionalMember = memberRepository.findById(id);
            if (optionalMember.isEmpty()) {
                throw new IllegalArgumentException("Member with ID " + id + " not found");
            }
            
            Member member = optionalMember.get();
            if (member.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot change password for deleted member");
            }
            
            if (!member.checkPassword(oldPassword)) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            
            member.setPasswordHashed(newPassword);
            memberRepository.save(member);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error changing password for member with ID " + id + ": " + e.getMessage(), e);
        }
    }
}
