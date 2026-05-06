package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.LoginDTO;
import com.example.librarymanagementsystem.dto.StaffDTO;
import com.example.librarymanagementsystem.model.Staff;
import com.example.librarymanagementsystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffService {
    
    @Autowired
    private StaffRepository staffRepository;
    
    /**
     * Convert Staff entity to StaffDTO
     */
    private StaffDTO convertToDTO(Staff staff) {
        if (staff == null) {
            return null;
        }
        
        return new StaffDTO(
            staff.getId(),
            staff.getName(),
            staff.getEmail(),
            null, // Don't expose password in DTO
            staff.getGender(),
            staff.getRole(),
            staff.getIsDeleted()
        );
    }
    
    /**
     * Convert StaffDTO to Staff entity
     */
    private Staff convertToEntity(StaffDTO staffDTO) {
        if (staffDTO == null) {
            return null;
        }
        
        Staff staff = new Staff();
        staff.setId(staffDTO.getId());
        staff.setName(staffDTO.getName());
        staff.setEmail(staffDTO.getEmail());
        staff.setGender(staffDTO.getGender());
        staff.setRole(staffDTO.getRole());
        staff.setIsDeleted(staffDTO.getIsDeleted() != null ? staffDTO.getIsDeleted() : false);
        
        // Handle password separately
        if (staffDTO.getPassword() != null && !staffDTO.getPassword().trim().isEmpty()) {
            staff.setPasswordHashed(staffDTO.getPassword());
        }
        
        return staff;
    }
    
    /**
     * Get all active staff
     */
    public List<StaffDTO> getAllStaff() {
        try {
            List<Staff> staff = staffRepository.findAllActive();
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving staff: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all staff including deleted ones
     */
    public List<StaffDTO> getAllStaffIncludingDeleted() {
        try {
            List<Staff> staff = staffRepository.findAll();
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all staff: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get staff by ID
     */
    public Optional<StaffDTO> getStaffById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            Optional<Staff> staff = staffRepository.findById(id);
            if (staff.isPresent() && staff.get().getIsDeleted()) {
                return Optional.empty();
            }
            return staff.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get staff by email
     */
    public Optional<StaffDTO> getStaffByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            Optional<Staff> staff = staffRepository.findActiveByEmailIgnoreCase(email.trim());
            return staff.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving staff with email " + email + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new staff member
     */
    public StaffDTO createStaff(StaffDTO staffDTO) {
        try {
            if (staffDTO == null) {
                throw new IllegalArgumentException("Staff cannot be null");
            }
            if (staffDTO.getName() == null || staffDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Staff name is required");
            }
            if (staffDTO.getEmail() == null || staffDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (staffDTO.getPassword() == null || staffDTO.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (staffDTO.getGender() == null || staffDTO.getGender().trim().isEmpty()) {
                throw new IllegalArgumentException("Gender is required");
            }
            if (staffDTO.getRole() == null || staffDTO.getRole().trim().isEmpty()) {
                throw new IllegalArgumentException("Role is required");
            }
            
            // Check if email already exists
            if (staffRepository.existsByEmailIgnoreCase(staffDTO.getEmail().trim())) {
                throw new IllegalArgumentException("Staff with email '" + staffDTO.getEmail() + "' already exists");
            }
            
            Staff staff = convertToEntity(staffDTO);
            staff.setName(staff.getName().trim());
            staff.setEmail(staff.getEmail().trim().toLowerCase());
            staff.setGender(staff.getGender().trim());
            staff.setRole(staff.getRole().trim());
            staff.setIsDeleted(false);
            
            Staff savedStaff = staffRepository.save(staff);
            return convertToDTO(savedStaff);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating staff: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing staff member
     */
    public StaffDTO updateStaff(Long id, StaffDTO staffDTO) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            if (staffDTO == null) {
                throw new IllegalArgumentException("Staff details cannot be null");
            }
            if (staffDTO.getName() == null || staffDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Staff name is required");
            }
            if (staffDTO.getEmail() == null || staffDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email is required");
            }
            if (staffDTO.getGender() == null || staffDTO.getGender().trim().isEmpty()) {
                throw new IllegalArgumentException("Gender is required");
            }
            if (staffDTO.getRole() == null || staffDTO.getRole().trim().isEmpty()) {
                throw new IllegalArgumentException("Role is required");
            }
            
            Optional<Staff> optionalStaff = staffRepository.findById(id);
            if (optionalStaff.isEmpty()) {
                throw new IllegalArgumentException("Staff with ID " + id + " not found");
            }
            
            Staff staff = optionalStaff.get();
            
            // Check if the new email conflicts with existing staff
            if (!staff.getEmail().equalsIgnoreCase(staffDTO.getEmail().trim())) {
                if (staffRepository.existsByEmailIgnoreCaseAndIdNot(staffDTO.getEmail().trim(), id)) {
                    throw new IllegalArgumentException("Staff with email '" + staffDTO.getEmail() + "' already exists");
                }
            }
            
            // Update fields
            staff.setName(staffDTO.getName().trim());
            staff.setEmail(staffDTO.getEmail().trim().toLowerCase());
            staff.setGender(staffDTO.getGender().trim());
            staff.setRole(staffDTO.getRole().trim());

            Staff savedStaff = staffRepository.save(staff);
            return convertToDTO(savedStaff);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a staff member
     */
    public boolean deleteStaff(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            
            Optional<Staff> optionalStaff = staffRepository.findById(id);
            if (optionalStaff.isEmpty()) {
                throw new IllegalArgumentException("Staff with ID " + id + " not found");
            }
            
            Staff staff = optionalStaff.get();
            if (staff.getIsDeleted()) {
                throw new IllegalArgumentException("Staff with ID " + id + " is already deleted");
            }
            
            staff.setIsDeleted(true);
            staffRepository.save(staff);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a staff member (permanently remove from database)
     */
    public boolean hardDeleteStaff(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            
            if (!staffRepository.existsById(id)) {
                throw new IllegalArgumentException("Staff with ID " + id + " not found");
            }
            
            staffRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted staff member
     */
    public boolean restoreStaff(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            
            Optional<Staff> optionalStaff = staffRepository.findById(id);
            if (optionalStaff.isEmpty()) {
                throw new IllegalArgumentException("Staff with ID " + id + " not found");
            }
            
            Staff staff = optionalStaff.get();
            if (!staff.getIsDeleted()) {
                throw new IllegalArgumentException("Staff with ID " + id + " is not deleted");
            }
            
            staff.setIsDeleted(false);
            staffRepository.save(staff);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Search staff by name
     */
    public List<StaffDTO> searchStaffByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return getAllStaff();
            }
            List<Staff> staff = staffRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name.trim());
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching staff by name: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search staff by role
     */
    public List<StaffDTO> searchStaffByRole(String role) {
        try {
            if (role == null || role.trim().isEmpty()) {
                return getAllStaff();
            }
            List<Staff> staff = staffRepository.findByRoleAndIsDeletedFalse(role.trim());
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching staff by role: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search staff by gender
     */
    public List<StaffDTO> searchStaffByGender(String gender) {
        try {
            if (gender == null || gender.trim().isEmpty()) {
                return getAllStaff();
            }
            List<Staff> staff = staffRepository.findByGenderAndIsDeletedFalse(gender.trim());
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching staff by gender: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search staff by multiple criteria
     */
    public List<StaffDTO> searchStaffByMultipleCriteria(String name, String role, String gender) {
        try {
            List<Staff> staff = staffRepository.findByMultipleCriteria(name, role, gender);
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching staff by multiple criteria: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all admins
     */
    public List<StaffDTO> getAllAdmins() {
        try {
            List<Staff> staff = staffRepository.findAllAdmins();
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving admins: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all librarians
     */
    public List<StaffDTO> getAllLibrarians() {
        try {
            List<Staff> staff = staffRepository.findAllLibrarians();
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving librarians: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all managers
     */
    public List<StaffDTO> getAllManagers() {
        try {
            List<Staff> staff = staffRepository.findAllManagers();
            return staff.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving managers: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if staff exists by email
     */
    public boolean staffExistsByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return false;
            }
            return staffRepository.existsByEmailIgnoreCase(email.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if staff exists by email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Authenticate staff (check email and password)
     */
    public Optional<StaffDTO> authenticateStaff(LoginDTO loginDTO) {
        try {
            if (loginDTO == null) {
                throw new IllegalArgumentException("Login credentials cannot be null");
            }
            if (loginDTO.getEmail() == null || loginDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (loginDTO.getPassword() == null || loginDTO.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            
            Optional<Staff> staff = staffRepository.findActiveByEmailIgnoreCase(loginDTO.getEmail().trim());
            if (staff.isPresent() && staff.get().checkPassword(loginDTO.getPassword())) {
                return Optional.of(convertToDTO(staff.get()));
            }
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error authenticating staff: " + e.getMessage(), e);
        }
    }
    
    /**
     * Change staff password
     */
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Staff ID cannot be null");
            }
            if (oldPassword == null || oldPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("Old password is required");
            }
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new IllegalArgumentException("New password is required");
            }
            
            Optional<Staff> optionalStaff = staffRepository.findById(id);
            if (optionalStaff.isEmpty()) {
                throw new IllegalArgumentException("Staff with ID " + id + " not found");
            }
            
            Staff staff = optionalStaff.get();
            if (staff.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot change password for deleted staff");
            }
            
            if (!staff.checkPassword(oldPassword)) {
                throw new IllegalArgumentException("Old password is incorrect");
            }
            
            staff.setPasswordHashed(newPassword);
            staffRepository.save(staff);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error changing password for staff with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get staff count by role
     */
    public long getStaffCountByRole(String role) {
        try {
            if (role == null || role.trim().isEmpty()) {
                throw new IllegalArgumentException("Role cannot be null or empty");
            }
            return staffRepository.countByRoleAndIsDeletedFalse(role.trim());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting staff count by role: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get staff count by gender
     */
    public long getStaffCountByGender(String gender) {
        try {
            if (gender == null || gender.trim().isEmpty()) {
                throw new IllegalArgumentException("Gender cannot be null or empty");
            }
            return staffRepository.countByGenderAndIsDeletedFalse(gender.trim());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting staff count by gender: " + e.getMessage(), e);
        }
    }
}
