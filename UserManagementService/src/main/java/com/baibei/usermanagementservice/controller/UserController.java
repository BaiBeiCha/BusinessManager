package com.baibei.usermanagementservice.controller;

import com.baibei.usermanagementservice.dto.UserAuthDto;
import com.baibei.usermanagementservice.entity.Role;
import com.baibei.usermanagementservice.entity.User;
import com.baibei.usermanagementservice.service.RoleService;
import com.baibei.usermanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/register")
    public UserAuthDto register(@RequestBody UserAuthDto userAuthDto) {
        if (existsBy(userAuthDto.getUsername(), userAuthDto.getEmail())) {
            return null;
        }

        User user = new User();
        user.setUsername(userAuthDto.getUsername());
        user.setPassword(userAuthDto.getPassword());
        user.setEmail(userAuthDto.getEmail());
        user.setRoles(getRolesFromRequest(userAuthDto));

        user.getRoles().forEach(roleService::save);
        userService.save(user);

        return userAuthDto;
    }

    private Set<Role> getRolesFromRequest(UserAuthDto userAuthDto) {
        Set<Role> roles = new HashSet<>();
        if (userAuthDto.getRoles() != null) {
            for (String role : userAuthDto.getRoles()) {
                roles.add(getRoleByName(role));
            }
        } else {
            roles = getRoles();
        }
        return roles;
    }

    private Set<Role> getRoles() {
        Set<Role> roles = new HashSet<>();
        Role user = getRoleByName("ROLE_USER");
        roles.add(user);
        return roles;
    }

    private Role getRoleByName(String name) {
        Role role;
        if (roleService.existsByRoleName(name)) {
            role = roleService.findByRoleName(name);
        } else {
            role = new Role();
            role.setName(name);
            roleService.save(role);
        }
        return role;
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
    public UserAuthDto findAuthByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        UserAuthDto dto = new UserAuthDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new)
        );

        return dto;
    }

    @PatchMapping("/{username}/auth")
    public UserAuthDto updateUser(@RequestBody UserAuthDto userAuthDto, @PathVariable String username) {
        User user = userService.findByUsername(username);
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

    @PostMapping("/{username}/enable")
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
