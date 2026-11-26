package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.entities.ParamNotif;
import com.tn.arabsoft.administrations.services.ParamNotifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParamNotif")
public class ParamNotifController {

    private static final String INTROUVABLE = "Cette notification n'existe pas.";

    private final ParamNotifService paramNotifService;

    @GetMapping("/all")
    public List<ParamNotif> getAll() {
        return paramNotifService.getAll();
    }

    @GetMapping("/corps")
    public List<Map<String, Object>> getCorps() {
        return paramNotifService.getCorps();
    }

    @GetMapping("/societes")
    public List<Map<String, Object>> getSocietes() {
        return paramNotifService.getSocietes();
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ParamNotif demande) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(paramNotifService.add(demande));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ParamNotif demande) {
        try {
            return paramNotifService.update(id, demande)
                    .<ResponseEntity<?>>map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(INTROUVABLE));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        if (!paramNotifService.delete(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(INTROUVABLE);
        }
        return ResponseEntity.ok().build();
    }

}
