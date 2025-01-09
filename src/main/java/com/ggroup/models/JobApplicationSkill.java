package com.ggroup.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIgnoreProperties({"jobApplication"})
public class JobApplicationSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String skillName;
    private String skillDescription;
    private String skillCategory;
    private String skillLevel;
    private Integer proficiencyPercentage;
    private Integer experienceYears;
    private Boolean certified;
    private LocalDate lastUsed;
    private Boolean isPrimarySkill;

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

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    public Integer getProficiencyPercentage() {
        return proficiencyPercentage;
    }

    public void setProficiencyPercentage(Integer proficiencyPercentage) {
        this.proficiencyPercentage = proficiencyPercentage;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(Integer experienceYears) {
        this.experienceYears = experienceYears;
    }

    public Boolean getCertified() {
        return certified;
    }

    public void setCertified(Boolean certified) {
        this.certified = certified;
    }

    public LocalDate getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDate lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Boolean getIsPrimarySkill() {
        return isPrimarySkill;
    }

    public void setIsPrimarySkill(Boolean isPrimarySkill) {
        this.isPrimarySkill = isPrimarySkill;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }

    public void setJobApplication(JobApplication jobApplication) {
        this.jobApplication = jobApplication;
    }
}
