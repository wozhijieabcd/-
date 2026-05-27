package com.example.blog.mapper;

import com.example.blog.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    Article selectById(Long id);

    List<Article> selectPage(@Param("offset") int offset,
                             @Param("limit") int limit,
                             @Param("categoryId") Long categoryId,
                             @Param("status") Integer status,
                             @Param("keyword") String keyword);

    int count(@Param("categoryId") Long categoryId,
              @Param("status") Integer status,
              @Param("keyword") String keyword);

    List<Article> selectTop(int limit);

    List<Article> selectByCategoryId(Long categoryId);

    int updateViewCount(Long id);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int insert(Article article);

    int update(Article article);

    int deleteById(Long id);
}
