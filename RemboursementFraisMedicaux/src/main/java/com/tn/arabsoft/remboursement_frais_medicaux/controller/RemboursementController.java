package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigActArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigAppArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigMedArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseCloture;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseRegBord;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.LigService;
import com.tn.arabsoft.remboursement_frais_medicaux.service.RemboursementService;
import com.tn.arabsoft.remboursement_frais_medicaux.util.DateParser;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Remboursement")
@RequiredArgsConstructor
@SuppressWarnings({ "java:S117", "java:S4684", "java:S6856" })
public class RemboursementController {

    private final RemboursementService remboursementService;
    private final BordArriverRepository bordArriverRepository;
    private final BultArriverRepository bultArriverRepository;
    private final LigActArriverRepository ligActArriverRepository;
    private final LigAppArriverRepository ligAppArriverRepository;
    private final LigMedArriverRepository ligMedArriverRepository;
    private final LigVisitArriverRepository ligVisitArriverRepository;
    private final BordEnvoiRepository bordEnvoiRepository;
    private final BultSoinRepository bultSoinRepository;

    private static final String KEY_BORDEREAU = "bordereau";
    private static final String KEY_BULLETINS = "bulletins";
    private static final String KEY_ACTES = "actes";
    private static final String KEY_VISITES = "visites";
    private static final String KEY_APPAREIL = "appareil";
    private static final String KEY_MEDICAMENTS = "medicaments";

    @GetMapping("/initialisationPlafond")
    void suspPret(@RequestParam String soc, @RequestParam String annee, @RequestParam String mat_deb,
            @RequestParam String mat_fin) {
        remboursementService.initialtionPlafond(soc, annee, mat_deb, mat_fin);
    }

    @GetMapping("/BordArriver")
    public ResponseEntity<List<BordArriverProjection>> getAllBordArriver() {
        return ResponseEntity.ok(bordArriverRepository.findAllBord());
    }

    @GetMapping("/GetAllBordArriver")
    public List<BordArriverProjection> getAllBordArriver(@RequestParam String cod_soc) {
        return bordArriverRepository.getAllBordArriver(cod_soc);
    }

    @GetMapping("/BordArriver/{id}")
    public ResponseEntity<BordArriver> getBordArriverById(@PathVariable String id) {
        Optional<BordArriver> bordArriver = bordArriverRepository.findById(id);
        return bordArriver.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/BultArriverByCodBord2")
    public ResponseEntity<List<BultArriverProjection>> getBultArriverByCodBord2(@RequestParam String cod_bord) {
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultArriverByCodBord2(cod_bord);
        if (bultArrivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bultArrivers);
    }

    @GetMapping("/BultArriverByCodBord3")
    public ResponseEntity<List<BultArriverProjection>> findBultEnvoiByCodBord(@RequestParam String cod_bord) {
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultEnvoiByCodBord(cod_bord);
        if (bultArrivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bultArrivers);
    }

    @PostMapping("/BordArriver")
    public BordArriver createBordArriver(@RequestBody BordArriver bordArriver) {
        return bordArriverRepository.save(bordArriver);
    }

    @PutMapping("/BordArriver/{id}")
    public ResponseEntity<BordArriver> updateBordArriver(@PathVariable String id,
            @RequestBody BordArriver bordArriverDetails) {
        return bordArriverRepository.findById(id).map(bordArriver -> {
            bordArriver.setCod_assur(bordArriverDetails.getCod_assur());
            bordArriver.setDat_bord(bordArriverDetails.getDat_bord());
            bordArriver.setDat_deb(bordArriverDetails.getDat_deb());
            bordArriver.setDat_fin(bordArriverDetails.getDat_fin());
            bordArriver.setNbr_bult(bordArriverDetails.getNbr_bult());
            bordArriver.setTot_honor(bordArriverDetails.getTot_honor());
            bordArriver.setTyp_bord(bordArriverDetails.getTyp_bord());
            bordArriver.setCod_soc(bordArriverDetails.getCod_soc());
            bordArriver.setTot_net(bordArriverDetails.getTot_net());
            bordArriver.setValid_bord(bordArriverDetails.getValid_bord());
            bordArriver.setReg_bord(bordArriverDetails.getReg_bord());
            bordArriver.setTot_remb(bordArriverDetails.getTot_remb());
            bordArriver.setValid(bordArriverDetails.getValid());
            bordArriver.setClot_bord(bordArriverDetails.getClot_bord());
            bordArriver.setTot(bordArriverDetails.getTot());
            bordArriver.setNum_retr(bordArriverDetails.getNum_retr());
            bordArriver.setCod_bord_cnam(bordArriverDetails.getCod_bord_cnam());
            bordArriver.setSeq_bord(bordArriverDetails.getSeq_bord());
            return ResponseEntity.ok(bordArriverRepository.save(bordArriver));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/BordArriver/{id}")
    public ResponseEntity<Object> deleteBordArriver(@PathVariable String id) {
        return bordArriverRepository.findById(id).map(bordArriver -> {
            bordArriverRepository.delete(bordArriver);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // bult arriver crud

    @GetMapping("/BultArriver")
    public List<BultArriver> getAllBultArriver() {
        return bultArriverRepository.findAll();
    }

    @GetMapping("/BordEnvoiEntete/{soc}")
    public List<BordEnvoiProjection> enteteBordEnvoi(@PathVariable String soc) {
        return bordEnvoiRepository.enteteBordEnvoi(soc);
    }

    @GetMapping("/BultArriverById")
    public BultArriver getBultArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = DateParser.parse(dat_soin);
        return bultArriverRepository.findBultArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @GetMapping("/BultArriverByCodBord")
    public ResponseEntity<List<BultArriverProjection>> getBultArriverByCodBord(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return ResponseEntity.ok(bultArriverRepository.findBultArriverByCodBord(cod_soc, cod_bord));
    }

    @GetMapping("/BultArriverByCodBord4")
    public ResponseEntity<List<BultArriverProjection>> getBultArriverByCodBord4(@RequestParam String cod_bord) {
        return ResponseEntity.ok(bultArriverRepository.findBultArriverByCodBord4(cod_bord));
    }

    @PostMapping("/BultArriver")
    public BultArriver createBultArriver(@RequestBody BultArriver bultArriver) {
        return bultArriverRepository.save(bultArriver);
    }

    @PostMapping("/BultArriver/batch")
    public ResponseEntity<List<BultArriver>> createBultArriverBatch(@RequestBody List<BultArriver> bultArrivers) {
        List<BultArriver> savedBultArrivers = bultArriverRepository.saveAll(bultArrivers);
        return ResponseEntity.ok(savedBultArrivers);
    }

    @PutMapping("/BultArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}")
    public ResponseEntity<BultArriver> updateBultArriver(@PathVariable String cod_soc, @PathVariable String mat_pers,
            @PathVariable Integer num_fam, @PathVariable String dat_soin,
            @RequestBody BultArriver bultArriverDetails) {
        BultArriverCle id = new BultArriverCle(cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin));
        return bultArriverRepository.findById(id).map(bultArriver -> {
            bultArriverDetails.setCod_soc(cod_soc);
            bultArriverDetails.setMat_pers(mat_pers);
            bultArriverDetails.setNum_fam(num_fam);
            bultArriverDetails.setDat_soin(LocalDate.parse(dat_soin));
            return ResponseEntity.ok(bultArriverRepository.save(bultArriverDetails));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/BultArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}")
    public ResponseEntity<Object> deleteBultArriver(@PathVariable String cod_soc, @PathVariable String mat_pers,
            @PathVariable Integer num_fam, @PathVariable String dat_soin) {
        BultArriverCle id = new BultArriverCle(cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin));
        return bultArriverRepository.findById(id).map(bultArriver -> {
            bultArriverRepository.delete(bultArriver);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Lig Act Arriver Crud

    @PostMapping("/LigActArriver")
    public ResponseEntity<LigActArriver> createLigActArriver(@RequestBody LigActArriver ligActArriver) {
        LigActArriver savedLigActArriver = ligActArriverRepository.save(ligActArriver);
        return ResponseEntity.ok(savedLigActArriver);
    }

    @PostMapping("/LigActArriver/batch")
    public ResponseEntity<List<LigActArriver>> createLigActArriverBatch(
            @RequestBody List<LigActArriver> ligActArrivers) {
        List<LigActArriver> savedLigActArrivers = ligActArriverRepository.saveAll(ligActArrivers);
        return ResponseEntity.ok(savedLigActArrivers);
    }

    @GetMapping("/LigActArriverById")
    public List<LigActArriverProjection> getLigActArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = DateParser.parse(dat_soin);
        return ligActArriverRepository.findLigActArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @GetMapping("/LigActArriver")
    public List<LigActArriver> getAllLigActArrivers() {
        return ligActArriverRepository.findAll();
    }

    @PutMapping("/LigActArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}/{indice}")
    public ResponseEntity<LigActArriver> updateLigActArriver(
            @PathVariable String cod_soc,
            @PathVariable String mat_pers,
            @PathVariable Integer num_fam,
            @PathVariable String dat_soin,
            @PathVariable Integer indice,
            @RequestBody LigActArriver ligActArriverDetails) {

        LigActArriverCle id = new LigActArriverCle(cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin), indice);
        Optional<LigActArriver> ligActArriverOptional = ligActArriverRepository.findById(id);

        if (ligActArriverOptional.isPresent()) {
            LigActArriver ligActArriver = ligActArriverOptional.get();

            // Mise à jour des champs
            ligActArriver.setAbrv_act(ligActArriverDetails.getAbrv_act());
            ligActArriver.setCod_act(ligActArriverDetails.getCod_act());
            ligActArriver.setNum_lig(ligActArriverDetails.getNum_lig());
            ligActArriver.setLet_cod(ligActArriverDetails.getLet_cod());
            ligActArriver.setCot_act(ligActArriverDetails.getCot_act());
            ligActArriver.setAct_prix(ligActArriverDetails.getAct_prix());
            ligActArriver.setMnt_honor(ligActArriverDetails.getMnt_honor());
            ligActArriver.setMnt_remb(ligActArriverDetails.getMnt_remb());
            ligActArriver.setAccord_act(ligActArriverDetails.getAccord_act());
            ligActArriver.setMnt_net(ligActArriverDetails.getMnt_net());
            ligActArriver.setIndice(ligActArriverDetails.getIndice());
            ligActArriver.setDat_act(ligActArriverDetails.getDat_act());
            ligActArriver.setPrf_typ(ligActArriverDetails.getPrf_typ());
            ligActArriver.setPrf_cod(ligActArriverDetails.getPrf_cod());
            ligActArriver.setMut_mnt_net(ligActArriverDetails.getMut_mnt_net());
            ligActArriver.setNum_pec_act(ligActArriverDetails.getNum_pec_act());
            ligActArriver.setDecis_act(ligActArriverDetails.getDecis_act());

            LigActArriver updatedLigActArriver = ligActArriverRepository.save(ligActArriver);
            return ResponseEntity.ok(updatedLigActArriver);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/LigActArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}/{indice}")
    public ResponseEntity<Void> deleteLigActArriver(
            @PathVariable String cod_soc,
            @PathVariable String mat_pers,
            @PathVariable Integer num_fam,
            @PathVariable String dat_soin,
            @PathVariable Integer indice) {

        LigActArriverCle id = new LigActArriverCle(cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin), indice);
        Optional<LigActArriver> ligActArriver = ligActArriverRepository.findById(id);

        if (ligActArriver.isPresent()) {
            ligActArriverRepository.delete(ligActArriver.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Lig app arriver crud
    @PostMapping("/LigAppArriver")
    public ResponseEntity<LigAppArriver> createLigAppArriver(@RequestBody LigAppArriver ligAppArriver) {
        LigAppArriver savedLigAppArriver = ligAppArriverRepository.save(ligAppArriver);
        return ResponseEntity.ok(savedLigAppArriver);
    }

    @PostMapping("/LigAppArriver/batch")
    public ResponseEntity<List<LigAppArriver>> createLigAppArriverBatch(
            @RequestBody List<LigAppArriver> ligAppArrivers) {
        List<LigAppArriver> savedLigAppArrivers = ligAppArriverRepository.saveAll(ligAppArrivers);
        return ResponseEntity.ok(savedLigAppArrivers);
    }

    @GetMapping("/LigAppArriverById")
    public List<LigAppArriverProjection> getLigAppArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate dateSoin = DateParser.parse(dat_soin);
        return ligAppArriverRepository.findLigAppArriverById(cod_soc, mat_pers, num_fam, dateSoin);
    }

    @GetMapping("/LigAppArriver")
    public List<LigAppArriver> getAllLigAppArrivers() {
        return ligAppArriverRepository.findAll();
    }

    @PutMapping("/LigAppArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}/{num_lig}")
    public ResponseEntity<LigAppArriver> upsertLigAppArriver(
            @PathVariable String cod_soc,
            @PathVariable String mat_pers,
            @PathVariable Integer num_fam,
            @PathVariable String dat_soin,
            @PathVariable Integer num_lig,
            @RequestBody LigAppArriver ligAppArriverDetails) {

        // Créer l'ID complet (PK composite)
        LigAppArriverCle id = new LigAppArriverCle(
                cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin), num_lig);

        // Si existe, on récupère pour UPDATE
        LigAppArriver ligApp = ligAppArriverRepository.findById(id)
                .orElseGet(() -> {
                    // Sinon on crée une nouvelle instance pour INSERT
                    LigAppArriver newLig = new LigAppArriver();
                    newLig.setCod_soc(cod_soc);
                    newLig.setMat_pers(mat_pers);
                    newLig.setNum_fam(num_fam);
                    newLig.setDat_soin(LocalDate.parse(dat_soin));
                    newLig.setNum_lig(num_lig);
                    return newLig;
                });

        // Mettre à jour tous les champs depuis le body
        ligApp.setAbrv_act(ligAppArriverDetails.getAbrv_act());
        ligApp.setCod_app(ligAppArriverDetails.getCod_app());
        ligApp.setMnt_honor(ligAppArriverDetails.getMnt_honor());
        ligApp.setMnt_net(ligAppArriverDetails.getMnt_net());
        ligApp.setMnt_remb(ligAppArriverDetails.getMnt_remb());
        ligApp.setAccord_app(ligAppArriverDetails.getAccord_app());
        ligApp.setIndice(ligAppArriverDetails.getIndice());
        ligApp.setDat_act(ligAppArriverDetails.getDat_act());
        ligApp.setPrf_typ(ligAppArriverDetails.getPrf_typ());
        ligApp.setPrf_cod(ligAppArriverDetails.getPrf_cod());
        ligApp.setNum_pec_app(ligAppArriverDetails.getNum_pec_app());
        ligApp.setNum_lig_app(ligAppArriverDetails.getNum_lig_app());
        ligApp.setMut_mnt_net(ligAppArriverDetails.getMut_mnt_net());

        // Sauvegarde (INSERT ou UPDATE)
        LigAppArriver saved = ligAppArriverRepository.save(ligApp);

        return ResponseEntity.ok(saved);
    }

    // ---------------------------
    // DELETE
    // ---------------------------
    @DeleteMapping("/LigAppArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}/{num_lig}")
    public ResponseEntity<Object> deleteLigAppArriver(
            @PathVariable String cod_soc,
            @PathVariable String mat_pers,
            @PathVariable Integer num_fam,
            @PathVariable String dat_soin,
            @PathVariable Integer num_lig) {

        LigAppArriverCle id = new LigAppArriverCle(
                cod_soc, mat_pers, num_fam, LocalDate.parse(dat_soin), num_lig);

        return ligAppArriverRepository.findById(id)
                .map(entity -> {
                    ligAppArriverRepository.delete(entity);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // Lig med arriver crud

    @PostMapping("/LigMedArriver")
    public ResponseEntity<LigMedArriver> createLigMedArriver(@RequestBody LigMedArriver ligMedArriver) {
        LigMedArriver savedLigMedArriver = ligMedArriverRepository.save(ligMedArriver);
        return new ResponseEntity<>(savedLigMedArriver, HttpStatus.CREATED);
    }

    @PostMapping("/LigMedArriver/batch")
    public ResponseEntity<List<LigMedArriver>> createLigMedArriverBatch(
            @RequestBody List<LigMedArriver> ligMedArrivers) {
        List<LigMedArriver> savedLigMedArrivers = ligMedArriverRepository.saveAll(ligMedArrivers);
        return ResponseEntity.ok(savedLigMedArrivers);
    }

    @GetMapping(("/LigMedArriver"))
    public List<LigMedArriver> getAllLigMedArrivers() {
        return ligMedArriverRepository.findAll();
    }

    @GetMapping("/LigMedArriverById")
    public List<LigMedArriverProjection> getLigMedArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        return ligMedArriverRepository.findLigMedArriverById(cod_soc, mat_pers, num_fam, DateParser.parse(dat_soin));
    }

    @DeleteMapping("/LigMedArriver/{cod_soc}/{mat_pers}/{num_fam}/{dat_soin}")
    public ResponseEntity<Void> deleteLigMedArriver(
            @PathVariable String cod_soc,
            @PathVariable String mat_pers,
            @PathVariable Integer num_fam,
            @PathVariable LocalDate dat_soin, @PathVariable Integer index) {

        Optional<LigMedArriver> ligMedArriver = ligMedArriverRepository
                .findById(new LigMedArriverCle(cod_soc, mat_pers, num_fam, dat_soin, index));

        if (ligMedArriver.isPresent()) {
            ligMedArriverRepository.delete(ligMedArriver.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // visite crud

    @GetMapping("/LigVisitArriverById")
    public List<LigVisitArriverProjection> getLigVisitArriverById(@RequestParam String cod_soc,
            @RequestParam String mat_pers,
            @RequestParam Integer num_fam,
            @RequestParam String dat_soin) {
        LocalDate date_soin_sql = DateParser.parse(dat_soin);
        return ligVisitArriverRepository.findLigVisitArriverById(cod_soc, mat_pers, num_fam, date_soin_sql);
    }

    @PostMapping("/LigVisitArriver/batch")
    public ResponseEntity<List<LigVisitArriver>> createLigVisitArriverBatch(
            @RequestBody List<LigVisitArriver> ligVisitArrivers) {
        List<LigVisitArriver> savedLigVisitArrivers = ligVisitArriverRepository.saveAll(ligVisitArrivers);
        return ResponseEntity.ok(savedLigVisitArrivers);
    }

    private final LigService ligService;
    private final ObjectMapper objectMapper;

    @PostMapping("/SaveBordereauComplet55")
    public ResponseEntity<String> saveBordereauComplet2(
            @RequestBody Map<String, Object> data) {

        try {
            BordArriver bord = null;
            List<BultArriver> bulletins = null;
            List<LigActArriver> actes = null;
            List<LigVisitArriver> visites = null;
            List<LigAppArriver> appareils = null;
            List<LigMedArriver> medicaments = null;

            if (data.containsKey(KEY_BORDEREAU)) {
                bord = objectMapper.convertValue(
                        data.get(KEY_BORDEREAU), BordArriver.class);
            }

            if (data.containsKey(KEY_BULLETINS)) {
                bulletins = objectMapper.convertValue(
                        data.get(KEY_BULLETINS),
                        new TypeReference<List<BultArriver>>() {
                        });
            }

            if (data.containsKey(KEY_ACTES)) {
                actes = objectMapper.convertValue(
                        data.get(KEY_ACTES),
                        new TypeReference<List<LigActArriver>>() {
                        });
            }

            if (data.containsKey(KEY_VISITES)) {
                visites = objectMapper.convertValue(
                        data.get(KEY_VISITES),
                        new TypeReference<List<LigVisitArriver>>() {
                        });
            }

            if (data.containsKey(KEY_APPAREIL)) {
                appareils = objectMapper.convertValue(
                        data.get(KEY_APPAREIL),
                        new TypeReference<List<LigAppArriver>>() {
                        });
            }

            if (data.containsKey(KEY_MEDICAMENTS)) {
                medicaments = objectMapper.convertValue(
                        data.get(KEY_MEDICAMENTS),
                        new TypeReference<List<LigMedArriver>>() {
                        });
            }

            ligService.saveComplet(
                    bord,
                    bulletins,
                    actes,
                    visites,
                    appareils,
                    medicaments);

            return ResponseEntity.ok("Bordereau complet enregistré avec succès");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    // Comprehensive batch save endpoint for entire bordereau
    @PostMapping("/SaveBordereauComplet")
    public ResponseEntity<String> saveBordereauComplet(
            @RequestBody Map<String, Object> data) {

        try {
            // Convert JSON to objects
            BordArriver bord = null;
            List<BultArriver> bulletins = null;
            List<LigActArriver> actes = null;
            List<LigVisitArriver> visites = null;
            List<LigAppArriver> appareils = null;
            List<LigMedArriver> medicaments = null;

            if (data.containsKey(KEY_BORDEREAU)) {
                bord = objectMapper.convertValue(data.get(KEY_BORDEREAU), BordArriver.class);
            }

            if (data.containsKey(KEY_BULLETINS)) {
                bulletins = objectMapper.convertValue(
                        data.get(KEY_BULLETINS),
                        new TypeReference<List<BultArriver>>() {
                        });
            }

            if (data.containsKey(KEY_ACTES)) {
                actes = objectMapper.convertValue(
                        data.get(KEY_ACTES),
                        new TypeReference<List<LigActArriver>>() {
                        });
            }

            if (data.containsKey(KEY_VISITES)) {
                visites = objectMapper.convertValue(
                        data.get(KEY_VISITES),
                        new TypeReference<List<LigVisitArriver>>() {
                        });
            }

            if (data.containsKey(KEY_APPAREIL)) {
                appareils = objectMapper.convertValue(
                        data.get(KEY_APPAREIL),
                        new TypeReference<List<LigAppArriver>>() {
                        });
            }

            if (data.containsKey(KEY_MEDICAMENTS)) {
                medicaments = objectMapper.convertValue(
                        data.get(KEY_MEDICAMENTS),
                        new TypeReference<List<LigMedArriver>>() {
                        });
            }

            // Save or update all using LigService
            ligService.saveComplet(
                    bord,
                    bulletins,
                    actes,
                    visites,
                    appareils,
                    medicaments);

            return ResponseEntity.ok("Bordereau complet enregistré avec succès");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement : " + e.getMessage());
        }
    }

    @GetMapping("/ControleBordCnam")
    public List<ControleBordCnamProjection> getControleBordCnam(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.getControleBordCnam(cod_soc, cod_bord);
    }

    @GetMapping("/EnteteSaisieBordArriverManuCnam")
    public List<BordArriverProjection> getEnteteSaisieBordArriverManuCnam(@RequestParam String cod_soc) {
        return bordArriverRepository.enteteSaisieBordArriverManuCnam(cod_soc);
    }

    @GetMapping("/EnteteSaisieLibreCnam")
    public List<BordArriverProjection> enteteSaisieLibreCnam(@RequestParam String cod_soc) {
        return bordArriverRepository.enteteSaisieLibreCnam(cod_soc);
    }

    @GetMapping("/EnteteCalCompteLibreCnam")
    public List<BordArriverProjection> enteteCalCompteLibreCnam(@RequestParam String cod_soc) {
        return bordArriverRepository.enteteCalCompteLibreCnam(cod_soc);
    }

    @GetMapping("/EnteteSaisieBordArriverManuLibreCnam")
    public List<BordArriverProjection> getEnteteSaisieBordArriverManuLibreCnam(@RequestParam String cod_soc) {
        return bordArriverRepository.enteteSaisieBordArriverManuLibreCnam(cod_soc);
    }

    @PostMapping("/EnteteSaisieBordArriverManuCnam")
    public ResponseEntity<BordArriver> addBordArriver(@RequestBody BordArriver bordArriver) {

        return ResponseEntity.ok(bordArriverRepository.save(bordArriver));

    }

    @GetMapping("/regler_bord")
    public ReponseRegBord reglerBord(@RequestParam String cod_soc, @RequestParam String cod_bord) {
        return remboursementService.reglerBord(cod_soc, cod_bord);
    }

    @GetMapping("/cloture_bord")
    public ReponseCloture clotureBord(@RequestParam String cod_soc, @RequestParam String cod_bord,
            @RequestParam String cod_assur) {
        return remboursementService.clotureBord(cod_soc, cod_assur, cod_bord);
    }

    @GetMapping("/cloture_bord_vir")
    public ReponseCloture clotureBordVir(@RequestParam String cod_soc, @RequestParam String cod_bord,
            @RequestParam String cod_assur) {
        return remboursementService.clotureBordVir(cod_soc, cod_assur, cod_bord);
    }

    @GetMapping("/BultArriveLibrCnam")
    public ResponseEntity<List<BultArriverLibreCnamProjection>> getBultArriverByCodBordLibreCnam(
            @RequestParam String cod_bord) {
        List<BultArriverLibreCnamProjection> bultArrivers = bultArriverRepository
                .findBultArriverByCodBordLibreCnam(cod_bord);
        if (bultArrivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bultArrivers);
    }

}
