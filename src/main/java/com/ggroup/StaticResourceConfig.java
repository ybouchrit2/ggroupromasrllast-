package com.ggroup; // الحزمة التي تحتوي على تطبيقك الرئيسي

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
    // تحديد المسار الذي سيستخدم للوصول إلى الملفات
    registry.addResourceHandler("/assets/**") // هذا هو المسار الذي سيتم الوصول من خلاله إلى الملفات
        .addResourceLocations("file:storage/"); // المسار الفعلي للملفات على الخادم
  }
}
