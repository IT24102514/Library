package com.example.librarymanagementsystem.config;

import com.example.librarymanagementsystem.model.Staff;
import com.example.librarymanagementsystem.repository.StaffRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(StaffRepository staffRepository) {
        return args -> {
            // Create default admin if no staff exists
            if (staffRepository.count() == 0) {
                Staff admin = new Staff("Admin", "admin@library.com", "admin123", "Male", "ADMIN");
                staffRepository.save(admin);
                System.out.println("Default admin created: admin@library.com / admin123");

                Staff librarian = new Staff("Librarian", "librarian@library.com", "lib123", "Female", "LIBRARIAN");
                staffRepository.save(librarian);
                System.out.println("Default librarian created: librarian@library.com / lib123");
            }
        };
    }
}
