package com.example.blog.controller;

import com.example.blog.entity.Comment;
import com.example.blog.security.UserPrincipal;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment")
    public String create(@ModelAttribute Comment comment,
                         @AuthenticationPrincipal UserPrincipal principal,
                         RedirectAttributes ra) {
        if (principal == null) {
            ra.addFlashAttribute("error", "请先登录");
            return "redirect:/article/" + comment.getArticleId();
        }
        comment.setUserId(principal.getId());
        comment.setParentId(comment.getParentId() != null ? comment.getParentId() : null);
        try {
            commentService.create(comment);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/article/" + comment.getArticleId();
    }
}
