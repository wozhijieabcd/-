package com.example.blog.mapper;

import com.example.blog.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Category selectById(Long id);

    Category selectByName(String name);

    List<Category> selectAll();

    int countArticlesByCategoryId(Long categoryId);

    int insert(Category category);

    int update(Category category);

    int deleteById(Long id);
}
