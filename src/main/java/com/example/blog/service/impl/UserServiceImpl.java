package com.example.blog.service.impl;

import com.example.blog.entity.User;
import com.example.blog.mapper.UserMapper;
import com.example.blog.security.UserPrincipal;
import com.example.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }
        return new UserPrincipal(user);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public void register(User user) {
        User exist = userMapper.selectByUsername(user.getUsername());
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        if (user.getEmail() != null && userMapper.selectByEmail(user.getEmail()) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : "USER");
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public void updateProfile(User user) {
        User exist = userMapper.selectById(user.getId());
        if (exist == null) {
            throw new RuntimeException("用户不存在");
        }
        exist.setNickname(user.getNickname());
        exist.setEmail(user.getEmail());
        exist.setAvatar(user.getAvatar());
        userMapper.update(exist);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        userMapper.updatePassword(userId, passwordEncoder.encode(newPassword));
    }
}
