package com.baibei.usermanagementservice.controller;

import com.baibei.usermanagementservice.dto.UserAuthDto;
import com.baibei.usermanagementservice.entity.Role;
import com.baibei.usermanagementservice.entity.User;
import com.baibei.usermanagementservice.service.RoleService;
import com.baibei.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @PostMapping("/register")
    public UserAuthDto register(@RequestBody UserAuthDto userAuthDto) {
        if (existsBy(userAuthDto.getUsername(), userAuthDto.getEmail())) {
            return null;
        }

        User user = new User();
        user.setUsername(userAuthDto.getUsername());
        user.setPassword(userAuthDto.getPassword());
        user.setEmail(userAuthDto.getEmail());
        user.setRoles(getRoles());

        userService.save(user);

        return userAuthDto;
    }

    private Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        Role user;

        if (roleService.existsByRoleName("ROLE_USER")) {
            user = roleService.findByRoleName("ROLE_USER");
        } else {
            user = new Role();
            user.setName("ROLE_USER");
            roleService.save(user);
        }

        roles.add(user);
        return roles;
    }

    @GetMapping("/exists")
    public Boolean exists(@RequestParam(value = "username", required = false) String username,
                          @RequestParam(value = "email", required = false) String email) {
        return existsBy(username, email);
    }

    @GetMapping("/{username}")
    public User findByUsername(@PathVariable(value = "username") String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/{username}/auth")
    public UserAuthDto findAuthByUsername(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username);
        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setUsername(user.getUsername());
        userAuthDto.setPassword(user.getPassword());
        userAuthDto.setEmail(user.getEmail());
        return userAuthDto;
    }

    @PatchMapping("/username/auth")
    public UserAuthDto updateUser(@RequestBody UserAuthDto userAuthDto) {
        User user = userService.findByUsername(userAuthDto.getUsername());
        if (existsBy(userAuthDto.getUsername(), userAuthDto.getEmail())) {
            user = new User();
        }

        user.setUsername(userAuthDto.getUsername());
        user.setPassword(userAuthDto.getPassword());
        user.setEmail(userAuthDto.getEmail());

        userService.save(user);
        return userAuthDto;
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setEnabled(false);
            userService.save(user);
        }
    }

    @GetMapping("/{username}/enable")
    public Boolean enableUser(@PathVariable(value = "username") String username) {
        User user = userService.findByUsername(username);

        if (user != null) {
            user.setEnabled(true);
            userService.save(user);
            return true;
        } else {
            return false;
        }
    }

    private boolean existsBy(String username, String email) {
        if (username != null && email != null) {
            return userService.existsByUsername(username) || userService.existsByEmail(email);
        } else if (username != null) {
            return userService.existsByUsername(username);
        } else if (email != null) {
            return userService.existsByEmail(email);
        } else {
            return false;
        }
    }
}
