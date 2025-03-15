package com.baibei.authservice.services.impl;

import com.baibei.authservice.dto.UserDto;
import com.baibei.authservice.entity.User;
import com.baibei.authservice.services.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class RestServiceImpl implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public User getUser(String username) {
        String url = "http://UserManagementService/api/users/{username}/auth";
        UserDto result = restTemplate.getForObject(url, UserDto.class, username);
        if (result == null) {
            return null;
        } else {
            return result.toUser();
        }
    }

    @Override
    public boolean registerUser(User user) {
        String url = "http://UserManagementService/api/users/register";
        var result = restTemplate.postForObject(url, user.toDto(), UserDto.class);
        return result != null;
    }

    @Override
    public boolean userExists(String username, String email) {
        String url = "http://UserManagementService/api/users/exists?username={username}&email={email}";
        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class, username, email));
    }

    @Override
    public boolean updateUser(User user) {
        String url = "http://UserManagementService/api/users/{username}/auth";
        var result = restTemplate.patchForObject(url, user.toDto(), UserDto.class, user.getUsername());
        return result != null;
    }

    @Override
    public boolean enableUser(String username) {
        String url = "http://UserManagementService/api/users/{username}/enable";
        return Boolean.TRUE.equals(restTemplate.postForObject(url, Boolean.TRUE, Boolean.class, username));
    }

    @Override
    public void deleteUser(String username) {
        String url = "http://UserManagementService/api/users/{username}";
        restTemplate.delete(url, username);
    }

    @Override
    public void deleteUser(User user) {
        deleteUser(user.getUsername());
    }
}
