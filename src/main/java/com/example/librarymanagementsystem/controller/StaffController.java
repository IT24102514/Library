package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.LoginDTO;
import com.example.librarymanagementsystem.dto.StaffDTO;
import com.example.librarymanagementsystem.model.Staff;
import com.example.librarymanagementsystem.repository.StaffRepository;
import com.example.librarymanagementsystem.service.StaffService;
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
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
public class StaffController {
    
    @Autowired
    private StaffService staffService;

    @Autowired
    private StaffRepository staffRepository;
    
    /**
     * Get all active staff
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllStaff() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.getAllStaff();
            response.put("success", true);
            response.put("message", "Staff retrieved successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all staff including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllStaffIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.getAllStaffIncludingDeleted();
            response.put("success", true);
            response.put("message", "All staff retrieved successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get staff by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getStaffById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<StaffDTO> staff = staffService.getStaffById(id);
            if (staff.isPresent()) {
                response.put("success", true);
                response.put("message", "Staff retrieved successfully");
                response.put("data", staff.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Staff with ID " + id + " not found");
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
            response.put("message", "Error retrieving staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get staff by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> getStaffByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<StaffDTO> staff = staffService.getStaffByEmail(email);
            if (staff.isPresent()) {
                response.put("success", true);
                response.put("message", "Staff retrieved successfully");
                response.put("data", staff.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Staff with email " + email + " not found");
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
            response.put("message", "Error retrieving staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new staff member
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStaff(@Valid @RequestBody StaffDTO staffDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            StaffDTO createdStaff = staffService.createStaff(staffDTO);
            response.put("success", true);
            response.put("message", "Staff created successfully");
            response.put("data", createdStaff);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing staff member
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateStaff(@PathVariable Long id, @Valid @RequestBody StaffDTO staffDTO) {
        Map<String, Object> response = new HashMap<>();
        try {

            StaffDTO updatedStaff = staffService.updateStaff(id, staffDTO);
            response.put("success", true);
            response.put("message", "Staff updated successfully");
            response.put("data", updatedStaff);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a staff member
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteStaff(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = staffService.deleteStaff(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Staff deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete staff");
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
            response.put("message", "Error deleting staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a staff member (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteStaff(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = staffService.hardDeleteStaff(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Staff permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete staff");
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
            response.put("message", "Error permanently deleting staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted staff member
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreStaff(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = staffService.restoreStaff(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Staff restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore staff");
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
            response.put("message", "Error restoring staff: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search staff by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<Map<String, Object>> searchStaffByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.searchStaffByName(name);
            response.put("success", true);
            response.put("message", "Search by name completed successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching staff by name: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search staff by role
     */
    @GetMapping("/search/role")
    public ResponseEntity<Map<String, Object>> searchStaffByRole(@RequestParam String role) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.searchStaffByRole(role);
            response.put("success", true);
            response.put("message", "Search by role completed successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching staff by role: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search staff by gender
     */
    @GetMapping("/search/gender")
    public ResponseEntity<Map<String, Object>> searchStaffByGender(@RequestParam String gender) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.searchStaffByGender(gender);
            response.put("success", true);
            response.put("message", "Search by gender completed successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching staff by gender: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search staff by multiple criteria
     */
    @GetMapping("/search/multiple")
    public ResponseEntity<Map<String, Object>> searchStaffByMultipleCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String gender) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.searchStaffByMultipleCriteria(name, role, gender);
            response.put("success", true);
            response.put("message", "Search by multiple criteria completed successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching staff by multiple criteria: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all admins
     */
    @GetMapping("/admins")
    public ResponseEntity<Map<String, Object>> getAllAdmins() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.getAllAdmins();
            response.put("success", true);
            response.put("message", "Admins retrieved successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving admins: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all librarians
     */
    @GetMapping("/librarians")
    public ResponseEntity<Map<String, Object>> getAllLibrarians() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.getAllLibrarians();
            response.put("success", true);
            response.put("message", "Librarians retrieved successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving librarians: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all managers
     */
    @GetMapping("/managers")
    public ResponseEntity<Map<String, Object>> getAllManagers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<StaffDTO> staff = staffService.getAllManagers();
            response.put("success", true);
            response.put("message", "Managers retrieved successfully");
            response.put("data", staff);
            response.put("count", staff.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving managers: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if staff exists by email
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkStaffExists(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = staffService.staffExistsByEmail(email);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking staff existence: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Authenticate staff (login)
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginStaff(@Valid @RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<StaffDTO> staff = staffService.authenticateStaff(loginDTO);
            if (staff.isPresent()) {
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("data", staff.get());
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
            response.put("message", "Error during login: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Change staff password
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
            
            boolean changed = staffService.changePassword(id, oldPassword, newPassword);
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
    
    /**
     * Get staff count by role
     */
    @GetMapping("/count/role/{role}")
    public ResponseEntity<Map<String, Object>> getStaffCountByRole(@PathVariable String role) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = staffService.getStaffCountByRole(role);
            response.put("success", true);
            response.put("message", "Staff count by role retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting staff count by role: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get staff count by gender
     */
    @GetMapping("/count/gender/{gender}")
    public ResponseEntity<Map<String, Object>> getStaffCountByGender(@PathVariable String gender) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = staffService.getStaffCountByGender(gender);
            response.put("success", true);
            response.put("message", "Staff count by gender retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting staff count by gender: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
