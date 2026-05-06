package com.example.librarymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/")
    public String  index(){
        return "index";
    }
    @GetMapping("/admin-login")
    public String  adminLogin(){
        return "admin_login";
    }
    @GetMapping("/admin-dashboard")
    public String  adminDashboard(){
        return "admin_dashboard";
    }
    @GetMapping("/library-login")
    public String  librarianLogin(){
        return "librarian_login";
    }
    @GetMapping("/librarian-dashboard")
    public String  librarianDashboard(){
        return "librarian_dashboard";
    }
    @GetMapping("/login")
    public String  userLogin(){
        return "login";
    }
    @GetMapping("/member-login")
    public String  memberLogin(){
        return "login";
    }
    @GetMapping("/profile")
    public String  userProfile(){
        return "profile";
    }
    @GetMapping("/member-profile")
    public String  memberProfile(){
        return "profile";
    }
    @GetMapping("/signup")
    public String  signup(){
        return "signup";
    }
    @GetMapping("/librarian-management")
    public String  librarianManagement(){
        return "librarian_management";
    }
    @GetMapping("/book-management")
    public String  bookManagement(){
        return "book_management";
    }
    @GetMapping("/category-management")
    public String  categoryManagement(){
        return "category-management";
    }
    @GetMapping("/librarian-profile")
    public String  librarianProfile(){
        return "librarian-profile";
    }
    @GetMapping("/librarian-borrowings")
    public String  librarianBorrowings(){
        return "librarian-borrowings";
    }
    @GetMapping("/librarian-books")
    public String  librarianBooks(){
        return "librarian_books";
    }
    @GetMapping("/member-management")
    public String  memberManagement(){
        return "member-management";
    }
    @GetMapping("/feedback-management")
    public String  feedbackManagement(){
        return "feedback-management";
    }

    @GetMapping("/member-borrowings")
    public String  memberBorrowings(){
        return "member-borrowings";
    }

    @GetMapping("/member-dashboard")
    public String  memberDashboard(){
        return "member-dashboard";
    }
}
