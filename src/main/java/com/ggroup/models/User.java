package com.ggroup.models;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // إزالة @NotEmpty و @Size

    private String email;  // إزالة @Email و @NotEmpty

    private String password;  // إزالة @NotEmpty و @Size

    private String phone;  // إزالة @Pattern

    private String role = "ROLE_USER"; // القيمة الافتراضية للدور

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<JobApplication> jobApplications;

    // حقول لتخزين تاريخ الإنشاء والتحديث
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // دالة تعيين تاريخ الإنشاء عند إضافة الكائن إلى قاعدة البيانات
    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // دالة تعيين تاريخ التحديث عند تحديث الكائن
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<JobApplication> getJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(Set<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
