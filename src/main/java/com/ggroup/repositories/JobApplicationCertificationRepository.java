package com.ggroup.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ggroup.models.JobApplicationCertification;

public interface JobApplicationCertificationRepository extends JpaRepository<JobApplicationCertification, Long> {
}
