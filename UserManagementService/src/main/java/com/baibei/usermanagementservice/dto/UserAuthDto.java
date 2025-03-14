package com.baibei.usermanagementservice.dto;

import com.baibei.usermanagementservice.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserAuthDto {

    private String username;
    private String password;
    private String email;
    private Set<Role> roles;

}
