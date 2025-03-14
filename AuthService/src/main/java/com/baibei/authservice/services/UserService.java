package com.baibei.authservice.services;

import com.baibei.authservice.dto.RegisterRequest;
import com.baibei.authservice.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);

    User registerUser(RegisterRequest request);

}
