package com.ggroup.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ggroup.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // البحث عن المستخدم بواسطة البريد الإلكتروني
    Optional<User> findByEmail(String email);

    

    // التأكد من وجود مستخدم بنفس البريد الإلكتروني
    boolean existsByEmail(String email);

    // استعلام لجلب بيانات المستخدم وجميع طلبات الوظائف
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.jobApplications WHERE u.id = :userId")
    User findUserWithJobApplications(@Param("userId") Long userId);

}
