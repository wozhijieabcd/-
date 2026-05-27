package com.example.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long id;
    private Long articleId;
    private Long userId;
    private Long parentId;
    private String content;
    private Integer status;
    private LocalDateTime createdAt;

    // 关联对象（非数据库字段）
    private User user;
    private List<Comment> replies;
}
