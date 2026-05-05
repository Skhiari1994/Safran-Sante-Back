package com.arabsoft.Credits.Controllers;

import com.arabsoft.Credits.DTO.ApiResponse;
import com.arabsoft.Credits.DTO.LigPretDTO;
import com.arabsoft.Credits.DTO.VirCartePretRequest;
import com.arabsoft.Credits.Entities.*;
import com.arabsoft.Credits.Entities.Response.*;
import com.arabsoft.Credits.Projections.DetailPretProjection;
import com.arabsoft.Credits.Projections.LigPretProjection;
import com.arabsoft.Credits.Projections.PretPErsProjection;
import com.arabsoft.Credits.Repositories.*;
import com.arabsoft.Credits.Services.CreditService;
import com.arabsoft.Credits.Services.VirCartePretDataService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Credit")
@EnableCaching
public class CreditController {

    @Autowired
    OrgPretRepository orgPretRepository;
    @Autowired
    PretPersRepository pretPersRepository;
    @Autowired
    CreditService creditService;
    @Autowired
    PiecePretPersRepository piecePretPersRepository;
    @Autowired
    LigPretRepository ligPretRepository;
    @Autowired
    EtatPretRepository etatPretRepository;
    @Autowired
    DiskPretRepository diskPretRepository;

    @Autowired
    PersonnelRepository personnelRepository;

    @PostMapping("/ajoutCredit")
    PretPers ajoutCredit(@RequestBody PretPers pretPers) {
        return pretPersRepository.save(pretPers);
    }

    @GetMapping("/orgPret")
    List<OrgPret> getOrgPret() {
        return orgPretRepository.findAll();
    }

    @GetMapping("/getDetailPret/{soc}")
    List<DetailPretProjection> getDetailPret(@PathVariable String soc) {
        return pretPersRepository.getDetailsPret(soc);
    }

    @GetMapping("/getGroupePret/{soc}")
    List<DetailPretProjection> getGroupePret(@PathVariable String soc) {
        return pretPersRepository.getGroupePret(soc);
    }

    // @GetMapping("/getPretEnCours/{soc}")
    // public ResponseEntity<Page<PretPers>> getPretEnCours(
    // @PathVariable String soc,
    // @RequestParam("page") int page, // Number of the page (0-based)
    // @RequestParam("size") int size) // Size of the page (number of items per
    // page)
    // {
    // Pageable pageable = PageRequest.of(page, size);
    // Page<PretPers> pretPersPage = pretPersRepository.getPretEnCours(soc,
    // pageable);
    // return ResponseEntity.ok(pretPersPage);
    // }
    @GetMapping("/getAll")
    @Cacheable("personnel")
    public ResponseEntity<List<PretPers>> getPretEnCours() {
        return ResponseEntity.ok(pretPersRepository.findAll());
    }

    @GetMapping("/getPretEnCours/{soc}/{mat}")
    public ResponseEntity<List<PretPErsProjection>> getPretEnCours(
            @PathVariable String soc, @PathVariable String mat) {
        List<PretPErsProjection> pretPersPage = pretPersRepository.getPretEnCours(soc, mat);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getPretValid/{soc}")
    public ResponseEntity<List<PretPErsProjection>> getPretValid(
            @PathVariable String soc) // Size of the page (number of items per page)
    {
        List<PretPErsProjection> pretPersPage = pretPersRepository.getPretValid(soc);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getPretDelet/{soc}")
    public ResponseEntity<List<PretPErsProjection>> getPretDelet(
            @PathVariable String soc) // Size of the page (number of items per page)
    {
        List<PretPErsProjection> pretPersPage = pretPersRepository.getPretDelet(soc);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getPretDebl/{soc}")
    public ResponseEntity<List<PretPErsProjection>> getPretDebl(
            @PathVariable String soc) // Size of the page (number of items per page)
    {
        List<PretPErsProjection> pretPersPage = pretPersRepository.getPretDebl(soc);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getLigPret")
    public ResponseEntity<List<LigPret>> getLigPret(
            @RequestParam String soc, @RequestParam String mat, @RequestParam String pret) // Size of the page (number
                                                                                           // of items per page)
    {
        List<LigPret> pretPersPage = ligPretRepository.getLigPRet(soc, mat, pret);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getLigPretReg")
    public ResponseEntity<List<LigPretProjection>> getLigRegManuelle(
            @RequestParam String soc, @RequestParam String mois, @RequestParam String filtre,
            @RequestParam String corps, @RequestParam String grpPret) // Size of the page (number of items per page)
    {
        List<LigPretProjection> pretPersPage = ligPretRepository.getLigRegManuelle(soc, mois, filtre, corps, grpPret);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/getPiecePret/{soc}/{mat}/{pret}")
    public ResponseEntity<List<PiecePretPers>> getPiecePret(@PathVariable String soc, @PathVariable String mat,
            @PathVariable String pret) // Size of the page (number of items per page)
    {
        List<PiecePretPers> pretPersPage = piecePretPersRepository.getPiecePretPers(soc, mat, pret);
        return ResponseEntity.ok(pretPersPage);
    }

    @GetMapping("/maxPret/{soc}/{mat}")
    Long getDetailPret(@PathVariable String soc, @PathVariable String mat) {
        return pretPersRepository.getMaxCodPret(soc, mat);
    }

    @GetMapping("/verifDatDeb")
    ReponseDatDeb verifDatDeb(@RequestParam String codSoc, @RequestParam Long wprtEch, @RequestParam String prt_dat_acc,
            @RequestParam String wdatDeb, @RequestParam String wdatRetr) {
        return creditService.verif_dat_deb(codSoc, wprtEch, prt_dat_acc, wdatDeb, wdatRetr);
    }

    @GetMapping("/verifDatAcc")
    ReponseDatAcc verifDatAcc(@RequestParam String codSoc, @RequestParam String mat_, @RequestParam String wcodGrpPret,
            @RequestParam String wtypPret, @RequestParam String wdatEmb, @RequestParam String wDatAcc) {
        System.out.println(codSoc + " " + mat_ + " " + wcodGrpPret + " " + wtypPret + " " + wdatEmb + " " + wDatAcc);
        return creditService.verif_dat_acc(codSoc, mat_, wcodGrpPret, wtypPret, wdatEmb, wDatAcc);
    }

    @GetMapping("/getDetailCredit")
    ReponseCalculDetailCredit getDetailCredit(@RequestParam String wcodSoc, @RequestParam String wdatfin,
            @RequestParam String wdatretr, @RequestParam Double wprtmntglb, @RequestParam Double wprtech,
            @RequestParam Double wprttaux, @RequestParam BigDecimal wmntreport, @RequestParam String codGrpPret) {
        return creditService.calcul_detail_credit(wcodSoc, wdatfin, wdatretr, wprtmntglb, wprtech, wprttaux, wmntreport,
                codGrpPret);
    }

    @GetMapping("/verifPretCours")
    VerifPretResponse verifPretCours(@RequestParam String wcodSoc, @RequestParam String wmatPers,
            @RequestParam String wcodGrpPret, @RequestParam String wtypPret, @RequestParam String prtDatDeb) {
        return creditService.verifPretCours(wcodSoc, wmatPers, wcodGrpPret, wtypPret, prtDatDeb);
    }

    @GetMapping("/calc_mnt_pret_ant")
    public VerifPretResponse calc_mnt_pret_ant(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam(required = false) String codPret,
            @RequestParam(required = false) String codPretAnt,
            @RequestParam String codGrpPret,
            @RequestParam String typPret) {

        return creditService.calc_mnt_pret_ant(
                codSoc,
                matPers,
                codPret,
                codPretAnt,
                codGrpPret,
                typPret);
    }

    @GetMapping("/insertRembTranch")
    void insertRembTranch(@RequestParam String wcodSoc, @RequestParam String wmat_pers, @RequestParam String wcod_pret,
            @RequestParam String wcod_etat_pret,
            @RequestParam String wprt_dat_acc, @RequestParam String wprt_mnt_rem, @RequestParam Long wnbr_tranche) {
        creditService.insert_remb_tranch(wcodSoc, wmat_pers, wcod_pret, wcod_etat_pret, wprt_dat_acc, wprt_mnt_rem,
                wnbr_tranche);
    }

    @GetMapping("/insertLigPret")
    void insertLigPret(@RequestParam String soc, @RequestParam String wmat_pers, @RequestParam String wcod_pret,
            @RequestParam String wprt_dat_deb,
            @RequestParam String wprt_dat_fin, @RequestParam BigDecimal wprt_interet, @RequestParam BigDecimal wprt_ech,
            @RequestParam BigDecimal wprt_mnt_rem,
            @RequestParam BigDecimal wrem_men, @RequestParam BigDecimal wdern_rem_men) {
        creditService.insert_lig_pret(soc, wmat_pers, wcod_pret, wprt_dat_deb, wprt_dat_fin, wprt_interet, wprt_ech,
                wprt_mnt_rem, wrem_men, wdern_rem_men);
    }

    @PostMapping("/addPiece")
    PiecePretPers getPiecePret(@RequestBody PiecePretPers piecePretPers) {
        return piecePretPersRepository.save(piecePretPers);
    }

    @GetMapping("/getEtatPret")
    public ResponseEntity<List<EtatPret>> getEtatPret() // Size of the page (number of items per page)
    {
        List<EtatPret> etat = etatPretRepository.getEtatPret();
        return ResponseEntity.ok(etat);
    }

    @PutMapping("/reglerLigPret/{reg}")
    LigPret reglerLigPret(@RequestParam String soc, @RequestParam String mat, @RequestParam String codPret,
            @RequestParam String lpret, @PathVariable String reg) {
        LigPret ligPret = this.ligPretRepository.getLigPretPers(soc, mat, codPret, lpret);

        ligPret.setReg_pret(reg);
        return ligPretRepository.save(ligPret);
    }

    @GetMapping("/getDiskPret")
    public List<DiskPret> getDiskPret(
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String corps,
            @RequestParam String mois,
            @RequestParam(required = false) String codPret) {
        return diskPretRepository.getDiskPret(mat, corps, mois, codPret);
    }

    @GetMapping("/maj_lig_pret")
    void maj_lig_pret(@RequestParam String wcodSoc, @RequestParam String wmatPers, @RequestParam String wcodGrpPret,
            @RequestParam String corps, @RequestParam String mois) {
        creditService.maj_lig_pret(wcodSoc, wmatPers, wcodGrpPret, corps, mois);
    }

    @GetMapping("/vir_carte")
    void maj_lig_pret(@RequestParam String wcodSoc, @RequestParam Long num_comm_, @RequestParam String dat_deblc) {
        creditService.vir_carte(wcodSoc, num_comm_, dat_deblc);
    }

    @GetMapping("/deblocage_carte")
    public ResponseEntity<?> deblocageCarte(
            @RequestParam String wcodSoc,
            @RequestParam String dat_deblc,
            @RequestParam String num_comm_,
            @RequestParam String refMetier,
            @RequestParam String datOp) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate dateDebloc = LocalDate.parse(dat_deblc, formatter);
            LocalDate dateOp = LocalDate.parse(datOp, formatter);

            creditService.deblocage_carte(
                    wcodSoc,
                    dateDebloc,
                    num_comm_,
                    refMetier,
                    dateOp);

            // ✅ SUCCESS RESPONSE
            return ResponseEntity.ok(Map.of(
                    "status", "SUCCESS",
                    "message", "Déblocage effectué avec succès"));

        } catch (Exception e) {
            // ✅ ERROR RESPONSE (sent to Angular)
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "status", "ERROR",
                            "message", e.getMessage()));
        }
    }

    @GetMapping("/getPersonnelAnticip/{soc}")
    void getPersonnelAnticip(@PathVariable String soc) {
        personnelRepository.getPersonnelsAnticipe(soc);
    }

    @GetMapping("/download-file")
    public ResponseEntity<FileSystemResource> downloadFile(
            @RequestParam(defaultValue = "y:/vir/virements.txt") String filePath) {
        // Générer le fichier
        creditService.generateVirementFile(filePath);

        // Vérifier si le fichier existe
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Préparer la réponse HTTP avec le fichier
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }
    //
    // @CrossOrigin("*")
    // @PutMapping("/updateValidDocFourn/{valid}")
    // DaDocFournisseur addValidDocFourn(@RequestParam String mat, @RequestParam
    // String refDa,@RequestParam String codEtape,@RequestParam String
    // codTypDoc,@PathVariable String valid)
    // {
    // DaDocFournisseur daDocEtape =
    // daDocFournisseur.getByMat(mat,refDa,codEtape,codTypDoc);
    // daDocEtape.setValid(valid);
    //
    // return daDocFournisseur.save(daDocEtape);
    // }

    @Autowired
    private VirCartePretDataService service;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateFile(@RequestBody VirCartePretRequest request) {
        // log.debug("Generating file with request: soc={}, datDebloc={}, numComm={}",
        // request.getSoc(), request.getDatDebloc(), request.getNumComm());
        try {
            Map<String, Object> response = service.generateVirCartePretFile(
                    request.getSoc(),
                    request.getDatDebloc(),
                    request.getNumComm(),
                    request.getIdFile());
            if (response.containsKey("errorDetails")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // log.error("Error generating file for soc={}, datDebloc={}, numComm={}",
            // request.getSoc(), request.getDatDebloc(), request.getNumComm(), e);
            Map<String, Object> errorResponse = Map.of(
                    "message", "Error generating file: " + e.getMessage(),
                    "errorDetails", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/download/{seq}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long seq) {
        // log.debug("Downloading file for seq: {}", seq);
        try {
            byte[] fileContent = service.downloadVirCartePretFile(seq);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vir_carte_pret_" + seq + ".txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContent);
        } catch (Exception e) {
            // log.error("Error downloading file for seq: {}", seq, e);
            String errorMessage = "Error downloading file: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(errorMessage.getBytes());
        }
    }

    @PostMapping("/deletElement")
    public ResponseProcedure deletElement(@RequestBody Map<String, Object> data) {
        String wcodSoc = (String) data.get("wcodSoc");
        String wmatPers = (String) data.get("wmatPers");
        Integer codPret = (Integer) data.get("codPret");

        return creditService.deleteCredit(wcodSoc, wmatPers, codPret);
    }

    @PutMapping("/updateRegPret")
    public ResponseEntity<?> updateRegPret(@RequestBody List<LigPret> list) {
        creditService.updateListLigPret(list);
        return ResponseEntity.ok("Mises à jour réussies");
    }

    @GetMapping("/checkExistPret")
    public ResponseEntity<Boolean> checkPret(
            @RequestParam String mat,
            @RequestParam String codGrp) {

        return ResponseEntity.ok(creditService.hasPretCondition(mat, codGrp));
    }

    @PostMapping("/anticip")
    public ResponseEntity<Void> anticipPret(
            @RequestParam String soc,
            @RequestParam String mat,
            @RequestParam Long cod,
            @RequestParam Long codAnt) {
        creditService.anticipPret(soc, mat, cod, codAnt);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/en-cours-lov")
    public ResponseEntity<List<PretPErsProjection>> getPretEnCoursLov(
            @RequestParam String soc,
            @RequestParam(required = false) String mat,
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String numComm,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String codPret, // Added this,
            @RequestParam(required = false) String modRemb, // Added this
            @RequestParam(required = false) String datSaisie,
            @RequestParam(required = false) String numRetr

    ) {
        List<PretPErsProjection> result = pretPersRepository.getPretEnCoursLOV(soc, mat, nom, numComm, etat, codPret,
                modRemb, datSaisie, numRetr);
        return ResponseEntity.ok(result);
    }
}
