package com.arabsoft.reports.controllers;

import com.arabsoft.reports.entities.RapportRub;
import com.arabsoft.reports.repositories.RapportRubDao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/RapportRubController")
public class RapportRubController {

    private final RapportRubDao rapportRubRepository;

    @GetMapping("/findAll")
    public List<RapportRub> getAllRapports() {
        return rapportRubRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RapportRub> getRapportById(@PathVariable Long id) {
        Optional<RapportRub> rapport = rapportRubRepository.findById(id);
        return rapport.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public RapportRub createRapport(@RequestBody RapportRub rapportRub) {
        return rapportRubRepository.save(rapportRub);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RapportRub> updateRapport(@PathVariable Long id, @RequestBody RapportRub updatedRapport) {
        return rapportRubRepository.findById(id).map(rapport -> {
            updatedRapport.setCod_rap(id);
            return ResponseEntity.ok(rapportRubRepository.save(updatedRapport));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRapport(@PathVariable Long id) {
        return (ResponseEntity<Object>) rapportRubRepository.findById(id).map(rapport -> {
            try {
                rapportRubRepository.deleteById(id);
                return ResponseEntity.status(204).body(null); // <- évite le warning
            } catch (DataIntegrityViolationException e) {
                return ResponseEntity
                        .status(500)
                        .body("ORA-02292: Des paramètres dépendent encore de ce rapport");
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}