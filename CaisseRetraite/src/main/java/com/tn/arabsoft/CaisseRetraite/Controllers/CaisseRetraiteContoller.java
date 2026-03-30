package com.tn.arabsoft.CaisseRetraite.Controllers;

import com.tn.arabsoft.CaisseRetraite.DTO.ResponseProcedure;
import com.tn.arabsoft.CaisseRetraite.Entities.*;
import com.tn.arabsoft.CaisseRetraite.Entities.Cles.CleBenefPrimeRetr;
import com.tn.arabsoft.CaisseRetraite.Entities.Cles.ClePrimeMutPers;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ClePrimeRetraite;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.RepCalculCotis;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ReponseCalculPrimeRetr;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ReponseProcedure;
import com.tn.arabsoft.CaisseRetraite.Projections.*;
import com.tn.arabsoft.CaisseRetraite.Repositories.*;
import com.tn.arabsoft.CaisseRetraite.Services.CaisseRetrService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/CaiseeRetr")
@RestController
public class CaisseRetraiteContoller {

    @Autowired
    PrimeMutPersRepository primeMutPersRepository;
    @Autowired
    LigPrimeMutPersRepository ligPrimeMutPersRepository;
    @Autowired
    CaisseRetrService caisseRetrService;
    @Autowired
    MotifRembourRepository motifRembourRepository;
    @Autowired
    PrimeRetraiteRepository primeRetraiteRepository;
    @Autowired
    BenefPrimeRetraiteRepository benefPrimeRetraiteRepository;

    @GetMapping("/getMaxNum")
    Long getMaxNum(@RequestParam("soc") String soc, @RequestParam("mat") String mat) {
        return primeMutPersRepository.getMaxNumPrime(soc, mat);
    }

    @GetMapping("/getAllPrimes")
    List<PrimeMutPersProjection> getAllPrimes() {
        return primeMutPersRepository.getAll();
    }
    @GetMapping("/getAllPrimeRet")
    List<PrimeMutPersProjection> getAllPrimes(@RequestParam("mat")String mat) {
        return primeMutPersRepository.getAllPrimeRet(mat);
    }

    @GetMapping("/getAllVald")
    List<PrimeMutPersProjection> getAllVald() {
        return primeMutPersRepository.getAllVald();
    }

    @GetMapping("/getAllByMat")
    List<PrimeMutPersProjection> getAllByMat(@RequestParam("mat") String mat) {
        return primeMutPersRepository.getAllByMat(mat);
    }

    @GetMapping("/getAllLigByMat")
    List<LigPrimeMutPers> getAllLigByMat(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("num") Long num, @RequestParam("dat1") LocalDate dat1, @RequestParam("dat2") LocalDate dat2) {
        return ligPrimeMutPersRepository.getAllByMat(soc, mat, num, dat1);
    }

    @GetMapping("/getAllLigPrimes")
    List<LigPrimeMutPers> getAllLigPrimes() {
        return ligPrimeMutPersRepository.getAll();
    }

    @GetMapping("/verifLigCotisation")
    ReponseProcedure verifLigCotisation(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("datDeb") String datDeb, @RequestParam("datFin") String datFin) {
        return caisseRetrService.verif_lig_cotisation_retr(soc, mat, datDeb, datFin);
    }

    @GetMapping("/calculCotisation")
    RepCalculCotis calcul_cotisation(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("datDeb") String datDeb, @RequestParam("datFin") String datFin,
            @RequestParam("brut") BigDecimal brut) {
        return caisseRetrService.calcul_cotisation(soc, mat, datDeb, datFin, brut);
    }

    @GetMapping("/majLigCotisationRetr")
    void calcul_cotisation(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("num") Long num,
            @RequestParam("datDeb") String datDeb, @RequestParam("datFin") String datFin,
            @RequestParam("cot") BigDecimal cot) {
        caisseRetrService.maj_lig_cotisation_retr(soc, mat, num, datDeb, datFin, cot);
    }

    @GetMapping("/getPersPrime")
    List<PersCotisProjection> getPersPrime(@RequestParam("soc") String soc) {
        return primeMutPersRepository.getPersPrime(soc);
    }

    @PostMapping("/addPrime")
    PrimeMutPers getPrimeMutPers(@RequestBody PrimeMutPers primeMutPers) {
        return primeMutPersRepository.save(primeMutPers);
    }

    @PatchMapping("/updateEtatPrime")
    public ResponseEntity<PrimeMutPers> updateEtatPrime(@RequestBody Map<String, String> payload) {
        String codSoc = payload.get("cod_soc");
        String matPers = payload.get("mat_pers");
        Long numPrime = Long.valueOf(payload.get("num_prime"));
        String etatPrime = payload.get("etat_prime");

        // ClePrimeMutPers cle = new ClePrimeMutPers(codSoc, matPers, numPrime);
        PrimeMutPers optional = primeMutPersRepository.getById(codSoc, matPers, numPrime);

        optional.setEtat_prime(etatPrime); // Exemple : "V"
        return ResponseEntity.ok(primeMutPersRepository.save(optional));

    }

    @GetMapping("/chargement_pers_prime")
    void chargement_pers_prime(@RequestParam("soc") String soc, @RequestParam("mois") String mois,
            @RequestParam("mat") String mat) {
        caisseRetrService.chargement_pers_prime(soc, mois, mat);
    }

    @GetMapping("/getPrimePersVal")
    List<PrimeMutPersProjection> getPrimePersVal(@RequestParam("soc") String soc, @RequestParam("mois") String mois,
            @RequestParam("matricule") String matricule, @RequestParam("corps") String corps) {
        return primeMutPersRepository.getPrimePersVal(soc, mois, corps, matricule);
    }

    @GetMapping("/maj_cotisation")
    public ResponseEntity<Integer> chargement_pers_prime(
            @RequestParam String soc,
            @RequestParam String mat,
            @RequestParam String mois,
            @RequestParam String corps) {

        int nb = caisseRetrService.maj_cotisation(soc, mat, mois, corps);
        return ResponseEntity.ok(nb);
    }


    @GetMapping("/getDiskPret")
    public List<DiskPretProjection> getDiskPret(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String corps,
            @RequestParam String mois) { // mois stays required

        return primeMutPersRepository.getDiskPret(mat, corps, mois);
    }

    @GetMapping("/getMotifRembour")
    List<MotifRembour> getMotifRembour() {
        return motifRembourRepository.findAll();
    }

    @GetMapping("/getPersonnel")
    List<PersonnelPrimeProjection> getPersonnel() {
        return primeMutPersRepository.getPersonnel();
    }

    @GetMapping("/getFamille")
    List<FamilleProjection> getFamille(@RequestParam String soc, @RequestParam String mat, @RequestParam String nom) {
        return primeMutPersRepository.getFamille(soc, mat, nom);
    }

    @GetMapping("/calcul_prime_retraite")
    ReponseCalculPrimeRetr chargement_pers_prime(@RequestParam("benef_prime") String benef_prime,
            @RequestParam("benef_droi") String benef_droi,
            @RequestParam("taux_prime") BigDecimal taux_prime,
            @RequestParam("dat_remb") String dat_remb,
            @RequestParam("mat_pers") String mat_pers,
            @RequestParam("soc") String soc) {
        return caisseRetrService.calcul_prime_retraite(benef_prime, benef_droi, taux_prime, dat_remb, mat_pers, soc);
    }

    @GetMapping("/getNumRemb")
    Long getNumRemb(@RequestParam("soc") String soc, @Param("mat") String mat) {
        return primeRetraiteRepository.getNumRemb(soc, mat);
    }

    @GetMapping("/getPrimeRetr")
    List<PrimeRetraiteProjection> getPrimeRetr() {
        return this.primeRetraiteRepository.getPrimeRetr();
    }

    @GetMapping("/getPrimeRetrSais")
    List<PrimeRetraiteProjection> getPrimeRetrSaisie() {
        return this.primeRetraiteRepository.getPrimeRetrSaisie();
    }

    @GetMapping("/getPrimeRetrSaisMat")
    List<PrimeRetraiteProjection> getPrimeRetrSaisMat(@RequestParam("mat") String mat) {
        return this.primeRetraiteRepository.getPrimeRetrSaisMat(mat);
    }

    @GetMapping("/getBenefPrime")
    List<BenefPrimeRetraite> getBenefPrime(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("num") Long num) {
        return this.benefPrimeRetraiteRepository.getBenefPrime(soc, mat, num);
    }

    @PostMapping("/addPrimeRetr")
    void addPrimeRetr(@RequestBody PrimeRetraiteDTO dto) {
        primeRetraiteRepository.insertPrimeRetr(
                dto.getCod_soc(),
                dto.getMat_pers(),
                dto.getNum_remb(),
                dto.getNum_retr(),
                dto.getCod_remb(),
                dto.getDat_remb(),
                dto.getBenef_prime(),
                dto.getBenef_droi(),
                dto.getTaux_prime(),
                dto.getDat_deb_ret(),
                dto.getDat_fin_ret(),
                dto.getEtat_prime(),
                dto.getMontant_cotis(),
                dto.getMontant_prime(),
                dto.getMontant_total(),
                dto.getMod_pay(),
                dto.getObs_retraite(),
                dto.getImput_cpt(),
                dto.getSeq_ecrt(),
                dto.getCheq_remb());
    }

    @PostMapping("/updatePrimeRetr")
    void updatePrimeRetr(@RequestBody PrimeRetraiteDTO dto) {
        primeRetraiteRepository.updatePrimeRetr(
                dto.getCod_soc(),
                dto.getMat_pers(),
                dto.getNum_remb(),
                dto.getNum_retr(),
                dto.getCod_remb(),
                dto.getDat_remb(),
                dto.getBenef_prime(),
                dto.getBenef_droi(),
                dto.getTaux_prime(),
                dto.getDat_deb_ret(),
                dto.getDat_fin_ret(),
                dto.getEtat_prime(),
                dto.getMontant_cotis(),
                dto.getMontant_prime(),
                dto.getMontant_total(),
                dto.getMod_pay(),
                dto.getObs_retraite(),
                dto.getImput_cpt(),
                dto.getSeq_ecrt(),
                dto.getCheq_remb());
    }

    @PostMapping("/addBenefPrimeRetr")
    List<BenefPrimeRetraite> addPrimeRetr(@RequestBody List<BenefPrimeRetraite> benefPrimeRetraite) {
        return benefPrimeRetraiteRepository.saveAll(benefPrimeRetraite);
    }

    @PostMapping("/import-fichier")
    public ResponseProcedure importFichier(
            @RequestParam("soc") String soc,
            @RequestParam("mois") String mois,
            @RequestParam("file") MultipartFile file) throws Exception {

        return caisseRetrService.lireFichierEtAppelerProcedure(soc, mois, file);
    }
    @DeleteMapping("/deletePrime")
    @Transactional
    public void deletePrimeRetraite(
            @RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("num") Long num,
            @RequestParam("numFam") Long numFam) {

        CleBenefPrimeRetr keyBenif = new CleBenefPrimeRetr();
        keyBenif.setCod_soc(soc);
        keyBenif.setMat_pers(mat);
        keyBenif.setNum_remb(num);
        keyBenif.setNum_fam(numFam);

        benefPrimeRetraiteRepository.deleteById(keyBenif);

        ClePrimeRetraite key = new ClePrimeRetraite();
        key.setCod_soc(soc);
        key.setMat_pers(mat);
        key.setNum_remb(num);

        primeRetraiteRepository.deleteById(key);
    }


}
