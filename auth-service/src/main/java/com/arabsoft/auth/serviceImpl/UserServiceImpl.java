package com.arabsoft.auth.serviceImpl;

import com.arabsoft.auth.authController.ChangePasswordRequest;
import com.arabsoft.auth.reset_password.PasswordResetTokenService;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.repository.UserRepository;
import com.arabsoft.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenService passwordresetTokenService ;

    @Override
    public User addUser(User user) {
        String pw = user.getPassword();
       // user.setPassword(passwordEncoder.encode(pw));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found by id  " + id));
    }

    @Override
    public void deleteById(Long id) {
        if(!userRepository.existsById(id)){
          throw new IllegalArgumentException("User not found by id  " + id);
        }
         userRepository.deleteById(id);
    }

    @Override
    public User update(Long id, User user) {
        User userExisting = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("USer not found by id  "  + id));

       user.setUse_fname(user.getUse_fname());
        /// implementation rest de code
        return userRepository.save(userExisting);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if(!passwordEncoder.matches(request.getCurrentPassword() , user.getPassword())){
            throw new IllegalArgumentException("Wrong password ")   ;
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalArgumentException("Password are not the same ");
        }
        // update the new  password
        user.setUse_pswd(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user) ;

    }

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        passwordresetTokenService.createPasswordResetTokenForUser(user, passwordResetToken);
    }


}