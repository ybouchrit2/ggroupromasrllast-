package com.ggroup.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggroup.models.JobApplicationExperience;
import com.ggroup.services.JobApplicationExperienceService;

@RestController
@RequestMapping("/api/job-application-experiences")
public class JobApplicationExperienceController {

    @Autowired
    private JobApplicationExperienceService jobApplicationExperienceService;

    // Create or Update Job Application Experience
    @PostMapping
    public ResponseEntity<JobApplicationExperience> createOrUpdateJobApplicationExperience(@RequestBody JobApplicationExperience jobApplicationExperience) {
        JobApplicationExperience savedJobApplicationExperience = jobApplicationExperienceService.saveJobApplicationExperience(jobApplicationExperience);
        return new ResponseEntity<>(savedJobApplicationExperience, HttpStatus.CREATED);
    }

    // Get Job Application Experience by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationExperience> getJobApplicationExperienceById(@PathVariable Long id) {
        Optional<JobApplicationExperience> jobApplicationExperience = jobApplicationExperienceService.getJobApplicationExperienceById(id);
        return jobApplicationExperience.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all Job Application Experiences
    @GetMapping
    public ResponseEntity<List<JobApplicationExperience>> getAllJobApplicationExperiences() {
        List<JobApplicationExperience> jobApplicationExperiences = jobApplicationExperienceService.getAllJobApplicationExperiences();
        return new ResponseEntity<>(jobApplicationExperiences, HttpStatus.OK);
    }

    // Delete Job Application Experience by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplicationExperience(@PathVariable Long id) {
        jobApplicationExperienceService.deleteJobApplicationExperience(id);
        return ResponseEntity.noContent().build();
    }
}
