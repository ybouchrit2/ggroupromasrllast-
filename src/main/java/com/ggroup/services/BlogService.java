package com.ggroup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggroup.models.Blog;
import com.ggroup.repositories.BlogRepository;

@Service
public class BlogService {

    @Autowired
    private final BlogRepository blogRepository;

    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public Optional<Blog> getBlogById(Long id) {
        return blogRepository.findById(id);
    }

    public List<Blog> getBlogs(String language, String type) {
        if (language != null && type != null) {
            return blogRepository.findByLanguageAndType(language, type);
        } else if (language != null) {
            return blogRepository.findByLanguage(language);
        } else if (type != null) {
            return blogRepository.findByType(type);
        } else {
            return blogRepository.findAll();
        }
    }

    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
