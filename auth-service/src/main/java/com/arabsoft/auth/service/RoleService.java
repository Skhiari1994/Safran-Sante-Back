package com.arabsoft.auth.service;

import com.arabsoft.auth.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
     Role addRole(Role role);
     void addRoleToUser(String username , String roleName) ;
     Page<Role> findAll(Pageable pageable);
     Role findById(Integer id);
    Role update(Integer id, Role role);

    void deleteById(Integer id);
}
