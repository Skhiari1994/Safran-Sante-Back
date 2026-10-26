package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import com.tn.arabsoft.administrations.projections.AutorisationNoeudProjection;
import com.tn.arabsoft.administrations.repositories.AdmeventtypeDAO;
import com.tn.arabsoft.administrations.repositories.AdmsubmoduleDAO;
import com.tn.arabsoft.administrations.repositories.MenuGestionDao;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/** Écran Gestion des modules : arbre complet, autorisations par noeud, rangs et suppressions en cascade. */
@RestController
@RequiredArgsConstructor
@RequestMapping("/Menu")
@SuppressWarnings({ "java:S100", "java:S117" })
public class MenuGestionController {

    private final MenuGestionDao dao;

    private final AdmeventtypeDAO eventTypeRepository;

    private final AdmsubmoduleDAO submoduleRepository;

    /** Tous les sous-menus de tous les niveaux (l arbre est reconstruit côté écran). */
    @GetMapping("/evtAll")
    public List<Admeventtype> getAllEvt() {
        return eventTypeRepository.findAll();
    }

    /** Autorisations (module, sous-menu, rôle) sans doublon. */
    @GetMapping("/autorisations")
    public List<AutorisationNoeudProjection> getAutorisations() {
        return dao.findAutorisations();
    }

    /**
     * Fixe la liste des rôles autorisés sur un noeud. Un rôle retiré l est aussi sur tous les
     * sous-éléments du noeud.
     */
    @PutMapping("/autorisations")
    @Transactional
    public ResponseEntity<Void> setAutorisations(@RequestBody Map<String, Object> body) {
        Long sumId = toLong(body.get("sumId"));
        Long evtId = toLong(body.get("evtId"));
        if (sumId == null) {
            return ResponseEntity.badRequest().build();
        }
        Set<Long> voulus = new HashSet<>();
        if (body.get("roleIds") instanceof List<?> liste) {
            liste.forEach(r -> voulus.add(toLong(r)));
        }
        Set<Long> actuels = dao.findAutorisations().stream()
                .filter(a -> sumId.equals(a.getSumId()) && Objects.equals(evtId, a.getEvtId()))
                .map(AutorisationNoeudProjection::getRoleId)
                .collect(Collectors.toSet());

        for (Long role : voulus) {
            if (!actuels.contains(role)) {
                dao.insertAutorisation(sumId, evtId, role);
            }
        }
        List<Long> noeuds = evtId == null
                ? eventTypeRepository.findAll().stream().filter(e -> sumId.equals(e.getSum_id()))
                        .map(Admeventtype::getEvt_id).collect(Collectors.toList())
                : descendants(evtId);
        for (Long role : actuels) {
            if (voulus.contains(role)) {
                continue;
            }
            dao.deleteAutorisation(sumId, evtId, role);
            for (Long enfant : noeuds) {
                dao.deleteAutorisation(sumId, enfant, role);
            }
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Crée un module. La séquence peut être en retard sur les identifiants déjà présents :
     * on avance jusqu à un identifiant libre au lieu d échouer sur la contrainte d unicité.
     */
    @PostMapping("/module")
    @Transactional
    public Map<String, Long> createModule(@RequestBody Map<String, Object> body) {
        Long id = dao.nextModuleId();
        while (submoduleRepository.existsById(id)) {
            id = dao.nextModuleId();
        }
        dao.insertModule(id, String.valueOf(body.getOrDefault("sum_name", "")),
                toLong(body.get("sum_rank")), String.valueOf(body.getOrDefault("sum_status", "O")),
                body.get("sum_rout") == null ? null : String.valueOf(body.get("sum_rout")));
        return Map.of("sum_id", id);
    }

    /** Met à jour les rangs : [{kind: module ou evt, id, rank}, ...]. */
    @PutMapping("/ranks")
    @Transactional
    public ResponseEntity<Void> setRanks(@RequestBody List<Map<String, Object>> rangs) {
        for (Map<String, Object> r : rangs) {
            Long id = toLong(r.get("id"));
            Long rank = toLong(r.get("rank"));
            if (id == null || rank == null) {
                continue;
            }
            if ("module".equals(r.get("kind"))) {
                dao.updateRangModule(id, rank);
            } else {
                dao.updateRangEvenement(id, rank);
            }
        }
        return ResponseEntity.ok().build();
    }

    /** Supprime un module avec ses sous-menus et leurs autorisations. */
    @DeleteMapping("/module/{id}")
    @Transactional
    public ResponseEntity<String> deleteModule(@PathVariable Long id) {
        if (!submoduleRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module introuvable.");
        }
        try {
            dao.deleteAutorisationsModule(id);
            dao.deleteEvenementsModule(id);
            submoduleRepository.deleteById(id);
            submoduleRepository.flush();
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce module est référencé ailleurs et ne peut pas être supprimé.");
        }
    }

    /** Supprime un sous-menu avec tous ses sous-éléments et leurs autorisations. */
    @DeleteMapping("/noeud/{id}")
    @Transactional
    public ResponseEntity<String> deleteNoeud(@PathVariable Long id) {
        if (!eventTypeRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sous-menu introuvable.");
        }
        try {
            List<Long> ids = descendants(id);
            ids.add(id);
            dao.deleteAutorisationsEvt(ids);
            eventTypeRepository.deleteAllById(ids);
            eventTypeRepository.flush();
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce sous-menu est référencé ailleurs et ne peut pas être supprimé.");
        }
    }

    /** Identifiants de tous les descendants (à tous les niveaux) d un sous-menu, lui-même exclu. */
    private List<Long> descendants(Long evtId) {
        List<Admeventtype> tous = eventTypeRepository.findAll();
        List<Long> resultat = new ArrayList<>();
        Deque<Long> aTraiter = new ArrayDeque<>(List.of(evtId));
        while (!aTraiter.isEmpty()) {
            Long parent = aTraiter.poll();
            for (Admeventtype e : tous) {
                if (parent.equals(e.getEvt_evt_id()) && !resultat.contains(e.getEvt_id())) {
                    resultat.add(e.getEvt_id());
                    aTraiter.add(e.getEvt_id());
                }
            }
        }
        return resultat;
    }

    private static Long toLong(Object o) {
        return o == null || String.valueOf(o).isBlank() ? null : Long.valueOf(String.valueOf(o));
    }

}
