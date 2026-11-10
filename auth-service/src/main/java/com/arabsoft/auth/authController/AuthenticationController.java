package com.arabsoft.auth.authController;

import com.arabsoft.auth.model.Token;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.projections.SocieteProjection;
import com.arabsoft.auth.repository.TokenRepository;
import com.arabsoft.auth.repository.UserRepository;

import com.arabsoft.auth.reset_password.PasswordResetRequest;
import com.arabsoft.auth.security.JwtService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final PasswordEncoder encoder;
    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;

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

    /**
     * Mot de passe fort : 8 caractères minimum, au moins une majuscule, un chiffre et un symbole.
     * La même règle est contrôlée dans l'écran « Gérer mon compte » (le serveur reste la référence).
     */
    private static final Pattern MOT_DE_PASSE_FORT = Pattern
            .compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");

    /**
     * L'utilisateur connecté change son propre mot de passe. Cette route est sous /api/auth (la seule exposée par le
     * gateway) : elle vérifie donc elle-même le token, puis l'ancien mot de passe.
     */
    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody ChangeMyPasswordRequest request) {

        User user = utilisateurDuToken(authorization);
        if (user == null) {
            return reponse(HttpStatus.UNAUTHORIZED, "Session expirée. Reconnectez-vous.");
        }
        if (request == null || isBlank(request.currentPassword()) || isBlank(request.newPassword())
                || isBlank(request.confirmationPassword())) {
            return reponse(HttpStatus.BAD_REQUEST, "Tous les champs sont obligatoires.");
        }
        if (!encoder.matches(request.currentPassword(), user.getPassword())) {
            return reponse(HttpStatus.BAD_REQUEST, "L'ancien mot de passe est incorrect.");
        }
        if (!request.newPassword().equals(request.confirmationPassword())) {
            return reponse(HttpStatus.BAD_REQUEST, "La confirmation ne correspond pas au nouveau mot de passe.");
        }
        if (!MOT_DE_PASSE_FORT.matcher(request.newPassword()).matches()) {
            return reponse(HttpStatus.BAD_REQUEST,
                    "Le mot de passe doit contenir au moins 8 caractères, une majuscule, un chiffre et un symbole.");
        }
        if (request.newPassword().equals(request.currentPassword())) {
            return reponse(HttpStatus.BAD_REQUEST, "Le nouveau mot de passe doit être différent de l'ancien.");
        }

        user.setUse_pswd(encoder.encode(request.newPassword()));
        userRepository.save(user);
        return reponse(HttpStatus.OK, "Mot de passe modifié avec succès.");
    }

    /** Compte correspondant au token (login + société de travail) ; null si le token est absent, invalide ou expiré. */
    private User utilisateurDuToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return null;
        }
        String token = authorization.substring(7);
        try {
            String login = jwtService.extractUsername(token);
            String codSoc = jwtService.extractClaim(token, c -> c.get("codSoc", String.class));
            // même résolution qu'à la connexion : compte du login dans la société, sinon profil RH/ADMIN
            User user = userRepository.findByUseloginAndCodSoc(login, codSoc == null ? "" : codSoc)
                    .or(() -> userRepository.findPrivilegedByUselogin(login).stream().findFirst())
                    .orElse(null);
            return user != null && jwtService.isTokenValid(token, user) ? user : null;
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    private static ResponseEntity<Map<String, String>> reponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }

    @GetMapping("/getSocieteByLogin/{matPers}/{cin}")
    public List<SocieteProjection> getSocieteByLogin(
            @PathVariable String matPers,
            @PathVariable String cin) {

        Long useId = userRepository.findUseIdByMatriculeAndCin(matPers, cin);
        Long roleId = userRepository.findRoleByUseId(useId);

        if ((roleId != null && useId != null) && (roleId == 3L || roleId == 4L || roleId == 5L)) {
            return userRepository.findAllSocietes();
        } else {
            return userRepository.findSocieteByMatriculeAndCin(matPers, cin);
        }

    }

}
