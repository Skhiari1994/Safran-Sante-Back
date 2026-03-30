package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;

 import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ResponseProcedure;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.AdherentService;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.data.repository.query.Param;
 import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Personnel")
public class PersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    AdherentService adherentService;
    @Autowired
    PersAffilRepository persAffilRepository;
    @Autowired
    RefFiliereRepository refFiliereRepository;
    @Autowired
    DossierMldRepository dossierMldRepository;
    @Autowired















    MaladieRepository maladieRepository;
    @Autowired
    PlafondAssurRepository plafondAssurRepository;
    @Autowired
    PlafondCnamRepository plafondCnamRepository;
    @Autowired
    PriseChargeRepository priseChargeRepository;
    @GetMapping("/getPersonnelBultSoin")
    List<PersonnelBultSoinProjection> getPersonnelBultSoin(@RequestParam String soc){
        return  personnelRepository.getPersonnelBultSoin(soc);
    }
    @GetMapping("/getAdherent")
    List<AdherentProjection> getAdherent(@RequestParam String soc,@RequestParam String mat){
        return  personnelRepository.getListAdherent(soc,mat);
    }
    @GetMapping("/getListAdherentDossMld")
    List<AdherentProjection> getListAdherentDossMld(@RequestParam String soc,@RequestParam String mat){
        return  personnelRepository.getListAdherentDossMld(soc,mat);
    }

    @GetMapping("/getPersonnelBultSoinSaisie")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisie(@RequestParam String cod_soc){
        return  personnelRepository.getPersonnelBultSoinSaisie(cod_soc);
    }
    @GetMapping("/getListFamillePersonnelBultSoinLibre")
    List<FamillePersonnelBultSoin> getListFamillePersonnelBultSoinLibre(@RequestParam String cod_soc,@RequestParam String pers){
        return  personnelRepository.getListFamillePersonnelBultSoinLibre(cod_soc,pers);
    }
    @GetMapping("/getPersonnelBultSoinSaisieLibre")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieLibre(@RequestParam String cod_soc,@RequestParam String ass){
        return  personnelRepository.getPersonnelBultSoinSaisieLibre(cod_soc,ass);
    }
    @GetMapping("/getPersonnelBultSoinSaisieCnam")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieCnam(@RequestParam String cod_soc, @RequestParam String ass, @RequestParam String cod_bord){
        return  personnelRepository.getPersonnelBultSoinSaisieCnam(cod_soc,ass,cod_bord);
    }
    @GetMapping("/getFamillePersonnelBultSoinSaisie")
    List<FamillePersonnelBultSoin> getPrestat(@RequestParam String cod_soc, @RequestParam String mat_pers, @RequestParam(required = false) String cod_bord){
        return  personnelRepository.getPrestat(cod_soc,mat_pers,cod_bord);
    }

    @GetMapping("/calPlafondMutuelle")
    ResponseProcedure cal_plafond_mutuelle(@RequestParam String wcodSoc,@RequestParam Long annee,@RequestParam String matDeb,@RequestParam String matFin){
        return adherentService.cal_plafond_mutuelle(wcodSoc,annee,matDeb,matFin);
    }

    @GetMapping("/calPlafondCnam")
    ResponseProcedure cal_plafond_cnam(@RequestParam String wcodSoc,@RequestParam String annee,@RequestParam String matDeb,@RequestParam String matFin){
        return adherentService.cal_plafond_cnam(wcodSoc,annee,matDeb,matFin);
    }

    @GetMapping("/majPecEnf")
    ResponseProcedure maj_pec_enf(@RequestParam String wcodSoc,@RequestParam String annee,@RequestParam String matDeb,@RequestParam String matFin){
        return adherentService.maj_pec_enf(wcodSoc,annee,matDeb,matFin);
    }

    @GetMapping("/GetAllPers")
    List<Personnel> GetAllPers(){
        return personnelRepository.findAll();
    }

    @GetMapping("/getPersAffil")
    List<PersAffilProjection> getPersAffil(@RequestParam String soc,@RequestParam String mat){
        return persAffilRepository.getPersAffil(soc,mat);
    }

    @GetMapping("/getRefFilliere")
    List<RefFiliere> getPersAffil(){
        return refFiliereRepository.findAll();
    }
    @PostMapping("/addPersAffil")
    List<PersAffil> addPersAffil(@RequestBody List<PersAffil> persAffil){
        return persAffilRepository.saveAll(persAffil);
    }

    @GetMapping("/getRefEtablis")
    List<RefEtablisProjection> getRefEtablis(){
        return persAffilRepository.getRefEtablis();
    }

    @GetMapping("/getPersActif")
    List<PersonnelProjection> getPersActif(){
        return personnelRepository.getPersActif();
    }

    @GetMapping("/getDossierMll")
    List<DossierMldProjection> getDossierMll(@RequestParam String mat){
        return dossierMldRepository.getDossierMll(mat);
    }

    @GetMapping("/getNumDoss")
    Long getNumDoss(@RequestParam String soc,@RequestParam String mat){
        return dossierMldRepository.getNumDoss(soc,mat);
    }

    @GetMapping("/getMaladie")
    List<Maladie> getMaladie(){
        return maladieRepository.getMaladie();
    }

    @PostMapping("/addDossMld")
    List<DossierMld> addDossApci(@RequestBody List<DossierMld> dossierMlds){
        return dossierMldRepository.saveAll(dossierMlds);
    }

    @GetMapping("/getPlafondAssur")
    List<PlafondAssurProjection> getPlafondAssur(@RequestParam String mat){
        return plafondAssurRepository.getPlafondAssur(mat);
    }

    @GetMapping("/getPlafondCnam")
    List<PlafondCnam> getPlafondCnam(@RequestParam String mat){
        return plafondCnamRepository.getPlafondCnam(mat);
    }

    @PostMapping("/addPlafAssur")
    PlafondAssur addPlafAssur(@RequestBody PlafondAssur plafondAssur){
        return plafondAssurRepository.save(plafondAssur);
    }

    @PostMapping("/addPlafCnam")
    PlafondCnam addPlafCnam(@RequestBody PlafondCnam plafondCnam){
        return plafondCnamRepository.save(plafondCnam);
    }

    @GetMapping("/getPriseCharge")
    List<PriseChargeProjection> getPriseCharge(@RequestParam String soc,@RequestParam String mat){
        return priseChargeRepository.getPriseCharge(soc,mat);
    }

    @PostMapping("/addPriseCharge")
    List<PriseCharge> addPriseCharge(@RequestBody List<PriseCharge> priseCharge){
        return priseChargeRepository.saveAll(priseCharge);
    }

    @DeleteMapping("/deletePriseCharge")
    void deletePriseCharge(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("pec")String pec){
        priseChargeRepository.deletePriseCharge(soc,mat,pec);
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
    List<AdherentProjection> getListAdherentFamille(@RequestParam String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam) {
        return this.personnelRepository.getListAdherentFamille(soc,mat,fam);
    }

}
