package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.BulletinSoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/BultSoin")
public class BulletinSoinController {

    @Autowired
    BulletinSoinService bulletinSoinService;
    @Autowired
    BultSoinRepository bultSoinRepository;
    @Autowired
    LigBultRepository ligBultRepository;
    @Autowired
    RefEtablisRepository refEtablisRepository;
    @Autowired
    LigPharRepository ligPharRepository;
    @Autowired
    ActeRepository acteRepository;
    @Autowired
    RegimeRembRepository regimeRembRepository;
    @Autowired
    LigBultVisitRepository ligBultVisitRepository;
    @Autowired
    LigBultActRepository ligBultActRepository;
    @Autowired
    LigBultMedRepository ligBultMedRepository;
    @Autowired
    LigBultAppRepository ligBultAppRepository;

    @Autowired RefVisitRepository refVisitRepository;
    @GetMapping("/getDatNaiss")
    ReponseDatNais getDatNaiss(@RequestParam String soc , @RequestParam String mat,@RequestParam String numFam){
        return bulletinSoinService.get_dat_nais(soc,mat,numFam);
    }

    @GetMapping("/getDatSexe")
    ReponseGetSexe getDatSexe(@RequestParam String soc , @RequestParam String mat, @RequestParam String numFam){
        return bulletinSoinService.get_sexe(soc,mat,numFam);
    }

    @GetMapping("/get_duree_bulletin")
    ResponseProcedure get_duree_bulletin(@RequestParam String wdat_soin , @RequestParam String wCOD_ASSUR){
        return bulletinSoinService.get_duree_bulletin(wdat_soin,wCOD_ASSUR);
    }
    @GetMapping("/get_plafond_mutuelle")
    ReponseGetPlafondMutuelle get_plafond_mutuelle(@RequestParam String soc , @RequestParam String mat,@RequestParam String wdat_soin ,
                                                   @RequestParam String datNais ,@RequestParam String datSaisie){
        return bulletinSoinService.get_plafond_mutuelle(soc,mat,wdat_soin,datNais,datSaisie);
    }
    @GetMapping("/get_plafond_cnam")
    ReponseGetPlafondMutuelle get_plafond_cnam(@RequestParam String soc , @RequestParam String mat,@RequestParam String wdat_soin ,
                                                   @RequestParam String datNais ,@RequestParam String codAssur ,@RequestParam String datSaisie){
        return bulletinSoinService.get_plafond_cnam(soc,mat,wdat_soin,datNais,codAssur,datSaisie);
    }
    @GetMapping("/getNbreBult")
    Long getNbreBult(@RequestParam String codAssur , @RequestParam String numSoin){
        return bultSoinRepository.getNbreBult(codAssur,numSoin);
    }

    @GetMapping("/getListEtablisPec")
    List<PECProjection> getListEtablisPec(@RequestParam String soc , @RequestParam String mat, @RequestParam String numFam, @RequestParam String datSoin){
        return bultSoinRepository.getListEtablisPec(soc,mat,numFam,datSoin);
    }

    @GetMapping("/getPersCons")
    List<PersConsProjection> getPersCons(@RequestParam String soc){
        return bultSoinRepository.getPersCons(soc);
    }

    @GetMapping("/getBultSoin")
    List<BultSoinProjection> getBultSoin(){
        return bultSoinRepository.getBulletinSoin();
    }

    @GetMapping("/getBultSoinCons")
    public List<BultSoinProjection> getBultSoinCons(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String num_fam,
            @RequestParam(required = false) String dat_soin) {
        return bultSoinRepository.getBulletinSoinCons(mat, num_fam, dat_soin);
    }
    @GetMapping("/getBultCnamCons")
    public List<BultSoinProjection> getBultCnamCons(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String num_fam,
            @RequestParam(required = false) String dat_soin) {
        return bultSoinRepository.getBulletinCnamCons(mat, num_fam, dat_soin);
    }
    @GetMapping("/getLigBult")
   public List<LigBult> getLigBult(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultRepository.getLigBult(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultCons")
    public List<LigBultProjection> getLigBultCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultRepository.getLigBultCons(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getAct")
    List<ActeProjection> getActe(@RequestParam("codFil")String codFil, @RequestParam("codAssur") String codAssur,
                                 @RequestParam("parente")String parente, @RequestParam("sexe")String sexe){
        return bultSoinRepository.getActe(codFil,codAssur,parente,sexe);
    }
    @GetMapping("/calculer_montant_net")
    ReponseCalculMntNet calculer_montant_net(@RequestParam String soc, @RequestParam String mat, @RequestParam String codFil, @RequestParam String codAssur,
                                                   @RequestParam String abrvAct, @RequestParam String datSoin, @RequestParam BigDecimal mntHnor, @RequestParam Long indice){
        return bulletinSoinService.calculer_montant_net(soc,mat,codFil,codAssur,abrvAct,datSoin,mntHnor,indice);
    }

    @GetMapping("/refEtablis")
    List<RefEtablis> refEtablis(@RequestParam String refTyp){
        return refEtablisRepository.getRefEtablis(refTyp);
    }
    @GetMapping("/refEtablisByCode")
    RefEtablis refEtablisByCode(@RequestParam String prfCod,@RequestParam String refTyp){
        return refEtablisRepository.getRefEtablisByCode(prfCod,refTyp);
    }
    @PostMapping("/addBultSoin")
    public BultSoin addBultSoin(@RequestBody BultSoin bultSoin) {
        BultSoinCle id = new BultSoinCle(
                bultSoin.getCod_soc(),
                bultSoin.getMat_pers(),
                bultSoin.getNum_fam(),
                bultSoin.getDat_soin()
         );

        if (bultSoinRepository.existsById(id)) {
            // Mise à jour si déjà existant
            BultSoin existingBult = bultSoinRepository.findById(id).get();
            existingBult.setTot_honor(bultSoin.getTot_honor());  // Exemple de mise à jour
            existingBult.setTot_net(bultSoin.getTot_net());
            existingBult.setObs(bultSoin.getObs());
            return bultSoinRepository.save(existingBult);
        } else {
            // Nouvelle insertion
            return bultSoinRepository.save(bultSoin);
        }
    }

    @PostMapping("/addLigBult")
    List<LigBult> addLigBult(@RequestBody List<LigBult> ligBults){
        return ligBultRepository.saveAll(ligBults);
    }

    @GetMapping("getAllActe")
    List<Acte> getAllActe(){
        return acteRepository.findAll();
    }

    @GetMapping("/getMed")
    List<LigPharProjection> getMed(@RequestParam String codFil, @RequestParam String codAssur){
        return ligPharRepository.getMed(codFil,codAssur);
    }
    @GetMapping("/getLigPhar")
    List<LigPhar> getMed(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligPharRepository.getLigPhar(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigPharCons")
    List<LigPharProjectionCons> getLigPharCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligPharRepository.getLigPharCons(soc,mat,numFam,datSoin);
    }
    @PostMapping("/addLigPhar")
    List<LigPhar> addLigPhar(@RequestBody List<LigPhar> ligPhars){
        return ligPharRepository.saveAll(ligPhars);
    }

    @GetMapping("/verif_indice")
    ResponseProcedure verif_indice(@RequestParam String abrv_act_,@RequestParam String cod_fil_,@RequestParam  String cod_assur_,@RequestParam String indice){
        return bulletinSoinService.verif_indice(abrv_act_,cod_fil_,cod_assur_,indice);
    }

    @GetMapping("/verif_vign")
    ResponseProcedure verif_vign(@RequestParam String abrv_act_,@RequestParam String cod_fil_,@RequestParam  String cod_assur_,@RequestParam BigDecimal mnt_honor_
            ,@RequestParam String nbr_vign_){
        return bulletinSoinService.verif_vign(abrv_act_,cod_fil_,cod_assur_,mnt_honor_,nbr_vign_);
    }
    @GetMapping("/verif_piece")
    ResponseProcedure verif_piece(@RequestParam String abrv_act_,@RequestParam String cod_fil_,@RequestParam  String cod_assur_,@RequestParam BigDecimal mnt_honor_
            ,@RequestParam String nbr_piece_){
        return bulletinSoinService.verif_piece(abrv_act_,cod_fil_,cod_assur_,mnt_honor_,nbr_piece_);
    }


    @DeleteMapping("/deleteLigBult")
    void deleteLigBult(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("abrv")String abrv,@RequestParam("numLig")String numLig){
     ligBultRepository.deleteLigBult(soc,mat,fam,datSoin,abrv,numLig);
    }

    @DeleteMapping("/deleteLigPhar")
    void deleteLigPhar(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("med")String med,@RequestParam("numLig")String numLig){
        ligPharRepository.deleteLigPhar(soc,mat,fam,datSoin,med,numLig);
    }

    @GetMapping("/getRegimeRemb")
    List<RegimeRemb> getRegimeRemb(){
        return  regimeRembRepository.getRegimeRemb();
    }


    @GetMapping("/vetif_mod_remb")
    ReponseVetifModRemb vetif_mod_remb(@RequestParam String soc,@RequestParam  String mat,@RequestParam  String numFam,@RequestParam  String datSoin,
                                       @RequestParam String reg_remb_,@RequestParam String parente,@RequestParam String sexe){
        return bulletinSoinService.vetif_mod_remb(soc,mat,numFam,datSoin,reg_remb_,parente,sexe);
    }

    @GetMapping("/getVisit")
    List<VisitProjection> getVisit(@RequestParam String parente,@RequestParam String sexe,@RequestParam String codFil,@RequestParam  String codAssur){
        return refVisitRepository.getVisit(parente,sexe,codFil,codAssur);
    }
    @GetMapping("/getVisitManuel")
    List<VisitProjection> getVisitManuel(@RequestParam String codFil,@RequestParam  String codAssur){
        return refVisitRepository.getVisitManuel(codFil,codAssur);
    }
    @GetMapping("/getAppareil")
    List<AppareilProjection> getAppareil(@RequestParam String codFil,@RequestParam  String codAssur){
        return refVisitRepository.getAppareil(codFil,codAssur);
    }

    @GetMapping("/calculer_montant_net_app")
    ReponseCalculMntNet calculer_montant_net_app(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam, @RequestParam String datSoin,
                                              @RequestParam BigDecimal mntHnor, @RequestParam String codApp,Long MutMntNet){
        return bulletinSoinService.calculer_montant_net_app(soc,mat,numFam,datSoin,mntHnor,codApp,MutMntNet);
    }

    @GetMapping("/calculer_montant_net_visit")
    ReponseCalculMntNet calculer_montant_net_visit(@RequestParam String soc, @RequestParam String mat, @RequestParam String codVisit,
                                                 @RequestParam BigDecimal mntHnor,@RequestParam String datSoin, @RequestParam BigDecimal prixVisit,
                                                   @RequestParam BigDecimal tauxRemb,@RequestParam(required = false) BigDecimal sauvNet){
        return bulletinSoinService.calculer_montant_net_visit(soc,mat,codVisit,mntHnor,datSoin,prixVisit,tauxRemb,sauvNet);
    }

    @GetMapping("/calculer_montant_net_act")
    ReponseCalculMntNet calculer_montant_net_act(@RequestParam String soc, @RequestParam String mat, @RequestParam String codAct,
                                                   @RequestParam BigDecimal mntHnor,@RequestParam String datSoin, @RequestParam BigDecimal actPrix,
                                                   @RequestParam BigDecimal tauxAct,@RequestParam(required = false) BigDecimal cumulNet,@RequestParam(required = false) BigDecimal sauvNet){
        return bulletinSoinService.calculer_montant_net_act(soc,mat,codAct,mntHnor,datSoin,actPrix,tauxAct,cumulNet,sauvNet);
    }

    @GetMapping("/calculer_montant_net_med")
    ReponseCalculMntNet calculer_montant_net_med(@RequestParam String soc, @RequestParam String mat, @RequestParam String codMed,
                                                 @RequestParam BigDecimal mntHnor,@RequestParam String datSoin, @RequestParam BigDecimal prixRemb,
                                                 @RequestParam Long indice,@RequestParam(required = false) BigDecimal cumulNet,@RequestParam(required = false) BigDecimal sauvNet){
        return bulletinSoinService.calculer_montant_net_med(soc,mat,codMed,mntHnor,datSoin,prixRemb,indice,cumulNet,sauvNet);
    }

    @GetMapping("/getActes")
    List<ActProjection> getActes(@RequestParam String parente ,@RequestParam String sexe ,@RequestParam String codFil , @RequestParam String codAssur){
        return bultSoinRepository.getActes(parente,sexe,codFil,codAssur);
    }
    @GetMapping("/getActesManuel")
    List<ActProjection> getActesManuel(@RequestParam String codFil , @RequestParam String codAssur){
        return bultSoinRepository.getActesManuel(codFil,codAssur);
    }
    @GetMapping("/getMeds")
    List<MedProjection> getMeds(@RequestParam String codFil , @RequestParam String codAssur){
        return bultSoinRepository.getListMedCnam(codFil,codAssur);
    }

    @PostMapping("/addLigBultVisit")
    List<LigBultVisit> addLigBultVisit(@RequestBody List<LigBultVisit> ligBultVisit)
    {
        return ligBultVisitRepository.saveAll(ligBultVisit);
    }
    @PostMapping("/addLigBultAct")
    List<LigBultAct> addLigBultAct(@RequestBody List<LigBultAct> ligBultActs)
    {
        return ligBultActRepository.saveAll(ligBultActs);
    }
    @PostMapping("/addLigBultMed")
    List<LigBultMed> addLigBultMed(@RequestBody List<LigBultMed> ligBultMeds)
    {
        return ligBultMedRepository.saveAll(ligBultMeds);
    }
    @PostMapping("/addLigBultApp")
    List<LigBultApp> addLigBultApp(@RequestBody List<LigBultApp> ligBultApps)
    {
        return ligBultAppRepository.saveAll(ligBultApps);
    }

    @GetMapping("/getNumLigMed")
    Long getNumLigMed(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam, @RequestParam  LocalDate datSoin, @RequestParam String codMed){
        System.out.println("datSoin"+datSoin);
        return ligBultMedRepository.getNumLigMed(soc,mat,numFam,datSoin,codMed);
    }

    @GetMapping("/getNumLigAct")
    Long getNumLigAct(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam, @RequestParam  LocalDate datSoin){
        System.out.println("datSoin"+datSoin);
        return ligBultActRepository.getNumLigAct(soc,mat,numFam,datSoin);
    }

    @GetMapping("/getNumLigApp")
    Long getNumLigApp(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam, @RequestParam  LocalDate datSoin, @RequestParam String codApp){
        System.out.println("datSoin"+datSoin);
        return ligBultAppRepository.getNumLigApp(soc,mat,numFam,datSoin,codApp);
    }

    @GetMapping("/getBultCnam")
    List<BultSoinProjection> getBultCnam(@RequestParam String soc){
         return bultSoinRepository.getBultCnam(soc);
    }
    @GetMapping("/getBultMut")
    List<BultSoinProjection> getBultMut(@RequestParam String soc){
        return bultSoinRepository.getBultMut(soc);
    }
    @GetMapping("/getBordtCnam")
    List<BordEnvoiPrejection> getBordtCnam(@RequestParam String soc){
        return bultSoinRepository.getBordtCnam(soc);
    }
    @GetMapping("/getBultCnamRecep")
    List<BultSoin> getBultCnamRecep(@RequestParam String soc,@RequestParam String bord){
        return bultSoinRepository.getBultCnamRecep(soc,bord);
    }
    @GetMapping("/getLigBultAct")
     public List<LigBultAct> getLigBultAct(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultActRepository.getLigBultAct(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultActCons")
    public List<LigBultActProjection> getLigBultActCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultActRepository.getLigBultActCons(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultVisit")
    public List<LigBultVisit> getLigBultVisit(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultVisitRepository.getLigBultVisit(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultVisitCons")
    public List<LigBultVisitProjection> getLigBultVisitCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultVisitRepository.getLigBultVisitCons(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultMed")
    public List<LigBultMed> getLigBultMed(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultMedRepository.getLigBultMed(soc,mat,numFam,datSoin);
    }

    @GetMapping("/getLigBultMedCons")
    public List<LigBultMedProjection> getLigBultMedCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultMedRepository.getLigBultMedCons(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultApp")
    public List<LigBultApp> getLigBultApp(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultAppRepository.getLigBultApp(soc,mat,numFam,datSoin);
    }
    @GetMapping("/getLigBultAppCons")
    public List<LigBultAppProjection> getLigBultAppCons(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin){
        return ligBultAppRepository.getLigBultAppCons(soc,mat,numFam,datSoin);
    }
    @DeleteMapping("/deleteLigBultAct")
    void deleteLigBultAct(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("abrv")String abrv,@RequestParam("numLig")String numLig,@RequestParam("datAct")String datAct
            ,@RequestParam("codAct")String codAct){
        ligBultActRepository.deleteLigBultAct(soc,mat,fam,datSoin,abrv,numLig,datAct,codAct);
    }

    @DeleteMapping("/deleteLigBultApp")
    void deleteLigBultApp(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("abrv")String abrv,@RequestParam("numLig")String numLig ,@RequestParam("codApp")String codApp){
        ligBultAppRepository.deleteLigBultApp(soc,mat,fam,datSoin,abrv,numLig,codApp);
    }

    @DeleteMapping("/deleteLigBultVisit")
    void deleteLigBultVisit(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("abrv")String abrv,@RequestParam("datAct")String datAct ,@RequestParam("codVisit")String codVisit){
        ligBultVisitRepository.deleteLigBultVisit(soc,mat,fam,datSoin,abrv,datAct,codVisit);
    }

    @DeleteMapping("/deleteLigBultMed")
    void deleteLigBultMed(@RequestParam("soc")String soc,@RequestParam("mat")String mat,@RequestParam("fam")String fam,@RequestParam("datSoin")String datSoin
            ,@RequestParam("abrv")String abrv,@RequestParam("numLig")String numLig ,@RequestParam("codMed")String codMed){
        ligBultMedRepository.deleteLigBultMed(soc,mat,fam,datSoin,abrv,numLig,codMed);
    }

    @PostMapping("/existeLigBult")
    public ResponseEntity<Boolean> existeLigBult(@RequestBody LigBultCle id) {

        boolean exists = ligBultRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/existeLigPhar")
    public ResponseEntity<Boolean> existeLigPhar(@RequestBody CleLigPhar id) {

        boolean exists = ligPharRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/existeLigBultAct")
    public ResponseEntity<Boolean> existeLigBultAct(@RequestBody CleLigBultAct id) {

        boolean exists = ligBultActRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/existeLigBultVisit")
    public ResponseEntity<Boolean> existeLigBultVisit(@RequestBody CleLigBultVisit id) {

        boolean exists = ligBultVisitRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/existeLigBultApp")
    public ResponseEntity<Boolean> existeLigBultApp(@RequestBody CleLigBultApp id) {

        boolean exists = ligBultAppRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/existeLigBultMed")
    public ResponseEntity<Boolean> existeLigBultMed(@RequestBody CleLigBultMed id) {

        boolean exists = ligBultMedRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
