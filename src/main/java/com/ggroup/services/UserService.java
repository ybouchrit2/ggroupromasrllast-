package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggroup.models.User;
import com.ggroup.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // حفظ المستخدم بدون أي حماية
    public User saveUser(User user) {
        // لا يتم تشفير كلمة المرور ولا يوجد تحقق من المدخلات
        return userRepository.save(user);
    }

    // العثور على مستخدم باستخدام البريد الإلكتروني
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email); // لا يوجد تحقق من صحة البريد الإلكتروني
    }

    // العثور على مستخدم باستخدام المعرف
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id); // لا يوجد تحقق من صحة المعرف
    }

    // الحصول على جميع المستخدمين
    public List<User> getAllUsers() {
        return userRepository.findAll(); // لا تحقق من صلاحيات أو دور المستخدم
    }

    // تحديث بيانات المستخدم
    @Transactional
    public Optional<User> updateUser(Long id, User user) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPhone(user.getPhone());
            userRepository.save(updatedUser);
            return Optional.of(updatedUser);
        } else {
            return Optional.empty(); // لا تحقق من صلاحيات أو وجود المستخدم
        }
    }

    // حذف المستخدم باستخدام المعرف
    public void deleteUser(Long id) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id); // لا تحقق من وجود المستخدم
        } else {
            throw new IllegalArgumentException("المستخدم غير موجود."); // لا تحقق من الصلاحيات
        }
    }

    // تغيير كلمة المرور بدون التحقق من كلمة المرور الحالية
    public void changePassword(Long id, String currentPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // فقط تغيير كلمة المرور بدون التحقق من الحالية
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("المستخدم غير موجود.");
        }
    }

    // الحصول على المستخدم مع طلبات الوظائف
    public User getUserWithJobApplications(Long userId) {
        return userRepository.findUserWithJobApplications(userId); // لا تحقق من صلاحيات
    }
}
