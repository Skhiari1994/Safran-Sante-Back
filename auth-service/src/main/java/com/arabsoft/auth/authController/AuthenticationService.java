package com.arabsoft.auth.authController;

import com.arabsoft.auth.email.EmailService;
import com.arabsoft.auth.email.EmailTemplateName;
import com.arabsoft.auth.exceptions.BadRequestException;
import com.arabsoft.auth.model.Token;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.repository.RoleRepository;
import com.arabsoft.auth.repository.TokenRepository;
import com.arabsoft.auth.repository.UserRepository;
import com.arabsoft.auth.reset_password.PasswordResetRequest;
import com.arabsoft.auth.security.JwtService;
import com.arabsoft.auth.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    @Value("${application.mailing.frontend.resetPassword-url}")
    private String resetPasswordUrl;

    public void register(RegistrationRequest request) throws MessagingException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already used by another account");
        }

        if (userRepository.findByUselogin(request.getUseLogin()).isPresent()) {
            throw new BadRequestException("Login is already taken");
        }

        var userRole = roleRepository.findByName(request.getRoles())
                .orElseThrow(() -> new BadRequestException("Role not found"));

        var user = User.builder()
                .use_fname(request.getFirstname())
                .use_lname(request.getLastname())
                .uselogin(request.getUseLogin())
                .email(request.getEmail())
                .use_pswd(passwordEncoder.encode(request.getPassword()))
                .use_status(false)
                .use_type(true)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        // send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"

        );
    }

    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generatedToken = generateActivationCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length()); // 0...9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUseLogin(),
                        request.getPassword()));
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .roles(user.getRoles())
                .matPers(user.getMatpers())
                .useLogin(user.getUselogin())
                .codSoc(user.getCod_soc())
                .build();
    }

    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException(
                    "Activation token has expired. A new token has been send to the same email address");
        }

        var user = userRepository.findById(savedToken.getUser().getUse_id())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUse_type(true);
        userRepository.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }

    public void resetPasswordRequest(PasswordResetRequest passwordResetRequest) throws MessagingException {
        User user = userRepository.findByUselogin(passwordResetRequest.getUseLogin())
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found by email" + passwordResetRequest.getUseLogin()));
        sendResetPasswordEmail(user);
    }

    private void sendResetPasswordEmail(User user) throws MessagingException {
        String passwordResetUrl = "";
        // generer le token de reset password
        String passwordResetToken = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, passwordResetToken);
        // construire l'uel qui contient le path de angular + le parametre token
        passwordResetUrl = this.resetPasswordUrl + "?token=" + passwordResetToken;
        emailService.sendEmailResetPassword(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.RESET_PASSWORD,
                passwordResetUrl,
                "Vérification de la demande de réinitialisation de mot de passe");

    }

}
