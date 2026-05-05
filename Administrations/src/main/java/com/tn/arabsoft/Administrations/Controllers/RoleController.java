package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.entities.Admstorageautorisation;
import com.tn.arabsoft.administrations.entities.Role;
import com.tn.arabsoft.administrations.repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.administrations.repositories.RoleDao;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Role")
public class RoleController {

    private final RoleDao roleDao;

    private final AdmstorageautorisationDAO admstorageautorisationDAO;

    @GetMapping("/role")
    public List<Role> getRole() {
        return roleDao.findAll();
    }

    @GetMapping("/roles/{roleId}")
    public List<Admstorageautorisation> getModukeByR(@PathVariable("roleId") Long roleId) {
        Role role = new Role();
        role.setId(roleId);
        return admstorageautorisationDAO.findByRole(role);
    }

    @PostMapping("/AddRole")
    public Role addRole(@RequestBody Role role) {
        return roleDao.save(role);

    }

    @DeleteMapping("/DeleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleDao.deleteById(roleId);
        return ResponseEntity.ok().build();
    }

}
