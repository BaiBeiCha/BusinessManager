package com.baibei.usermanagementservice.service;

import com.baibei.usermanagementservice.entity.User;

import java.util.List;

public interface UserService {

    User save(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    List<User> findAll();
}
