package com.ggroup.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ggroup.models.Blog;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByLanguage(String language);
    List<Blog> findByType(String type);
    List<Blog> findByLanguageAndType(String language, String type);
}
