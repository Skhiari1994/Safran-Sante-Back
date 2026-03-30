package com.arabsoft.referentiel.Controllers;

import com.arabsoft.referentiel.Entities.NatureDon;
import com.arabsoft.referentiel.Entities.NatureDonMotif;
import com.arabsoft.referentiel.Projections.AffectationProjection;
import com.arabsoft.referentiel.Projections.CompteIndParamProjection;
import com.arabsoft.referentiel.Projections.NatureDonProjection;
import com.arabsoft.referentiel.Repositories.NatureDonMotifRepository;
import com.arabsoft.referentiel.Repositories.NatureDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/CompteIndParam")
public class ParamIndemniteController {

    @Autowired
    NatureDonRepository natureDonRepository;
    @Autowired
    NatureDonMotifRepository natureDonMotifRepository;


    @GetMapping("/typind")
    List<CompteIndParamProjection> CompteIndParam(){
        return natureDonRepository.getListCompte();
    }
    @PostMapping("/addNatDon")
    public NatureDon addNatDon(@RequestBody  NatureDon natDonList) {
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
