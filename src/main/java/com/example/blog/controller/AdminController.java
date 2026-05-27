package com.example.blog.controller;

import com.example.blog.common.PageResult;
import com.example.blog.entity.Article;
import com.example.blog.entity.Category;
import com.example.blog.entity.Comment;
import com.example.blog.mapper.CommentMapper;
import com.example.blog.security.UserPrincipal;
import com.example.blog.service.ArticleService;
import com.example.blog.service.CategoryService;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @GetMapping({"/dashboard", "/", ""})
    public String dashboard(Model model) {
        int totalArticles = articleService.findPage(1, 1, null, null, null).getTotal();
        int publishedCount = articleService.findPage(1, 1, null, 1, null).getTotal();
        int draftCount = totalArticles - publishedCount;
        int totalCategories = categoryService.findAll().size();
        List<Comment> recentComments = commentMapper.selectRecent(5);

        model.addAttribute("totalArticles", totalArticles);
        model.addAttribute("publishedCount", publishedCount);
        model.addAttribute("draftCount", draftCount);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("recentComments", recentComments);
        return "admin/dashboard";
    }

    // ---- 文章管理 ----

    @GetMapping("/articles")
    public String articles(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) Long categoryId,
                           @RequestParam(required = false) Integer status,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        PageResult<Article> articlePage = articleService.findPage(page, size, categoryId, status, keyword);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("categories", categories);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        return "admin/articles";
    }

    @GetMapping("/article/new")
    public String newArticle(Model model) {
        model.addAttribute("article", new Article());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/article-form";
    }

    @GetMapping("/article/{id}/edit")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        if (article == null) {
            return "error/404";
        }
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/article-form";
    }

    @PostMapping("/article")
    public String saveArticle(@ModelAttribute Article article,
                              @AuthenticationPrincipal UserPrincipal principal,
                              RedirectAttributes ra) {
        article.setUserId(principal.getId());
        articleService.createOrUpdate(article);
        ra.addFlashAttribute("success", article.getId() != null ? "文章保存成功" : "文章发布成功");
        return "redirect:/admin/articles";
    }

    @GetMapping("/article/{id}/delete")
    public String deleteArticle(@PathVariable Long id, RedirectAttributes ra) {
        articleService.delete(id);
        ra.addFlashAttribute("success", "文章已删除");
        return "redirect:/admin/articles";
    }

    @GetMapping("/article/{id}/publish")
    public String publishArticle(@PathVariable Long id, RedirectAttributes ra) {
        articleService.publish(id);
        ra.addFlashAttribute("success", "文章已发布");
        return "redirect:/admin/articles";
    }

    @GetMapping("/article/{id}/withdraw")
    public String withdrawArticle(@PathVariable Long id, RedirectAttributes ra) {
        articleService.withdraw(id);
        ra.addFlashAttribute("success", "文章已撤回");
        return "redirect:/admin/articles";
    }

    // ---- 分类管理 ----

    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin/categories";
    }

    @PostMapping("/category")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes ra) {
        try {
            if (category.getId() == null) {
                categoryService.create(category);
            } else {
                categoryService.update(category);
            }
            ra.addFlashAttribute("success", "分类保存成功");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/category/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes ra) {
        try {
            categoryService.delete(id);
            ra.addFlashAttribute("success", "分类已删除");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    // ---- 评论管理 ----

    @GetMapping("/comments")
    public String comments(Model model) {
        model.addAttribute("comments", commentMapper.selectAll());
        return "admin/comments";
    }

    @GetMapping("/comment/{id}/toggle")
    public String toggleComment(@PathVariable Long id) {
        Comment comment = commentMapper.selectById(id);
        if (comment != null) {
            commentMapper.updateStatus(id, comment.getStatus() == 1 ? 0 : 1);
        }
        return "redirect:/admin/comments";
    }

    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id, RedirectAttributes ra) {
        commentService.delete(id);
        ra.addFlashAttribute("success", "评论已删除");
        return "redirect:/admin/comments";
    }
}
