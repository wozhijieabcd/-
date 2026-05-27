package com.example.blog.service;

import com.example.blog.entity.User;

public interface UserService {

    User findById(Long id);

    User findByUsername(String username);

    void register(User user);

    void updateProfile(User user);

    void updatePassword(Long userId, String oldPassword, String newPassword);
}
