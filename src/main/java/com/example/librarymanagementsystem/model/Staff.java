package com.example.librarymanagementsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "staff")
public class Staff {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Staff name is required")
    @Size(min = 2, max = 100, message = "Staff name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    @Column(name = "password", nullable = false)
    private String password;
    
    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    @Column(name = "gender", nullable = false)
    private String gender;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|LIBRARIAN|MANAGER)$", message = "Role must be ADMIN, LIBRARIAN, or MANAGER")
    @Column(name = "role", nullable = false)
    private String role;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
    
    // Default constructor
    public Staff() {}
    
    // Constructor with required fields
    public Staff(String name, String email, String password, String gender, String role) {
        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.gender = gender;
        this.role = role;
        this.isDeleted = false;
    }
    
    // Constructor with all fields
    public Staff(String name, String email, String password, String gender, String role, Boolean isDeleted) {
        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.gender = gender;
        this.role = role;
        this.isDeleted = isDeleted != null ? isDeleted : false;
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
    
    // Method to set password with hashing
    public void setPasswordHashed(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    // Method to check password
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.password);
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
    
    // Helper method to check if staff is admin
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
    
    // Helper method to check if staff is librarian
    public boolean isLibrarian() {
        return "LIBRARIAN".equals(this.role);
    }
    
    // Helper method to check if staff is manager
    public boolean isManager() {
        return "MANAGER".equals(this.role);
    }
    
    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", role='" + role + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
