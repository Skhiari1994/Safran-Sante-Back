package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultSoin;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseReglerBord;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.ReglementService;
import com.tn.arabsoft.remboursement_frais_medicaux.util.DateParser;

import lombok.RequiredArgsConstructor;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Reglement")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class ReglementController {

    private final ReglementService reglementService;
    private final BordEnvoiRepository bordEnvoiRepository;
    private final BultSoinRepository bultSoinRepository;
    private final BordArriverRepository bordArriverRepository;
    private final BultArriverRepository bultArriverRepository;
    private final LigBultArriverRepository ligBultArriverRepository;

    @GetMapping("/regler_bord")
    public ReponseReglerBord reglerBord(
            @RequestParam String soc,
            @RequestParam String codBord,
            @RequestParam String codAssur) {

        return reglementService.reglerBord(soc, codBord, codAssur);
    }

    @GetMapping("/BordEnvoi/{soc}")
    public List<BordEnvoiProjection> bordEnvoi(@PathVariable String soc) {
        return bordEnvoiRepository.bordEnvoi(soc);
    }

    @GetMapping("/PayBordEnvoi/{soc}")
    public List<BordEnvoiProjection> payBordEnvoi(@PathVariable String soc) {
        return bordEnvoiRepository.payBordEnvoi(soc);
    }

    @GetMapping("/ControleBordMutuelle")
    public List<ControleBordCnamProjection> controleBordMutuelle(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.getControleBordMutuelle(cod_soc, cod_bord);
    }

    @GetMapping("/getPayBordMutuelle")
    public List<ControleBordCnamProjection> getPayBordMutuelle(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.getPayBordMutuelle(cod_soc, cod_bord);
    }

    @GetMapping("/majModPayement")
    public List<ControleBordCnamProjection> majModPayement(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.majModePayement(cod_soc, cod_bord);
    }

    @GetMapping("/getBultSoinCpt")
    public List<ControleBordCnamProjection> getBultSoinCpt(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.getBultSoinCpt(cod_soc, cod_bord);
    }

    @GetMapping("/getBultSoinCheqCpt")
    public List<ControleBordCnamProjection> getBultSoinCheqCpt(@RequestParam String cod_soc,
            @RequestParam String cod_bord) {
        return bultSoinRepository.getBultSoinCheqCpt(cod_soc, cod_bord);
    }

    @PutMapping("/reglerAdh/{reg}")
    BultSoin reglerAdh(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam LocalDate dat, @PathVariable String reg) {
        BultSoin bultSoin = this.bultSoinRepository.getBultSoinById(soc, mat, numFam, dat);
        bultSoin.setReg_adh(reg);
        return bultSoinRepository.save(bultSoin);
    }

    @GetMapping("/getBordArriver")
    public List<BordArriverProjection> getBordArriver() {
        return bordArriverRepository.bordArriver();
    }

    @GetMapping("/ConsultBordArriver")
    public List<BordArriverProjection> consultBordArriver() {
        return bordArriverRepository.consultBordArriver();
    }

    @GetMapping("/findBultArriverReg")
    public ResponseEntity<List<BultArriverProjection>> findBultArriverReg(@RequestParam String cod_bord) {
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultArriverReg(cod_bord);
        if (bultArrivers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bultArrivers);
    }

    @GetMapping("/findBultArriverCptCnam")
    public List<BultArriverProjection> findBultArriverCptCnam(@RequestParam String cod_bord) {
        return bultArriverRepository.findBultArriverCptCnam(cod_bord);
    }

    @GetMapping("/getLigBultArriver")
    public ResponseEntity<List<LigBultArriverProjection>> getLigBultArriver(@RequestParam String soc,
            @RequestParam String mat, @RequestParam String numFam, @RequestParam String datSoin) {
        List<LigBultArriverProjection> bultArriverReg = ligBultArriverRepository.getLigBultArriver(soc, mat, numFam,
                datSoin);

        return ResponseEntity.ok(bultArriverReg);
    }

    @GetMapping("/updateBordArriver")
    public void updateBordArriver(@RequestParam String soc, @RequestParam String assur, @RequestParam String bord) {
        ligBultArriverRepository.updateBordArriver(soc, assur, bord);
    }

    @PutMapping("/addReclam")
    BultArriver updateReclamation(@RequestBody BultArriverCle bultArriverCle, @RequestParam String reclam) {

        BultArriver bultArriver = this.bultArriverRepository.findById(bultArriverCle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bulletin non trouvé"));
        bultArriver.setReclam(reclam);
        return this.bultArriverRepository.save(bultArriver);

    }

    @PutMapping("/MajModPay/{modPay}")
    BultSoin majModPay(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam LocalDate dat, @PathVariable String modPay) {

        BultSoin bultSoin = this.bultSoinRepository.getBultSoinById(soc, mat, numFam, dat);
        bultSoin.setMod_pay(modPay);
        return bultSoinRepository.save(bultSoin);

    }

    @GetMapping("/vir_bord")
    public ResponseProcedure virBord(@RequestParam String soc, @RequestParam String codBord,
            @RequestParam String nomFichier) {
        return reglementService.vir_bord(soc, codBord, nomFichier);

    }

    @GetMapping("/download-file")
    public ResponseEntity<FileSystemResource> downloadFile(
            @RequestParam(defaultValue = "C:/vir/virements.txt") String filePath, @RequestParam String fileName) {
        // Générer le fichier
        reglementService.generateVirementFile(filePath, fileName);

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

    @PutMapping("/MajModPayCnam/{modPay}")
    BultArriver majModPayCnam(@RequestParam String soc, @RequestParam String mat, @RequestParam Integer numFam,
            @RequestParam String dat, @PathVariable String modPay) {
        LocalDate dateSoin = DateParser.parse(dat);
        BultArriver bultArriver = this.bultArriverRepository.findBultArriverById(soc, mat, numFam, dateSoin);
        bultArriver.setMod_pay(modPay);
        return bultArriverRepository.save(bultArriver);
    }

    @PutMapping("/majRegAdh/{reg_adh}")
    BultSoin majRegAdh(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam,
            @RequestParam LocalDate dat, @PathVariable String reg_adh) {
        BultSoin bultSoin = this.bultSoinRepository.getBultSoinById(soc, mat, numFam, dat);
        bultSoin.setReg_adh(reg_adh);
        return bultSoinRepository.save(bultSoin);
    }

    @GetMapping("/majRegAdh")
    public void majRegAdh(@RequestParam String soc, @RequestParam String codBord) {
        reglementService.majRegAdh(soc, codBord);
    }

    @GetMapping("/majRegAdhVirCnam")
    public void majRegAdhVirCnam(@RequestParam String soc, @RequestParam String codBord) {
        reglementService.majRegAdhVirCnam(soc, codBord);
    }
}
