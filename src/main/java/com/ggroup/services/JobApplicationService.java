package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggroup.models.JobApplication;
import com.ggroup.repositories.JobApplicationRepository;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    // Get all job applications
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    // Get a job application by ID
    public Optional<JobApplication> getJobApplicationById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    // Create a new job application
    public JobApplication createJobApplication(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }

    // Update a job application
    public JobApplication updateJobApplication(Long id, JobApplication jobApplication) {
        jobApplication.setId(id);
        return jobApplicationRepository.save(jobApplication);
    }

    // Delete a job application
    public void deleteJobApplication(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    // الحصول على جميع طلبات الوظائف لمستخدم معين
    public List<JobApplication> getJobApplicationsByUserId(Long userId) {
        return jobApplicationRepository.findAllByUserId(userId);
    }

    // في خدمة JobApplicationService
    public Optional<JobApplication> getJobApplicationByUserId(Long userId) {
        return jobApplicationRepository.findByUserId(userId);
    }

}
