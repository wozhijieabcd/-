package com.example.blog.service.impl;

import com.example.blog.entity.Category;
import com.example.blog.mapper.CategoryMapper;
import com.example.blog.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public void create(Category category) {
        Category exist = categoryMapper.selectByName(category.getName());
        if (exist != null) {
            throw new RuntimeException("分类名称已存在");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        categoryMapper.insert(category);
    }

    @Override
    public void update(Category category) {
        Category exist = categoryMapper.selectById(category.getId());
        if (exist == null) {
            throw new RuntimeException("分类不存在");
        }
        Category sameName = categoryMapper.selectByName(category.getName());
        if (sameName != null && !sameName.getId().equals(category.getId())) {
            throw new RuntimeException("分类名称已存在");
        }
        exist.setName(category.getName());
        exist.setDescription(category.getDescription());
        exist.setSortOrder(category.getSortOrder());
        categoryMapper.update(exist);
    }

    @Override
    public void delete(Long id) {
        int articleCount = categoryMapper.countArticlesByCategoryId(id);
        if (articleCount > 0) {
            throw new RuntimeException("该分类下还有 " + articleCount + " 篇文章，无法删除");
        }
        categoryMapper.deleteById(id);
    }
}
