package com.arabsoft.gestion_adherent.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import com.arabsoft.gestion_adherent.entities.ActiviteFamille;
import com.arabsoft.gestion_adherent.entities.CarteSoinsCnam;
import com.arabsoft.gestion_adherent.entities.CarteSoinsPers;
import com.arabsoft.gestion_adherent.entities.cle.CleCarteSoinsCnam;
import com.arabsoft.gestion_adherent.entities.Famille;
import com.arabsoft.gestion_adherent.exceptions.FamilleCreationException;
import com.arabsoft.gestion_adherent.projections.FamilleProjection;
import com.arabsoft.gestion_adherent.repositories.ActiviteFamilleRepository;
import com.arabsoft.gestion_adherent.repositories.CarteSoinsCnamRepository;
import com.arabsoft.gestion_adherent.repositories.CarteSoinsPersRepository;
import com.arabsoft.gestion_adherent.repositories.FamilleRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import jakarta.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/famille")
public class FamilleController {

    private final FamilleRepository familleRepository;
    private final ActiviteFamilleRepository activiteFamilleRepository;
    private final CarteSoinsPersRepository carteSoinsPersRepository;
    private final CarteSoinsCnamRepository carteSoinsCnamRepository;

    @GetMapping("/getFamille/{codSoc}/{matPers}")
    List<Famille> getFamille(@PathVariable String codSoc, @PathVariable String matPers) {
        System.out.println("getFamille @@@@@@@@@@@@@@@");
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
        // Sexe Pere P : Masculin
        if ("P".equals(f.getParente())
                && (f.getSexe() == null || f.getSexe().isEmpty())) {
            f.setSexe("M");
        }

        // Sexe Mere M : Feminin
        if ("M".equals(f.getParente())
                && (f.getSexe() == null || f.getSexe().isEmpty())) {
            f.setSexe("F");
        }

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

    @GetMapping("/getCarteSoinsCnam/{soc}/{mat}")
    List<CarteSoinsCnam> getCarteSoinsCnam(@PathVariable("soc") String soc, @PathVariable("mat") String mat) {
        return this.carteSoinsCnamRepository.getCarteSoinsCnam(soc, mat);
    }

    /** Cartes CNAM de l'utilisateur connecté : le matricule suffit, la société n'est pas toujours renseignée sur le compte. */
    @GetMapping("/getCarteSoinsCnamUtilisateur")
    List<CarteSoinsCnam> getCarteSoinsCnamUtilisateur(@RequestParam("mat") String mat,
            @RequestParam(value = "soc", required = false) String soc) {
        List<CarteSoinsCnam> cartes = this.carteSoinsCnamRepository.getCarteSoinsCnamParMatricule(mat);
        if (soc == null || soc.isBlank()) {
            return cartes;
        }
        return cartes.stream().filter(c -> soc.equals(c.getCod_soc())).toList();
    }

    private static CleCarteSoinsCnam cleCarteSoinsCnam(String soc, String mat, String nat, LocalDate dateDebut) {
        CleCarteSoinsCnam cle = new CleCarteSoinsCnam();
        cle.setCod_soc(soc);
        cle.setMat_pers(mat);
        cle.setNat_cart(nat);
        cle.setDate_debut(dateDebut);
        return cle;
    }

    private static void validerCarteSoinsCnam(CarteSoinsCnam c) {
        if (c.getCod_soc() == null || c.getMat_pers() == null || c.getNat_cart() == null
                || c.getDat_cart() == null || c.getDate_debut() == null || c.getDate_fin() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Champs obligatoires manquants");
        }
        if (c.getDate_fin().isBefore(c.getDate_debut())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La date de fin doit être postérieure à la date de début");
        }
    }

    /** Refuse une période qui recouvre une autre carte du même adhérent (bornes incluses), hors la carte `ignoree`. */
    private void verifierPasDeChevauchement(CarteSoinsCnam c, CleCarteSoinsCnam ignoree) {
        boolean chevauche = carteSoinsCnamRepository.getCarteSoinsCnam(c.getCod_soc(), c.getMat_pers()).stream()
                .filter(autre -> autre.getNat_cart().equals(c.getNat_cart()))
                .filter(autre -> ignoree == null || !autre.getDate_debut().equals(ignoree.getDate_debut())
                        || !autre.getNat_cart().equals(ignoree.getNat_cart()))
                .anyMatch(autre -> !autre.getDate_debut().isAfter(c.getDate_fin())
                        && !c.getDate_debut().isAfter(autre.getDate_fin()));
        if (chevauche) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cette période chevauche une autre carte de l'adhérent");
        }
    }

    @PostMapping("/addCarteSoinsCnam")
    public CarteSoinsCnam addCarteSoinsCnam(@RequestBody CarteSoinsCnam c) {
        validerCarteSoinsCnam(c);
        if (carteSoinsCnamRepository.existsById(
                cleCarteSoinsCnam(c.getCod_soc(), c.getMat_pers(), c.getNat_cart(), c.getDate_debut()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Une carte existe déjà pour cette date de début");
        }
        verifierPasDeChevauchement(c, null);
        return carteSoinsCnamRepository.save(c);
    }

    /** La date de début fait partie de la clé : si elle change, l'ancienne ligne est supprimée puis recréée. */
    @PutMapping("/updateCarteSoinsCnam/{oldNat}/{oldDateDebut}")
    @Transactional
    public CarteSoinsCnam updateCarteSoinsCnam(@PathVariable("oldNat") String oldNat,
            @PathVariable("oldDateDebut") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate oldDateDebut,
            @RequestBody CarteSoinsCnam c) {
        validerCarteSoinsCnam(c);
        CleCarteSoinsCnam ancienne = cleCarteSoinsCnam(c.getCod_soc(), c.getMat_pers(), oldNat, oldDateDebut);
        if (!carteSoinsCnamRepository.existsById(ancienne)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte introuvable");
        }
        CleCarteSoinsCnam nouvelle = cleCarteSoinsCnam(c.getCod_soc(), c.getMat_pers(), c.getNat_cart(),
                c.getDate_debut());
        verifierPasDeChevauchement(c, ancienne);
        if (!nouvelle.getNat_cart().equals(oldNat) || !nouvelle.getDate_debut().equals(oldDateDebut)) {
            if (carteSoinsCnamRepository.existsById(nouvelle)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Une carte existe déjà pour cette date de début");
            }
            carteSoinsCnamRepository.deleteById(ancienne);
        }
        return carteSoinsCnamRepository.save(c);
    }

    @DeleteMapping("/deleteCarteSoinsCnam/{soc}/{mat}/{nat}/{dateDebut}")
    public void deleteCarteSoinsCnam(@PathVariable("soc") String soc, @PathVariable("mat") String mat,
            @PathVariable("nat") String nat,
            @PathVariable("dateDebut") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate dateDebut) {
        CleCarteSoinsCnam cle = cleCarteSoinsCnam(soc, mat, nat, dateDebut);
        if (!carteSoinsCnamRepository.existsById(cle)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carte introuvable");
        }
        carteSoinsCnamRepository.deleteById(cle);
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
