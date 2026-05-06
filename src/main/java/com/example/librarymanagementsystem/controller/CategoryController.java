package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.model.Category;
import com.example.librarymanagementsystem.service.CategoryService;
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
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Get all active categories
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCategories() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categories = categoryService.getAllCategories();
            response.put("success", true);
            response.put("message", "Categories retrieved successfully");
            response.put("data", categories);
            response.put("count", categories.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving categories: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all categories including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllCategoriesIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categories = categoryService.getAllCategoriesIncludingDeleted();
            response.put("success", true);
            response.put("message", "All categories retrieved successfully");
            response.put("data", categories);
            response.put("count", categories.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all categories: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get category by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Category> category = categoryService.getActiveCategoryById(id);
            if (category.isPresent()) {
                response.put("success", true);
                response.put("message", "Category retrieved successfully");
                response.put("data", category.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Category with ID " + id + " not found");
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
            response.put("message", "Error retrieving category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new category
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@Valid @RequestBody Category category) {
        Map<String, Object> response = new HashMap<>();
        try {
            Category createdCategory = categoryService.createCategory(category);
            response.put("success", true);
            response.put("message", "Category created successfully");
            response.put("data", createdCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing category
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable Long id, @Valid @RequestBody Category categoryDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
            response.put("success", true);
            response.put("message", "Category updated successfully");
            response.put("data", updatedCategory);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a category
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = categoryService.deleteCategory(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Category deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete category");
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
            response.put("message", "Error deleting category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a category (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = categoryService.hardDeleteCategory(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Category permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete category");
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
            response.put("message", "Error permanently deleting category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted category
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = categoryService.restoreCategory(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Category restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore category");
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
            response.put("message", "Error restoring category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search categories by name
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchCategoriesByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Category> categories = categoryService.searchCategoriesByName(name);
            response.put("success", true);
            response.put("message", "Search completed successfully");
            response.put("data", categories);
            response.put("count", categories.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching categories: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if category exists by name
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkCategoryExists(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = categoryService.categoryExistsByName(name);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking category existence: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
