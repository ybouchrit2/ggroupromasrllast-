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

import com.ggroup.models.JobApplicationCertification;
import com.ggroup.services.JobApplicationCertificationService;

@RestController
@RequestMapping("/api/job-application-certifications")
public class JobApplicationCertificationController {

    @Autowired
    private JobApplicationCertificationService jobApplicationCertificationService;

    // Create or Update Job Application Certification
    @PostMapping
    public ResponseEntity<JobApplicationCertification> createOrUpdateJobApplicationCertification(@RequestBody JobApplicationCertification jobApplicationCertification) {
        JobApplicationCertification savedJobApplicationCertification = jobApplicationCertificationService.saveJobApplicationCertification(jobApplicationCertification);
        return new ResponseEntity<>(savedJobApplicationCertification, HttpStatus.CREATED);
    }

    // Get Job Application Certification by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationCertification> getJobApplicationCertificationById(@PathVariable Long id) {
        Optional<JobApplicationCertification> jobApplicationCertification = jobApplicationCertificationService.getJobApplicationCertificationById(id);
        return jobApplicationCertification.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all Job Application Certifications
    @GetMapping
    public ResponseEntity<List<JobApplicationCertification>> getAllJobApplicationCertifications() {
        List<JobApplicationCertification> jobApplicationCertifications = jobApplicationCertificationService.getAllJobApplicationCertifications();
        return new ResponseEntity<>(jobApplicationCertifications, HttpStatus.OK);
    }

    // Delete Job Application Certification by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplicationCertification(@PathVariable Long id) {
        jobApplicationCertificationService.deleteJobApplicationCertification(id);
        return ResponseEntity.noContent().build();
    }
}
