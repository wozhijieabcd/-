package com.example.blog.service.impl;

import com.example.blog.entity.Comment;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    @Override
    public List<Comment> findByArticleId(Long articleId) {
        List<Comment> allComments = commentMapper.selectByArticleId(articleId);
        // 组装树形回复：parent_id为null的是顶层评论，其余按parent_id归入对应的replies
        for (Comment comment : allComments) {
            if (comment.getParentId() == null) {
                comment.setReplies(
                        allComments.stream()
                                .filter(c -> comment.getId().equals(c.getParentId()))
                                .toList()
                );
            }
        }
        return allComments.stream()
                .filter(c -> c.getParentId() == null)
                .toList();
    }

    @Override
    public void create(Comment comment) {
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            throw new RuntimeException("评论内容不能为空");
        }
        comment.setStatus(1);
        commentMapper.insert(comment);
    }

    @Override
    public void delete(Long id) {
        commentMapper.deleteById(id);
    }
}
