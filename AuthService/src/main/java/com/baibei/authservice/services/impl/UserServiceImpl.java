package com.baibei.authservice.services.impl;

import com.baibei.authservice.dto.RegisterRequest;
import com.baibei.authservice.entity.Role;
import com.baibei.authservice.entity.User;
import com.baibei.authservice.services.RestService;
import com.baibei.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final RestService restService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(restService.getUser(username));
    }

    @Override
    public User registerUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Set.of(new Role("ROLE_USER")));

        if (restService.registerUser(user)) {
            return user;
        } else {
            return null;
        }
    }
}
