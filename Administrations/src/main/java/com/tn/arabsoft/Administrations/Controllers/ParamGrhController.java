package com.tn.arabsoft.administrations.controllers;

import com.tn.arabsoft.administrations.entities.ParamGrh;
import com.tn.arabsoft.administrations.services.ParamGrhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParamGrh")
public class ParamGrhController {

    private final ParamGrhService paramGrhService;

    @GetMapping("/all")
    public List<ParamGrh> getAll() {
        return paramGrhService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParamGrh> getById(@PathVariable Integer id) {
        return paramGrhService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-lib/{libParam}")
    public ResponseEntity<ParamGrh> getByLib(@PathVariable String libParam) {
        return paramGrhService.getByLibParam(libParam)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ParamGrh param) {
        try {
            ParamGrh saved = paramGrhService.add(param);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ParamGrh form) {
        try {
            return paramGrhService.update(id, form)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        paramGrhService.delete(id);
        return ResponseEntity.ok().build();
    }
}
