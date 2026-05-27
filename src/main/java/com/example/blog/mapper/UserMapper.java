package com.example.blog.mapper;

import com.example.blog.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectById(Long id);

    User selectByUsername(String username);

    User selectByEmail(String email);

    List<User> selectAll();

    int insert(User user);

    int update(User user);

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int deleteById(Long id);
}
