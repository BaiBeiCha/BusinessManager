package com.baibei.authservice.services;

import com.baibei.authservice.entity.User;

public interface RestService {
    User getUser(String username);

    boolean registerUser(User user);

    boolean userExists(String username, String email);

    boolean updateUser(User user);

    boolean enableUser(String username);

    void deleteUser(String username);

    void deleteUser(User user);
}
