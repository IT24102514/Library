package com.example.librarymanagementsystem.controller;

import com.example.librarymanagementsystem.dto.BookDTO;
import com.example.librarymanagementsystem.service.BookService;
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
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    
    @Autowired
    private BookService bookService;
    
    /**
     * Get all active books
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBooks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.getAllBooks();
            response.put("success", true);
            response.put("message", "Books retrieved successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving books: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get all books including deleted ones
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBooksIncludingDeleted() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.getAllBooksIncludingDeleted();
            response.put("success", true);
            response.put("message", "All books retrieved successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving all books: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<BookDTO> book = bookService.getBookById(id);
            if (book.isPresent()) {
                response.put("success", true);
                response.put("message", "Book retrieved successfully");
                response.put("data", book.get());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Book with ID " + id + " not found");
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
            response.put("message", "Error retrieving book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get books by category ID
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getBooksByCategoryId(@PathVariable Long categoryId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.getBooksByCategoryId(categoryId);
            response.put("success", true);
            response.put("message", "Books for category retrieved successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving books for category: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get available books (not borrowed)
     */
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableBooks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.getAvailableBooks();
            response.put("success", true);
            response.put("message", "Available books retrieved successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving available books: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Get borrowed books
     */
    @GetMapping("/borrowed")
    public ResponseEntity<Map<String, Object>> getBorrowedBooks() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.getBorrowedBooks();
            response.put("success", true);
            response.put("message", "Borrowed books retrieved successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving borrowed books: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Create a new book
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBook(@Valid @RequestBody BookDTO bookDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookDTO createdBook = bookService.createBook(bookDTO);
            response.put("success", true);
            response.put("message", "Book created successfully");
            response.put("data", createdBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Update an existing book
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            response.put("success", true);
            response.put("message", "Book updated successfully");
            response.put("data", updatedBook);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error updating book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Soft delete a book
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBook(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = bookService.deleteBook(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Book deleted successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to delete book");
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
            response.put("message", "Error deleting book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Hard delete a book (permanently remove)
     */
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Map<String, Object>> hardDeleteBook(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = bookService.hardDeleteBook(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "Book permanently deleted");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to permanently delete book");
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
            response.put("message", "Error permanently deleting book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Restore a soft-deleted book
     */
    @PostMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreBook(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean restored = bookService.restoreBook(id);
            if (restored) {
                response.put("success", true);
                response.put("message", "Book restored successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to restore book");
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
            response.put("message", "Error restoring book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Borrow a book
     */
    @PostMapping("/{id}/borrow")
    public ResponseEntity<Map<String, Object>> borrowBook(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean borrowed = bookService.borrowBook(id);
            if (borrowed) {
                response.put("success", true);
                response.put("message", "Book borrowed successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to borrow book");
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
            response.put("message", "Error borrowing book: " + e.getMessage());
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
            boolean returned = bookService.returnBook(id);
            if (returned) {
                response.put("success", true);
                response.put("message", "Book returned successfully");
                response.put("data", null);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to return book");
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
            response.put("message", "Error returning book: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search books by name
     */
    @GetMapping("/search/name")
    public ResponseEntity<Map<String, Object>> searchBooksByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.searchBooksByName(name);
            response.put("success", true);
            response.put("message", "Search by name completed successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching books by name: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Search books by author name
     */
    @GetMapping("/search/author")
    public ResponseEntity<Map<String, Object>> searchBooksByAuthor(@RequestParam String author) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<BookDTO> books = bookService.searchBooksByAuthor(author);
            response.put("success", true);
            response.put("message", "Search by author completed successfully");
            response.put("data", books);
            response.put("count", books.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error searching books by author: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * Check if book exists by name and author
     */
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Object>> checkBookExists(@RequestParam String name, @RequestParam String author) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = bookService.bookExistsByNameAndAuthor(name, author);
            response.put("success", true);
            response.put("message", "Check completed successfully");
            response.put("data", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error checking book existence: " + e.getMessage());
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
