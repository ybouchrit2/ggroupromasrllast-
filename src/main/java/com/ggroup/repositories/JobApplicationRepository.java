package com.ggroup.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ggroup.models.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
   // استعلام لجلب جميع طلبات الوظائف لمستخدم معين
    @Query("SELECT ja FROM JobApplication ja WHERE ja.user.id = :userId")
    List<JobApplication> findAllByUserId(Long userId);
    Optional<JobApplication> findByUserId(Long userId);
}
