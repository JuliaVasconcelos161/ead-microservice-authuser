package com.ead.authuser.service;

import com.ead.authuser.enums.RoleType;
import com.ead.authuser.model.RoleModel;

import java.util.Optional;

public interface RoleService {
    Optional<RoleModel> findByRoleName(RoleType name);
}
