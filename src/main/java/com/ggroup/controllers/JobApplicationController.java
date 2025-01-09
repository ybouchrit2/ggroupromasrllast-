package com.ggroup.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ggroup.models.JobApplication;
import com.ggroup.models.User;
import com.ggroup.services.JobApplicationService;
import com.ggroup.services.UserService;

@RestController
@RequestMapping("/api/jobapplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @Autowired
    private UserService userService;

    // Endpoint to get job application by userId
    @GetMapping("/user/{userId}")
    public ResponseEntity<JobApplication> getJobApplicationByUserId(@PathVariable Long userId) {
        return jobApplicationService.getJobApplicationByUserId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    // Endpoint to create or update a job application (with resume and profile image
    // upload)
    @PostMapping("/create")
    public ResponseEntity<?> createOrUpdateJobApplication(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam("userId") Long userId) {
        try {
            // التحقق من وجود المستخدم
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "المستخدم غير موجود"));

            // التحقق من وجود طلب وظيفة سابق للمستخدم
            JobApplication jobApplication = jobApplicationService
                    .getJobApplicationByUserId(userId).orElse(new JobApplication());

            // التعامل مع الملفات
            handleOldFiles(jobApplication);

            String resumePath = processFile(file, userId, "resumes");
            String profileImagePath = processFile(profileImage, userId, "profile-images");

            // تحديث طلب الوظيفة
            jobApplication.setResumePath(resumePath);
            jobApplication.setProfileImagePath(profileImagePath);
            jobApplication.setUser(user);

            jobApplicationService.createJobApplication(jobApplication);

            return ResponseEntity.ok(jobApplication);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("خطأ في تحميل الملفات: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("حدث خطأ غير متوقع: " + e.getMessage());
        }
    }

    // Helper method to process files
    private String processFile(MultipartFile file, Long userId, String directoryName) throws IOException {
        if (file != null && !file.isEmpty()) {
            return saveFile(file, userId, directoryName);
        }
        return null;
    }

    // Helper method to save any file
    private String saveFile(MultipartFile file, Long userId, String directoryName) throws IOException {
        String directory = "storage/" + directoryName + "/";
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + userId + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(directory, fileName);
        Files.write(filePath, file.getBytes());

        return "/assets/" + directoryName + "/" + fileName;
    }

    // Helper method to handle old files (delete if exists)
    private void handleOldFiles(JobApplication jobApplication) {
        deleteFileIfExists(jobApplication.getResumePath());
        deleteFileIfExists(jobApplication.getProfileImagePath());
    }

    // Helper method to delete files if they exist
    private void deleteFileIfExists(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            File file = new File("storage" + filePath);
            if (file.exists() && !file.delete()) {
                System.out.println("فشل في حذف الملف: " + file.getAbsolutePath());
            }
        }
    }

    // Endpoint to delete specific file (resume or profile image) based on fileType
    // Endpoint to delete resume file by userId
    @DeleteMapping("/delete-resume/{userId}")
    public ResponseEntity<?> deleteResume(@PathVariable Long userId) {
        try {
            Optional<JobApplication> jobApplicationOpt = jobApplicationService.getJobApplicationByUserId(userId);
            if (!jobApplicationOpt.isPresent()) {
                return ResponseEntity.status(404).body("طلب الوظيفة غير موجود.");
            }

            JobApplication jobApplication = jobApplicationOpt.get();
            String filePath = jobApplication.getResumePath();

            if (filePath != null && !filePath.isEmpty()) {
                // حذف السيرة الذاتية
                File file = new File("storage" + filePath);
                if (file.exists() && file.delete()) {
                    jobApplication.setResumePath(null); // تعيين المسار إلى null
                    jobApplicationService.createJobApplication(jobApplication);
                    return ResponseEntity.ok("تم حذف السيرة الذاتية بنجاح.");
                } else {
                    return ResponseEntity.status(500).body("فشل في حذف السيرة الذاتية.");
                }
            } else {
                return ResponseEntity.status(404).body("السيرة الذاتية غير موجودة.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("خطأ في حذف السيرة الذاتية: " + e.getMessage());
        }
    }

    // Endpoint to delete profile image file by userId
    @DeleteMapping("/delete-profile-image/{userId}")
    public ResponseEntity<?> deleteProfileImage(@PathVariable Long userId) {
        try {
            Optional<JobApplication> jobApplicationOpt = jobApplicationService.getJobApplicationByUserId(userId);
            if (!jobApplicationOpt.isPresent()) {
                return ResponseEntity.status(404).body("طلب الوظيفة غير موجود.");
            }

            JobApplication jobApplication = jobApplicationOpt.get();
            String filePath = jobApplication.getProfileImagePath();

            if (filePath != null && !filePath.isEmpty()) {
                // حذف صورة الملف الشخصي
                File file = new File("storage" + filePath);
                if (file.exists() && file.delete()) {
                    jobApplication.setProfileImagePath(null); // تعيين المسار إلى null
                    jobApplicationService.createJobApplication(jobApplication);
                    return ResponseEntity.ok("تم حذف صورة الملف الشخصي بنجاح.");
                } else {
                    return ResponseEntity.status(500).body("فشل في حذف صورة الملف الشخصي.");
                }
            } else {
                return ResponseEntity.status(404).body("صورة الملف الشخصي غير موجودة.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("خطأ في حذف صورة الملف الشخصي: " + e.getMessage());
        }
    }

}
