package com.example.blog.service.impl;

import com.example.blog.common.PageResult;
import com.example.blog.entity.Article;
import com.example.blog.mapper.ArticleMapper;
import com.example.blog.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;

    @Override
    public Article findById(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public PageResult<Article> findPage(int page, int size, Long categoryId, Integer status, String keyword) {
        int offset = (page - 1) * size;
        List<Article> records = articleMapper.selectPage(offset, size, categoryId, status, keyword);
        int total = articleMapper.count(categoryId, status, keyword);
        return new PageResult<>(records, total, page, size);
    }

    @Override
    public List<Article> findTop(int limit) {
        return articleMapper.selectTop(limit);
    }

    @Override
    public void createOrUpdate(Article article) {
        if (article.getSummary() == null || article.getSummary().isBlank()) {
            String plainText = article.getContent().replaceAll("<[^>]+>", "").replaceAll("\\s+", " ");
            article.setSummary(plainText.length() > 200 ? plainText.substring(0, 200) : plainText);
        }
        if (article.getStatus() == null) {
            article.setStatus(0);
        }
        if (article.getIsTop() == null) {
            article.setIsTop(0);
        }
        if (article.getId() == null) {
            article.setViewCount(0);
            articleMapper.insert(article);
        } else {
            articleMapper.update(article);
        }
    }

    @Override
    public void publish(Long id) {
        articleMapper.updateStatus(id, 1);
    }

    @Override
    public void withdraw(Long id) {
        articleMapper.updateStatus(id, 0);
    }

    @Override
    public void delete(Long id) {
        articleMapper.deleteById(id);
    }

    @Override
    public void incrementViews(Long id) {
        articleMapper.updateViewCount(id);
    }
}
