package com.example.blog.service;

import com.example.blog.common.PageResult;
import com.example.blog.entity.Article;

import java.util.List;

public interface ArticleService {

    Article findById(Long id);

    PageResult<Article> findPage(int page, int size, Long categoryId, Integer status, String keyword);

    List<Article> findTop(int limit);

    void createOrUpdate(Article article);

    void publish(Long id);

    void withdraw(Long id);

    void delete(Long id);

    void incrementViews(Long id);
}
