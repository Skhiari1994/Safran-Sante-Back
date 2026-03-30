package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;

import com.tn.arabsoft.RemboursementFraisMedicaux.DTO.FileContentDTO;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.BordArriverCnamService;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/BordArriv")
public class BordArriveCnamController {
    private final BordArriverCnamService service;

    public BordArriveCnamController(BordArriverCnamService service) {
        this.service = service;
    }

    @Autowired
    private BordArriverRepository bordArriverRepository;
    @Autowired
    private BultArriverRepository bultArriverRepository;
    @Autowired
    LigBultArriverRepository ligBultArriverRepository;
    @Autowired
    LigVisitArriverRepository ligVisitArriverRepository;
    @Autowired
    LigActArriverRepository ligActArriverRepository;
    @Autowired
    LigMedArriverRepository ligMedArriverRepository;
    @Autowired
    LigAppArriverRepository ligAppArriverRepository;
    @Autowired
    private com.tn.arabsoft.RemboursementFraisMedicaux.Service.BatchInsertService batchInsertService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateCodBord(
            @RequestParam String codSoc) {

        String codBord = service.getGeneratedCodBord(codSoc);
        return ResponseEntity.ok(codBord);
    }

    @PostMapping("/saveBordArrive")
    public ResponseEntity<?> saveBordArriver(@RequestBody BordArriver bordArriver) {
        try {
            System.out.println("🔍 Données reçues : " + bordArriver);

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
            response.put("success", true);
            response.put("count", savedList.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error inserting actes: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigVisitArriver")
    public ResponseEntity<Map<String, Object>> addLigVisitArriver(@RequestBody List<LigVisitArriver> ligBultActs) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<LigVisitArriver> savedList = ligVisitArriverRepository.saveAll(ligBultActs);

            response.put("success", true);
            response.put("message", "Inserted/Updated " + savedList.size() + " visites successfully");
            response.put("count", savedList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error inserting visites: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigMedArriver")
    public ResponseEntity<Map<String, Object>> addLigMedArriver(@RequestBody List<LigMedArriver> ligBultMeds) {
        try {
            List<LigMedArriver> savedList = ligMedArriverRepository.saveAll(ligBultMeds);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", savedList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error inserting medicaments: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/addLigAppArriver")
    public ResponseEntity<Map<String, Object>> addLigAppArriver(@RequestBody List<LigAppArriver> ligBultApps) {
        try {
            List<LigAppArriver> savedList = ligAppArriverRepository.saveAll(ligBultApps);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", savedList.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error inserting appareils: " + e.getMessage());
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

        return bordArriverRepository.BordArriver();
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
        List<LigVisitArriverProjection> ligVisitArriver = ligVisitArriverRepository.findLigVisitArriverById(cod_soc,
                mat_pers, num_fam, dat_soin);
        return ligVisitArriver;
    }

    @GetMapping("/LigActArriverById")
    public List<LigActArriverProjection> getLigActArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        List<LigActArriverProjection> ligActArriver = ligActArriverRepository.findLigActArriverById(cod_soc, mat_pers,
                num_fam, dat_soin);
        return ligActArriver;
    }

    @GetMapping("/LigMedArriverById")
    public List<LigMedArriverProjection> getLigMedArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        List<LigMedArriverProjection> ligMedArriver = ligMedArriverRepository.findLigMedArriverById(cod_soc, mat_pers,
                num_fam, dat_soin);
        return ligMedArriver;
    }

    @GetMapping("/LigAppArriverById")
    public List<LigAppArriverProjection> getLigAppArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        List<LigAppArriverProjection> ligAppArriver = ligAppArriverRepository.findLigAppArriverById(cod_soc, mat_pers,
                num_fam, dat_soin);
        return ligAppArriver;
    }

    @GetMapping("/BultArriverByCodBord")
    public ResponseEntity<List<BultArriverProjection>> getBultArriverByCodBord(@RequestParam String cod_bord) {
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultArriverByCodBord(cod_bord);
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
            ligActArriverRepository.deleteLigActArriver(soc, mat, fam, datSoin, abrv, numLig, datAct, codAct);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/deleteLigAppArriver")
    public ResponseEntity<Map<String, Object>> deleteLigAppArriver(@RequestParam("soc") String soc,
            @RequestParam("mat") String mat,
            @RequestParam("fam") String fam, @RequestParam("datSoin") String datSoin, @RequestParam("abrv") String abrv,
            @RequestParam(value = "numLig", required = false) String numLig, @RequestParam("codApp") String codApp) {
        try {
            ligAppArriverRepository.deleteLigAppArriver(soc, mat, fam, datSoin, abrv, numLig, codApp);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
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
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
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
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
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
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
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
            ligActArriverRepository.deleteByBulletin(soc, mat, famInt, datSoin);
            ligVisitArriverRepository.deleteByBulletin(soc, mat, famLong, datSoin);
            ligMedArriverRepository.deleteByBulletin(soc, mat, famInt, datSoin);
            ligAppArriverRepository.deleteByBulletin(soc, mat, famInt, datSoin);
            ligBultArriverRepository.deleteByBulletin(soc, mat, fam, datSoin);

            // 2. Delete the bulletin header
            bultArriverRepository.deleteBulletin(soc, mat, fam, datSoin);

            response.put("success", true);
            response.put("message", "Bulletin and all details deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error deleting bulletin: " + e.getMessage());
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
        BultArriver bultArriver = bultArriverRepository.findBultArriverById(cod_soc, mat_pers, num_fam, dat_soin);
        return bultArriver;
    }

    @Autowired
    private FileProcessingService fileProcessingService;

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
            response.put("message", "Bord validé avec succès");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Aucun bord trouvé pour validation");
            return ResponseEntity.ok(response);
        }
    }

}
