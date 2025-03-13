package com.baibei.usermanagementservice.service.impl;

import com.baibei.usermanagementservice.entity.Role;
import com.baibei.usermanagementservice.repository.RoleRepository;
import com.baibei.usermanagementservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByRoleName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }

    @Override
    public boolean existsByRoleName(String roleName) {
        return roleRepository.existsByName(roleName);
    }
}
