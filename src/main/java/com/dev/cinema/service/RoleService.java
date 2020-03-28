package com.dev.cinema.service;

import com.dev.cinema.model.Role;

public interface RoleService {

    Role add(Role role);

    Role getByRoleName(String roleName);
}
