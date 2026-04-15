package com.example.librarymanagementsystem.service;

import com.example.librarymanagementsystem.dto.BookDTO;
import com.example.librarymanagementsystem.dto.CategoryDTO;
import com.example.librarymanagementsystem.model.Book;
import com.example.librarymanagementsystem.model.Category;
import com.example.librarymanagementsystem.repository.BookRepository;
import com.example.librarymanagementsystem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Convert Book entity to BookDTO
     */
    private BookDTO convertToDTO(Book book) {
        if (book == null) {
            return null;
        }
        
        CategoryDTO categoryDTO = null;
        if (book.getCategory() != null) {
            categoryDTO = new CategoryDTO(
                book.getCategory().getId(),
                book.getCategory().getName(),
                book.getCategory().getDescription(),
                book.getCategory().getIsDeleted()
            );
        }
        
        return new BookDTO(
            book.getId(),
            book.getName(),
            book.getAuthorName(),
            book.getDescription(),
            book.getImageUrl(),
            categoryDTO,
            book.getIsBorrowed(),
            book.getIsDeleted()
        );
    }
    
    /**
     * Convert BookDTO to Book entity
     */
    private Book convertToEntity(BookDTO bookDTO) {
        if (bookDTO == null) {
            return null;
        }
        
        Book book = new Book();
        book.setId(bookDTO.getId());
        book.setName(bookDTO.getName());
        book.setAuthorName(bookDTO.getAuthorName());
        book.setDescription(bookDTO.getDescription());
        book.setImageUrl(bookDTO.getImageUrl());
        book.setIsBorrowed(bookDTO.getIsBorrowed() != null ? bookDTO.getIsBorrowed() : false);
        book.setIsDeleted(bookDTO.getIsDeleted() != null ? bookDTO.getIsDeleted() : false);
        
        // Set category if provided
        if (bookDTO.getCategory() != null && bookDTO.getCategory().getId() != null) {
            Optional<Category> category = categoryRepository.findById(bookDTO.getCategory().getId());
            if (category.isPresent()) {
                book.setCategory(category.get());
            }
        }
        
        return book;
    }
    
    /**
     * Get all active books
     */
    public List<BookDTO> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get all books including deleted ones
     */
    public List<BookDTO> getAllBooksIncludingDeleted() {
        try {
            List<Book> books = bookRepository.findAll();
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get book by ID
     */
    public Optional<BookDTO> getBookById(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            Optional<Book> book = bookRepository.findById(id);
            if (book.isPresent() && book.get().getIsDeleted()) {
                return Optional.empty();
            }
            return book.map(this::convertToDTO);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get books by category ID
     */
    public List<BookDTO> getBooksByCategoryId(Long categoryId) {
        try {
            if (categoryId == null) {
                throw new IllegalArgumentException("Category ID cannot be null");
            }
            
            // Verify category exists
            if (!categoryRepository.existsById(categoryId)) {
                throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
            }
            
            List<Book> books = bookRepository.findByCategoryIdAndIsDeletedFalse(categoryId);
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving books for category " + categoryId + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Get available books (not borrowed)
     */
    public List<BookDTO> getAvailableBooks() {
        try {
            List<Book> books = bookRepository.findAvailableBooks();
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving available books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get borrowed books
     */
    public List<BookDTO> getBorrowedBooks() {
        try {
            List<Book> books = bookRepository.findBorrowedBooks();
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving borrowed books: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create a new book
     */
    public BookDTO createBook(BookDTO bookDTO) {
        try {
            if (bookDTO == null) {
                throw new IllegalArgumentException("Book cannot be null");
            }
            if (bookDTO.getName() == null || bookDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Book name is required");
            }
            if (bookDTO.getAuthorName() == null || bookDTO.getAuthorName().trim().isEmpty()) {
                throw new IllegalArgumentException("Author name is required");
            }
            if (bookDTO.getCategory() == null || bookDTO.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category is required");
            }
            
            // Check if book already exists
            if (bookRepository.existsByNameAndAuthorNameIgnoreCase(
                    bookDTO.getName().trim(), bookDTO.getAuthorName().trim())) {
                throw new IllegalArgumentException("Book with name '" + bookDTO.getName() + 
                    "' by author '" + bookDTO.getAuthorName() + "' already exists");
            }
            
            // Verify category exists
            Optional<Category> category = categoryRepository.findById(bookDTO.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Category with ID " + bookDTO.getCategory().getId() + " not found");
            }
            
            Book book = convertToEntity(bookDTO);
            book.setName(book.getName().trim());
            book.setAuthorName(book.getAuthorName().trim());
            if (book.getDescription() != null) {
                book.setDescription(book.getDescription().trim());
            }
            if (book.getImageUrl() != null) {
                book.setImageUrl(book.getImageUrl().trim());
            }
            book.setIsBorrowed(false);
            book.setIsDeleted(false);
            
            Book savedBook = bookRepository.save(book);
            return convertToDTO(savedBook);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating book: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update an existing book
     */
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            if (bookDTO == null) {
                throw new IllegalArgumentException("Book details cannot be null");
            }
            if (bookDTO.getName() == null || bookDTO.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Book name is required");
            }
            if (bookDTO.getAuthorName() == null || bookDTO.getAuthorName().trim().isEmpty()) {
                throw new IllegalArgumentException("Author name is required");
            }
            if (bookDTO.getCategory() == null || bookDTO.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category is required");
            }
            
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            Book book = optionalBook.get();
            
            // Check if the new name and author combination conflicts with existing books
            if (!book.getName().equalsIgnoreCase(bookDTO.getName().trim()) ||
                !book.getAuthorName().equalsIgnoreCase(bookDTO.getAuthorName().trim())) {
                
                if (bookRepository.existsByNameAndAuthorNameIgnoreCaseAndIdNot(
                        bookDTO.getName().trim(), bookDTO.getAuthorName().trim(), id)) {
                    throw new IllegalArgumentException("Book with name '" + bookDTO.getName() + 
                        "' by author '" + bookDTO.getAuthorName() + "' already exists");
                }
            }
            
            // Verify category exists
            Optional<Category> category = categoryRepository.findById(bookDTO.getCategory().getId());
            if (category.isEmpty()) {
                throw new IllegalArgumentException("Category with ID " + bookDTO.getCategory().getId() + " not found");
            }
            
            // Update fields
            book.setName(bookDTO.getName().trim());
            book.setAuthorName(bookDTO.getAuthorName().trim());
            book.setDescription(bookDTO.getDescription() != null ? bookDTO.getDescription().trim() : null);
            book.setImageUrl(bookDTO.getImageUrl() != null ? bookDTO.getImageUrl().trim() : null);
            book.setCategory(category.get());
            
            Book savedBook = bookRepository.save(book);
            return convertToDTO(savedBook);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Soft delete a book
     */
    public boolean deleteBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            Book book = optionalBook.get();
            if (book.getIsDeleted()) {
                throw new IllegalArgumentException("Book with ID " + id + " is already deleted");
            }
            
            book.setIsDeleted(true);
            bookRepository.save(book);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Hard delete a book (permanently remove from database)
     */
    public boolean hardDeleteBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            if (!bookRepository.existsById(id)) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            bookRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error permanently deleting book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Restore a soft-deleted book
     */
    public boolean restoreBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            Book book = optionalBook.get();
            if (!book.getIsDeleted()) {
                throw new IllegalArgumentException("Book with ID " + id + " is not deleted");
            }
            
            book.setIsDeleted(false);
            bookRepository.save(book);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error restoring book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Borrow a book
     */
    public boolean borrowBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            Book book = optionalBook.get();
            if (book.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot borrow deleted book");
            }
            if (book.getIsBorrowed()) {
                throw new IllegalArgumentException("Book is already borrowed");
            }
            
            book.setIsBorrowed(true);
            bookRepository.save(book);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error borrowing book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Return a borrowed book
     */
    public boolean returnBook(Long id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("Book ID cannot be null");
            }
            
            Optional<Book> optionalBook = bookRepository.findById(id);
            if (optionalBook.isEmpty()) {
                throw new IllegalArgumentException("Book with ID " + id + " not found");
            }
            
            Book book = optionalBook.get();
            if (book.getIsDeleted()) {
                throw new IllegalArgumentException("Cannot return deleted book");
            }
            if (!book.getIsBorrowed()) {
                throw new IllegalArgumentException("Book is not currently borrowed");
            }
            
            book.setIsBorrowed(false);
            bookRepository.save(book);
            return true;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error returning book with ID " + id + ": " + e.getMessage(), e);
        }
    }
    
    /**
     * Search books by name
     */
    public List<BookDTO> searchBooksByName(String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                return getAllBooks();
            }
            List<Book> books = bookRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(name.trim());
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching books by name: " + e.getMessage(), e);
        }
    }
    
    /**
     * Search books by author name
     */
    public List<BookDTO> searchBooksByAuthor(String authorName) {
        try {
            if (authorName == null || authorName.trim().isEmpty()) {
                return getAllBooks();
            }
            List<Book> books = bookRepository.findByAuthorNameContainingIgnoreCaseAndIsDeletedFalse(authorName.trim());
            return books.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error searching books by author: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if book exists by name and author
     */
    public boolean bookExistsByNameAndAuthor(String name, String authorName) {
        try {
            if (name == null || name.trim().isEmpty() || authorName == null || authorName.trim().isEmpty()) {
                return false;
            }
            return bookRepository.existsByNameAndAuthorNameIgnoreCase(name.trim(), authorName.trim());
        } catch (Exception e) {
            throw new RuntimeException("Error checking if book exists: " + e.getMessage(), e);
        }
    }
}
