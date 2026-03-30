package com.arabsoft.auth.controller;


import com.arabsoft.auth.authController.ChangePasswordRequest;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService ;

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
      return ResponseEntity.ok(userService.addUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@RequestParam Long id , @RequestBody User user){
      return ResponseEntity.ok(userService.update(id , user)) ;
    }
    @GetMapping
    public ResponseEntity<List<User>>  findAll() {
     return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
      return ResponseEntity.ok(userService.findById(id))    ;
    }



    @DeleteMapping("/{id}")
    public void delet(@PathVariable Long id) {
       userService.deleteById(id);
    }


    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request , Principal connectedUser){
        userService.changePassword(request , connectedUser);
        return ResponseEntity.ok().build();
    }
}
