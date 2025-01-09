package com.ggroup.filters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSFilter {

    @Bean
    public CorsFilter corsFilter() {
        // إنشاء مصدر تكوين CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        // إعداد CORS
        CorsConfiguration config = new CorsConfiguration();
        
        // السماح بالوصول من هذا الأصل (تأكد من أنه نفس الأصل الذي تعمل عليه الـ Frontend)
        config.addAllowedOrigin("http://localhost:3000");  // واجهة React
        config.addAllowedOrigin("http://localhost:8080");  // واجهة الخادم (إذا كانت تستخدم منفذ آخر)

        // السماح بالطلبات GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.OPTIONS);  // إضافة OPTIONS لتجنب مشكلات CORS

        // السماح بالترويسات مثل Content-Type و Authorization و Content-Disposition
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Disposition"); // لتحديد الملف
        config.addAllowedHeader("Accept");
        
        // السماح بالـ Credentials إذا كانت هناك حاجة
        config.setAllowCredentials(true);
        
        // تطبيق الإعدادات على جميع المسارات
        source.registerCorsConfiguration("/**", config);
        
        // إرجاع كائن CORSFilter مع الإعدادات
        return new CorsFilter(source);
    }
}
