package com.ggroup.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class JobApplicationCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificationName;
    private String certificationDescription;
    private String certificationIssuer;
    private LocalDate certificationDate;
    private LocalDate expirationDate;
    private String certificationLevel;
    private String certificationCredentialId;
    private String certificationUrl;
    private Boolean isVerified;

    @ManyToOne
    @JoinColumn(name = "job_application_id", nullable = false)
    @JsonBackReference
    private JobApplication jobApplication;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getCertificationDescription() {
        return certificationDescription;
    }

    public void setCertificationDescription(String certificationDescription) {
        this.certificationDescription = certificationDescription;
    }

    public String getCertificationIssuer() {
        return certificationIssuer;
    }

    public void setCertificationIssuer(String certificationIssuer) {
        this.certificationIssuer = certificationIssuer;
    }

    public LocalDate getCertificationDate() {
        return certificationDate;
    }

    public void setCertificationDate(LocalDate certificationDate) {
        this.certificationDate = certificationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCertificationLevel() {
        return certificationLevel;
    }

    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }

    public String getCertificationCredentialId() {
        return certificationCredentialId;
    }

    public void setCertificationCredentialId(String certificationCredentialId) {
        this.certificationCredentialId = certificationCredentialId;
    }

    public String getCertificationUrl() {
        return certificationUrl;
    }

    public void setCertificationUrl(String certificationUrl) {
        this.certificationUrl = certificationUrl;
    }

    public Boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }
}
