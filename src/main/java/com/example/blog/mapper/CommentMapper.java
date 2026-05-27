package com.example.blog.mapper;

import com.example.blog.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {

    Comment selectById(Long id);

    List<Comment> selectByArticleId(Long articleId);

    List<Comment> selectByParentId(Long parentId);

    int countByArticleId(Long articleId);

    int insert(Comment comment);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteById(Long id);

    int deleteByArticleId(Long articleId);

    List<Comment> selectAll();

    List<Comment> selectRecent(int limit);
}
