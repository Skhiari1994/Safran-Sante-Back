package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurRoleProjection;
import com.tn.arabsoft.administrations.repositories.UtilisateurConsultationDao;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Utilisateur")
public class UtilisateurController {

    private final UtilisateurConsultationDao dao;

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
