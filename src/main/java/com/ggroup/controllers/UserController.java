package com.ggroup.controllers;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ggroup.models.User;
import com.ggroup.responses.ErrorResponse;
import com.ggroup.services.UserService;
import com.ggroup.utils.JwtUtil;
import com.ggroup.utils.PasswordUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // إضافة خدمة JwtUtil لتوليد التوكن

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    // تسجيل مستخدم جديد
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            // تحقق من وجود البريد الإلكتروني مسبقًا
            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("البريد الإلكتروني هذا مسجل بالفعل.");
            }

            // تعيين role بشكل افتراضي بناءً على عدد المستخدمين في قاعدة البيانات
            if (userService.getAllUsers().spliterator().getExactSizeIfKnown() == 0) {
                user.setRole("ROLE_ADMIN"); // تعيين role = ROLE_ADMIN لأول مستخدم
            } else {
                user.setRole("ROLE_USER"); // تعيين role = ROLE_USER لبقية المستخدمين
            }

            String encryptedPassword = PasswordUtil.hashPassword(user.getPassword()); // تشفير كلمة المرور
            user.setPassword(encryptedPassword); // تعيين كلمة المرور المشفرة

            userService.saveUser(user); // حفظ المستخدم مباشرة
            return ResponseEntity.status(HttpStatus.CREATED).body("تم إنشاء المستخدم بنجاح");

        } catch (Exception e) {
            log.error("حدث خطأ أثناء إنشاء المستخدم: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("حدث خطأ أثناء إنشاء المستخدم: " + e.getMessage());
        }
    }

    // تسجيل الدخول
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody User user) {
        try {
            log.info("استلام طلب تسجيل الدخول: {}", user.getEmail());

            // العثور على المستخدم باستخدام البريد الإلكتروني
            User foundUser = userService.getUserByEmail(user.getEmail()).orElse(null);
            if (foundUser == null) {
                log.warn("المستخدم غير موجود: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("المستخدم غير موجود"));
            }

            // التحقق من كلمة المرور
            if (!PasswordUtil.checkPassword(user.getPassword(), foundUser.getPassword())) {
                log.warn("كلمة المرور غير صحيحة للمستخدم: {}", user.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("كلمة المرور غير صحيحة"));
            }

            // توليد توكن JWT عند تسجيل الدخول بنجاح
            String token = jwtUtil.generateJwt(foundUser.getEmail(), foundUser.getId()); // استخدام email و userId
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            log.error("حدث خطأ أثناء تسجيل الدخول: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("حدث خطأ أثناء تسجيل الدخول، يُرجى المحاولة لاحقًا."));
        }
    }

    // الحصول على مستخدم بالـ ID مع التحقق من الدور
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("التوكن غير موجود أو غير صالح"));
            }

            // استخراج البريد الإلكتروني من التوكن
            String email = jwtUtil.extractUsername(token.substring(7)); // إزالة "Bearer " من التوكن
            Optional<User> currentUserOpt = userService.getUserByEmail(email);

            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                // إذا كان المستخدم من نوع ROLE_ADMIN، يمكنه الوصول إلى أي مستخدم
                // أما إذا كان من نوع ROLE_USER، يمكنه فقط الوصول إلى بياناته الشخصية
                if ("ROLE_ADMIN".equals(currentUser.getRole()) || currentUser.getId().equals(id)) {
                    Optional<User> userOptional = userService.getUserById(id);
                    if (userOptional.isPresent()) {
                        return ResponseEntity.ok(userOptional.get()); // إرجاع المستخدم في حالة النجاح
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(new ErrorResponse("المستخدم غير موجود"));
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("ليس لديك إذن للوصول إلى بيانات هذا المستخدم"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("المستخدم غير موجود"));
            }
        } catch (Exception e) {
            log.error("حدث خطأ أثناء جلب المستخدم: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("حدث خطأ أثناء جلب المستخدم"));
        }
    }

    // الحصول على جميع المستخدمين (محدود للمسؤولين فقط)
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("التوكن غير موجود أو غير صالح"));
            }

            // استخراج البريد الإلكتروني من التوكن
            String email = jwtUtil.extractUsername(token.substring(7)); // إزالة "Bearer " من التوكن
            Optional<User> currentUserOpt = userService.getUserByEmail(email);

            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                if ("ROLE_ADMIN".equals(currentUser.getRole())) {
                    Iterable<User> users = userService.getAllUsers();
                    return ResponseEntity.ok(users); // إرجاع المستخدمين في حالة النجاح
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("ليس لديك إذن للوصول إلى هذه البيانات"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("المستخدم غير موجود"));
            }
        } catch (Exception e) {
            log.error("حدث خطأ أثناء جلب المستخدمين: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("حدث خطأ أثناء جلب المستخدمين"));
        }
    }

    // تحديث بيانات مستخدم (التحقق من الدور)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse("التوكن غير موجود أو غير صالح"));
            }

            // استخراج البريد الإلكتروني من التوكن
            String email = jwtUtil.extractUsername(token.substring(7)); // إزالة "Bearer " من التوكن
            Optional<User> currentUserOpt = userService.getUserByEmail(email);

            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                if ("ROLE_ADMIN".equals(currentUser.getRole()) || currentUser.getId().equals(id)) {
                    Optional<User> userOptional = userService.getUserById(id);
                    if (userOptional.isPresent()) {
                        User existingUser = userOptional.get();

                        // تحقق من تحديث البريد الإلكتروني فقط إذا كان جديدًا
                        if (!existingUser.getEmail().equals(user.getEmail())
                                && userService.getUserByEmail(user.getEmail()).isPresent()) {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                    .body(new ErrorResponse("البريد الإلكتروني هذا مسجل بالفعل."));
                        }

                        existingUser.setName(user.getName());
                        existingUser.setEmail(user.getEmail());
                        userService.saveUser(existingUser);
                        return ResponseEntity.ok("تم تحديث البيانات بنجاح");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("المستخدم غير موجود");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("ليس لديك إذن لتحديث بيانات هذا المستخدم"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("المستخدم غير موجود");
            }
        } catch (Exception e) {
            log.error("حدث خطأ أثناء تحديث البيانات: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("حدث خطأ أثناء تحديث البيانات");
        }
    }

    // حذف مستخدم (التحقق من الدور)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            // استخراج البريد الإلكتروني من التوكن
            String email = jwtUtil.extractUsername(token.substring(7)); // إزالة "Bearer " من التوكن
            Optional<User> currentUserOpt = userService.getUserByEmail(email);

            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                if ("ROLE_ADMIN".equals(currentUser.getRole()) || currentUser.getId().equals(id)) {
                    userService.deleteUser(id);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse("ليس لديك إذن لحذف هذا المستخدم"));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("المستخدم غير موجود"));
            }
        } catch (Exception e) {
            log.error("حدث خطأ أثناء حذف المستخدم: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("حدث خطأ أثناء حذف المستخدم"));
        }
    }

    // تغيير كلمة المرور (التحقق من الدور)
    @PutMapping("/{id}/change-password")
    public ResponseEntity<String> changePassword(@PathVariable Long id, @RequestBody Map<String, String> passwords,
            @RequestHeader("Authorization") String token) {
        try {
            // استخراج البريد الإلكتروني من التوكن
            String email = jwtUtil.extractUsername(token.substring(7)); // إزالة "Bearer " من التوكن
            Optional<User> currentUserOpt = userService.getUserByEmail(email);

            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                // التحقق من أن المستخدم يمكنه تغيير كلمة مروره أو إذا كان ADMIN
                if ("ROLE_ADMIN".equals(currentUser.getRole()) || currentUser.getId().equals(id)) {
                    Optional<User> userOptional = userService.getUserById(id);
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        String newPassword = passwords.get("newPassword");

                        // تغيير كلمة المرور مباشرة
                        String encryptedPassword = PasswordUtil.hashPassword(newPassword);
                        user.setPassword(encryptedPassword);
                        userService.saveUser(user);
                        return ResponseEntity.ok("تم تغيير كلمة المرور بنجاح");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("المستخدم غير موجود");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("ليس لديك إذن لتغيير كلمة مرور هذا المستخدم");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("المستخدم غير موجود");
            }
        } catch (Exception e) {
            log.error("حدث خطأ أثناء تغيير كلمة المرور: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("حدث خطأ أثناء تغيير كلمة المرور");
        }
    }
}
