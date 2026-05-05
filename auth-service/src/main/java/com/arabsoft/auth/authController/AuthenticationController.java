package com.arabsoft.auth.authController;

import com.arabsoft.auth.model.Token;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.repository.TokenRepository;
import com.arabsoft.auth.repository.UserRepository;

import com.arabsoft.auth.reset_password.PasswordResetRequest;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final PasswordEncoder encoder;
    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @PostMapping("/register")
    public void register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        service.register(request);

    }

    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/activate-account")
    public void confirm(
            @RequestParam String token) throws MessagingException {
        service.activateAccount(token);
    }

    @GetMapping("/getToken/{token}")
    public Optional<Token> getToken(
            @PathVariable String token) {
        return tokenRepository.findByToken(token);
    }

    @PostMapping("/password-reset-request")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> resetPasswordRequest(@RequestBody PasswordResetRequest passwordResetRequest)
            throws MessagingException {
        service.resetPasswordRequest(passwordResetRequest);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/updatePass")
    public ResponseEntity<User> updatePass(@RequestBody User Ag) {
        Optional<User> agData = userRepository.findByUselogin(Ag.getUselogin());
        if (agData.isPresent()) {
            User agg = agData.get();
            agg.setUse_pswd(encoder.encode(Ag.getUse_pswd()));
            return new ResponseEntity<>(userRepository.save(agg), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
