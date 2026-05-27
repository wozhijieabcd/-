package com.example.blog.controller;

import com.example.blog.common.PageResult;
import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.Comment;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(required = false) Long categoryId,
                        Model model) {
        PageResult<Article> articlePage = articleService.findPage(page, size, categoryId, 1, null);
        List<Category> categories = categoryService.findAll();
        List<Article> topArticles = articleService.findTop(5);

        model.addAttribute("articlePage", articlePage);
        model.addAttribute("categories", categories);
        model.addAttribute("topArticles", topArticles);
        model.addAttribute("currentCategoryId", categoryId);
        return "home/index";
    }

    @GetMapping("/article/{id}")
    public String article(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        if (article == null || article.getStatus() != 1) {
            return "error/404";
        }
        articleService.incrementViews(id);
        article.setViewCount(article.getViewCount() + 1);

        List<Comment> comments = commentService.findByArticleId(id);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        model.addAttribute("categories", categories);
        return "home/article";
    }
}
