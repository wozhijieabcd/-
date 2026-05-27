package com.example.blog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private Long categoryId;
    private Long userId;
    private Integer status;
    private Integer viewCount;
    private Integer isTop;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 关联对象（非数据库字段，按需查询填充）
    private Category category;
    private User user;
}
