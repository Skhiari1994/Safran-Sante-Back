package com.arabsoft.reports.controllers;

import com.arabsoft.reports.entities.ParamRub;
import com.arabsoft.reports.repositories.ParamRubDao;
import com.arabsoft.reports.services.DatabaseService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParamRubController")
@SuppressWarnings({ "java:S4684", "java:S1452" })
public class ParamRubController {

    private final ParamRubDao paramRubRepo;

    private final DatabaseService dbService;

    @GetMapping
    public List<ParamRub> getAllParamRub() {
        return paramRubRepo.findAll();
    }

    @GetMapping("/{idRap}")
    public ResponseEntity<ParamRub> getParamRubById(@PathVariable Long idRap) {
        return paramRubRepo.findById(idRap)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createParamRub(@RequestBody ParamRub paramRub) {

        if (paramRub.getCod_param() == null) {
            return ResponseEntity.badRequest().body("cod_param ne peut pas être null");
        }

        Long idRap = dbService.isSqlServer() ? paramRubRepo.getNextSequenceValueSqlServer()
                : paramRubRepo.getNextSequenceValueOracle();
        if (paramRub.getId_rap() == null) {
            paramRub.setId_rap(idRap);
        }

        return ResponseEntity.ok(paramRubRepo.save(paramRub));
    }

    @PutMapping("/{idRap}")
    public ResponseEntity<ParamRub> updateParamRub(@PathVariable Long idRap,
            @RequestBody ParamRub updatedParam) {

        if (!paramRubRepo.existsById(idRap)) {
            return ResponseEntity.notFound().build();
        }

        updatedParam.setId_rap(idRap);
        return ResponseEntity.ok(paramRubRepo.save(updatedParam));
    }

    @DeleteMapping("/{idRap}")
    public ResponseEntity<Void> deleteParamRub(@PathVariable Long idRap) {
        if (!paramRubRepo.existsById(idRap)) {
            return ResponseEntity.notFound().build();
        }
        paramRubRepo.deleteById(idRap);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/GetParametreById/{codRap}")
    public ResponseEntity<List<ParamRub>> getParamByCodRap(@PathVariable Long codRap) {
        List<ParamRub> params = paramRubRepo.getParams(codRap);

        if (params.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(params);
    }
}
