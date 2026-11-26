
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
import com.tn.arabsoft.remboursement_frais_medicaux.service.BulletinSoinCnamService;
import com.tn.arabsoft.remboursement_frais_medicaux.util.DateParser;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BultSoin")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class BulletinSoinController {

    private final BulletinSoinService bulletinSoinService;
    private final BulletinSoinCnamService bulletinCnamService;
    private final BultSoinRepository bultSoinRepository;
    private final DossierMldRepository dossierMldRepository;
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

    public static final Comparator<String> MATRICULE_COMPARATOR = Comparator.nullsLast((a, b) -> {

        int i = 0;
        int j = 0;

        while (i < a.length() && j < b.length()) {

            char ca = a.charAt(i);
            char cb = b.charAt(j);

            // Both are digits → compare the complete numeric parts
            if (Character.isDigit(ca) && Character.isDigit(cb)) {

                int startI = i;
                int startJ = j;

                while (i < a.length() && Character.isDigit(a.charAt(i))) {
                    i++;
                }

                while (j < b.length() && Character.isDigit(b.charAt(j))) {
                    j++;
                }

                String numA = a.substring(startI, i);
                String numB = b.substring(startJ, j);

                // Remove leading zeros for comparison
                String normalizedA = numA.replaceFirst("^0+(?!$)", "");
                String normalizedB = numB.replaceFirst("^0+(?!$)", "");

                // First compare length
                int result = Integer.compare(
                        normalizedA.length(),
                        normalizedB.length());

                if (result != 0) {
                    return result;
                }

                // Same length → compare numerically as strings
                result = normalizedA.compareTo(normalizedB);

                if (result != 0) {
                    return result;
                }

            } else {

                // Compare letters / other characters ignoring case
                int result = Character.compare(
                        Character.toLowerCase(ca),
                        Character.toLowerCase(cb));

                if (result != 0) {
                    return result;
                }

                i++;
                j++;
            }
        }

        // Shorter value first
        return Integer.compare(a.length(), b.length());
    });

    @GetMapping("/getDatNaiss")
    ReponseDatNais getDatNaiss(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam) {
        return bulletinSoinService.getDatNais(soc, mat, numFam);
    }

    @GetMapping("/getDatSexe")
    ReponseGetSexe getDatSexe(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam) {
        return bulletinSoinService.getSexe(soc, mat, numFam);
    }

    @GetMapping("/get_duree_bulletin")
    ResponseProcedure getDureeBulletin(@RequestParam("datSoin") String datSoin,
            @RequestParam("codAssur") String codAssur) {
        return bulletinSoinService.getDureeBulletin(datSoin, codAssur);
    }

    @GetMapping("/get_plafond_mutuelle")
    ReponseGetPlafondMutuelle getPlafondMutuelle(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String wdat_soin,
            @RequestParam String datNais, @RequestParam String datSaisie,
            @RequestParam(required = false) String numFam) {
        return bulletinSoinService.getPlafondMutuelle(soc, mat, wdat_soin, datNais, datSaisie, numFam);
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
            @RequestParam String soc,
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) Integer num_fam,
            @RequestParam(required = false) String dat_soin) {

        LocalDate localDate = null;

        if (dat_soin != null && !dat_soin.isBlank()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            localDate = LocalDate.parse(dat_soin, formatter);
        }

        return bultSoinRepository.getBulletinCnamCons(soc, mat, num_fam, localDate);

    }

    /**
     * Liste légère pour la fenêtre de recherche : la fiche complète se charge via
     * /getBultCnamCons à la sélection.
     */
    @GetMapping("/getBultCnamListe")
    public List<BultSoinListeProjection> getBultCnamListe(@RequestParam String soc) {
        return bultSoinRepository.getBulletinCnamListe(soc);
    }

    @GetMapping("/getLigBult")
    public List<LigBult> getLigBult(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam String datSoin) {
        Integer famInt = Integer.valueOf(numFam);
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultRepository.getLigBult(soc, mat, famInt, date_soin);
    }

    @GetMapping("/getLigBultCons")
    public List<LigBultProjection> getLigBultCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        Integer famInt = Integer.valueOf(numFam);
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultRepository.getLigBultCons(soc, mat, famInt, date_soin);
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
        // New bulletin: generate the number and insert
        if (bultSoin.getNum_soin() == null) {
            bultSoin.setNum_soin(bultSoinRepository.nextNumSoin());
            return bultSoinRepository.save(bultSoin);
        }

        // Existing bulletin: look it up by the full key and update
        BultSoinCle id = new BultSoinCle(
                bultSoin.getCod_soc(),
                bultSoin.getMat_pers(),
                bultSoin.getNum_fam(),
                bultSoin.getDat_soin(),
                bultSoin.getNum_soin());

        return bultSoinRepository.findById(id)
                .map(existingBult -> {
                    existingBult.setTot_honor(bultSoin.getTot_honor());
                    existingBult.setTot_net(bultSoin.getTot_net());
                    existingBult.setObs(bultSoin.getObs());
                    return bultSoinRepository.save(existingBult);
                })
                .orElseGet(() -> bultSoinRepository.save(bultSoin));
    }

    /*
     * @PostMapping("/addBultSoin")
     * public BultSoin addBultSoin(@RequestBody BultSoin bultSoin) {
     * BultSoinCle id = new BultSoinCle(
     * bultSoin.getCod_soc(),
     * bultSoin.getMat_pers(),
     * bultSoin.getNum_fam(),
     * bultSoin.getDat_soin());
     * 
     * System.out.println("getCod_soc :: " + bultSoin.getCod_soc() +
     * ", getMat_pers : " + bultSoin.getMat_pers()
     * + ",getNum_fam:: " + bultSoin.getNum_fam());
     * System.out.println("getDat_soin :: " + bultSoin.getDat_soin());
     * 
     * return bultSoinRepository.findById(id)
     * .map(existingBult -> {
     * // Mise à jour
     * existingBult.setTot_honor(bultSoin.getTot_honor());
     * existingBult.setTot_net(bultSoin.getTot_net());
     * existingBult.setObs(bultSoin.getObs());
     * return bultSoinRepository.save(existingBult);
     * })
     * .orElseGet(() -> bultSoinRepository.save(bultSoin));
     * }
     */

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
        Integer numFamInt = Integer.valueOf(numFam);
        LocalDate datSoinSql = DateParser.parse(datSoin);
        return ligPharRepository.getLigPhar(soc, mat, numFamInt, datSoinSql);
    }

    @GetMapping("/getLigPharCons")
    List<LigPharProjectionCons> getLigPharCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        Integer numFamInt = Integer.valueOf(numFam);
        LocalDate datSoinSql = DateParser.parse(datSoin);
        return ligPharRepository.getLigPharCons(soc, mat, numFamInt, datSoinSql);
    }

    @PostMapping("/addLigPhar")
    List<LigPhar> addLigPhar(@RequestBody List<LigPhar> ligPhars) {
        return ligPharRepository.saveAll(ligPhars);
    }

    @GetMapping("/verif_indice")
    ResponseProcedure verifIndice(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam String indice) {
        return bulletinCnamService.verifIndice(abrv_act_, cod_fil_, cod_assur_, indice);
    }

    @GetMapping("/verif_vign")
    ResponseProcedure verifVign(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam BigDecimal mnt_honor_, @RequestParam String nbr_vign_) {
        return bulletinCnamService.verifVign(abrv_act_, cod_fil_, cod_assur_, mnt_honor_, nbr_vign_);
    }

    @GetMapping("/verif_piece")
    ResponseProcedure verifPiece(@RequestParam String abrv_act_, @RequestParam String cod_fil_,
            @RequestParam String cod_assur_, @RequestParam BigDecimal mnt_honor_, @RequestParam String nbr_piece_) {
        return bulletinCnamService.verifPiece(abrv_act_, cod_fil_, cod_assur_, mnt_honor_, nbr_piece_);
    }

    @DeleteMapping("/deleteLigBult")
    void deleteLigBult(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig) {
        Integer famInt = Integer.valueOf(fam);
        LocalDate date_soin = DateParser.parse(datSoin);
        Long num_lig = Long.valueOf(numLig);
        ligBultRepository.deleteLigBult(soc, mat, famInt, date_soin, abrv, num_lig);
    }

    @DeleteMapping("/deleteLigPhar")
    void deleteLigPhar(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("med") String med,
            @RequestParam("numLig") String numLig) {
        Integer famInt = Integer.valueOf(fam);
        LocalDate date_soin_sql = DateParser.parse(datSoin);
        Long numero_ligne = Long.valueOf(numLig);
        ligPharRepository.deleteLigPhar(soc, mat, famInt, date_soin_sql, med, numero_ligne);
    }

    @GetMapping("/getRegimeRemb")
    List<RegimeRemb> getRegimeRemb() {
        return regimeRembRepository.getRegimeRemb();
    }

    @GetMapping("/vetif_mod_remb")
    ReponseVetifModRemb vetifModRemb(@RequestParam("codSoc") String codSoc,
            @RequestParam("matPers") String matPers,
            @RequestParam("numFam") String numFam,
            @RequestParam("datSoin") String datSoin,
            @RequestParam("regRemb") String regRemb,
            @RequestParam("parente") String parente,
            @RequestParam("sexe") String sexe) {
        return bulletinSoinService.vetifModRemb(codSoc, matPers, Integer.valueOf(numFam), datSoin, regRemb, parente,
                sexe);
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

    // calcul mnt_net visite
    @GetMapping("/calculer_montant_net_visit")
    ReponseCalculMntNet calculerMontantNetVisit(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codVisit,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal prixVisit,
            @RequestParam BigDecimal tauxRemb, @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinCnamService.calculerMontantNetVisit(soc, mat, codVisit, mntHnor, datSoin, prixVisit, tauxRemb,
                sauvNet);
    }

    // calcul mnt_net médicaments
    @GetMapping("/calculer_montant_net_med")
    ReponseCalculMntNet calculerMontantNetMed(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codMed,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal prixRemb,
            @RequestParam Long indice, @RequestParam(required = false) BigDecimal cumulNet,
            @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinCnamService.calculerMontantNetMed(soc, mat, codMed, mntHnor, datSoin, prixRemb, indice,
                cumulNet, sauvNet);
    }

    // calcul mnt_net acte
    @GetMapping("/calculer_montant_net_act")
    ReponseCalculMntNet calculerMontantNetAct(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String codAct,
            @RequestParam BigDecimal mntHnor, @RequestParam String datSoin, @RequestParam BigDecimal actPrix,
            @RequestParam BigDecimal tauxAct, @RequestParam(required = false) BigDecimal cumulNet,
            @RequestParam(required = false) BigDecimal sauvNet) {
        return bulletinCnamService.calculerMontantNetAct(soc, mat, codAct, mntHnor, datSoin, actPrix, tauxAct,
                cumulNet, sauvNet);
    }

    // // calcul mnt_net appareil
    @GetMapping("/calculer_montant_net_app")
    ReponseCalculMntNet calculerMontantNetApp(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin,
            @RequestParam BigDecimal mntHnor, @RequestParam String codApp, Long MutMntNet) {
        return bulletinCnamService.calculerMontantNetApp(soc, mat, numFam, datSoin, mntHnor, codApp, MutMntNet);
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

    @GetMapping("/getDossiersMaladie")
    List<DossierMaladieProjection> getDossiersMaladie(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam) {
        return dossierMldRepository.getDossiersMaladie(soc, mat, numFam);
    }

    @PostMapping("/addLigBultVisit")
    List<LigBultVisit> addLigBultVisit(@RequestBody List<LigBultVisit> ligBultVisit) {

        // next free number per bulletin, kept in memory for the lines of this call
        Map<String, Integer> nextByBulletin = new HashMap<>();

        for (LigBultVisit l : ligBultVisit) {
            if (l.getNum_lig() == null) {
                String key = l.getCod_soc() + "|" + l.getMat_pers() + "|" + l.getNum_fam()
                        + "|" + l.getDat_soin() + "|" + l.getNum_soin();

                int next = nextByBulletin.computeIfAbsent(key, k -> ligBultVisitRepository.nextNumLig(
                        l.getCod_soc(), l.getMat_pers(), l.getNum_fam(),
                        l.getDat_soin(), l.getNum_soin()));

                l.setNum_lig(next);
                nextByBulletin.put(key, next + 1);
            }
        }

        return ligBultVisitRepository.saveAll(ligBultVisit);
    }

    @PostMapping("/addLigBultAct")
    List<LigBultAct> addLigBultAct(@RequestBody List<LigBultAct> ligBultActs) {

        // next free number per bulletin, kept in memory for the lines of this call
        Map<String, Integer> nextByBulletin = new HashMap<>();

        for (LigBultAct l : ligBultActs) {
            if (l.getNum_lig() == null) {
                String key = l.getCod_soc() + "|" + l.getMat_pers() + "|" + l.getNum_fam()
                        + "|" + l.getDat_soin() + "|" + l.getNum_soin();

                int next = nextByBulletin.computeIfAbsent(key, k -> ligBultActRepository.nextNumLig(
                        l.getCod_soc(), l.getMat_pers(), l.getNum_fam(),
                        l.getDat_soin(), l.getNum_soin()));

                l.setNum_lig(next);
                nextByBulletin.put(key, next + 1);
            }
        }

        return ligBultActRepository.saveAll(ligBultActs);
    }

    @PostMapping("/addLigBultMed")
    List<LigBultMed> addLigBultMed(@RequestBody List<LigBultMed> ligBultMeds) {

        // next free number per bulletin, kept in memory for the lines of this call
        Map<String, Integer> nextByBulletin = new HashMap<>();

        for (LigBultMed l : ligBultMeds) {
            if (l.getNum_lig() == null) {
                String key = l.getCod_soc() + "|" + l.getMat_pers() + "|" + l.getNum_fam()
                        + "|" + l.getDat_soin() + "|" + l.getNum_soin();

                int next = nextByBulletin.computeIfAbsent(key, k -> ligBultMedRepository.nextNumLig(
                        l.getCod_soc(), l.getMat_pers(), l.getNum_fam(),
                        l.getDat_soin(), l.getNum_soin()));

                l.setNum_lig(next);
                nextByBulletin.put(key, next + 1);
            }
        }

        return ligBultMedRepository.saveAll(ligBultMeds);
    }

    @PostMapping("/addLigBultApp")
    List<LigBultApp> addLigBultApp(@RequestBody List<LigBultApp> ligBultApps) {

        // next free number per bulletin, kept in memory for the lines of this call
        Map<String, Integer> nextByBulletin = new HashMap<>();

        for (LigBultApp l : ligBultApps) {
            if (l.getNum_lig() == null) {
                String key = l.getCod_soc() + "|" + l.getMat_pers() + "|" + l.getNum_fam()
                        + "|" + l.getDat_soin() + "|" + l.getNum_soin();

                int next = nextByBulletin.computeIfAbsent(key, k -> ligBultAppRepository.nextNumLig(
                        l.getCod_soc(), l.getMat_pers(), l.getNum_fam(),
                        l.getDat_soin(), l.getNum_soin()));

                l.setNum_lig(next);
                nextByBulletin.put(key, next + 1);
            }
        }

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
        List<BultSoinProjection> bultCnamBySoc = bultSoinRepository.getBultCnam(soc);
        bultCnamBySoc.sort(
                Comparator.comparing(
                        BultSoinProjection::getMat_pers,
                        MATRICULE_COMPARATOR));
        return bultCnamBySoc;
    }

    @GetMapping("/getBultMut")
    List<BultSoinProjection> getBultMut(@RequestParam String soc) {
        return bultSoinRepository.getBultMut(soc);
    }

    /** Liste légère pour la fenêtre de recherche des bulletins mutuelle. */
    @GetMapping("/getBultMutListe")
    List<BultSoinListeProjection> getBultMutListe(@RequestParam String soc) {
        return bultSoinRepository.getBultMutListe(soc);
    }

    /** Fiche complète d'un bulletin mutuelle choisi dans la liste légère. */
    @GetMapping("/getBultMutFiche")
    List<BultSoinProjection> getBultMutFiche(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String num_fam, @RequestParam String dat_soin) {
        LocalDate localDate = LocalDate.parse(dat_soin, DateTimeFormatter.ofPattern(DATE_FORMAT));
        return bultSoinRepository.getBultMutFiltre(soc, mat, Integer.valueOf(num_fam.trim()), localDate);
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
            @RequestParam Integer numFam, @RequestParam String datSoin, @RequestParam(required = false) Long numSoin) {
        LocalDate dat_soin = DateParser.parse(datSoin);
        return ligBultActRepository.getLigBultAct(soc, mat, numFam, dat_soin, numSoin);
    }

    @GetMapping("/getLigBultActCons")
    public List<LigBultActProjection> getLigBultActCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        LocalDate dat_soin = DateParser.parse(datSoin);
        return ligBultActRepository.getLigBultActCons(soc, mat, Long.valueOf(numFam), dat_soin);
    }

    @GetMapping("/getLigBultVisit")
    public List<LigBultVisit> getLigBultVisit(@RequestParam String soc, @RequestParam String mat,
            @RequestParam Integer numFam, @RequestParam String datSoin, @RequestParam(required = false) Long numSoin) {
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultVisitRepository.getLigBultVisit(soc, mat, numFam, date_soin, numSoin);
    }

    @GetMapping("/getLigBultVisitCons")
    public List<LigBultVisitProjection> getLigBultVisitCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        Integer famInt = Integer.valueOf(numFam);
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultVisitRepository.getLigBultVisitCons(soc, mat, famInt, date_soin);
    }

    @GetMapping("/getLigBultMed")
    public List<LigBultMed> getLigBultMed(@RequestParam String soc, @RequestParam String mat,
            @RequestParam Integer numFam, @RequestParam String datSoin, @RequestParam(required = false) Long numSoin) {
        LocalDate date_soin_sql = DateParser.parse(datSoin);
        return ligBultMedRepository.getLigBultMed(soc, mat, numFam, date_soin_sql, numSoin);
    }

    @GetMapping("/getLigBultMedCons")
    public List<LigBultMedProjection> getLigBultMedCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        Integer famInt = Integer.valueOf(numFam);
        LocalDate date_soin_sql = DateParser.parse(datSoin);
        return ligBultMedRepository.getLigBultMedCons(soc, mat, famInt, date_soin_sql);
    }

    @GetMapping("/getLigBultApp")
    public List<LigBultApp> getLigBultApp(@RequestParam String soc, @RequestParam String mat,
            @RequestParam Integer numFam, @RequestParam String datSoin, @RequestParam(required = false) Long numSoin) {
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultAppRepository.getLigBultApp(soc, mat, numFam, date_soin, numSoin);
    }

    @GetMapping("/getLigBultAppCons")
    public List<LigBultAppProjection> getLigBultAppCons(@RequestParam String soc, @RequestParam String mat,
            @RequestParam String numFam, @RequestParam String datSoin) {
        Integer prestInt = Integer.valueOf(numFam);
        LocalDate date_soin = DateParser.parse(datSoin);
        return ligBultAppRepository.getLigBultAppCons(soc, mat, prestInt, date_soin);
    }

    @DeleteMapping("/deleteLigBultAct")
    void deleteLigBultAct(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("datAct") String datAct,
            @RequestParam("codAct") String codAct,
            @RequestParam(value = "numSoin", required = false) String numSoin) {
        // ligne jamais enregistrée (clé vide) : rien à supprimer en base
        if (fam == null || fam.isBlank() || numLig == null || numLig.isBlank()) {
            return;
        }
        LocalDate date_soin = DateParser.parse(datSoin);
        LocalDate date_acte = DateParser.parse(datAct);
        Long num_prest = Long.valueOf(fam);
        Long numero_ligne = Long.valueOf(numLig);
        if (numSoin != null && !numSoin.isBlank()) {
            // clé complète : seul l'acte de ce bulletin est supprimé (pas celui d'un autre bulletin de même date)
            ligBultActRepository.deleteLigBultActBulletin(soc, mat, num_prest, date_soin, abrv, numero_ligne, date_acte,
                    codAct, Long.valueOf(numSoin.trim()));
        } else {
            ligBultActRepository.deleteLigBultAct(soc, mat, num_prest, date_soin, abrv, numero_ligne, date_acte, codAct);
        }
    }

    @DeleteMapping("/deleteLigBultApp")
    void deleteLigBultApp(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("codApp") String codApp,
            @RequestParam(value = "numSoin", required = false) String numSoin,
            @RequestParam(value = "datAct", required = false) String datAct) {
        // ligne jamais enregistrée (clé vide) : rien à supprimer en base
        if (fam == null || fam.isBlank() || numLig == null || numLig.isBlank()) {
            return;
        }
        Integer prestInt = Integer.valueOf(fam);
        LocalDate date_soin = DateParser.parse(datSoin);
        if (numSoin != null && !numSoin.isBlank() && datAct != null && !datAct.isBlank()) {
            // clé complète (numLig = numéro de ligne du bulletin) : seule la ligne de ce bulletin est supprimée
            ligBultAppRepository.deleteLigBultAppBulletin(soc, mat, prestInt, date_soin, abrv, DateParser.parse(datAct),
                    codApp, Integer.valueOf(numLig.trim()), Long.valueOf(numSoin.trim()));
            return;
        }
        // ancien appel (numLig = num_lig_app)
        Long numero_ligne = Long.valueOf(numLig);
        ligBultAppRepository.deleteLigBultApp(soc, mat, prestInt, date_soin, abrv, numero_ligne, codApp);
    }

    @DeleteMapping("/deleteLigBultVisit")
    void deleteLigBultVisit(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("datAct") String datAct, @RequestParam("codVisit") String codVisit,
            @RequestParam(value = "numLig", required = false) String numLig,
            @RequestParam(value = "numSoin", required = false) String numSoin) {
        // ligne jamais enregistrée (clé vide) : rien à supprimer en base
        if (fam == null || fam.isBlank()) {
            return;
        }
        Integer famInt = Integer.valueOf(fam);
        LocalDate date_soin = DateParser.parse(datSoin);
        LocalDate date_acte = DateParser.parse(datAct);
        boolean avecBulletin = numLig != null && !numLig.isBlank() && numSoin != null && !numSoin.isBlank();
        if (avecBulletin) {
            // clé complète : seule la visite de ce bulletin est supprimée (pas celle d'un
            // autre bulletin de même date)
            ligBultVisitRepository.deleteLigBultVisitBulletin(soc, mat, famInt, date_soin, abrv, date_acte, codVisit,
                    Integer.valueOf(numLig.trim()), Long.valueOf(numSoin.trim()));
        } else {
            ligBultVisitRepository.deleteLigBultVisit(soc, mat, famInt, date_soin, abrv, date_acte, codVisit);
        }
    }

    @DeleteMapping("/deleteLigBultMed")
    void deleteLigBultMed(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("codMed") String codMed,
            @RequestParam(value = "numSoin", required = false) String numSoin,
            @RequestParam(value = "datAct", required = false) String datAct) {
        // ligne jamais enregistrée (clé vide) : rien à supprimer en base
        if (fam == null || fam.isBlank() || numLig == null || numLig.isBlank()) {
            return;
        }
        Integer famInt = Integer.valueOf(fam);
        LocalDate date_soin_sql = DateParser.parse(datSoin);
        if (numSoin != null && !numSoin.isBlank() && datAct != null && !datAct.isBlank()) {
            // clé complète (numLig = numéro de ligne du bulletin) : fonctionne aussi pour les anciennes lignes,
            // dont num_lig_med est vide
            ligBultMedRepository.deleteLigBultMedBulletin(soc, mat, famInt, date_soin_sql, abrv, DateParser.parse(datAct),
                    codMed, Integer.valueOf(numLig.trim()), Long.valueOf(numSoin.trim()));
            return;
        }
        // ancien appel (numLig = num_lig_med)
        Long numero_ligne = Long.valueOf(numLig);
        ligBultMedRepository.deleteLigBultMed(soc, mat, famInt, date_soin_sql, abrv, numero_ligne, codMed);
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

    @GetMapping("/consultation")
    public ResponseEntity<List<BultSoinProjection>> getConsultationBulletins(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam(required = false) String natBult,
            @RequestParam int annee) {
        String soc = (codSoc != null) ? codSoc.trim() : "";
        String mat = (matPers != null) ? matPers.trim() : "";
        String nat = (natBult != null && !natBult.trim().isEmpty() && !"null".equalsIgnoreCase(natBult))
                ? natBult.trim().toUpperCase()
                : null;

        List<BultSoinProjection> list = bultSoinRepository.findBulletinsConsultation(soc, mat, nat, annee);
        return ResponseEntity.ok(list);
    }
}
