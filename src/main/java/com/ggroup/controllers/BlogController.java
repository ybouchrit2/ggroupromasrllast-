package com.ggroup.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ggroup.models.Blog;
import com.ggroup.models.Contact;
import com.ggroup.services.BlogService;
import com.ggroup.services.ContactService;
import com.ggroup.services.EmailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
    private final BlogService blogService;

    @Autowired
    private final ContactService contactService;

    @Autowired
    private final EmailService emailService;

    public BlogController(BlogService blogService, ContactService contactService, EmailService emailService) {
        this.blogService = blogService;
        this.contactService = contactService;
        this.emailService = emailService;
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<String> toggleBlogActivation(@PathVariable Long id) {
        try {
            Optional<Blog> blogOptional = blogService.getBlogById(id);
            if (blogOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Blog not found with ID: " + id);
            }

            Blog blog = blogOptional.get();
            boolean isActive = blog.isActive();
            blog.setActive(!isActive); // التبديل بين التفعيل والتعطيل
            blogService.saveBlog(blog);

            if (!isActive) {
                sendActivationEmails(blog);
            }

            return ResponseEntity.ok(isActive ? "Blog deactivated successfully."
                    : "Blog activated and notifications sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error occurred: " + e.getMessage());
        }
    }

    private void sendActivationEmails(Blog blog) {
        List<Contact> contacts = contactService.getAllContacts();
        if (contacts.isEmpty()) {
            System.out.println("No contacts found to send notifications.");
            return;
        }

        for (Contact contact : contacts) {
            try {
                emailService.sendBlogNotification(contact, blog.getTitle(), blog.getDescription(),
                        "https://ggrouproma.com/blogs/" + blog.getId());
            } catch (MessagingException e) {
                System.err.println("Failed to send email to: " + contact.getEmail() + " - " + e.getMessage());
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable Long id) {
        try {
            Optional<Blog> blogOptional = blogService.getBlogById(id);
            if (blogOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Blog not found with ID: " + id);
            }
            return ResponseEntity.ok(blogOptional.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching blog: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlog(@RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("type") String type,
            @RequestParam("language") String language,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Blog blog = new Blog(title, content, description, author, type, language, LocalDateTime.now(), false);

            if (image != null && !image.isEmpty()) {
                String imageFileName = saveImage(image);
                blog.setImagePath(imageFileName);
            }

            blogService.saveBlog(blog);
            return ResponseEntity.ok(blog);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving image: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Blog>> getBlogs(@RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "type", required = false) String type) {
        try {
            List<Blog> blogs = blogService.getBlogs(language, type);
            return ResponseEntity.ok(blogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlog(@PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("description") String description,
            @RequestParam("author") String author,
            @RequestParam("type") String type,
            @RequestParam("language") String language,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            Optional<Blog> blogOptional = blogService.getBlogById(id);
            if (blogOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Blog not found with ID: " + id);
            }

            Blog blog = blogOptional.get();
            blog.updateBlogDetails(title, content, description, author, type, language);

            if (image != null && !image.isEmpty()) {
                String imageFileName = saveImage(image);
                blog.setImagePath(imageFileName);
            }

            blogService.saveBlog(blog);
            return ResponseEntity.ok(blog);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving image: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long id) {
        try {
            Optional<Blog> blogOptional = blogService.getBlogById(id);
            if (blogOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Blog not found with ID: " + id);
            }

            blogService.deleteBlog(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Blog deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting blog: " + e.getMessage());
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = "./storage/imageBlogs";
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path path = Paths.get(uploadDir, fileName);

        Files.createDirectories(path.getParent());
        Files.copy(image.getInputStream(), path);

        return fileName;
    }
}
