package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggroup.models.JobApplicationCertification;
import com.ggroup.repositories.JobApplicationCertificationRepository;

@Service
public class JobApplicationCertificationService {

    @Autowired
    private JobApplicationCertificationRepository jobApplicationCertificationRepository;

    // Create or update a JobApplicationCertification
    public JobApplicationCertification saveJobApplicationCertification(JobApplicationCertification jobApplicationCertification) {
        return jobApplicationCertificationRepository.save(jobApplicationCertification);
    }

    // Get a JobApplicationCertification by ID
    public Optional<JobApplicationCertification> getJobApplicationCertificationById(Long id) {
        return jobApplicationCertificationRepository.findById(id);
    }

    // Get all JobApplicationCertifications
    public List<JobApplicationCertification> getAllJobApplicationCertifications() {
        return jobApplicationCertificationRepository.findAll();
    }

    // Delete a JobApplicationCertification by ID
    public void deleteJobApplicationCertification(Long id) {
        jobApplicationCertificationRepository.deleteById(id);
    }
}
