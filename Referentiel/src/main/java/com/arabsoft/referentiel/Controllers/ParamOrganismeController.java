package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.ParamOrganisme;
import com.arabsoft.referentiel.services.ParamOrganismeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParamOrganisme")
public class ParamOrganismeController {

    private static final String INTROUVABLE = "Cet organisme n'existe pas.";
    private static final String CONFLIT = "L'enregistrement est en conflit avec un autre organisme (ajouté en même temps ?) : réessayez.";

    private final ParamOrganismeService service;

    @GetMapping("/all")
    public List<ParamOrganisme> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ParamOrganisme demande) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.add(demande));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CONFLIT);
        }
    }

    @PutMapping("/update/{code}")
    public ResponseEntity<?> update(@PathVariable Integer code, @RequestBody ParamOrganisme demande) {
        try {
            return service.update(code, demande)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(INTROUVABLE));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CONFLIT);
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> delete(@PathVariable Integer code) {
        try {
            if (!service.delete(code)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(INTROUVABLE);
            }
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cet organisme est utilisé ailleurs et ne peut pas être supprimé.");
        }
    }

}
