package com.ggroup.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // تحميل SECRET_KEY من ملف الإعدادات
    @Value("${jwt.secret-key}")
    private String secretKey;

    // تحويل secretKey إلى Key
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes()); // تحويل string إلى Key
    }

    // استخراج البريد الإلكتروني (بدلاً من username)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // نستخدم البريد الإلكتروني
    }

    public Long extractUserId(String token) {
        return extractClaim(token, claims -> claims.get("userId", Long.class)); // استخراج userId
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())  // استخدام المفتاح من `getSecretKey()`
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalArgumentException("توكن غير صالح أو منتهي الصلاحية", e);
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // توليد JWT باستخدام البريد الإلكتروني و userId
    public String generateJwt(String email, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // إضافة userId إلى claims
        return createToken(claims, email);  // البريد الإلكتروني سيكون subject
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)  // استخدام البريد الإلكتروني هنا
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 ساعات
                .signWith(getSecretKey()) // استخدام `getSecretKey()`
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String email = extractUsername(token); // نستخدم البريد الإلكتروني
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));  // التأكد من التوكن والبريد الإلكتروني
    }
}
