package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.model.Category;
import com.example.librarymanagementsystem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Get all active categories
     */
    public List<Category> getAllCategories() {
        try {
            return categoryRepository.findAllActive();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving categories: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all categories including deleted ones
     */
    public List<Category> getAllCategoriesIncludingDeleted() {
        try {
            return categoryRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all categories: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get category by ID
     */
    public Optional<Category> getCategoryById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            return categoryRepository.findById(id);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get active category by ID
     */
    public Optional<Category> getActiveCategoryById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent() && category.get().getIsDeleted()) {
                return Optional.empty();
            }
            return category;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving active category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new category
     */
    public Category createCategory(Category category) {
        try {
            if (category == null) {
                throw new IllegalArgumentException("Category cannot be null");
            }
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name is required");
            }
            
            // Check if category name already exists
            if (categoryRepository.existsByNameIgnoreCase(category.getName().trim())) {
                throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
            }
            
            // Set default values
            category.setName(category.getName().trim());
            if (category.getDescription() != null) {
                category.setDescription(category.getDescription().trim());
            }
            category.setIsDeleted(false);
            
            return categoryRepository.save(category);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating category: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing category
     */
    public Category updateCategory(Long id, Category categoryDetails) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            if (categoryDetails == null) {
                throw new IllegalArgumentException("Category details cannot be null");
            }
            if (categoryDetails.getName() == null || categoryDetails.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Category name is required");
            }
            
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()) {
                throw new IllegalArgumentException("Category with ID " + id + " not found");
            }
            
            Category category = optionalCategory.get();
            
            // Check if the new name conflicts with existing categories (excluding current one)
            if (!category.getName().equalsIgnoreCase(categoryDetails.getName().trim()) &&
                categoryRepository.existsByNameIgnoreCaseAndIdNot(categoryDetails.getName().trim(), id)) {
                throw new IllegalArgumentException("Category with name '" + categoryDetails.getName() + "' already exists");
            }
            
            // Update fields
            category.setName(categoryDetails.getName().trim());
            category.setDescription(categoryDetails.getDescription() != null ? 
                categoryDetails.getDescription().trim() : null);
            
            return categoryRepository.save(category);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a category (mark as deleted)
     */
    public boolean deleteCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()) {
                throw new IllegalArgumentException("Category with ID " + id + " not found");
            }
            
            Category category = optionalCategory.get();
            if (category.getIsDeleted()) {
                throw new IllegalArgumentException("Category with ID " + id + " is already deleted");
            }
            
            category.setIsDeleted(true);
            categoryRepository.save(category);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a category (permanently remove from database)
     */
    public boolean hardDeleteCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            if (!categoryRepository.existsById(id)) {
                throw new IllegalArgumentException("Category with ID " + id + " not found");
            }
            
            categoryRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted category
     */
    public boolean restoreCategory(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isEmpty()) {
                throw new IllegalArgumentException("Category with ID " + id + " not found");
            }
            
            Category category = optionalCategory.get();
            if (!category.getIsDeleted()) {
                throw new IllegalArgumentException("Category with ID " + id + " is not deleted");
            }
            
            category.setIsDeleted(false);
            categoryRepository.save(category);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring category with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Search categories by name
     */
    public List<Category> searchCategoriesByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return getAllCategories();
            }
            return categoryRepository.findByNameContainingIgnoreCase(name.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error searching categories by name: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if category exists by name
     */
    public boolean categoryExistsByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return false;
            }
            return categoryRepository.existsByNameIgnoreCase(name.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if category exists by name: " + e.getMessage(), e);
        }
    }
}
