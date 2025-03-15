package com.baibei.authservice.dto;

import com.baibei.authservice.entity.Role;
import com.baibei.authservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String username;
    private String email;
    private String password;
    private String[] roles;

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(getRoles());
        return user;
    }

    private Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        if (this.roles != null) {
            for (String role : this.roles) {
                roles.add(new Role(role));
            }
        } else {
            roles.add(new Role("ROLE_USER"));
        }
        return roles;
    }
}
