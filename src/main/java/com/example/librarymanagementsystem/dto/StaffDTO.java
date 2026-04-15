package com.example.librarymanagementsystem.dto;

import jakarta.validation.constraints.*;

public class StaffDTO {
    
    private Long id;
    
    @NotBlank(message = "Staff name is required")
    @Size(min = 2, max = 100, message = "Staff name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    private String password;
    
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|LIBRARIAN|MANAGER)$", message = "Role must be ADMIN, LIBRARIAN, or MANAGER")
    private String role;
    
    private Boolean isDeleted;
    
    // Default constructor
    public StaffDTO() {}
    
    // Constructor with all fields
    public StaffDTO(Long id, String name, String email, String password, String gender, String role, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.isDeleted = isDeleted;
    }
    
    // Constructor with required fields
    public StaffDTO(String name, String email, String password, String gender, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.role = role;
        this.isDeleted = false;
    }
    
    // Constructor for updates (without password)
    public StaffDTO(Long id, String name, String email, String gender, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.isDeleted = false;
    }
    
    // Constructor for response (without password)
    public StaffDTO(Long id, String name, String email, String gender, String role, Boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = null; // Don't expose password in response
        this.gender = gender;
        this.role = role;
        this.isDeleted = isDeleted;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Boolean getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    @Override
    public String toString() {
        return "StaffDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
