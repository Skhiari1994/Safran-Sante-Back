package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.NatureDon;
import com.arabsoft.referentiel.entities.NatureDonMotif;
import com.arabsoft.referentiel.projections.AffectationProjection;
import com.arabsoft.referentiel.projections.CompteIndParamProjection;
import com.arabsoft.referentiel.projections.NatureDonProjection;
import com.arabsoft.referentiel.repositories.NatureDonMotifRepository;
import com.arabsoft.referentiel.repositories.NatureDonRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/CompteIndParam")
@SuppressWarnings({ "java:S100", "java:S117" })
public class ParamIndemniteController {

    private final NatureDonRepository natureDonRepository;

    private final NatureDonMotifRepository natureDonMotifRepository;

    @GetMapping("/typind")
    List<CompteIndParamProjection> CompteIndParam() {
        return natureDonRepository.getListCompte();
    }

    @PostMapping("/addNatDon")
    public NatureDon addNatDon(@RequestBody NatureDon natDonList) {
        return natureDonRepository.save(natDonList);
    }

    @GetMapping("/getNatDon")
    public List<NatureDon> getNatDon() {
        return natureDonRepository.findAll();
    }

    @GetMapping("/affectInd")
    public List<AffectationProjection> getAffectation() {
        return natureDonRepository.getAffectation();
    }

    @PostMapping("/addMotifs")
    public List<NatureDonMotif> addMotifs(@RequestBody List<NatureDonMotif> natDonMotifList) {
        return natureDonMotifRepository.saveAll(natDonMotifList);
    }

    @GetMapping("/getNatDonByNat")
    public List<NatureDonProjection> getNatDonByNat(@RequestParam String nat) {
        return natureDonRepository.getAffectationByNat(nat);
    }

    @DeleteMapping("/deletNat/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable String id) {
        if (natureDonRepository.existsById(id)) {
            natureDonRepository.deleteById(id);
            return ResponseEntity.ok("Item deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Item not found with ID: " + id);
        }
    }

}
