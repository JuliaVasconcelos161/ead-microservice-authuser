package com.ead.authuser.service.impl;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.model.RoleModel;
import com.ead.authuser.repository.RoleRepository;
import com.ead.authuser.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<RoleModel> findByRoleName(RoleType name) {
        return roleRepository.findByRoleName(name);
    }
}
