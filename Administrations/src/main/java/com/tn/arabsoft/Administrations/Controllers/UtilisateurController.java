package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import com.tn.arabsoft.administrations.repositories.UtilisateurConsultationDao;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Utilisateur")
public class UtilisateurController {

    private final UtilisateurConsultationDao dao;

    @GetMapping("/consultation")
    public List<UtilisateurConsultationProjection> getUtilisateurs() {
        return dao.findUtilisateurs();
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
