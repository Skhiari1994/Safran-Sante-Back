package com.arabsoft.auth.controller;

import com.arabsoft.auth.model.Role;
import com.arabsoft.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
@RequiredArgsConstructor
public class RoleController {
   private final RoleService roleService ;

   @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
     return ResponseEntity.ok(roleService.addRole(role));
   }

   @GetMapping("/{id}")
   public ResponseEntity<Role> findById(@PathVariable Integer id ){
     return ResponseEntity.ok(roleService.findById(id))  ;
   }

   @PostMapping("addRoleToUser")
   public void addRoleToUser(@RequestParam String email , @RequestParam String roleName) {
      roleService.addRoleToUser(email , roleName);
   }

   @PutMapping("/{id}")
   public ResponseEntity<Role> update(@PathVariable Integer id , @RequestBody Role role) {
    return ResponseEntity.ok(roleService.update(id , role));
   }


   @GetMapping("/AllRole")
    public ResponseEntity<Page<Role>> findAll(
            @RequestParam(defaultValue = "10") int pageSize,
             @RequestParam(defaultValue = "0") int pageNumber
   ){
      return  ResponseEntity.ok(this.roleService.findAll(PageRequest.of(pageNumber,pageSize)));
   }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id ) {
       roleService.deleteById(id) ;
  }
}
