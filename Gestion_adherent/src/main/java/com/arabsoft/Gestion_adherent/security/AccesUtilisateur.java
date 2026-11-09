package com.arabsoft.gestion_adherent.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Périmètre de données imposé par le token (et non par le front, qui peut être contourné).
 * Un compte dont le seul rôle est UTILISATEUR ne peut consulter que sa propre fiche ; les autres profils
 * (CHEF, RH, ADMIN, frais médicaux…) gardent l'accès à plusieurs personnes.
 */
@Component
@RequiredArgsConstructor
public class AccesUtilisateur {

    private static final String ROLE_UTILISATEUR = "UTILISATEUR";

    private final JwtService jwtService;

    /** Société et matricule de l'utilisateur ; renseigné seulement pour un compte limité à ses propres données. */
    public record Portee(String codSoc, String matricule) {
    }

    public Optional<Portee> porteeLimitee(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return Optional.empty();
        }
        Claims claims;
        try {
            claims = jwtService.extractClaim(header.substring(7), c -> c);
        } catch (Exception e) {
            return Optional.empty();
        }
        Object brut = claims.get("authorities");
        if (!(brut instanceof List<?> roles) || roles.isEmpty()
                || !roles.stream().allMatch(r -> ROLE_UTILISATEUR.equalsIgnoreCase(String.valueOf(r).trim()))) {
            return Optional.empty();
        }
        String matricule = claims.get("matricule", String.class);
        if (matricule == null || matricule.isBlank()) {
            // compte sans matricule identifiable : on ne renvoie rien plutôt que tout
            matricule = "#aucun#";
        }
        String codSoc = claims.get("codSoc", String.class);
        return Optional.of(new Portee(codSoc == null ? "" : codSoc, matricule));
    }
}
