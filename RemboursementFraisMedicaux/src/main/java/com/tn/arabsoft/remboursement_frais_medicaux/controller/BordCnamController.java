package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import com.tn.arabsoft.remboursement_frais_medicaux.util.LovRecherche;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.dto.TotauxCnamDto;
import com.tn.arabsoft.remboursement_frais_medicaux.dto.UpdateBultSoinDTO;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultSoin;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.CnamFileGen;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultSoinCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.AssuranceRepository;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.BordEnvoiRepository;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.BultSoinRepository;
import com.tn.arabsoft.remboursement_frais_medicaux.service.BulletinSoinService;
import com.tn.arabsoft.remboursement_frais_medicaux.service.CnamFileGenService;
import com.tn.arabsoft.remboursement_frais_medicaux.service.TotauxCnamService;
import com.tn.arabsoft.remboursement_frais_medicaux.service.BordEnvoiService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/BordEnvoi")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class BordCnamController {

    private final BordEnvoiRepository bordEnvoiRepository;
    private final AssuranceRepository assuranceRepository;
    private final BultSoinRepository bultSoinRepository;
    private final BulletinSoinService bulletinSoinService;
    private final CnamFileGenService fileGenService;
    private final TotauxCnamService totauxCnamService;
    private final BordEnvoiService bordEnvoiService;

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ERROR = "error";

    /**
     * Fenêtre de recherche des bordereaux CNAM non envoyés, page par page (même liste que
     * /Remboursement/BordEnvoiEntete/{soc}). cols : colonnes affichées par l'écran (recherche unique q).
     */
    @GetMapping("/lovBordEnvoiEntete")
    public Map<String, Object> lovBordEnvoiEntete(@RequestParam String soc,
            @RequestParam(required = false) String code, @RequestParam(required = false) String cod_bord,
            @RequestParam(required = false) String dat_bord, @RequestParam(required = false) String q,
            @RequestParam(required = false) String cols,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int taille) {
        int t = LovRecherche.taille(taille);
        int p = LovRecherche.page(page);
        String exact = LovRecherche.vide(code);
        String bord = LovRecherche.motifContient(cod_bord);
        String dat = LovRecherche.motifContient(dat_bord);
        java.util.Set<String> visibles = LovRecherche.colonnes(cols, "cod_bord", "dat_bord");
        int vB = visibles.contains("cod_bord") ? 1 : 0;
        int vD = visibles.contains("dat_bord") ? 1 : 0;
        String[] m = LovRecherche.mots(q);
        String l1 = LovRecherche.motifContient(m[0]), l2 = LovRecherche.motifContient(m[1]), l3 = LovRecherche.motifContient(m[2]);
        long total = bordEnvoiRepository.compterBordEnvoiEntete(soc, exact, bord, dat, vB, vD, l1, l2, l3);
        List<BordEnvoiProjection> lignes = total == 0 ? List.of()
                : bordEnvoiRepository.pageBordEnvoiEntete(soc, exact, bord, dat, vB, vD, l1, l2, l3, p * t, t);
        return LovRecherche.reponse(lignes, total, p, t);
    }

    /**
     * Fenêtre de recherche des bordereaux CNAM envoyés, page par page : même liste que /getAllBordEnvoi et /getCodBord
     * (nonVide=false) ou que /BultSoin/getBordtCnam (nonVide=true). cols : colonnes affichées par l'écran.
     */
    @GetMapping("/lovBordEnvoiCnam")
    @SuppressWarnings("java:S107")
    public Map<String, Object> lovBordEnvoiCnam(@RequestParam String soc,
            @RequestParam(defaultValue = "false") boolean nonVide,
            @RequestParam(required = false) String code, @RequestParam(required = false) String cod_bord,
            @RequestParam(required = false) String dat_bord, @RequestParam(required = false) String cod_assur,
            @RequestParam(required = false) String nbr_bult, @RequestParam(required = false) String tot_remb,
            @RequestParam(required = false) String q, @RequestParam(required = false) String cols,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "50") int taille) {
        int t = LovRecherche.taille(taille);
        int p = LovRecherche.page(page);
        String exact = LovRecherche.vide(code);
        String bord = LovRecherche.motifContient(cod_bord);
        String dat = LovRecherche.motifContient(dat_bord);
        String assur = LovRecherche.motifContient(cod_assur);
        String nbr = LovRecherche.motifContient(nbr_bult);
        String tot = LovRecherche.motifContient(tot_remb);
        java.util.Set<String> v = LovRecherche.colonnes(cols, "cod_bord", "dat_bord");
        int vB = v.contains("cod_bord") ? 1 : 0;
        int vD = v.contains("dat_bord") ? 1 : 0;
        int vA = v.contains("cod_assur") ? 1 : 0;
        int vN = v.contains("nbr_bult") ? 1 : 0;
        int vT = v.contains("tot_remb") ? 1 : 0;
        String[] m = LovRecherche.mots(q);
        String l1 = LovRecherche.motifContient(m[0]), l2 = LovRecherche.motifContient(m[1]), l3 = LovRecherche.motifContient(m[2]);
        int nv = nonVide ? 1 : 0;
        long total = bordEnvoiRepository.compterBordEnvoiCnam(soc, exact, bord, dat, assur, nbr, tot,
                vB, vD, vA, vN, vT, l1, l2, l3, nv);
        List<BordEnvoiPrejection> lignes = total == 0 ? List.of()
                : bordEnvoiRepository.pageBordEnvoiCnam(soc, exact, bord, dat, assur, nbr, tot,
                        vB, vD, vA, vN, vT, l1, l2, l3, nv, p * t, t);
        return LovRecherche.reponse(lignes, total, p, t);
    }

    @GetMapping("/getCodBord")
    List<BordEnvoiPrejection> getCodBord(@RequestParam String soc) {
        return bordEnvoiRepository.getListBordEnvoiCNAM(soc);
    }

    @GetMapping("/getBord")
    List<BordEnvoiPrejection> getBord(@RequestParam String codBord, @RequestParam LocalDate datBord) {
        return bordEnvoiRepository.getBordEnvoi(codBord, datBord);
    }

    @GetMapping("/getCodBordMut")
    List<BordereauProjection> getCodBordMut(@RequestParam String soc) {
        return bordEnvoiRepository.getListBordEnvoiMut(soc);
    }

    @GetMapping("/getAllBordEnvoi")
    List<BordEnvoiPrejection> getAllBordEnvoi(@RequestParam String cod_soc) {
        return bordEnvoiRepository.getListBordEnvoiCNAM(cod_soc);
    }

    @GetMapping("/getListBordEnvoiCNAM")
    List<BordEnvoiPrejection> getListBordEnvoiCNAM(@RequestParam String soc) {
        return bordEnvoiRepository.getListBordEnvoiCNAM(soc);
    }

    @GetMapping("/getBordMut")
    List<BordEnvoiPrejection> getBordMut(@RequestParam String codBord, @RequestParam LocalDate datBord) {
        return bordEnvoiRepository.getBordEnvoiMut(codBord, datBord);
    }

    @GetMapping("/getAssur")
    List<ListAssurProjection> getAssur() {
        return assuranceRepository.getListAssurance();
    }

    @GetMapping("/getBsoinBordCnam")
    List<BultSoinProjection> getBsoinBordCnam(@RequestParam String codSoc, @RequestParam String codBord,
            @RequestParam String codAssur) {
        return bultSoinRepository.getBulletinSoinEnvoi(codSoc, codBord, codAssur);
    }

    @GetMapping("/consBs")
    List<BultSoinProjection> getBulletSoinConsultaion(@RequestParam String codSoc, @RequestParam String codAssur,
            @RequestParam(required = false) LocalDate datDeb, @RequestParam(required = false) LocalDate datFin) {
        return bultSoinRepository.getBulletSoinConsultaion(codSoc, codAssur, datDeb, datFin);
    }

    @GetMapping("/getBsoinBordMut")
    List<BultSoinProjection> getBsoinBordMut(@RequestParam String codSoc, @RequestParam String codBord,
            @RequestParam String codAssur) {
        return bultSoinRepository.getBulletinSoinEnvoiMut(codSoc, codBord, codAssur);
    }

    @GetMapping("/consBsMut")
    List<BultSoinProjection> getBulletSoinConsultaionMut(@RequestParam String codSoc, @RequestParam String codAssur,
            @RequestParam(required = false) LocalDate datDeb, @RequestParam(required = false) LocalDate datFin) {
        return bultSoinRepository.getBulletSoinConsultaionMut(codSoc, codAssur, datDeb, datFin);
    }

    @PostMapping("/generateCodeBord")
    public ResponseEntity<String> generateAndSaveBord(
            @RequestParam String codAssur,
            @RequestParam String codSoc,
            @RequestParam(required = false) String datDeb,
            @RequestParam(required = false) String datFin,
            @RequestParam int nbrBult,
            @RequestParam double totHonor,
            @RequestParam double totNet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDatDeb = null;
        LocalDate localDatFin = null;

        try {
            if (datDeb != null && !datDeb.trim().isEmpty()) {
                localDatDeb = LocalDate.parse(datDeb, formatter);
            }
            if (datFin != null && !datFin.trim().isEmpty()) {
                localDatFin = LocalDate.parse(datFin, formatter);
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Format date invalide. Utilisez dd/MM/yyyy");
        }

        String codBord = bordEnvoiService.genererBord(codAssur, codSoc, localDatDeb, localDatFin, nbrBult, totHonor,
                totNet);

        return ResponseEntity.ok(codBord);
    }

    @PostMapping("/generateCodeBordMut")
    public ResponseEntity<String> generateAndSaveBordMut(
            @RequestParam String codAssur,
            @RequestParam String codSoc,
            @RequestParam(required = false) String datDeb,
            @RequestParam(required = false) String datFin,
            @RequestParam int nbrBult,
            @RequestParam double totHonor,
            @RequestParam double totNet) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDatDeb = null;
        LocalDate localDatFin = null;

        try {
            if (datDeb != null && !datDeb.trim().isEmpty()) {
                localDatDeb = LocalDate.parse(datDeb, formatter);
            }
            if (datFin != null && !datFin.trim().isEmpty()) {
                localDatFin = LocalDate.parse(datFin, formatter);
            }
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Format date invalide. Utilisez dd/MM/yyyy");
        }

        // ✅ La procédure gen_bord_mut a été corrigée pour générer des codes au bon
        // format
        String codBord = bulletinSoinService.callGenerateBordCodeMut(
                codAssur,
                codSoc,
                localDatDeb,
                localDatFin,
                nbrBult,
                totHonor,
                totNet);
        return ResponseEntity.ok(codBord);
    }

    @PostMapping("/updateBultSoinCnam")
    public ResponseEntity<String> createBordBult(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam int numFam,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datSoin,
            @RequestParam Long numSoin,
            @RequestParam String codBord,
            @RequestParam int ordBult,
            @RequestParam BigDecimal totHonor,
            @RequestParam BigDecimal totNet) {
        try {
            // Appel du service
            String message = bulletinSoinService.updateBultSoinCnam(codSoc, matPers, numFam, datSoin, numSoin,
                    codBord, ordBult, totHonor, totNet);

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du bordereau : " + e.getMessage());
        }
    }

    @PostMapping("/callDeletBord")
    public ResponseEntity<Map<String, String>> callDeletBord(
            @RequestParam String codSoc,
            @RequestParam String codBord) {
        try {
            String resultMessage = bulletinSoinService.supprimerBordereau(codSoc, codBord);

            Map<String, String> response = new HashMap<>();
            response.put(KEY_MESSAGE, resultMessage);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Échec de la suppression du bordereau {} / {}", codSoc, codBord, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap(KEY_ERROR, "Une erreur est survenue lors de la suppression."));
        }
    }

    @PostMapping("/modifProc")
    public ResponseEntity<String> modifyBordereau(@RequestParam String pCodAssur,
            @RequestParam String pCodSoc,
            @RequestParam String pMatPers,
            @RequestParam String pNumFam,
            @RequestParam LocalDate pDatSoin,
            @RequestParam Double pTotHonor,
            @RequestParam Double pTotNet,
            @RequestParam String pChoix,
            @RequestParam String pCodBord,
            @RequestParam int vNbrBult) {
        try {
            bulletinSoinService.callModifBord(
                    pCodAssur, pCodSoc, pMatPers, pNumFam, pDatSoin,
                    pTotHonor, pTotNet, pChoix, pCodBord, vNbrBult);

            return ResponseEntity.ok().body("{\"message\": \"Procédure exécutée avec succès!\"}");

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("{\"message\": \"Erreur lors de l'exécution : " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/createBord")
    public ResponseEntity<Map<String, Object>> createBord(
            @RequestParam String pCodAssur,
            @RequestParam String pCodSoc,
            @RequestParam Double pTotHonor,
            @RequestParam Double pTotNet,
            @RequestParam String pChoix,
            @RequestParam Long vNBRBult,
            @RequestParam String pCodBord,
            @RequestParam(required = false) LocalDate datDeb,
            @RequestParam(required = false) LocalDate datFin) {

        // Call the service method to invoke the stored procedure
        Map<String, Object> result = bulletinSoinService.callCreateBord(pCodAssur, pCodSoc, pTotHonor, pTotNet, pChoix,
                vNBRBult, pCodBord, datDeb, datFin);

        // Return the result (empty map or custom response)
        return ResponseEntity.ok(result);
    }

    @PostMapping("/totauCnam")
    public ResponseEntity<TotauxCnamDto> createBord(
            @RequestParam String pCodSoc,
            @RequestParam String pMatPers,
            @RequestParam Integer pNumFam,
            @RequestParam LocalDate pDatSoin,
            @RequestParam Long pNumSoin) {

        // Call the service method to invoke the stored procedure
        // Map<String, Object> result =
        // bulletinSoinService.callCalculateTotalHonor(pCodSoc, pMatPers, pNumFam,
        // pDatSoin);

        // Return the result (empty map or custom response)
        return ResponseEntity.ok(totauxCnamService.calculateTotalHonor(pCodSoc, pMatPers, pNumFam, pDatSoin, pNumSoin));
    }

    @PostMapping("/deletBultin")
    public ResponseEntity<Map<String, String>> deleteItem(@RequestBody BultSoinCle id) {
        if (bultSoinRepository.existsById(id)) {
            bultSoinRepository.deleteById(id);

            // Return a JSON response instead of a plain string
            Map<String, String> response = new HashMap<>();
            response.put(KEY_MESSAGE, "Item deleted successfully");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put(KEY_ERROR, "Item not found with ID: " + id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/updateBultinCnam")
    public ResponseEntity<List<BultSoin>> updateDemandeDon(
            @RequestBody List<UpdateBultSoinDTO> request) {

        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return 400 if request is empty
        }

        List<BultSoin> updatedBultSoin = new ArrayList<>();

        for (UpdateBultSoinDTO row : request) {
            BultSoinCle id = new BultSoinCle(
                    row.getCod_soc(),
                    row.getMat_pers(),
                    row.getNum_fam(),
                    row.getDat_soin(),
                    row.getNum_soin());

            BultSoin updatedDemande = bulletinSoinService.updateBultSoin(id, row.getEnvoi());
            updatedBultSoin.add(updatedDemande);
        }

        return ResponseEntity.ok(updatedBultSoin);
    }

    @PostMapping("/updateBultSoin")
    public ResponseEntity<Map<String, String>> updateBultSoin(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam int numFam,
            @RequestParam String datSoin, // Changed to String for better parsing
            @RequestParam double mntHonor,
            @RequestParam double mntTot) {

        try {
            // Convert datSoin String to LocalDate
            LocalDate parsedDate = LocalDate.parse(datSoin, DateTimeFormatter.ofPattern(DATE_FORMAT));

            bulletinSoinService.callUpdateBultSoin(codSoc, matPers, numFam, parsedDate, mntHonor, mntTot);

            return ResponseEntity.ok(Collections.singletonMap(KEY_MESSAGE, "Procédure exécutée avec succès!"));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap(KEY_ERROR, "Invalid date format: " + datSoin));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap(KEY_ERROR, "Erreur lors de l'exécution : " + e.getMessage()));
        }
    }

    @PostMapping("/prepFich")
    public ResponseEntity<Map<String, String>> prepFichCnam(
            @RequestParam String codSoc,
            @RequestParam String codBord) {

        try {
            bulletinSoinService.callPrepFichCnam(codSoc, codBord);
            return ResponseEntity.ok(Map.of(KEY_MESSAGE, "Procédure exécutée avec succès!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(KEY_MESSAGE, "Erreur : " + e.getMessage()));
        }

    }

    @PostMapping("/envoiBord")
    public ResponseEntity<Map<String, String>> envoiBordEnvoi(
            @RequestParam String codSoc,
            @RequestParam String codBord) {

        try {
            bulletinSoinService.callEnvoiBordEnvoi(codSoc, codBord);
            return ResponseEntity.ok(Map.of(KEY_MESSAGE, "Procédure envoi_bord_envoi exécutée avec succès!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(KEY_MESSAGE, "Erreur : " + e.getMessage()));
        }

    }

    @GetMapping("/downloadFile")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam String codSoc,
            @RequestParam String codBord) {

        Optional<CnamFileGen> fileGenOptional = fileGenService.getFileByCodSocAndCodBord(codSoc, codBord);

        if (fileGenOptional.isPresent()) {
            CnamFileGen fileGen = fileGenOptional.get();
            byte[] fileBytes = fileGenService.convertClobToBytes(fileGen.getFileContent());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment; filename=" + fileGen.getFileName());
            headers.set("Content-Type", "text/plain"); // Update content type based on file type

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("File not found for codSoc: " + codSoc + " and codBord: " + codBord).getBytes());

        }
    }

}