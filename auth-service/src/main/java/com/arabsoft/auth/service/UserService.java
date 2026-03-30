package com.arabsoft.auth.service;

import com.arabsoft.auth.authController.ChangePasswordRequest;
import com.arabsoft.auth.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> findAll();


    User findById(Long id);

    void deleteById(Long id);

    User update(Long id, User user);

    void changePassword(ChangePasswordRequest request, Principal connectedUser);

    void createPasswordResetTokenForUser(User user, String passwordResetToken);
}
