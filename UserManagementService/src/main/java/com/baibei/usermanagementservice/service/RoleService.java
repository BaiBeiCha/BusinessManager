package com.baibei.usermanagementservice.service;

import com.baibei.usermanagementservice.entity.Role;

public interface RoleService {

    Role save(Role role);
    Role findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);

}
