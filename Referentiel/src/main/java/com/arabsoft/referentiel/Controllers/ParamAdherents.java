package com.arabsoft.referentiel.Controllers;

import com.arabsoft.referentiel.Entities.*;
import com.arabsoft.referentiel.Entities.Cle.CleAgence;
import com.arabsoft.referentiel.Entities.Cle.ClePoste;
import com.arabsoft.referentiel.Repositories.*;
import com.arabsoft.referentiel.Services.RefirentielleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/paramAdherent")
public class ParamAdherents {

    @Autowired
    NationaliteRepository nationaliteRepository;
    @Autowired
    GouvernoratRepository gouvernoratRepository;
    @Autowired
    PosteRepository posteRepository;
    @Autowired
    BanqueRepository banqueRepository;
    @Autowired
    AgenceRepository agenceRepository;
    @Autowired
    PrmLieuGeogRepository prmLieuGeogRepository;
    @Autowired
    SocieteRepository societeRepository;
    @Autowired
    AffectationRepository affectationRepository;
    @Autowired
    TypeDepartRepository typeDepartRepository;
    @Autowired
    ActiviteFamilleRepository activiteFamilleRepository;

    @GetMapping("getNationalite")
    List<Nationalite> getNationalites(){
       return nationaliteRepository.findAll();
    }
    @PostMapping("/addNationalite")
    Nationalite savePersonnel(@RequestBody Nationalite personnel){
        return nationaliteRepository.save(personnel);
    }

    @DeleteMapping("/{codNatp}")
    public void deleteNationalite(@PathVariable String codNatp) {
        nationaliteRepository.deleteById(codNatp);
    }
    @GetMapping("/getGouvernorat")
    List<Gouvernorat> getGouvernorat(){
        return gouvernoratRepository.findAll();
    }
    @GetMapping("/getPoste/{gouv}")
    List<Poste> getPoste(@PathVariable String gouv){
        return posteRepository.getPoste(gouv);
    }

    @PostMapping("/addGouv")
    Gouvernorat addGouv(@RequestBody Gouvernorat gouvernorat){
        return gouvernoratRepository.save(gouvernorat);
    }

    @PostMapping("/addPoste")
    public void addPoste(@RequestBody List<Poste> poste){
         posteRepository.saveAll(poste);
    }
    @PostMapping("/deletePoste")
    public void deletePoste(@RequestBody ClePoste poste){
        posteRepository.deleteById(poste);
    }
    @PostMapping("/deleteAgence")
    public void deleteAgence(@RequestBody CleAgence agence){
        agenceRepository.deleteById(agence);
    }
    @GetMapping("/getBanque")
    List<Banque> getBanque(){
        return banqueRepository.findAll();
    }

    @GetMapping("/getAgence/{codBanq}")
    List<Agence> getAgence(@PathVariable String codBanq){
        return agenceRepository.getAgence(codBanq);
    }

    @PostMapping("/addBanq")
    Banque addBanque(@RequestBody Banque banque){
        return banqueRepository.save(banque);
    }

    @PostMapping("/addAgence")
    public void addAgence(@RequestBody List<Agence> f){

        agenceRepository.saveAll(f);

    }
    @PostMapping("/addSociete")
    public void addSociete(@RequestBody Societe f){

        societeRepository.save(f);

    }
    @GetMapping("/getSociete")
    List<Societe> getSociete(){
        return societeRepository.findAll();
    }

    @PostMapping("/addDepartement")
    public void addDepartement(@RequestBody PrmLieuGeographique f){

        prmLieuGeogRepository.save(f);
    }
    @PostMapping("/deleteDep")
    public void deleteDep(@RequestBody String f){

        prmLieuGeogRepository.deleteById(f);
    }
    @GetMapping("/getDepartement")
    List<PrmLieuGeographique> getDepartement(){
        return prmLieuGeogRepository.findAll();
    }


    @PostMapping("/addAffectation")
    public void addAffectation(@RequestBody Affectation f){

        affectationRepository.save(f);
    }
    @PostMapping("/delAffectation")
    public void delAffectation(@RequestBody String f){

        affectationRepository.deleteById(f);
    }
    @GetMapping("/getAffectation")
    List<Affectation> getAffectation(){
        return affectationRepository.findAll();
    }

    @PostMapping("/addTypDepart")
    public void addTypDepart(@RequestBody TypeDepart f){

        typeDepartRepository.save(f);
    }
    @PostMapping("/deleTypeDepart")
    public void deleTypeDepart(@RequestBody String f){

        typeDepartRepository.deleteById(f);
    }
    @GetMapping("/getTypDepart")
    List<TypeDepart> getTypDepart(){
        return typeDepartRepository.findAll();
    }


    @PostMapping("/addActiviteEnf")
    public void addActiviteEnf(@RequestBody ActiviteFamille f){

        activiteFamilleRepository.save(f);
    }
    @PostMapping("/deletActiviteEnf")
    public void deletActiviteEnf(@RequestBody String f){

        activiteFamilleRepository.deleteById(f);
    }
    @GetMapping("/getActiviteEnf")
    List<ActiviteFamille> getActiviteEnf(){
        return activiteFamilleRepository.findAll();
    }



}
