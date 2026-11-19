package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import com.tn.arabsoft.remboursement_frais_medicaux.service.AnneeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Annee")
@RequiredArgsConstructor
@SuppressWarnings({ "java:S117" })
public class AnneeController {

    private final AnneeService anneeService;

    /**
     * Endpoint retournant la liste distincte des années triées en ordre décroissant
     * présentes dans BULT_SOIN, BULT_ARRIVER, PLAFOND_ASSUR et PLAFOND_CNAM.
     * Exemple d'appel :
     *   GET /Remb/Annee
     *   GET /Remb/Annee?codSoc=01&matPers=1234
     */
    @GetMapping
    public ResponseEntity<List<Integer>> getAnnees(
            @RequestParam(required = false) String codSoc,
            @RequestParam(required = false) String matPers) {
        List<Integer> annees = anneeService.getAnneesDistinctes(codSoc, matPers);
        return ResponseEntity.ok(annees);
    }
}
