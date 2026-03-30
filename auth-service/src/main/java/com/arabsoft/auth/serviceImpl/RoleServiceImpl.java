package com.arabsoft.auth.serviceImpl;

import com.arabsoft.auth.repository.RoleRepository;
import com.arabsoft.auth.model.Role;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.repository.UserRepository;
import com.arabsoft.auth.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository ;
    private final UserRepository userRepository ;
    @Override
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
      User user = userRepository.findByUselogin(username)
              .orElseThrow(() -> new IllegalArgumentException("User not found bu username  " +  username) );
      Role role = roleRepository.findByName(roleName)
              .orElseThrow(() -> new IllegalArgumentException("Role not found By name" +  roleName));
      user.getRoles().add(role);
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);

    }

    @Override
    public Role findById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found bu id : " + id ));
    }

    @Override
    public Role update(Integer id, Role role) {
        Role  roleExisting = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found by id"));
        roleExisting.setName(role.getName());
        return   roleRepository.save(roleExisting);
    }

    @Override
    public void deleteById(Integer id) {
        if(!roleRepository.existsById(id)){
          throw new IllegalArgumentException("Role not found By Id "  + id) ;
        }

         roleRepository.deleteById(id);
    }
}

