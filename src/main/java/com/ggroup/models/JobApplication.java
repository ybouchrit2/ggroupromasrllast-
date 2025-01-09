package com.ggroup.models;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // العلاقة ManyToOne مع User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference  // منع تسلسل الكائن User هنا
    private User user;

    // مجموعة المهارات المرتبطة بالطلب الوظيفي
    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplicationSkill> jobApplicationSkills;

    // مجموعة الخبرات المرتبطة بالطلب الوظيفي
    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplicationExperience> jobApplicationExperiences;

    // مجموعة الشهادات المرتبطة بالطلب الوظيفي
    @OneToMany(mappedBy = "jobApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JobApplicationCertification> jobApplicationCertifications;

    // مسار السيرة الذاتية
    private String resumePath;

    // مسار صورة الملف الشخصي
    private String profileImagePath;

    // تاريخ الإنشاء (لن يمكن تغييره بعد الإنشاء)
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    // تاريخ التحديث
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<JobApplicationSkill> getJobApplicationSkills() {
        return jobApplicationSkills;
    }

    public void setJobApplicationSkills(Set<JobApplicationSkill> jobApplicationSkills) {
        this.jobApplicationSkills = jobApplicationSkills;
    }

    public Set<JobApplicationExperience> getJobApplicationExperiences() {
        return jobApplicationExperiences;
    }

    public void setJobApplicationExperiences(Set<JobApplicationExperience> jobApplicationExperiences) {
        this.jobApplicationExperiences = jobApplicationExperiences;
    }

    public Set<JobApplicationCertification> getJobApplicationCertifications() {
        return jobApplicationCertifications;
    }

    public void setJobApplicationCertifications(Set<JobApplicationCertification> jobApplicationCertifications) {
        this.jobApplicationCertifications = jobApplicationCertifications;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
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

    // تعيين الحقول تلقائيًا عند إنشاء الكائن
    @PrePersist
    public void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // تعيين الحقل updatedAt تلقائيًا عند تحديث الكائن
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Date();
    }
}
