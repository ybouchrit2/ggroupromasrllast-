package com.ggroup.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    // منشئ لـ BCryptPasswordEncoder
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // تشفير كلمة المرور
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);  // تشفير كلمة المرور باستخدام BCrypt
    }

    // التحقق من كلمة المرور المدخلة
    public static boolean checkPassword(String plainPassword, String encryptedPassword) {
        return passwordEncoder.matches(plainPassword, encryptedPassword);  // مقارنة كلمة المرور المدخلة مع المشفرة
    }
}
