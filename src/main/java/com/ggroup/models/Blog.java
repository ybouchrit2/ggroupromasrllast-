package com.ggroup.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "blogs")
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Content is required")
    private String content;

    @NotNull(message = "Description is required") // إضافة الوصف مع التحقق
    private String description;

    @NotNull(message = "Author is required") // إضافة المؤلف مع التحقق
    private String author;

    @NotNull(message = "Type is required") // إضافة النوع مع التحقق
    private String type;

    private String language;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private String imagePath; // الإبقاء على خاصية مسار الصورة

    private boolean active; // إضافة خاصية التفعيل (active)

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Constructor بدون معلمات (لـ JPA)
    public Blog() {}

    // Constructor مع المعلمات
    public Blog(String title, String content, String description, String author, String type, String language, LocalDateTime createdAt, boolean active) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.author = author;
        this.type = type;
        this.language = language;
        this.createdAt = createdAt;
        this.active = active;
    }

    // دالة لتحديث تفاصيل المدونة
    public void updateBlogDetails(String title, String content, String description, String author, String type, String language) {
        this.title = title;
        this.content = content;
        this.description = description;
        this.author = author;
        this.type = type;
        this.language = language;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Getter and Setter for active
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
