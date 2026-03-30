package com.arabsoft.Gestion_adherent.Controllers;

import com.arabsoft.Gestion_adherent.Entities.ActiviteFamille;
import com.arabsoft.Gestion_adherent.Entities.CarteSoinsPers;
import com.arabsoft.Gestion_adherent.Entities.Famille;
import com.arabsoft.Gestion_adherent.Projections.FamilleProjection;
import com.arabsoft.Gestion_adherent.Repositories.ActiviteFamilleRepository;
import com.arabsoft.Gestion_adherent.Repositories.CarteSoinsPersRepository;
import com.arabsoft.Gestion_adherent.Repositories.FamilleRepository;
import com.arabsoft.Gestion_adherent.Services.FamilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/famille")
public class FamilleController {
    @Autowired
    FamilleService familleService;
    @Autowired
    FamilleRepository familleRepository;
    @Autowired
    ActiviteFamilleRepository activiteFamilleRepository;
    @Autowired
    CarteSoinsPersRepository carteSoinsPersRepository;

    @GetMapping("/getFamille/{codSoc}/{matPers}")
    List<Famille> getFamille(@PathVariable String codSoc, @PathVariable String matPers) {
        return this.familleRepository.getFamille(codSoc, matPers);
    }

    @GetMapping("/getMere/{codSoc}/{matPers}")
    Famille getFamilleMere(@PathVariable String codSoc, @PathVariable String matPers) {
        return this.familleRepository.getMere(codSoc, matPers);
    }

    @GetMapping("/getPere/{codSoc}/{matPers}")
    Famille getFamillePere(@PathVariable String codSoc, @PathVariable String matPers) {
        return this.familleRepository.getPere(codSoc, matPers);
    }

    @GetMapping("/getConjoint/{codSoc}/{matPers}")
    Famille getConjoint(@PathVariable String codSoc, @PathVariable String matPers) {
        return this.familleRepository.getConjoint(codSoc, matPers);
    }

    @PostMapping("/saveFam")
    public void save(@RequestBody Famille f) {

        this.familleRepository.save(f);
    }

    @GetMapping("/getEnfants/{codSoc}/{matPers}")
    List<FamilleProjection> getEnfants(@PathVariable String codSoc, @PathVariable String matPers) {
        return this.familleRepository.getEnfants(codSoc, matPers);
    }

    /*
     * @GetMapping("/getConjoint/{codSoc}/{matPers}")
     * List<FamilleProjection> getConjoint(@PathVariable String
     * codSoc, @PathVariable String matPers){
     * return this.familleRepository.getEnfants(codSoc,matPers);
     * }
     */
    @GetMapping("/getActivite")
    List<ActiviteFamille> getActivite() {
        return this.activiteFamilleRepository.findAll();
    }

    @GetMapping("/getcarteSoinsPers/{soc}/{mat}")
    List<CarteSoinsPers> getcarteSoinsPers(@PathVariable("soc") String soc, @PathVariable("mat") String mat) {
        return this.carteSoinsPersRepository.getCarteSoinsPers(soc, mat);
    }

    @GetMapping("/getMaxNumFamEnfants/{soc}/{mat}")
    Long getMaxNumFamEnfants(@PathVariable("soc") String soc, @PathVariable("mat") String mat) {
        return this.familleRepository.getmaxEnfant(soc, mat);
    }

    @PostMapping("/AddEnfant")
    public void AddNewLineEnfant(@RequestBody List<Famille> f) {
        try {
            // Get the cod_soc and mat_pers from the first element (all should have the same
            // values)
            if (f == null || f.isEmpty()) {
                throw new IllegalArgumentException("Liste des enfants est vide");
            }

            String codSoc = f.get(0).getCod_soc();
            String matPers = f.get(0).getMat_pers();

            // Get the maximum num_fam once for efficiency
            Long maxNumFam = familleRepository.getmaxEnfant(codSoc, matPers);
            Long nextNumFam = (maxNumFam == null) ? 1L : maxNumFam + 1;

            // Set parente and num_fam for each enfant
            for (int i = 0; i < f.size(); i++) {
                f.get(i).setParente("E");

                // If num_fam is not already set, assign the next available number
                if (f.get(i).getNum_fam() == null) {
                    f.get(i).setNum_fam(nextNumFam);
                    nextNumFam++;
                }
            }

            // Save all enfants in a single operation
            familleRepository.saveAll(f);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout des enfants: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Échec de l'enregistrement des enfants: " + e.getMessage(), e);
        }
    }

    @PostMapping("/AddCarteSoin")
    public void AddCarteSoin(@RequestBody List<CarteSoinsPers> f) {

        carteSoinsPersRepository.saveAll(f);

    }

    @DeleteMapping("/{soc}/{num}/{mat}")
    public void deletFam(@PathVariable("soc") String soc, @PathVariable("num") Long num,
            @PathVariable("mat") String mat) {
        familleRepository.deleteFamille(soc, num, mat);
    }

    @GetMapping("/getNbreEnf")
    Long getNbreEnf(@RequestParam("soc") String soc, @RequestParam("mat") String mat) {
        return this.familleRepository.countNbreEnf(soc, mat);
    }
}
