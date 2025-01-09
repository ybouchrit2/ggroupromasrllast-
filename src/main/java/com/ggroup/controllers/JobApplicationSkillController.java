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

import com.ggroup.models.JobApplicationSkill;
import com.ggroup.services.JobApplicationSkillService;

@RestController
@RequestMapping("/api/job-application-skills")
public class JobApplicationSkillController {

    @Autowired
    private JobApplicationSkillService jobApplicationSkillService;

    // Create or Update Job Application Skill
    @PostMapping
    public ResponseEntity<JobApplicationSkill> createOrUpdateJobApplicationSkill(@RequestBody JobApplicationSkill jobApplicationSkill) {
        JobApplicationSkill savedJobApplicationSkill = jobApplicationSkillService.saveJobApplicationSkill(jobApplicationSkill);
        return new ResponseEntity<>(savedJobApplicationSkill, HttpStatus.CREATED);
    }

    // Get Job Application Skill by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationSkill> getJobApplicationSkillById(@PathVariable Long id) {
        Optional<JobApplicationSkill> jobApplicationSkill = jobApplicationSkillService.getJobApplicationSkillById(id);
        return jobApplicationSkill.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get all Job Application Skills
    @GetMapping
    public ResponseEntity<List<JobApplicationSkill>> getAllJobApplicationSkills() {
        List<JobApplicationSkill> jobApplicationSkills = jobApplicationSkillService.getAllJobApplicationSkills();
        return new ResponseEntity<>(jobApplicationSkills, HttpStatus.OK);
    }

    // Delete Job Application Skill by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplicationSkill(@PathVariable Long id) {
        jobApplicationSkillService.deleteJobApplicationSkill(id);
        return ResponseEntity.noContent().build();
    }
}
