package com.tn.arabsoft.Administrations.Controllers;

import com.tn.arabsoft.Administrations.Entities.Admstorageautorisation;
import com.tn.arabsoft.Administrations.Entities.Role;
import com.tn.arabsoft.Administrations.Repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.Administrations.Repositories.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Role")
public class RoleController {

    @Autowired
    RoleDao roleDao;
    @Autowired
    AdmstorageautorisationDAO admstorageautorisationDAO;
    @GetMapping("/role")
    public List<Role> getRole()
    {
        return roleDao.findAll();
    }


    @GetMapping("/roles/{roleId}")
    public List<Admstorageautorisation> getModukeByR(@PathVariable("roleId") Long roleId)
    {
        Role role =new Role();
        role.setId(roleId);
        return admstorageautorisationDAO.findByRole(role);
    }
    @PostMapping("/AddRole")
    public Role addRole(@RequestBody Role role)
    {
        return roleDao.save(role);

    }

    @DeleteMapping("/DeleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleDao.deleteById(roleId);
        return ResponseEntity.ok().build();
    }
}
