package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.AdherentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Personnel")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class PersonnelController {

    private final PersonnelRepository personnelRepository;
    private final AdherentService adherentService;
    private final PersAffilRepository persAffilRepository;
    private final RefFiliereRepository refFiliereRepository;
    private final DossierMldRepository dossierMldRepository;
    private final MaladieRepository maladieRepository;
    private final PlafondAssurRepository plafondAssurRepository;
    private final PlafondCnamRepository plafondCnamRepository;
    private final PriseChargeRepository priseChargeRepository;

    @GetMapping("/getPersonnelBultSoin")
    List<PersonnelBultSoinProjection> getPersonnelBultSoin(@RequestParam String soc) {
        return personnelRepository.getPersonnelBultSoin(soc);
    }

    @GetMapping("/getAdherent")
    List<AdherentProjection> getAdherent(@RequestParam String soc, @RequestParam String mat) {
        return personnelRepository.getListAdherent(soc, mat);
    }

    @GetMapping("/getListAdherentDossMld")
    List<AdherentProjection> getListAdherentDossMld(@RequestParam String soc, @RequestParam String mat) {
        return personnelRepository.getListAdherentDossMld(soc, mat);
    }

    @GetMapping("/getPersonnelBultSoinSaisie")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisie(@RequestParam String cod_soc) {
        return personnelRepository.getPersonnelBultSoinSaisie(cod_soc);
    }

    @GetMapping("/getListFamillePersonnelBultSoinLibre")
    List<FamillePersonnelBultSoin> getListFamillePersonnelBultSoinLibre(@RequestParam String cod_soc,
            @RequestParam String pers) {
        return personnelRepository.getListFamillePersonnelBultSoinLibre(cod_soc, pers);
    }

    @GetMapping("/getPersonnelBultSoinSaisieLibre")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieLibre(@RequestParam String cod_soc,
            @RequestParam String ass) {
        return personnelRepository.getPersonnelBultSoinSaisieLibre(cod_soc, ass);
    }

    @GetMapping("/getPersonnelBultSoinSaisieCnam")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieCnam(@RequestParam String cod_soc,
            @RequestParam String ass, @RequestParam String cod_bord) {
        return personnelRepository.getPersonnelBultSoinSaisieCnam(cod_soc, ass, cod_bord);
    }

    @GetMapping("/getFamillePersonnelBultSoinSaisie")
    List<FamillePersonnelBultSoin> getPrestat(@RequestParam String cod_soc, @RequestParam String mat_pers,
            @RequestParam(required = false) String cod_bord) {
        return personnelRepository.getPrestat(cod_soc, mat_pers, cod_bord);
    }

    @GetMapping("/calPlafondMutuelle")
    ResponseProcedure calPlafondMutuelle(@RequestParam String wcodSoc, @RequestParam Long annee,
            @RequestParam String matDeb, @RequestParam String matFin) {
        return adherentService.cal_plafond_mutuelle(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/calPlafondCnam")
    ResponseProcedure calPlafondCnam(@RequestParam String wcodSoc, @RequestParam String annee,
            @RequestParam String matDeb, @RequestParam String matFin) {
        return adherentService.cal_plafond_cnam(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/majPecEnf")
    ResponseProcedure majPecEnf(@RequestParam String wcodSoc, @RequestParam String annee, @RequestParam String matDeb,
            @RequestParam String matFin) {
        return adherentService.maj_pec_enf(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/GetAllPers")
    List<Personnel> getAllPers() {
        return personnelRepository.findAll();
    }

    @GetMapping("/getPersAffil")
    List<PersAffilProjection> getPersAffil(@RequestParam String soc, @RequestParam String mat) {
        return persAffilRepository.getPersAffil(soc, mat);
    }

    @GetMapping("/getRefFilliere")
    List<RefFiliere> getPersAffil() {
        return refFiliereRepository.findAll();
    }

    @PostMapping("/addPersAffil")
    List<PersAffil> addPersAffil(@RequestBody List<PersAffil> persAffil) {
        return persAffilRepository.saveAll(persAffil);
    }

    @GetMapping("/getRefEtablis")
    List<RefEtablisProjection> getRefEtablis() {
        return persAffilRepository.getRefEtablis();
    }

    @GetMapping("/getPersActif")
    List<PersonnelProjection> getPersActif() {
        return personnelRepository.getPersActif();
    }

    @GetMapping("/getDossierMll")
    List<DossierMldProjection> getDossierMll(@RequestParam String soc, @RequestParam String mat) {
        return dossierMldRepository.getDossierMll(soc, mat);
    }

    @GetMapping("/getNumDoss")
    Long getNumDoss(@RequestParam String soc, @RequestParam String mat) {
        return dossierMldRepository.getNumDoss(soc, mat);
    }

    @GetMapping("/getMaladie")
    List<Maladie> getMaladie() {
        return maladieRepository.getMaladie();
    }

    @PostMapping("/addDossMld")
    List<DossierMld> addDossApci(@RequestBody List<DossierMld> dossierMlds) {
        return dossierMldRepository.saveAll(dossierMlds);
    }

    @GetMapping("/getPlafondAssur")
    List<PlafondAssurProjection> getPlafondAssur(@RequestParam String mat) {
        return plafondAssurRepository.getPlafondAssur(mat);
    }

    @GetMapping("/getPlafondCnam")
    List<PlafondCnam> getPlafondCnam(@RequestParam String mat) {
        return plafondCnamRepository.getPlafondCnam(mat);
    }

    @PostMapping("/addPlafAssur")
    PlafondAssur addPlafAssur(@RequestBody PlafondAssur plafondAssur) {
        return plafondAssurRepository.save(plafondAssur);
    }

    @PostMapping("/addPlafCnam")
    PlafondCnam addPlafCnam(@RequestBody PlafondCnam plafondCnam) {
        return plafondCnamRepository.save(plafondCnam);
    }

    @GetMapping("/getPriseCharge")
    List<PriseChargeProjection> getPriseCharge(@RequestParam String soc, @RequestParam String mat) {
        return priseChargeRepository.getPriseCharge(soc, mat);
    }

    @PostMapping("/addPriseCharge")
    List<PriseCharge> addPriseCharge(@RequestBody List<PriseCharge> priseCharge) {
        return priseChargeRepository.saveAll(priseCharge);
    }

    @DeleteMapping("/deletePriseCharge")
    void deletePriseCharge(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("pec") String pec) {
        priseChargeRepository.deletePriseCharge(soc, mat, pec);
    }

    @GetMapping("/getPersBultCnamDeb")
    List<PersonnelProjection> getPersBultCnamDeb(@RequestParam String soc) {
        return this.personnelRepository.getPersBultCnamDeb(soc);
    }

    @GetMapping("/getPersBultCnamFin")
    List<PersonnelProjection> getPersBultCnamFin(@RequestParam String soc) {
        return this.personnelRepository.getPersBultCnamFin(soc);
    }

    @GetMapping("/getListAdherentFamille")
    List<AdherentProjection> getListAdherentFamille(@RequestParam String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam) {
        return this.personnelRepository.getListAdherentFamille(soc, mat, fam);
    }

}
