package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.MemberDTO;
import com.example.librarymanagementsystem.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "*")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    /**
     * Get all active members
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMembers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.getAllMembers();
            response.put("success", true);
            response.put("message", "Members retrieved successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving members: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all members including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllMembersIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.getAllMembersIncludingDeleted();
            response.put("success", true);
            response.put("message", "All members retrieved successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all members: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get member by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMemberById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<MemberDTO> member = memberService.getMemberById(id);
            if (member.isPresent()) {
                response.put("success", true);
                response.put("message", "Member retrieved successfully");
                response.put("data", member.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Member with ID " + id + " not found");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get member by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getMemberByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<MemberDTO> member = memberService.getMemberByEmail(email);
            if (member.isPresent()) {
                response.put("success", true);
                response.put("message", "Member retrieved successfully");
                response.put("data", member.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Member with email " + email + " not found");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new member
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createMember(@Valid @RequestBody MemberDTO memberDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            MemberDTO createdMember = memberService.createMember(memberDTO);
            response.put("success", true);
            response.put("message", "Member created successfully");
            response.put("data", createdMember);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing member
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMember(@PathVariable Long id, @Valid @RequestBody MemberDTO memberDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            MemberDTO updatedMember = memberService.updateMember(id, memberDTO);
            response.put("success", true);
            response.put("message", "Member updated successfully");
            response.put("data", updatedMember);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a member
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = memberService.deleteMember(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Member deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete member");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error deleting member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a member (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteMember(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = memberService.hardDeleteMember(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Member permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete member");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error permanently deleting member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted member
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreMember(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = memberService.restoreMember(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Member restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore member");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error restoring member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search members by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<Map<String, Object>> searchMembersByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.searchMembersByName(name);
            response.put("success", true);
            response.put("message", "Search by name completed successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching members by name: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search members by city
     */
    @GetMapping("/search/city")
    public ResponseEntity<Map<String, Object>> searchMembersByCity(@RequestParam String city) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.searchMembersByCity(city);
            response.put("success", true);
            response.put("message", "Search by city completed successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching members by city: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search members by gender
     */
    @GetMapping("/search/gender")
    public ResponseEntity<Map<String, Object>> searchMembersByGender(@RequestParam String gender) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.searchMembersByGender(gender);
            response.put("success", true);
            response.put("message", "Search by gender completed successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching members by gender: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search members by age range
     */
    @GetMapping("/search/age-range")
    public ResponseEntity<Map<String, Object>> searchMembersByAgeRange(@RequestParam Integer minAge, @RequestParam Integer maxAge) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.searchMembersByAgeRange(minAge, maxAge);
            response.put("success", true);
            response.put("message", "Search by age range completed successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching members by age range: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search members by multiple criteria
     */
    @GetMapping("/search/multiple")
    public ResponseEntity<Map<String, Object>> searchMembersByMultipleCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MemberDTO> members = memberService.searchMembersByMultipleCriteria(name, city, gender, minAge, maxAge);
            response.put("success", true);
            response.put("message", "Search by multiple criteria completed successfully");
            response.put("data", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching members by multiple criteria: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if member exists by email
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkMemberExists(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = memberService.memberExistsByEmail(email);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking member existence: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Authenticate member (login)
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, Object>> authenticateMember(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            if (email == null || email.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Email is required");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (password == null || password.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            Optional<MemberDTO> member = memberService.authenticateMember(email, password);
            if (member.isPresent()) {
                response.put("success", true);
                response.put("message", "Authentication successful");
                response.put("data", member.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Invalid email or password");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error authenticating member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Change member password
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Map<String, Object>> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        Map<String, Object> response = new HashMap<>();
        try {
            String oldPassword = passwords.get("oldPassword");
            String newPassword = passwords.get("newPassword");
            
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Old password is required");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            if (newPassword == null || newPassword.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "New password is required");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            boolean changed = memberService.changePassword(id, oldPassword, newPassword);
            if (changed) {
                response.put("success", true);
                response.put("message", "Password changed successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to change password");
                response.put("data", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error changing password: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
