package com.arabsoft.gestion_adherent.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import com.arabsoft.gestion_adherent.entities.ActiviteFamille;
import com.arabsoft.gestion_adherent.entities.CarteSoinsPers;
import com.arabsoft.gestion_adherent.entities.Famille;
import com.arabsoft.gestion_adherent.exceptions.FamilleCreationException;
import com.arabsoft.gestion_adherent.projections.FamilleProjection;
import com.arabsoft.gestion_adherent.repositories.ActiviteFamilleRepository;
import com.arabsoft.gestion_adherent.repositories.CarteSoinsPersRepository;
import com.arabsoft.gestion_adherent.repositories.FamilleRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/famille")
public class FamilleController {

    private final FamilleRepository familleRepository;
    private final ActiviteFamilleRepository activiteFamilleRepository;
    private final CarteSoinsPersRepository carteSoinsPersRepository;

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
    public void addNewLineEnfant(@RequestBody List<Famille> f) {
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

        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new FamilleCreationException("Échec de l'enregistrement des enfants: " + e.getMessage(), e);
        }
    }

    @PostMapping("/AddCarteSoin")
    public void addCarteSoin(@RequestBody List<CarteSoinsPers> f) {
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
