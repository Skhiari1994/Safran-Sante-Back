
package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.dto.FileContentDTO;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.*;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.BordArriverCnamService;
import com.tn.arabsoft.remboursement_frais_medicaux.service.FileProcessingService;
import com.tn.arabsoft.remboursement_frais_medicaux.util.DateParser;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/BordArriv")
@SuppressWarnings({ "java:S117", "java:S4684", "java:S1452" })
public class BordArriveCnamController {

    private final BordArriverCnamService service;
    private final BordArriverRepository bordArriverRepository;
    private final BultArriverRepository bultArriverRepository;
    private final LigBultArriverRepository ligBultArriverRepository;
    private final LigVisitArriverRepository ligVisitArriverRepository;
    private final LigActArriverRepository ligActArriverRepository;
    private final LigMedArriverRepository ligMedArriverRepository;
    private final LigAppArriverRepository ligAppArriverRepository;
    private final FileProcessingService fileProcessingService;

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_COUNT = "count";

    @GetMapping("/generate")
    public ResponseEntity<String> generateCodBord(
            @RequestParam String codSoc) {

        String codBord = service.getGeneratedCodBord(codSoc);
        return ResponseEntity.ok(codBord);
    }

    @PostMapping("/saveBordArrive")
    public ResponseEntity<?> saveBordArriver(@RequestBody BordArriver bordArriver) {
        try {
            if (bordArriver.getCod_bord() == null || bordArriver.getDat_bord() == null) {
                return ResponseEntity.badRequest().body("❌ Données manquantes : cod_bord ou dat_bord");
            }

            BordArriver saved = bordArriverRepository.save(bordArriver);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Erreur interne : " + e.getMessage());
        }
    }

    @PostMapping("/saveBultArrive")
    public ResponseEntity<List<BultArriver>> saveBultArrive(@RequestBody List<BultArriver> buArriver) {
        List<BultArriver> saved = bultArriverRepository.saveAll(buArriver);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/addLigActArriver")
    public ResponseEntity<Map<String, Object>> addLigActArriver(@RequestBody List<LigActArriver> ligBultActs) {
        try {

            List<LigActArriver> savedList = ligActArriverRepository.saveAll(ligBultActs);

            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            response.put(KEY_COUNT, savedList.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, "Error inserting actes: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigVisitArriver")
    public ResponseEntity<Map<String, Object>> addLigVisitArriver(@RequestBody List<LigVisitArriver> ligBultActs) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<LigVisitArriver> savedList = ligVisitArriverRepository.saveAll(ligBultActs);

            response.put(KEY_SUCCESS, true);
            response.put(KEY_MESSAGE, "Inserted/Updated " + savedList.size() + " visites successfully");
            response.put(KEY_COUNT, savedList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, "Error inserting visites: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigMedArriver")
    public ResponseEntity<Map<String, Object>> addLigMedArriver(@RequestBody List<LigMedArriver> ligBultMeds) {
        try {
            List<LigMedArriver> savedList = ligMedArriverRepository.saveAll(ligBultMeds);

            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            response.put(KEY_COUNT, savedList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, "Error inserting medicaments: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigAppArriver")
    public ResponseEntity<Map<String, Object>> addLigAppArriver(@RequestBody List<LigAppArriver> ligBultApps) {
        try {
            List<LigAppArriver> savedList = ligAppArriverRepository.saveAll(ligBultApps);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            response.put(KEY_COUNT, savedList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, "Error inserting appareils: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/lov-numfil")
    public ResponseEntity<Page<LovNumFilCnamLibre>> getLovNumFil(
            @RequestParam String codSoc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LovNumFilCnamLibre> result = bultArriverRepository.lovNumFilCnamLibre(codSoc, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/getBordArriver")
    public List<BordArriverProjection> getBordArriver() {
        return bordArriverRepository.bordArriver();
    }

    @GetMapping("/getcodBordArriver")
    public List<BordArriverProjection> getBordArriver(@RequestParam String bord) {

        return bordArriverRepository.getCodBordArriver(bord);
    }

    @GetMapping("/getLigBultArriver")
    public ResponseEntity<List<LigBultArriverProjection>> getLigBultArriver(@RequestParam String soc,
            @RequestParam String mat, @RequestParam String numFam, @RequestParam String datSoin) {
        List<LigBultArriverProjection> bultArriverReg = ligBultArriverRepository.getLigBultArriver(soc, mat, numFam,
                datSoin);

        return ResponseEntity.ok(bultArriverReg);
    }

    @GetMapping("/LigVisitArriverById")
    public List<LigVisitArriverProjection> getLigVisitArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        return ligVisitArriverRepository.findLigVisitArriverById(cod_soc,
                mat_pers, num_fam, dat_soin);
    }

    @GetMapping("/LigActArriverById")
    public List<LigActArriverProjection> getLigActArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = LocalDate.parse(dat_soin);
        return ligActArriverRepository.findLigActArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @GetMapping("/LigMedArriverById")
    public List<LigMedArriverProjection> getLigMedArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        return ligMedArriverRepository.findLigMedArriverById(cod_soc, mat_pers,
                num_fam, dat_soin);
    }

    @GetMapping("/LigAppArriverById")
    public List<LigAppArriverProjection> getLigAppArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = DateParser.parse(dat_soin);
        return ligAppArriverRepository.findLigAppArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @GetMapping("/BultArriverByCodBord")
    public ResponseEntity<List<BultArriverProjection>> getBultArriverByCodBord(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultArriverByCodBord(cod_soc, cod_bord);
        if (bultArrivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bultArrivers);
    }

    @DeleteMapping("/deleteLigActArriver")
    public ResponseEntity<Map<String, Object>> deleteLigActArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("numLig") String numLig, @RequestParam("datAct") String datAct,
            @RequestParam("codAct") String codAct) {
        try {
            LocalDate date_soin = LocalDate.parse(datSoin);
            LocalDate date_acte = LocalDate.parse(datAct);
            ligActArriverRepository.deleteLigActArriver(soc, mat, fam, date_soin, abrv, numLig, date_acte, codAct);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteLigAppArriver")
    public ResponseEntity<Map<String, Object>> deleteLigAppArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam(value = "numLig", required = false) String numLig, @RequestParam("codApp") String codApp) {
        try {
            LocalDate dat_soin = DateParser.parse(datSoin);
            ligAppArriverRepository.deleteLigAppArriver(soc, mat, fam, dat_soin, abrv, numLig, codApp);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteLigVisitArriver")
    public ResponseEntity<Map<String, Object>> deleteLigVisitArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam("datAct") String datAct, @RequestParam("codVisit") String codVisit,
            @RequestParam("numLig") String numLig) {
        try {
            ligVisitArriverRepository.deleteLigVisitArriver(soc, mat, fam, datSoin, abrv, datAct, codVisit, numLig);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteLigMedArriver")
    public ResponseEntity<Map<String, Object>> deleteLigMedArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin,
            @RequestParam("abrv") String abrv, @RequestParam("numLig") String numLig,
            @RequestParam("codMed") String codMed) {
        try {
            ligMedArriverRepository.deleteMedArriver(soc, mat, fam, datSoin, abrv, codMed, numLig);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteLigBultArriver")
    public ResponseEntity<Map<String, Object>> deleteLigBultArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin,
            @RequestParam("numLig") String numLig) {
        try {
            ligBultArriverRepository.deleteLigBultArriver(soc, mat, fam, datSoin, numLig);
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteBultArrive")
    @jakarta.transaction.Transactional
    public ResponseEntity<Map<String, Object>> deleteBultArrive(
            @RequestParam String soc,
            @RequestParam String mat,
            @RequestParam String fam,
            @RequestParam String datSoin) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer famInt = Integer.parseInt(fam);
            Long famLong = Long.parseLong(fam);

            // 1. Cleanup all detail tables
            LocalDate date_soin = DateParser.parse(datSoin);
            ligActArriverRepository.deleteByBulletin(soc, mat, famInt, date_soin);
            ligVisitArriverRepository.deleteByBulletin(soc, mat, famLong, datSoin);
            ligMedArriverRepository.deleteByBulletin(soc, mat, famInt, datSoin);
            ligAppArriverRepository.deleteByBulletin(soc, mat, famInt, date_soin);
            ligBultArriverRepository.deleteByBulletin(soc, mat, fam, datSoin);

            // 2. Delete the bulletin header
            bultArriverRepository.deleteBulletin(soc, mat, fam, date_soin);

            response.put(KEY_SUCCESS, true);
            response.put(KEY_MESSAGE, "Bulletin and all details deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put(KEY_SUCCESS, false);
            response.put(KEY_MESSAGE, "Error deleting bulletin: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/existeLigActArriver")
    public ResponseEntity<Boolean> existeLigActArriver(@RequestBody LigActArriverCle id) {

        boolean exists = ligActArriverRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/existeLigVisitArriver")
    public ResponseEntity<Boolean> existeLigVisitArriver(@RequestBody LigVisitArriverCle id) {

        boolean exists = ligVisitArriverRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/existeLigAppArriver")
    public ResponseEntity<Boolean> existeLigAppArriver(@RequestBody LigAppArriverCle id) {

        boolean exists = ligAppArriverRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/existeLigMedArriver")
    public ResponseEntity<Boolean> existeLigMedArriver(@RequestBody LigMedArriverCle id) {

        boolean exists = ligMedArriverRepository.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/BultArriverById")
    public BultArriver getBultArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = DateParser.parse(dat_soin);
        return bultArriverRepository.findBultArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @PostMapping("/upload-file")
    public ResponseEntity<String> uploadFile(@RequestBody FileContentDTO fileContent) {
        try {
            fileProcessingService.loadFile(fileContent.getLines(), fileContent.getCodSoc());
            return ResponseEntity.ok("File processed successfully");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error processing file: " + e.getMessage());
        }
    }

    @PutMapping("/validate")
    public ResponseEntity<Map<String, String>> validateBord(
            @RequestParam String codSoc,
            @RequestParam String codAssur,
            @RequestParam String codBord) {

        int updated = service.validateBord(codSoc, codAssur, codBord);

        Map<String, String> response = new HashMap<>();
        if (updated > 0) {
            response.put(KEY_MESSAGE, "Bord validé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put(KEY_MESSAGE, "Aucun bord trouvé pour validation");
            return ResponseEntity.ok(response);
        }
    }

}
