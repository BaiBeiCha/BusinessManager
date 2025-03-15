package com.baibei.authservice.entity;

import com.baibei.authservice.dto.UserDto;
import lombok.Data;

import java.util.Set;

@Data
public class User {

    private String username;
    private String email;
    private String password;
    private Set<Role> roles;

    public UserDto toDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setEmail(email);
        userDto.setPassword(password);
        userDto.setRoles(roleNames());
        return userDto;
    }

    private String[] roleNames() {
        return roles.stream().map(Role::getName).toArray(String[]::new);
    }
}
