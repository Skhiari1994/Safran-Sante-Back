package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;

import com.tn.arabsoft.RemboursementFraisMedicaux.DTO.BordereauRequest;
import com.tn.arabsoft.RemboursementFraisMedicaux.DTO.UpdateBultSoinDTO;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultSoin;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BultSoinCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.CnamFileGen;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.AssuranceRepository;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.BordEnvoiRepository;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.BultSoinRepository;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.BulletinSoinService;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.CnamFileGenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@RequestMapping("/BordEnvoi")
public class BordCnamController {
    @Autowired
    BordEnvoiRepository bordEnvoiRepository;

    @Autowired
    AssuranceRepository assuranceRepository;
    @Autowired
    BultSoinRepository bultSoinRepository;
    @Autowired
    BulletinSoinService bulletinSoinService;

    private final CnamFileGenService fileGenService;

    public BordCnamController(CnamFileGenService fileGenService) {
        this.fileGenService = fileGenService;
    }

    @GetMapping("/getCodBord")
    List<BordEnvoiPrejection> getCodBord(@RequestParam String soc ){
        return bordEnvoiRepository.getListBordEnvoiCNAM(soc);
    }

    @GetMapping("/getBord")
    List<BordEnvoiPrejection> getBord(@RequestParam String codBord, @RequestParam LocalDate datBord ){
        return bordEnvoiRepository.getBordEnvoi(codBord,datBord);
    }
    @GetMapping("/getCodBordMut")
    List<BordereauProjection> getCodBordMut(@RequestParam String soc ){
        return bordEnvoiRepository.getListBordEnvoiMut(soc);
    }
    @GetMapping("/getAllBordEnvoi")
    List<BordEnvoiPrejection> getAllBordEnvoi(@RequestParam String cod_soc){
        return bordEnvoiRepository.getListBordEnvoiCNAM(cod_soc);
    }
    @GetMapping("/getListBordEnvoiCNAM")
    List<BordEnvoiPrejection> getListBordEnvoiCNAM(@RequestParam String soc ){
        return bordEnvoiRepository.getListBordEnvoiCNAM(soc);
    }
    @GetMapping("/getBordMut")
    List<BordEnvoiPrejection> getBordMut(@RequestParam String codBord, @RequestParam LocalDate datBord ){
        return bordEnvoiRepository.getBordEnvoiMut(codBord,datBord);
    }
    @GetMapping("/getAssur")
    List<ListAssurProjection> getAssur( ){
        return assuranceRepository.getListAssurance();
    }
    @GetMapping("/getBsoinBordCnam")
    List<BultSoinProjection> getBsoinBordCnam(@RequestParam String codSoc,@RequestParam String codBord, @RequestParam String codAssur ){
        return bultSoinRepository.getBulletinSoinEnvoi(codSoc,codBord,codAssur);
    }

    @GetMapping("/consBs")
    List<BultSoinProjection> getBulletSoinConsultaion(@RequestParam String codSoc,@RequestParam String codAssur, @RequestParam(required = false) LocalDate datDeb,@RequestParam(required = false) LocalDate datFin ){
        return bultSoinRepository.getBulletSoinConsultaion(codSoc,codAssur,datDeb,datFin);
    }



    @GetMapping("/getBsoinBordMut")
    List<BultSoinProjection> getBsoinBordMut(@RequestParam String codSoc,@RequestParam String codBord, @RequestParam String codAssur ){
        return bultSoinRepository.getBulletinSoinEnvoiMut(codSoc,codBord,codAssur);
    }

    @GetMapping("/consBsMut")
    List<BultSoinProjection> getBulletSoinConsultaionMut(@RequestParam String codSoc,@RequestParam String codAssur, @RequestParam(required = false) LocalDate datDeb,@RequestParam(required = false) LocalDate datFin ){
        return bultSoinRepository.getBulletSoinConsultaionMut(codSoc,codAssur,datDeb,datFin);
    }
    /*@PostMapping("/generateCodeBord")
    public ResponseEntity<Map<String, String>> generateBordCode(
            @RequestParam String codAssur,
            @RequestParam String codSoc
    ) {
        try {
            String bordCode = bulletinSoinService.callGenerateBordCode(codAssur, codSoc);
            Map<String, String> response = new HashMap<>();
            response.put("bordCode", bordCode);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }*/

    @PostMapping("/generateCodeBord")
    public ResponseEntity<String> generateAndSaveBord(
            @RequestParam String codAssur,
            @RequestParam String codSoc,
            @RequestParam(required = false) String datDeb,
            @RequestParam(required = false) String datFin,
            @RequestParam int nbrBult,
            @RequestParam double totHonor,
            @RequestParam double totNet
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

        String codBord = bulletinSoinService.callGenerateBordCode(
                codAssur,
                codSoc,
                localDatDeb,
                localDatFin,
                nbrBult,
                totHonor,
                totNet
        );
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
            @RequestParam double totNet
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

        // ✅ La procédure gen_bord_mut a été corrigée pour générer des codes au bon format
        String codBord = bulletinSoinService.callGenerateBordCodeMut(
                codAssur,
                codSoc,
                localDatDeb,
                localDatFin,
                nbrBult,
                totHonor,
                totNet
        );
        return ResponseEntity.ok(codBord);
    }
    @PostMapping("/updateBultSoinCnam")
    public ResponseEntity<String> createBordBult(
            @RequestParam String codBord,
            @RequestParam String matPers,
            @RequestParam String codSoc,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate datSoin,
            @RequestParam int numFam,
            @RequestParam int ordBult,
            @RequestParam BigDecimal totHonor,
            @RequestParam BigDecimal totNet
    ) {
        try {
            // Appel du service
            String message = bulletinSoinService.callCreateBordBultAll(
                    codBord, matPers, codSoc, datSoin, numFam, ordBult, totHonor, totNet);

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
            @RequestParam String codBord
    ) {
        try {
            String resultMessage = bulletinSoinService.callDeletBord(codSoc, codBord);

            Map<String, String> response = new HashMap<>();
            response.put("message", resultMessage);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Une erreur est survenue lors de la suppression."));
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
                                                  @RequestParam int vNbrBult
    ) {
        try {
            bulletinSoinService.callModifBord(
                    pCodAssur, pCodSoc, pMatPers, pNumFam, pDatSoin,
                    pTotHonor, pTotNet, pChoix, pCodBord, vNbrBult
            );

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
        Map<String, Object> result = bulletinSoinService.callCreateBord(pCodAssur, pCodSoc, pTotHonor, pTotNet, pChoix, vNBRBult, pCodBord, datDeb,datFin);

        // Return the result (empty map or custom response)
        return ResponseEntity.ok(result);
    }


    @PostMapping("/totauCnam")
    public ResponseEntity<Map<String, Object>> createBord(
            @RequestParam String pCodSoc,
            @RequestParam String pMatPers,
            @RequestParam Long pNumFam,
            @RequestParam  LocalDate pDatSoin) {

        // Call the service method to invoke the stored procedure
        Map<String, Object> result = bulletinSoinService.callCalculateTotalHonor(pCodSoc,pMatPers, pNumFam,pDatSoin);

        // Return the result (empty map or custom response)
        return ResponseEntity.ok(result);
    }

    @PostMapping("/deletBultin")
    public ResponseEntity<Map<String, String>> deleteItem(@RequestBody BultSoinCle id) {
        if (bultSoinRepository.existsById(id)) {
            bultSoinRepository.deleteById(id);

            // Return a JSON response instead of a plain string
            Map<String, String> response = new HashMap<>();
            response.put("message", "Item deleted successfully");

            return ResponseEntity.ok(response);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Item not found with ID: " + id);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/updateBultinCnam")
    public ResponseEntity<List<BultSoin>> updateDemandeDon(
            @RequestBody List<UpdateBultSoinDTO> request) {

        System.out.println("Received data: " + request); // Debugging

        if (request == null || request.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return 400 if request is empty
        }

        List<BultSoin> updatedBultSoin = new ArrayList<>();

        for (UpdateBultSoinDTO row : request) {
            BultSoinCle id = new BultSoinCle(
                    row.getCod_soc(),
                    row.getMat_pers(),
                    row.getNum_fam(),
                    row.getDat_soin()
            );

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
            LocalDate parsedDate = LocalDate.parse(datSoin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            bulletinSoinService.callUpdateBultSoin(codSoc, matPers, numFam, parsedDate, mntHonor, mntTot);

            return ResponseEntity.ok(Collections.singletonMap("message", "Procédure exécutée avec succès!"));

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid date format: " + datSoin));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Collections.singletonMap("error", "Erreur lors de l'exécution : " + e.getMessage()));
        }
    }
    @PostMapping("/prepFich")
    public ResponseEntity<Map<String, String>> prepFichCnam(
            @RequestParam String codSoc,
            @RequestParam String codBord) {
        try {
            bulletinSoinService.callPrepFichCnam(codSoc, codBord);
            return ResponseEntity.ok(Map.of("message", "Procédure exécutée avec succès!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erreur : " + e.getMessage()));
        }
    }

    @PostMapping("/envoiBord")
    public ResponseEntity<Map<String, String>> envoiBordEnvoi(
            @RequestParam String codSoc,
            @RequestParam String codBord) {
        try {
            bulletinSoinService.callEnvoiBordEnvoi(codSoc, codBord);
            return ResponseEntity.ok(Map.of("message", "Procédure envoi_bord_envoi exécutée avec succès!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Erreur : " + e.getMessage()));
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
