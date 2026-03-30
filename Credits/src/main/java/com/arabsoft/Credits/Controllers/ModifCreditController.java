package com.arabsoft.Credits.Controllers;


import com.arabsoft.Credits.DTO.AnticipImputationRequest;
import com.arabsoft.Credits.Entities.EtatPret;
import com.arabsoft.Credits.Entities.EtatPretPers;
import com.arabsoft.Credits.Entities.Response.*;
import com.arabsoft.Credits.Entities.VirAnticip;
import com.arabsoft.Credits.Projections.*;
import com.arabsoft.Credits.Repositories.EtatPretPersRepository;
import com.arabsoft.Credits.Repositories.EtatPretRepository;
import com.arabsoft.Credits.Repositories.PretPersRepository;
import com.arabsoft.Credits.Repositories.VirAnticipRepository;
import com.arabsoft.Credits.Services.AnticipComptaService;
import com.arabsoft.Credits.Services.CreditService;
import com.arabsoft.Credits.Services.ModifCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/ModifCredit")
public class ModifCreditController {
  @Autowired
    PretPersRepository pretPersRepository;
  @Autowired
  EtatPretPersRepository etatPretPersRepository;
@Autowired
  VirAnticipRepository virAnticipRepository;
  @Autowired
  ModifCreditService modifCreditService;

  @Autowired
  EtatPretRepository etatPretRepository;

  @Autowired
  private AnticipComptaService anticipComptaService;
  @GetMapping("/getPretAnticip")
  List<PretAnticipProjection> getPretAnticip(@RequestParam String soc, @RequestParam String mat)
  {
       return pretPersRepository.getPretAnticip(soc,mat);
  }
  @GetMapping("/getPretAnticipByPret/{soc}/{mat}/{codPret}")
  PretAnticipProjection getPretAnticipByPret(@PathVariable String soc, @PathVariable String mat,@PathVariable String codPret)
  {
    return pretPersRepository.getPretAnticipByPret(soc,mat,codPret);
  }
  @PostMapping("/addVirAnticip")
  VirAnticip addVirAnticip(@RequestBody VirAnticip virAnticip)
  {
    return virAnticipRepository.save(virAnticip);
  }
  @GetMapping("/getAllVirAnticip")
  List<VirAnticipProjection> getAllVirAnticip(){
   return this.virAnticipRepository.getAllVirAnticip();
  }
  @GetMapping("/getVirAnticipInst")
  List<VirAnticipProjection> getVirAnticipInst(){
    return this.virAnticipRepository.getVirAnticipInst();
  }
  @GetMapping("/getPretAnticipDetails")
  PretAnticipProjection getDetailPretAnticipDetails(@RequestParam String soc, @RequestParam String mat,@RequestParam String codPret)
  {
     return pretPersRepository.getPretAnticipDetails(soc,mat,codPret);
  }
  @GetMapping("maxNumVir")
  Long getMaxNumVir(@RequestParam String mat)
  {
    return  virAnticipRepository.getMaxNumVir(mat);
  }
  @GetMapping("getVirAnticipByMat")
  List<VirAnticipProjection> getVirAnticipByMat(@RequestParam String mat)
  {
    return  virAnticipRepository.getVirAnticipByMat(mat);
  }
  @GetMapping("getnuEtatPret")
  Long getnuEtatPret(@RequestParam String soc)
  {
    return  etatPretPersRepository.getnuEtatPret(soc);
  }
  @GetMapping("getEtatPretPers")
  List<EtatPretPersProjection> getEtatPretPers()
  {
    return  etatPretPersRepository.getEtatPretPers();
  }
  @GetMapping("getEtatPretPersSusp")
  List<EtatPretPersProjection> getEtatPretPersSusp()
  {
    return  etatPretPersRepository.getEtatPretPersSusp();
  }
  @GetMapping("/Controller")
  ControllerResponse Controller(@RequestParam String wcodSoc,@RequestParam String wmatPers,@RequestParam String cod_pret_,@RequestParam String mois_debut,@RequestParam String mois_fin)
  {
    return modifCreditService.controler(wcodSoc,wmatPers,cod_pret_,mois_debut,mois_fin);
  }
  @GetMapping("/periode_ant_tot")
  PeriodeAnticipTotalReponse periode_ant_tot(@RequestParam String wcodSoc, @RequestParam String wmatPers, @RequestParam String cod_pret_)
  {
    return modifCreditService.periode_ant_tot(wcodSoc,wmatPers,cod_pret_);
  }

  @GetMapping("/calcul_ant_part")
  CalculAnticipPart calcul_ant_part(@RequestParam String wcodSoc, @RequestParam String wmatPers, @RequestParam String cod_pret_, @RequestParam String dat_debut_etat_, @RequestParam String dat_fin_etat_,@RequestParam String typ_anticip)
  {
    return modifCreditService.calcul_ant_part(wcodSoc,wmatPers,cod_pret_,dat_debut_etat_,dat_fin_etat_,typ_anticip);
  }

  @GetMapping("/calcul")
  BigDecimal calcul_ant_part(@RequestParam String wcodSoc, @RequestParam String wmatPers, @RequestParam String cod_pret_, @RequestParam String dat_debut_, @RequestParam String dat_fin_)
  {
    return modifCreditService.calcul(wcodSoc,wmatPers,cod_pret_,dat_debut_,dat_fin_);
  }
  @GetMapping("/Valider")
  ValiderReponse Valider(@RequestParam String wcodSoc,@RequestParam  String wmatPers,@RequestParam  String cod_pret_, @RequestParam String mod_pay_ ,
                                 @RequestParam   String nat_etat_,@RequestParam String typ_anticip,@RequestParam  String dat_deb_,@RequestParam  String dat_fin_,
                                 @RequestParam  BigDecimal mnt_capital_,@RequestParam BigDecimal mnt_anticip_,@RequestParam BigDecimal mnt_interet_,
                                 @RequestParam  BigDecimal prt_rendu_)
  {
    return modifCreditService.Valider(wcodSoc,wmatPers,cod_pret_,mod_pay_,nat_etat_,typ_anticip,dat_deb_,dat_fin_,mnt_capital_,mnt_anticip_,mnt_interet_,prt_rendu_);
  }
  @PostMapping("/AddEtatPretPers")
  EtatPretPers AddEtatPretPers(@RequestBody EtatPretPers pretPers ){


    return etatPretPersRepository.save(pretPers);
  }
  @GetMapping("/reechelonner")
  ResponseProcedure reechelonner(@RequestParam String wcodSoc,@RequestParam String wmatPers,@RequestParam String codPret,@RequestParam String lPret)
  {
   return modifCreditService.reechelonner(wcodSoc,wmatPers,codPret,lPret);
  }
  @GetMapping("/reechelonner_2")
  ResponseProcedure reechelonner_2(@RequestParam String wcodSoc,@RequestParam String wmatPers,@RequestParam String codPret)
  {
    return modifCreditService.reechelonner_2(wcodSoc,wmatPers,codPret);
  }
  public static LocalDate setFirstDayOfMonth(LocalDate date) {
    return date.withDayOfMonth(1); // Remplace le jour par "01"
  }

  @GetMapping("/getPretReech/{soc}")
  public ResponseEntity<List<PretPErsProjection>> getPretReech(
          @PathVariable String soc)
  {
    List<PretPErsProjection> pretPersPage = pretPersRepository.getPretReech(soc);
    return ResponseEntity.ok(pretPersPage);
  }

  @GetMapping("/getPretReech/{soc}/{mat}")
  public ResponseEntity<List<PretReechProjection>> getPretReech(
          @PathVariable String soc,
          @PathVariable String mat)
  {
    List<PretReechProjection> pretPersPage = pretPersRepository.getDetailPretReech(soc,mat);
    return ResponseEntity.ok(pretPersPage);
  }

  @GetMapping("/getDetailCreditReech")
  ReponseCalculDetailCredit calcul_detail_credit_reech(@RequestParam String wcodSoc,@RequestParam String wdatfin,@RequestParam String wdatretr,@RequestParam Double wprtmntglb,@RequestParam Double wprtech,@RequestParam Double wprttaux,@RequestParam BigDecimal wmntreport,@RequestParam String codGrpPret)
  {
    return modifCreditService.calcul_detail_credit_reech(wcodSoc,wdatfin,wdatretr,wprtmntglb,wprtech,wprttaux,wmntreport,codGrpPret);
  }

  @GetMapping("/calcul_interet")
  ReponseCalcInteret calcul_interet(@RequestParam String wprt_ech,@RequestParam String wprt_mnt_glb, @RequestParam String wprt_taux
          ,@RequestParam String wprt_mnt_rem,@RequestParam String wPRT_INTERET,@RequestParam String wrem_men)
  {
    return modifCreditService.calcul_interet(wprt_ech,wprt_mnt_glb,wprt_taux,wprt_mnt_rem,wPRT_INTERET,wrem_men);
  }

  @GetMapping("/insertRembTranch")
  void insertRembTranch(@RequestParam String wcodSoc, @RequestParam String wmat_pers, @RequestParam String wcod_pret, @RequestParam String wcod_etat_pret,
                        @RequestParam String wprt_dat_acc,@RequestParam String wprt_mnt_rem,@RequestParam Long wnbr_tranche)
  {
    modifCreditService.insert_remb_tranch(wcodSoc,wmat_pers,wcod_pret,wcod_etat_pret,wprt_dat_acc,wprt_mnt_rem,wnbr_tranche);
  }

  @GetMapping("/susp_pret")
  void susp_pret(@RequestParam String wcodSoc, @RequestParam String wmat_pers, @RequestParam String wcod_pret_ant, @RequestParam String wcod_pret)
  {
    modifCreditService.susp_pret(wcodSoc,wmat_pers,wcod_pret_ant,wcod_pret);
  }

  @GetMapping("/getEtatPretSusp")
  public ResponseEntity<List<EtatPret>> getEtatPretSusp()
  {
    List<EtatPret> etat = etatPretRepository.getEtatPretSusp();
    return ResponseEntity.ok(etat);
  }

  @GetMapping("/getEtatPretSusp/{etat}")
  public ResponseEntity<EtatPret> getEtatPretSusp(@PathVariable String etat)
  {
    EtatPret etatPret = etatPretRepository.getEtatPretSuspByEtat(etat);
    return ResponseEntity.ok(etatPret);
  }
  @GetMapping("/ValiderSusp")
  ValidSuspReponse ValiderSusp(@RequestParam String wcodSoc,@RequestParam  String wmatPers,@RequestParam  String cod_pret_,@RequestParam String num_etat_pret ,
                         @RequestParam String etat_pret,@RequestParam String nat_etat,@RequestParam String typ_anticip,@RequestParam String dat_deb_,@RequestParam String dat_fin_  )
  {
    return modifCreditService.Valider_susp(wcodSoc,wmatPers,cod_pret_,num_etat_pret,etat_pret,nat_etat,typ_anticip,dat_deb_,dat_fin_);
  }

  @PostMapping("/imputationAnticipation")
  public ResponseEntity<ResponseProcedure> imputationAnticipation(
          @RequestBody AnticipImputationRequest request) {

    ResponseProcedure response = anticipComptaService.imputationAnticipation(
            request.getCodSoc(),
            request.getMatPers(),
            request.getNumVir(),
            request.getRefMetier(),
            request.getMontVir(),
            request.getMntEsp(),
            request.getRestVir()
    );

    if (response.getMessage() != null && response.getMessage().startsWith("ERROR")) {
      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(response);
  }
  @GetMapping("/imputationAnticipationSimple")
  public ResponseEntity<ResponseProcedure> imputationAnticipationSimple(
          @RequestParam String codSoc,
          @RequestParam String matPers,
          @RequestParam String numVir,
          @RequestParam String refMetier,
          @RequestParam BigDecimal montVir,
          @RequestParam(required = false) BigDecimal mntEsp,
          @RequestParam(required = false) BigDecimal restVir) {

    ResponseProcedure response = anticipComptaService.imputationAnticipation(
            codSoc,
            matPers,
            numVir,
            refMetier,
            montVir,
            mntEsp != null ? mntEsp : BigDecimal.ZERO,
            restVir != null ? restVir : BigDecimal.ZERO
    );

    if (response.getMessage() != null && response.getMessage().startsWith("ERROR")) {
      return ResponseEntity.badRequest().body(response);
    }

    return ResponseEntity.ok(response);
  }
  @GetMapping("/getVirementsByMat")
  public ResponseEntity<List<VirAnticipProjection>> getVirementsByMat(
          @RequestParam String codSoc,
          @RequestParam String matPers) {

    List<VirAnticipProjection> virements = anticipComptaService.loadVirementsByMat(codSoc, matPers);
    return ResponseEntity.ok(virements);
  }
}
