package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggroup.models.JobApplicationSkill;
import com.ggroup.repositories.JobApplicationSkillRepository;

@Service
public class JobApplicationSkillService {

    @Autowired
    private JobApplicationSkillRepository jobApplicationSkillRepository;

    // Create or update a JobApplicationSkill
    public JobApplicationSkill saveJobApplicationSkill(JobApplicationSkill jobApplicationSkill) {
        return jobApplicationSkillRepository.save(jobApplicationSkill);
    }

    // Get a JobApplicationSkill by ID
    public Optional<JobApplicationSkill> getJobApplicationSkillById(Long id) {
        return jobApplicationSkillRepository.findById(id);
    }

    // Get all JobApplicationSkills
    public List<JobApplicationSkill> getAllJobApplicationSkills() {
        return jobApplicationSkillRepository.findAll();
    }

    // Delete a JobApplicationSkill by ID
    public void deleteJobApplicationSkill(Long id) {
        jobApplicationSkillRepository.deleteById(id);
    }
}
