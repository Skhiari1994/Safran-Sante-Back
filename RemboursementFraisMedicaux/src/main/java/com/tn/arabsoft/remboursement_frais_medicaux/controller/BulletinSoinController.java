
package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.*;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.BulletinSoinService;
import com.tn.arabsoft.remboursement_frais_medicaux.util.DateParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BultSoin")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class BulletinSoinController {

    private final BulletinSoinService bulletinSoinService;
    private final BultSoinRepository bultSoinRepository;
    private final LigBultRepository ligBultRepository;
    private final RefEtablisRepository refEtablisRepository;
    private final LigPharRepository ligPharRepository;
    private final ActeRepository acteRepository;
    private final RegimeRembRepository regimeRembRepository;
    private final LigBultVisitRepository ligBultVisitRepository;
    private final LigBultActRepository ligBultActRepository;
    private final LigBultMedRepository ligBultMedRepository;
    private final LigBultAppRepository ligBultAppRepository;
    private final RefVisitRepository refVisitRepository;

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @GetMapping("/getDatNaiss")
    ReponseDatNais getDatNaiss(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam) {
        return bulletinSoinService.getDatNais(soc, mat, numFam);
    }

    @GetMapping("/getDatSexe")
    ReponseGetSexe getDatSexe(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam) {
        return bulletinSoinService.getSexe(soc, mat, numFam);
    }

    @GetMapping("/get_duree_bulletin")
    ResponseProcedure getDureeBulletin(@RequestParam String wdat_soin, @RequestParam String wCOD_ASSUR) {
        return bulletinSoinService.getDureeBulletin(wdat_soin, wCOD_ASSUR);
    }

    @GetMapping("/get_plafond_mutuelle")
    ReponseGetPlafondMutuelle getPlafondMutuelle(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String wdat_soin,
            @RequestParam String datNais, @RequestParam String datSaisie) {
        return bulletinSoinService.getPlafondMutuelle(soc, mat, wdat_soin, datNais, datSaisie);
    }

    @GetMapping("/get_plafond_cnam")
    ReponseGetPlafondMutuelle getPlafondCnam(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String wdat_soin,
            @RequestParam String datNais, @RequestParam String codAssur, @RequestParam String datSaisie) {
        return bulletinSoinService.getPlafondCnam(soc, mat, wdat_soin, datNais, codAssur, datSaisie);
    }

    @GetMapping("/getNbreBult")
    Long getNbreBult(@RequestParam String codAssur, @RequestParam String numSoin) {
        return bultSoinRepository.getNbreBult(codAssur, numSoin);
    }

    @GetMapping("/getListEtablisPec")
    List<PECProjection> getListEtablisPec(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(datSoin, formatter);
        return bultSoinRepository.getListEtablisPec(soc, mat, numFam, localDate);
    }

    @GetMapping("/getPersCons")
    List<PersConsProjection> getPersCons(@RequestParam String soc) {
        return bultSoinRepository.getPersCons(soc);
    }

    @GetMapping("/getBultSoin")
    List<BultSoinProjection> getBultSoin() {
        return bultSoinRepository.getBulletinSoin();
    }

    @GetMapping("/getBultSoinCons")
    public List<BultSoinProjection> getBultSoinCons(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String num_fam,
            @RequestParam(required = false) String dat_soin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(dat_soin, formatter);
        return bultSoinRepository.getBulletinSoinCons(mat, num_fam, localDate);
    }

    @GetMapping("/getBultCnamCons")
    public List<BultSoinProjection> getBultCnamCons(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String num_fam,
            @RequestParam(required = false) String dat_soin) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(dat_soin, formatter);
        return bultSoinRepository.getBulletinCnamCons(mat, num_fam, localDate);
    }

    @GetMapping("/getLigBult")
    public List<LigBult> getLigBult(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam String datSoin) {
        return ligBultRepository.getLigBult(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultCons")
    public List<LigBultProjection> getLigBultCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultRepository.getLigBultCons(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getAct")
    List<ActeProjection> getActe(@RequestParam("codFil") String codFil, @RequestParam("codAssur") String codAssur,
            @RequestParam("parente") String parente, @RequestParam("sexe") String sexe) {
        return bultSoinRepository.getActe(codFil, codAssur, parente, sexe);
    }

    @GetMapping("/calculer_montant_net")
    ReponseCalculMntNet calculerMontantNet(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codFil, @RequestParam String codAssur,
            @RequestParam String abrvAct, @RequestParam String datSoin, @RequestParam BigDecimal mntHnor,
            @RequestParam Long indice) {
        return bulletinSoinService.calculerMontantNet(soc, mat, codFil, codAssur, abrvAct, datSoin, mntHnor, indice);
    }

    @GetMapping("/refEtablis")
    List<RefEtablis> refEtablis(@RequestParam String refTyp) {
        return refEtablisRepository.getRefEtablis(refTyp);
    }

    @GetMapping("/refEtablisByCode")
    RefEtablis refEtablisByCode(@RequestParam String prfCod, @RequestParam String refTyp) {
        return refEtablisRepository.getRefEtablisByCode(prfCod, refTyp);
    }

    @PostMapping("/addBultSoin")
    public BultSoin addBultSoin(@RequestBody BultSoin bultSoin) {
        BultSoinCle id = new BultSoinCle(
                bultSoin.getCod_soc(),
                bultSoin.getMat_pers(),
                bultSoin.getNum_fam(),
                bultSoin.getDat_soin());

        return bultSoinRepository.findById(id)
                .map(existingBult -> {
                    // Mise à jour
                    existingBult.setTot_honor(bultSoin.getTot_honor());
                    existingBult.setTot_net(bultSoin.getTot_net());
                    existingBult.setObs(bultSoin.getObs());
                    return bultSoinRepository.save(existingBult);
                })
                .orElseGet(() -> bultSoinRepository.save(bultSoin));
    }

    @PostMapping("/addLigBult")
    List<LigBult> addLigBult(@RequestBody List<LigBult> ligBults) {
        return ligBultRepository.saveAll(ligBults);
    }

    @GetMapping("getAllActe")
    List<Acte> getAllActe() {
        return acteRepository.findAll();
    }

    @GetMapping("/getMed")
    List<LigPharProjection> getMed(@RequestParam String codFil, @RequestParam String codAssur) {
        return ligPharRepository.getMed(codFil, codAssur);
    }

    @GetMapping("/getLigPhar")
    List<LigPhar> getMed(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam String datSoin) {
        return ligPharRepository.getLigPhar(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigPharCons")
    List<LigPharProjectionCons> getLigPharCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligPharRepository.getLigPharCons(soc, mat, numFam, datSoin);
    }

    @PostMapping("/addLigPhar")
    List<LigPhar> addLigPhar(@RequestBody List<LigPhar> ligPhars) {
        return ligPharRepository.saveAll(ligPhars);
    }

    @GetMapping("/verif_indice")
    ResponseProcedure verifIndice(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam String indice) {
        return bulletinSoinService.verifIndice(abrv_act_, cod_fil_, cod_assur_, indice);
    }

    @GetMapping("/verif_vign")
    ResponseProcedure verifVign(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam BigDecimal mnt_honor_, @RequestParam String nbr_vign_) {
        return bulletinSoinService.verifVign(abrv_act_, cod_fil_, cod_assur_, mnt_honor_, nbr_vign_);
    }

    @GetMapping("/verif_piece")
    ResponseProcedure verifPiece(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam BigDecimal mnt_honor_, @RequestParam String nbr_piece_) {
        return bulletinSoinService.verifPiece(abrv_act_, cod_fil_, cod_assur_, mnt_honor_, nbr_piece_);
    }

    @DeleteMapping("/deleteLigBult")
    void deleteLigBult(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig) {
        ligBultRepository.deleteLigBult(soc, mat, fam, datSoin, abrv, numLig);
    }

    @DeleteMapping("/deleteLigPhar")
    void deleteLigPhar(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("med") String med,
            @RequestParam("numLig") String numLig) {
        ligPharRepository.deleteLigPhar(soc, mat, fam, datSoin, med, numLig);
    }

    @GetMapping("/getRegimeRemb")
    List<RegimeRemb> getRegimeRemb() {
        return regimeRembRepository.getRegimeRemb();
    }

    @GetMapping("/vetif_mod_remb")
    ReponseVetifModRemb vetifModRemb(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam String datSoin,
            @RequestParam String reg_remb_, @RequestParam String parente, @RequestParam String sexe) {
        return bulletinSoinService.vetifModRemb(soc, mat, numFam, datSoin, reg_remb_, parente, sexe);
    }

    @GetMapping("/getVisit")
    List<VisitProjection> getVisit(@RequestParam String parente, @RequestParam String sexe, @RequestParam String codFil,
            @RequestParam String codAssur) {
        return refVisitRepository.getVisit(parente, sexe, codFil, codAssur);
    }

    @GetMapping("/getVisitManuel")
    List<VisitProjection> getVisitManuel(@RequestParam String codFil, @RequestParam String codAssur) {
        return refVisitRepository.getVisitManuel(codFil, codAssur);
    }

    @GetMapping("/getAppareil")
    List<AppareilProjection> getAppareil(@RequestParam String codFil, @RequestParam String codAssur) {
        return refVisitRepository.getAppareil(codFil, codAssur);
    }

    @GetMapping("/calculer_montant_net_app")
    ReponseCalculMntNet calculerMontantNetApp(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin,
            @RequestParam BigDecimal mntHnor, @RequestParam String codApp, Long MutMntNet) {
        return bulletinSoinService.calculerMontantNetApp(soc, mat, numFam, datSoin, mntHnor, codApp, MutMntNet);
    }

    @GetMapping("/calculer_montant_net_visit")
    ReponseCalculMntNet calculerMontantNetVisit(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codVisit,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal prixVisit,
            @RequestParam BigDecimal tauxRemb, @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinSoinService.calculerMontantNetVisit(soc, mat, codVisit, mntHnor, datSoin, prixVisit, tauxRemb,
                sauvNet);
    }

    @GetMapping("/calculer_montant_net_act")
    ReponseCalculMntNet calculerMontantNetAct(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codAct,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal actPrix,
            @RequestParam BigDecimal tauxAct, @RequestParam(required = false) BigDecimal cumulNet,
            @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinSoinService.calculerMontantNetAct(soc, mat, codAct, mntHnor, datSoin, actPrix, tauxAct,
                cumulNet, sauvNet);
    }

    @GetMapping("/calculer_montant_net_med")
    ReponseCalculMntNet calculerMontantNetMed(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codMed,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal prixRemb,
            @RequestParam Long indice, @RequestParam(required = false) BigDecimal cumulNet,
            @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinSoinService.calculerMontantNetMed(soc, mat, codMed, mntHnor, datSoin, prixRemb, indice,
                cumulNet, sauvNet);
    }

    @GetMapping("/getActes")
    List<ActProjection> getActes(@RequestParam String parente, @RequestParam String sexe, @RequestParam String codFil,
            @RequestParam String codAssur) {
        return bultSoinRepository.getActes(parente, sexe, codFil, codAssur);
    }

    @GetMapping("/getActesManuel")
    List<ActProjection> getActesManuel(@RequestParam String codFil, @RequestParam String codAssur) {
        return bultSoinRepository.getActesManuel(codFil, codAssur);
    }

    @GetMapping("/getMeds")
    List<MedProjection> getMeds(@RequestParam String codFil, @RequestParam String codAssur) {
        return bultSoinRepository.getListMedCnam(codFil, codAssur);
    }

    @PostMapping("/addLigBultVisit")
    List<LigBultVisit> addLigBultVisit(@RequestBody List<LigBultVisit> ligBultVisit) {
        return ligBultVisitRepository.saveAll(ligBultVisit);
    }

    @PostMapping("/addLigBultAct")
    List<LigBultAct> addLigBultAct(@RequestBody List<LigBultAct> ligBultActs) {
        return ligBultActRepository.saveAll(ligBultActs);
    }

    @PostMapping("/addLigBultMed")
    List<LigBultMed> addLigBultMed(@RequestBody List<LigBultMed> ligBultMeds) {
        return ligBultMedRepository.saveAll(ligBultMeds);
    }

    @PostMapping("/addLigBultApp")
    List<LigBultApp> addLigBultApp(@RequestBody List<LigBultApp> ligBultApps) {
        return ligBultAppRepository.saveAll(ligBultApps);
    }

    @GetMapping("/getNumLigMed")
    Long getNumLigMed(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam,
            @RequestParam LocalDate datSoin, @RequestParam String codMed) {
        return ligBultMedRepository.getNumLigMed(soc, mat, numFam, datSoin, codMed);
    }

    @GetMapping("/getNumLigAct")
    Long getNumLigAct(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam,
            @RequestParam LocalDate datSoin) {
        return ligBultActRepository.getNumLigAct(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getNumLigApp")
    Long getNumLigApp(@RequestParam String soc, @RequestParam String mat, @RequestParam Long numFam,
            @RequestParam LocalDate datSoin, @RequestParam String codApp) {
        return ligBultAppRepository.getNumLigApp(soc, mat, numFam, datSoin, codApp);
    }

    @GetMapping("/getBultCnam")
    List<BultSoinProjection> getBultCnam(@RequestParam String soc) {
        return bultSoinRepository.getBultCnam(soc);
    }

    @GetMapping("/getBultMut")
    List<BultSoinProjection> getBultMut(@RequestParam String soc) {
        return bultSoinRepository.getBultMut(soc);
    }

    @GetMapping("/getBordtCnam")
    List<BordEnvoiPrejection> getBordtCnam(@RequestParam String soc) {
        return bultSoinRepository.getBordtCnam(soc);
    }

    @GetMapping("/getBultCnamRecep")
    List<BultSoin> getBultCnamRecep(@RequestParam String soc, @RequestParam String bord) {
        return bultSoinRepository.getBultCnamRecep(soc, bord);
    }

    @GetMapping("/getLigBultAct")
    public List<LigBultAct> getLigBultAct(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        LocalDate dat_soin = DateParser.parse(datSoin);
        return ligBultActRepository.getLigBultAct(soc, mat, Long.valueOf(numFam), dat_soin);
    }

    @GetMapping("/getLigBultActCons")
    public List<LigBultActProjection> getLigBultActCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        LocalDate dat_soin = DateParser.parse(datSoin);
        return ligBultActRepository.getLigBultActCons(soc, mat, Long.valueOf(numFam), dat_soin);
    }

    @GetMapping("/getLigBultVisit")
    public List<LigBultVisit> getLigBultVisit(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultVisitRepository.getLigBultVisit(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultVisitCons")
    public List<LigBultVisitProjection> getLigBultVisitCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultVisitRepository.getLigBultVisitCons(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultMed")
    public List<LigBultMed> getLigBultMed(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultMedRepository.getLigBultMed(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultMedCons")
    public List<LigBultMedProjection> getLigBultMedCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultMedRepository.getLigBultMedCons(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultApp")
    public List<LigBultApp> getLigBultApp(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultAppRepository.getLigBultApp(soc, mat, numFam, datSoin);
    }

    @GetMapping("/getLigBultAppCons")
    public List<LigBultAppProjection> getLigBultAppCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        return ligBultAppRepository.getLigBultAppCons(soc, mat, numFam, datSoin);
    }

    @DeleteMapping("/deleteLigBultAct")
    void deleteLigBultAct(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("datAct") String datAct,
            @RequestParam("codAct") String codAct) {
        LocalDate date_soin = DateParser.parse(datSoin);
        LocalDate date_acte = DateParser.parse(datAct);
        Long num_prest = Long.valueOf(fam);
        Long numero_ligne = Long.valueOf(numLig);
        ligBultActRepository.deleteLigBultAct(soc, mat, num_prest, date_soin, abrv, numero_ligne, date_acte, codAct);
    }

    @DeleteMapping("/deleteLigBultApp")
    void deleteLigBultApp(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("codApp") String codApp) {
        ligBultAppRepository.deleteLigBultApp(soc, mat, fam, datSoin, abrv, numLig, codApp);
    }

    @DeleteMapping("/deleteLigBultVisit")
    void deleteLigBultVisit(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("datAct") String datAct, @RequestParam("codVisit") String codVisit) {
        ligBultVisitRepository.deleteLigBultVisit(soc, mat, fam, datSoin, abrv, datAct, codVisit);
    }

    @DeleteMapping("/deleteLigBultMed")
    void deleteLigBultMed(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("codMed") String codMed) {
        ligBultMedRepository.deleteLigBultMed(soc, mat, fam, datSoin, abrv, numLig, codMed);
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
