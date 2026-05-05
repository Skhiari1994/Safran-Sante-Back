package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.*;
import com.arabsoft.referentiel.entities.cle.CleAgence;
import com.arabsoft.referentiel.entities.cle.ClePoste;
import com.arabsoft.referentiel.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paramAdherent")
public class ParamAdherents {

    private final NationaliteRepository nationaliteRepository;
    private final GouvernoratRepository gouvernoratRepository;
    private final PosteRepository posteRepository;
    private final BanqueRepository banqueRepository;
    private final AgenceRepository agenceRepository;
    private final PrmLieuGeogRepository prmLieuGeogRepository;
    private final SocieteRepository societeRepository;
    private final AffectationRepository affectationRepository;
    private final TypeDepartRepository typeDepartRepository;
    private final ActiviteFamilleRepository activiteFamilleRepository;

    @GetMapping("getNationalite")
    List<Nationalite> getNationalites() {
        return nationaliteRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Nationalite::getCod_natp))
                .toList();
    }

    @PostMapping("/addNationalite")
    Nationalite savePersonnel(@RequestBody Nationalite personnel) {
        return nationaliteRepository.save(personnel);
    }

    @DeleteMapping("/{codNatp}")
    public void deleteNationalite(@PathVariable String codNatp) {
        nationaliteRepository.deleteById(codNatp);
    }

    @GetMapping("/getGouvernorat")
    List<Gouvernorat> getGouvernorat() {
        return gouvernoratRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Gouvernorat::getCod_gouv))
                .toList();
    }

    @GetMapping("/getPoste/{gouv}")
    List<Poste> getPoste(@PathVariable String gouv) {
        return posteRepository.getPoste(gouv);
    }

    @PostMapping("/addGouv")
    Gouvernorat addGouv(@RequestBody Gouvernorat gouvernorat) {
        return gouvernoratRepository.save(gouvernorat);
    }

    @PostMapping("/addPoste")
    public void addPoste(@RequestBody List<Poste> poste) {
        posteRepository.saveAll(poste);
    }

    @PostMapping("/deletePoste")
    public void deletePoste(@RequestBody ClePoste poste) {
        posteRepository.deleteById(poste);
    }

    @PostMapping("/deleteAgence")
    public void deleteAgence(@RequestBody CleAgence agence) {
        agenceRepository.deleteById(agence);
    }

    @GetMapping("/getBanque")
    List<Banque> getBanque() {
        return banqueRepository.findAll();
    }

    @GetMapping("/getAgence/{codBanq}")
    List<Agence> getAgence(@PathVariable String codBanq) {
        return agenceRepository.getAgence(codBanq);
    }

    @PostMapping("/addBanq")
    Banque addBanque(@RequestBody Banque banque) {
        return banqueRepository.save(banque);
    }

    @PostMapping("/addAgence")
    public void addAgence(@RequestBody List<Agence> f) {

        agenceRepository.saveAll(f);

    }

    @PostMapping("/addSociete")
    public void addSociete(@RequestBody Societe f) {

        societeRepository.save(f);

    }

    @GetMapping("/getSociete")
    List<Societe> getSociete() {
        return societeRepository.findAll();
    }

    @PostMapping("/addDepartement")
    public void addDepartement(@RequestBody PrmLieuGeographique f) {

        prmLieuGeogRepository.save(f);
    }

    @PostMapping("/deleteDep")
    public void deleteDep(@RequestBody String f) {

        prmLieuGeogRepository.deleteById(f);
    }

    @GetMapping("/getDepartement")
    List<PrmLieuGeographique> getDepartement() {
        return prmLieuGeogRepository.findAll();
    }

    @PostMapping("/addAffectation")
    public void addAffectation(@RequestBody Affectation f) {

        affectationRepository.save(f);
    }

    @PostMapping("/delAffectation")
    public void delAffectation(@RequestBody String f) {

        affectationRepository.deleteById(f);
    }

    @GetMapping("/getAffectation")
    List<Affectation> getAffectation() {
        return affectationRepository.findAll();
    }

    @PostMapping("/addTypDepart")
    public void addTypDepart(@RequestBody TypeDepart f) {

        typeDepartRepository.save(f);
    }

    @PostMapping("/deleTypeDepart")
    public void deleTypeDepart(@RequestBody String f) {

        typeDepartRepository.deleteById(f);
    }

    @GetMapping("/getTypDepart")
    List<TypeDepart> getTypDepart() {
        return typeDepartRepository.findAll();
    }

    @PostMapping("/addActiviteEnf")
    public void addActiviteEnf(@RequestBody ActiviteFamille f) {

        activiteFamilleRepository.save(f);
    }

    @PostMapping("/deletActiviteEnf")
    public void deletActiviteEnf(@RequestBody String f) {

        activiteFamilleRepository.deleteById(f);
    }

    @GetMapping("/getActiviteEnf")
    List<ActiviteFamille> getActiviteEnf() {
        return activiteFamilleRepository.findAll();
    }

}
