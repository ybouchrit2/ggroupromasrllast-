package com.ggroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.ggroup.repositories") // تأكد من أن الحزمة تحتوي على الـ Repositories
@EntityScan(basePackages = "com.ggroup.models") // تأكد من أن الحزمة تحتوي على الـ Entities
public class GgroupApplication {

    public static void main(String[] args) {
        SpringApplication.run(GgroupApplication.class, args);
    }
}
