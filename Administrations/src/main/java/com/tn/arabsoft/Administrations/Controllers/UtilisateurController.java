package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.projections.PersonnelSansCompteProjection;
import com.tn.arabsoft.administrations.projections.PersonnelUtilisateurProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurRoleProjection;
import com.tn.arabsoft.administrations.repositories.UtilisateurConsultationDao;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Utilisateur")
public class UtilisateurController {

    private final UtilisateurConsultationDao dao;

    /** Meme regle que le service d'authentification : 8 caracteres minimum, une majuscule, un chiffre, un symbole. */
    private static final Pattern MOT_DE_PASSE_FORT = Pattern
            .compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final BCryptPasswordEncoder HACHEUR = new BCryptPasswordEncoder();

    private static ResponseEntity<Map<String, Object>> erreur(HttpStatus statut, String message) {
        return ResponseEntity.status(statut).body(Map.of("message", message));
    }

    /** Personnel d'une societe sans compte utilisateur : la liste de choix de la fenetre d'ajout. */
    @GetMapping("/personnel-sans-compte")
    public List<Map<String, Object>> personnelSansCompte(@RequestParam String codSoc) {
        List<Map<String, Object>> resultat = new ArrayList<>();
        for (PersonnelSansCompteProjection p : dao.findPersonnelSansCompte(codSoc.trim())) {
            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("mat_pers", p.getMatPers());
            ligne.put("cin", p.getCin());
            ligne.put("dat_nais", p.getDatNais());
            ligne.put("full_name", p.getFullName());
            resultat.add(ligne);
        }
        return resultat;
    }

    /** Fiche du personnel d'un matricule, telle qu'elle sera reprise dans le compte (voir la creation ci-dessous). */
    @GetMapping("/personnel/{codSoc}/{matricule}")
    public ResponseEntity<Map<String, Object>> personnel(@PathVariable String codSoc, @PathVariable String matricule) {
        PersonnelUtilisateurProjection p = dao.findPersonnelPourUtilisateur(codSoc.trim(), matricule.trim());
        if (p == null) {
            return erreur(HttpStatus.NOT_FOUND, "Fiche personnel introuvable.");
        }
        Map<String, Object> fiche = new LinkedHashMap<>();
        fiche.put("codSoc", p.getCodSoc());
        fiche.put("matricule", p.getMatricule());
        fiche.put("nom", p.getNom());
        fiche.put("prenom", p.getPrenom());
        fiche.put("cin", p.getCin());
        fiche.put("email", p.getEmail());
        fiche.put("dejaUtilisateur", p.getDejaUtilisateur() != null && p.getDejaUtilisateur() > 0);
        return ResponseEntity.ok(fiche);
    }

    /**
     * Cree un compte a partir de la fiche du personnel (cod_soc, mat_pers, nom_pers, pren_pers, cin -> ADMUSER),
     * avec ses roles. Corps : codSoc, matricule, email (facultatif), roleIds (au moins un), password.
     */
    @PostMapping
    @Transactional
    public ResponseEntity<Map<String, Object>> creer(@RequestBody Map<String, Object> body) {
        String codSoc = String.valueOf(body.getOrDefault("codSoc", "")).trim();
        String matricule = String.valueOf(body.getOrDefault("matricule", "")).trim();
        String email = body.get("email") == null ? "" : String.valueOf(body.get("email")).trim();
        String motDePasse = body.get("password") == null ? "" : String.valueOf(body.get("password"));

        if (codSoc.isEmpty() || matricule.isEmpty()) {
            return erreur(HttpStatus.BAD_REQUEST, "Choisissez un membre du personnel.");
        }
        List<Long> roleIds = new ArrayList<>();
        if (body.get("roleIds") instanceof List<?> liste) {
            for (Object roleId : liste) {
                roleIds.add(Long.valueOf(String.valueOf(roleId)));
            }
        }
        if (roleIds.isEmpty()) {
            return erreur(HttpStatus.BAD_REQUEST, "Choisissez au moins un rôle.");
        }
        for (Long roleId : roleIds) {
            if (!dao.existsById(roleId)) {
                return erreur(HttpStatus.BAD_REQUEST, "Rôle introuvable : " + roleId);
            }
        }
        if (!MOT_DE_PASSE_FORT.matcher(motDePasse).matches()) {
            return erreur(HttpStatus.BAD_REQUEST,
                    "Mot de passe trop faible : 8 caractères minimum, avec une majuscule, un chiffre et un symbole.");
        }
        if (!email.isEmpty() && !EMAIL.matcher(email).matches()) {
            return erreur(HttpStatus.BAD_REQUEST, "Adresse e-mail invalide.");
        }

        PersonnelUtilisateurProjection fiche = dao.findPersonnelPourUtilisateur(codSoc, matricule);
        if (fiche == null) {
            return erreur(HttpStatus.NOT_FOUND, "Fiche personnel introuvable.");
        }
        if (fiche.getCin() == null || fiche.getCin().trim().isEmpty()) {
            return erreur(HttpStatus.BAD_REQUEST, "Cette fiche personnel n'a pas de CIN : il sert d'identifiant de connexion.");
        }
        if (fiche.getDejaUtilisateur() != null && fiche.getDejaUtilisateur() > 0) {
            return erreur(HttpStatus.CONFLICT, "Cet utilisateur existe déjà (matricule " + matricule + ").");
        }
        Integer memeLogin = dao.countParLogin(fiche.getCin().trim());
        if (memeLogin != null && memeLogin > 0) {
            return erreur(HttpStatus.CONFLICT, "Le CIN " + fiche.getCin().trim() + " est déjà utilisé par un autre compte.");
        }

        dao.insertUtilisateurDepuisPersonnel(codSoc, matricule, email, HACHEUR.encode(motDePasse));
        Long useId = dao.findUseIdParMatricule(codSoc, matricule);
        for (Long roleId : roleIds) {
            dao.insertRoleUtilisateur(useId, roleId);
        }

        Map<String, Object> resultat = new LinkedHashMap<>();
        resultat.put("useId", useId);
        resultat.put("matricule", matricule);
        resultat.put("login", fiche.getCin().trim());
        return ResponseEntity.status(HttpStatus.CREATED).body(resultat);
    }

    @GetMapping("/consultation")
    public List<Map<String, Object>> getUtilisateurs(@RequestParam(required = false) String codSoc) {
        // société demandée : seuls ses utilisateurs sont renvoyés ; sans paramètre, tous
        String soc = codSoc == null ? "" : codSoc.trim();

        Map<Long, List<UtilisateurRoleProjection>> rolesParUtilisateur = new HashMap<>();
        for (UtilisateurRoleProjection r : dao.findRolesUtilisateurs(soc)) {
            rolesParUtilisateur.computeIfAbsent(r.getUseId(), k -> new ArrayList<>()).add(r);
        }

        List<Map<String, Object>> resultat = new ArrayList<>();
        for (UtilisateurConsultationProjection u : dao.findUtilisateurs(soc)) {
            List<UtilisateurRoleProjection> roles = rolesParUtilisateur.getOrDefault(u.getUseId(), List.of());
            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("useId", u.getUseId());
            ligne.put("matricule", u.getMatricule());
            ligne.put("nom", u.getNom());
            ligne.put("prenom", u.getPrenom());
            ligne.put("cin", u.getCin());
            ligne.put("libSoc", u.getLibSoc());
            ligne.put("role", roles.isEmpty() ? null
                    : roles.stream().map(UtilisateurRoleProjection::getRoleName).collect(Collectors.joining(", ")));
            ligne.put("roleIds", roles.isEmpty() ? null
                    : roles.stream().map(r -> String.valueOf(r.getRoleId())).sorted().collect(Collectors.joining(",")));
            resultat.add(ligne);
        }
        return resultat;
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> modifier(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String nom = String.valueOf(body.getOrDefault("nom", "")).trim();
        String prenom = String.valueOf(body.getOrDefault("prenom", "")).trim();
        if (dao.updateNomPrenom(id, nom, prenom) == 0) {
            return ResponseEntity.notFound().build();
        }
        Object roles = body.get("roleIds");
        if (roles instanceof List<?> liste) {
            dao.deleteRolesUtilisateur(id);
            for (Object roleId : liste) {
                dao.insertRoleUtilisateur(id, Long.valueOf(String.valueOf(roleId)));
            }
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        dao.deleteRolesUtilisateur(id);
        dao.deleteTokensUtilisateur(id);
        dao.deleteResetTokensUtilisateur(id);
        if (dao.deleteUtilisateur(id) == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
