package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultSoin;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BultArriverCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseCalculMntNet;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseReglerBord;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ResponseProcedure;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.ReglementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/Reglement")
public class ReglementController {

    @Autowired
    ReglementService reglementService;

    @Autowired
    BordEnvoiRepository bordEnvoiRepository;
    @Autowired
    BultSoinRepository bultSoinRepository;
    @Autowired
    BordArriverRepository bordArriverRepository;
    @Autowired
    BultArriverRepository bultArriverRepository;

    @Autowired
    LigBultArriverRepository ligBultArriverRepository;

    @GetMapping("/regler_bord")
    public ReponseReglerBord reglerBord(
            @RequestParam String soc,
            @RequestParam String codBord,
            @RequestParam String codAssur) {

        return reglementService.reglerBord(soc, codBord, codAssur);
    }


    @GetMapping("/BordEnvoi/{soc}")
    public List<BordEnvoiProjection> BordEnvoi(@PathVariable String soc) {
        List<BordEnvoiProjection> bordEnvoiProjection = bordEnvoiRepository.BordEnvoi(soc);
        return bordEnvoiProjection;
    }
    @GetMapping("/PayBordEnvoi/{soc}")
    public List<BordEnvoiProjection> PayBordEnvoi(@PathVariable String soc) {
        List<BordEnvoiProjection> bordEnvoiProjection = bordEnvoiRepository.PayBordEnvoi(soc);
        return bordEnvoiProjection;
    }
    @GetMapping("/ControleBordMutuelle")
    public List<ControleBordCnamProjection> ControleBordMutuelle(@RequestParam String cod_soc,
                                                                @RequestParam String cod_bord) {
        List<ControleBordCnamProjection> ControleBordCnam = bultSoinRepository.getControleBordMutuelle(cod_soc, cod_bord);
        return ControleBordCnam;
    }
    @GetMapping("/getPayBordMutuelle")
    public List<ControleBordCnamProjection> getPayBordMutuelle(@RequestParam String cod_soc,
                                                                 @RequestParam String cod_bord) {
        List<ControleBordCnamProjection> ControleBordCnam = bultSoinRepository.getPayBordMutuelle(cod_soc, cod_bord);
        return ControleBordCnam;
    }
    @GetMapping("/majModPayement")
    public List<ControleBordCnamProjection> majModPayement(@RequestParam String cod_soc,
                                                               @RequestParam String cod_bord) {
        List<ControleBordCnamProjection> ControleBordCnam = bultSoinRepository.MajModePayement(cod_soc, cod_bord);
        return ControleBordCnam;
    }

    @GetMapping("/getBultSoinCpt")
    public List<ControleBordCnamProjection> getBultSoinCpt(@RequestParam String cod_soc,
                                                           @RequestParam String cod_bord) {
        List<ControleBordCnamProjection> ControleBordCnam = bultSoinRepository.getBultSoinCpt(cod_soc, cod_bord);
        return ControleBordCnam;
    }

    @GetMapping("/getBultSoinCheqCpt")
    public List<ControleBordCnamProjection> getBultSoinCheqCpt(@RequestParam String cod_soc,
                                                           @RequestParam String cod_bord) {
        List<ControleBordCnamProjection> ControleBordCnam = bultSoinRepository.getBultSoinCheqCpt(cod_soc, cod_bord);
        return ControleBordCnam;
    }
    @PutMapping("/reglerAdh/{reg}")
    BultSoin reglerAdh(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam, @RequestParam LocalDate dat, @PathVariable String reg)
    {
        BultSoin bultSoin =this.bultSoinRepository.getBultSoinById(soc,mat,numFam,dat);

        bultSoin.setReg_adh(reg);
        return bultSoinRepository.save(bultSoin);
    }

    @GetMapping("/getBordArriver")
    public List<BordArriverProjection> getBordArriver() {

        return bordArriverRepository.BordArriver();
    }
    @GetMapping("/ConsultBordArriver")
    public List<BordArriverProjection> ConsultBordArriver() {

        return bordArriverRepository.ConsultBordArriver();
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
        List<BultArriverProjection> bultArrivers = bultArriverRepository.findBultArriverCptCnam(cod_bord);

        return bultArrivers;
    }
    @GetMapping("/getLigBultArriver")
    public ResponseEntity<List<LigBultArriverProjection>> getLigBultArriver(@RequestParam String soc,@RequestParam String mat,@RequestParam String numFam,@RequestParam String datSoin) {
        List<LigBultArriverProjection> bultArriverReg = ligBultArriverRepository.getLigBultArriver(soc,mat,numFam,datSoin);

        return ResponseEntity.ok(bultArriverReg);
    }
    @GetMapping("/updateBordArriver")
    public void updateBordArriver(@RequestParam String soc,@RequestParam String assur,@RequestParam String bord) {
      ligBultArriverRepository.updateBordArriver(soc,assur,bord);

    }
    @PutMapping("/addReclam")
    BultArriver updateReclamation(@RequestBody BultArriverCle bultArriverCle,@RequestParam String reclam){

        BultArriver bultArriver =this.bultArriverRepository.findById(bultArriverCle) .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bulletin non trouvé"));;

        bultArriver.setReclam(reclam);
        return this.bultArriverRepository.save(bultArriver);
    }


    @PutMapping("/MajModPay/{modPay}")
    BultSoin MajModPay(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam, @RequestParam LocalDate dat, @PathVariable String modPay)
    {
        BultSoin bultSoin =this.bultSoinRepository.getBultSoinById(soc,mat,numFam,dat);

        bultSoin.setMod_pay(modPay);
        return bultSoinRepository.save(bultSoin);
    }


    @GetMapping("/vir_bord")
    public ResponseProcedure vir_bord(@RequestParam String soc, @RequestParam String codBord, @RequestParam String nomFichier) {
        return reglementService.vir_bord(soc,codBord,nomFichier);

    }

    @GetMapping("/download-file")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam(defaultValue = "C:/vir/virements.txt") String filePath,@RequestParam String fileName) {
        // Générer le fichier
        reglementService.generateVirementFile(filePath,fileName);

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
    BultArriver MajModPayCnam(@RequestParam String soc, @RequestParam String mat, @RequestParam Integer numFam, @RequestParam String dat, @PathVariable String modPay)
    {
        BultArriver bultArriver =this.bultArriverRepository.findBultArriverById(soc,mat,numFam,dat);
        System.out.println(bultArriver);
        bultArriver.setMod_pay(modPay);
        return bultArriverRepository.save(bultArriver);
    }

    @PutMapping("/majRegAdh/{reg_adh}")
    BultSoin majRegAdh(@RequestParam String soc, @RequestParam String mat, @RequestParam String numFam, @RequestParam LocalDate dat, @PathVariable String reg_adh)
    {
        BultSoin bultSoin =this.bultSoinRepository.getBultSoinById(soc,mat,numFam,dat);
        System.out.println(bultSoin);
        bultSoin.setReg_adh(reg_adh);
        return bultSoinRepository.save(bultSoin);
    }

    @GetMapping("/majRegAdh")
    public void majRegAdh(@RequestParam String soc, @RequestParam String codBord) {
         reglementService.majRegAdh(soc,codBord);

    }

    @GetMapping("/majRegAdhVirCnam")
    public void majRegAdhVirCnam(@RequestParam String soc, @RequestParam String codBord) {
        reglementService.majRegAdhVirCnam(soc,codBord);

    }
}
