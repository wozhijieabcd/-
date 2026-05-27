package com.example.blog.service;

import com.example.blog.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findByArticleId(Long articleId);

    void create(Comment comment);

    void delete(Long id);
}
