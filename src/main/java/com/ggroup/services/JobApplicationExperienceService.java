package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggroup.models.JobApplicationExperience;
import com.ggroup.repositories.JobApplicationExperienceRepository;

@Service
public class JobApplicationExperienceService {

    @Autowired
    private JobApplicationExperienceRepository jobApplicationExperienceRepository;

    // Create or update a JobApplicationExperience
    public JobApplicationExperience saveJobApplicationExperience(JobApplicationExperience jobApplicationExperience) {
        return jobApplicationExperienceRepository.save(jobApplicationExperience);
    }

    // Get a JobApplicationExperience by ID
    public Optional<JobApplicationExperience> getJobApplicationExperienceById(Long id) {
        return jobApplicationExperienceRepository.findById(id);
    }

    // Get all JobApplicationExperiences
    public List<JobApplicationExperience> getAllJobApplicationExperiences() {
        return jobApplicationExperienceRepository.findAll();
    }

    // Delete a JobApplicationExperience by ID
    public void deleteJobApplicationExperience(Long id) {
        jobApplicationExperienceRepository.deleteById(id);
    }
}
