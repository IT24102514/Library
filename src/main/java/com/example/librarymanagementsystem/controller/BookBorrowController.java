package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.BookBorrowDTO;
import com.example.librarymanagementsystem.service.BookBorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrows")
@CrossOrigin(origins = "*")
public class BookBorrowController {
    
    @Autowired
    private BookBorrowService bookBorrowService;
    
    /**
     * Get all active borrows
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBorrows() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getAllBorrows();
            response.put("success", true);
            response.put("message", "Borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all borrows including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBorrowsIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getAllBorrowsIncludingDeleted();
            response.put("success", true);
            response.put("message", "All borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrow by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBorrowById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<BookBorrowDTO> borrow = bookBorrowService.getBorrowById(id);
            if (borrow.isPresent()) {
                response.put("success", true);
                response.put("message", "Borrow retrieved successfully");
                response.put("data", borrow.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Borrow with ID " + id + " not found");
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
            response.put("message", "Error retrieving borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrows by book ID
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Map<String, Object>> getBorrowsByBookId(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getBorrowsByBookId(bookId);
            response.put("success", true);
            response.put("message", "Borrows for book retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows for book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrows by member ID
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<Map<String, Object>> getBorrowsByMemberId(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getBorrowsByMemberId(memberId);
            response.put("success", true);
            response.put("message", "Borrows for member retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows for member: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get active borrows (not returned)
     */
    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveBorrows() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getActiveBorrows();
            response.put("success", true);
            response.put("message", "Active borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving active borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get returned borrows
     */
    @GetMapping("/returned")
    public ResponseEntity<Map<String, Object>> getReturnedBorrows() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getReturnedBorrows();
            response.put("success", true);
            response.put("message", "Returned borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving returned borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get overdue borrows
     */
    @GetMapping("/overdue")
    public ResponseEntity<Map<String, Object>> getOverdueBorrows() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getOverdueBorrows();
            response.put("success", true);
            response.put("message", "Overdue borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving overdue borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new borrow
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBorrow(@Valid @RequestBody BookBorrowDTO bookBorrowDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookBorrowDTO createdBorrow = bookBorrowService.createBorrow(bookBorrowDTO);
            response.put("success", true);
            response.put("message", "Borrow created successfully");
            response.put("data", createdBorrow);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing borrow
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBorrow(@PathVariable Long id, @Valid @RequestBody BookBorrowDTO bookBorrowDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookBorrowDTO updatedBorrow = bookBorrowService.updateBorrow(id, bookBorrowDTO);
            response.put("success", true);
            response.put("message", "Borrow updated successfully");
            response.put("data", updatedBorrow);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Return a borrowed book
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<Map<String, Object>> returnBook(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookBorrowDTO returnedBorrow = bookBorrowService.returnBook(id);
            response.put("success", true);
            response.put("message", "Book returned successfully");
            response.put("data", returnedBorrow);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error returning book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a borrow
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBorrow(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = bookBorrowService.deleteBorrow(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Borrow deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete borrow");
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
            response.put("message", "Error deleting borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a borrow (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteBorrow(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = bookBorrowService.hardDeleteBorrow(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Borrow permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete borrow");
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
            response.put("message", "Error permanently deleting borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted borrow
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreBorrow(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = bookBorrowService.restoreBorrow(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Borrow restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore borrow");
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
            response.put("message", "Error restoring borrow: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrows by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<Map<String, Object>> getBorrowsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getBorrowsByDateRange(startDate, endDate);
            response.put("success", true);
            response.put("message", "Borrows by date range retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows by date range: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrows due on specific date
     */
    @GetMapping("/due-on")
    public ResponseEntity<Map<String, Object>> getBorrowsDueOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getBorrowsDueOnDate(dueDate);
            response.put("success", true);
            response.put("message", "Borrows due on date retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows due on date: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrows due within days
     */
    @GetMapping("/due-within")
    public ResponseEntity<Map<String, Object>> getBorrowsDueWithinDays(@RequestParam int days) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getBorrowsDueWithinDays(days);
            response.put("success", true);
            response.put("message", "Borrows due within days retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrows due within days: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search borrows by multiple criteria
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBorrowsByMultipleCriteria(
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Boolean isReturned,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startBorrowDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endBorrowDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startReturnDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endReturnDate) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.searchBorrowsByMultipleCriteria(
                    bookId, memberId, isReturned, startBorrowDate, endBorrowDate, startReturnDate, endReturnDate);
            response.put("success", true);
            response.put("message", "Search by multiple criteria completed successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching borrows by multiple criteria: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get recent borrows
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentBorrows() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookBorrowDTO> borrows = bookBorrowService.getRecentBorrows();
            response.put("success", true);
            response.put("message", "Recent borrows retrieved successfully");
            response.put("data", borrows);
            response.put("count", borrows.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving recent borrows: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if book is currently borrowed
     */
    @GetMapping("/book/{bookId}/is-borrowed")
    public ResponseEntity<Map<String, Object>> isBookCurrentlyBorrowed(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isBorrowed = bookBorrowService.isBookCurrentlyBorrowed(bookId);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", isBorrowed);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking if book is borrowed: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get current borrow for a book
     */
    @GetMapping("/book/{bookId}/current")
    public ResponseEntity<Map<String, Object>> getCurrentBorrowForBook(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<BookBorrowDTO> borrow = bookBorrowService.getCurrentBorrowForBook(bookId);
            if (borrow.isPresent()) {
                response.put("success", true);
                response.put("message", "Current borrow for book retrieved successfully");
                response.put("data", borrow.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", true);
                response.put("message", "No current borrow found for this book");
                response.put("data", null);
                return ResponseEntity.ok(response);
            }
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting current borrow for book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get active borrow count for member
     */
    @GetMapping("/member/{memberId}/active-count")
    public ResponseEntity<Map<String, Object>> getActiveBorrowCountForMember(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = bookBorrowService.getActiveBorrowCountForMember(memberId);
            response.put("success", true);
            response.put("message", "Active borrow count retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting active borrow count: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get total borrow count for member
     */
    @GetMapping("/member/{memberId}/total-count")
    public ResponseEntity<Map<String, Object>> getTotalBorrowCountForMember(@PathVariable Long memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = bookBorrowService.getTotalBorrowCountForMember(memberId);
            response.put("success", true);
            response.put("message", "Total borrow count retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting total borrow count: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get total borrow count for book
     */
    @GetMapping("/book/{bookId}/total-count")
    public ResponseEntity<Map<String, Object>> getTotalBorrowCountForBook(@PathVariable Long bookId) {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = bookBorrowService.getTotalBorrowCountForBook(bookId);
            response.put("success", true);
            response.put("message", "Total borrow count retrieved successfully");
            response.put("data", count);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error getting total borrow count: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
