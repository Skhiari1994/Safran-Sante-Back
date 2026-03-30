package com.arabsoft.gestionconvention.Controllers;
import com.arabsoft.gestionconvention.Entities.*;
import com.arabsoft.gestionconvention.Entities.Cle.CleLigOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.Cle.CleOffConv;
import com.arabsoft.gestionconvention.Projections.*;
import com.arabsoft.gestionconvention.Repositories.*;
import com.arabsoft.gestionconvention.Services.ConventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import com.arabsoft.gestionconvention.DTO.OffDTO;
import com.arabsoft.gestionconvention.Entities.Cle.CleOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.DemandeConv;
import com.arabsoft.gestionconvention.Entities.OffDemandeConv;
import com.arabsoft.gestionconvention.Repositories.DemandeConvRepository;
import com.arabsoft.gestionconvention.Repositories.OffDemandeConvRepository;
import com.arabsoft.gestionconvention.Services.DemandeConvService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;
@RestController
@RequestMapping("/conv")
public class DemandeConvController {

    @Autowired
    DemandeConvRepository demandeConvRepository;
    @Autowired
    OffDemandeConvRepository offDemandeConvRepository;
    @Autowired
    MotifSuspConvRepository motifSuspConvRepository;
    @Autowired
    LigOffDemandeConvRepository ligOffDemandeConvRepository;
    @Autowired
    ConventionService conventionService;
    @Autowired
    DiskPretRepository diskPretRepository;
    @Autowired
    ConventionRepository conventionRepository;
    @Autowired
    OffConvRepository offConvRepository;
    @Autowired
    DemandeConvService demandeConvService;


    @GetMapping("/demConv/{soc}")
    public List<DemondeConvProjection> getDemandeConv(@PathVariable("soc") String soc) {
        return demandeConvRepository.getDemandeConv(soc);
    }

    @GetMapping("/listOffConv/{codConv}")
    public List<OffConvProjection> getLisOffConv(@PathVariable ("codConv") String codConv){
        return  offDemandeConvRepository.getLisOffConv(codConv);
    }
    @GetMapping("/listCodConv")
    public List<CodConvProjection> getListCodConv(){
        return  offDemandeConvRepository.getListCodConv();
    }

    @GetMapping("/listPers")
    public List<DemondeConvProjection> getListPersonnel(){
        return  demandeConvRepository.getPersonnel();
    }
    @GetMapping("/getNais/{codSoc}/{matPers}")
    public List<NaisProjection> getNais(@PathVariable ("codSoc") String codSoc,@PathVariable ("matPers") String matPers){
        return  demandeConvRepository.getNais(codSoc,matPers);
    }


    @PostMapping("/execProcOff")
    public ResponseEntity<Map<String, Object>> executeProcedure(@RequestBody OffDTO request) {

        Map<String, Object> result = demandeConvService.callProcessDemandeConvProc(
                request.getPCodSoc(),
                request.getPMatPers(),
                request.getPEtatOffDem(),
                request.getPDatDemConv(),
                request.getPDatOffDem()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/addOff")
    public ResponseEntity<OffDemandeConv> addOffDemande(@RequestBody OffDemandeConv offDemandeConv) {
        try {
            OffDemandeConv saved = offDemandeConvRepository.save(offDemandeConv);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/seq")
    public ResponseEntity<Integer> getNextSeq(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam String codConv,
            @RequestParam String codOff) {

        Integer nextSeq = demandeConvService.getNextSeqOffDemandeConv(codSoc, matPers, codConv, codOff);
        return ResponseEntity.ok(nextSeq);
    }

    @PostMapping("/addDemConv")
    public ResponseEntity<DemandeConv> addDemOffreConv(@RequestBody DemandeConv demOffreConv) {
        try {
            DemandeConv saved =demandeConvRepository.save(demOffreConv);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list-dem-off")
    public ResponseEntity<List<OffDemandeConv>> getListDemOff(
            @RequestParam String codConv,
            @RequestParam String codSoc,
            @RequestParam String matPers) {

        List<OffDemandeConv> result = offDemandeConvRepository.getListDemOff(codConv, codSoc, matPers);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/getListDemOffPers")
    public ResponseEntity<List<DemondeConvProjection>> getListDemOffPers(
            @RequestParam String codSoc,
            @RequestParam String matPers) {

        List<DemondeConvProjection> result = offDemandeConvRepository.getListDemOffPers( codSoc, matPers);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/getConvValInCodConv")
    public ResponseEntity<List<ConvProjection>> getConvValInCodConv(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam String codConv) {

        List<ConvProjection> result = demandeConvRepository.getConvValInCodConv( codSoc, matPers,codConv);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }


    @GetMapping("/getConvValSusp")
    public ResponseEntity<List<ConvProjection>> getConvValSusp(
            @RequestParam String codSoc) {

        List<ConvProjection> result = demandeConvRepository.getConvValSusp( codSoc);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
    @GetMapping("/getConvVal")
    public ResponseEntity<List<ConvProjection>> getConvVal(
            @RequestParam String codSoc,
            @RequestParam String matPers) {

        List<ConvProjection> result = demandeConvRepository.getConvVal(codSoc,matPers);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/getConvValIn")
    public ResponseEntity<List<ConvProjection>> getConvValIn(
            @RequestParam String codSoc,
            @RequestParam String matPers) {

        List<ConvProjection> result = demandeConvRepository.getConvValIn(codSoc,matPers);

        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }
    @PostMapping("/updateEtatDemConvBatch")
    @Transactional
    public ResponseEntity<OffDemandeConv> updateEtatOffDem(@RequestBody OffDemandeConv request) {
        try {
            // Log request for debugging
            System.out.println("Processing request: codSoc=" + request.getCod_soc() +
                    ", codConv=" + request.getCod_conv() +
                    ", matPers=" + request.getMat_pers() +
                    ", codOff=" + request.getCod_off() +
                    ", seq=" + request.getSeq() +
                    ", etatOffDem=" + request.getEtat_off_dem());

            CleOffDemandeConv id = new CleOffDemandeConv(
                    request.getCod_conv(),
                    request.getCod_soc(),

            request.getMat_pers(),
                    request.getCod_off(),
                    request.getSeq()
            );

            Optional<OffDemandeConv> optional = offDemandeConvRepository.findById(id);

            if (optional.isPresent()) {
                OffDemandeConv existingDemConv = optional.get();
                existingDemConv.setEtat_off_dem(request.getEtat_off_dem());
                OffDemandeConv updatedDemConv = offDemandeConvRepository.save(existingDemConv);
                return ResponseEntity.ok(updatedDemConv);
            } else {
                System.out.println("No OffDemandeConv found for ID: " + id);
                return ResponseEntity.ok(null); // Indicate no update was performed
            }
        } catch (Exception e) {
            System.err.println("Error updating OffDemandeConv: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/getOffConv")
    List<OffDemandeConvProjection> getOffConv(@RequestParam String conv, @RequestParam String mat) {
        return offDemandeConvRepository.getOffDemandeConv(conv, mat);
    }


    @GetMapping("/findAllMotif")
    List<MotifSuspConv> findAllMotif() {
        return motifSuspConvRepository.findAll();
    }
    @PostMapping("/addMotifSusp")
    MotifSuspConv addMotifSusp(@RequestBody MotifSuspConv motifSuspConv) {
        return this.motifSuspConvRepository.save(motifSuspConv);
    }
    @DeleteMapping("/deleteMotifSusp/{motif}")
    void deleteMotifSusp(@PathVariable String motif) {
        motifSuspConvRepository.deleteById(motif);
    }
    @PostMapping("/addOffDemConv")
    OffDemandeConv addOffDemConv(@RequestBody OffDemandeConv demandeConv) {
        return this.offDemandeConvRepository.save(demandeConv);
    }

    @GetMapping("/getLigOffConv")
    List<LigOffDemandeConvProjection> getOffConv(@RequestParam String soc, @RequestParam String mois, @RequestParam(required = false) String corps) {
        return ligOffDemandeConvRepository.getLigOffConv(soc, mois, corps);
    }

    @GetMapping("/updateLigOffConvModPay")
    void updateLigOffConvModPay(@RequestParam String conv, @RequestParam String off, @RequestParam String mat, @RequestParam String seq, @RequestParam String pay) {
        ligOffDemandeConvRepository.updateLigOffConvModPay(conv, off, mat, seq, pay);
    }


    @PutMapping("/updateLigOffConvModEtat")
    int getOffConv(@RequestParam String conv, @RequestParam String off, @RequestParam String mat, @RequestParam Long seq, @RequestParam LocalDate mois, @RequestParam String etat) {
        LigOffDemandeConv ligOffDemandeConv = ligOffDemandeConvRepository.getLigOffById(conv, off, mat, seq,mois);
        ligOffDemandeConv.setEtat_lig_off(etat);
        System.out.println("etata "+ligOffDemandeConv.getEtat_lig_off());
        return ligOffDemandeConvRepository.updateLigOffConvEtatVal(conv, off, mat, seq,mois,ligOffDemandeConv.getEtat_lig_off());
    }
    @PutMapping("/updateLigOffConv")
    public ResponseEntity<LigOffDemandeConv> updateLigOffConv(
            @RequestParam String conv,
            @RequestParam String soc,
            @RequestParam String off,
            @RequestParam String mat,
            @RequestParam Long seq,
            @RequestParam LocalDate mois,
            @RequestParam String etat) {

        CleLigOffDemandeConv id = new CleLigOffDemandeConv(conv, soc, mat, off, seq,mois);

        Optional<LigOffDemandeConv> optionalEntity = ligOffDemandeConvRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        LigOffDemandeConv entity = optionalEntity.get();
        entity.setEtat_lig_off(etat);
        LigOffDemandeConv updated = ligOffDemandeConvRepository.save(entity);

        return ResponseEntity.ok(updated);
    }


    @PutMapping("/updateLigOffConvVir")
    public ResponseEntity<LigOffDemandeConv> updateLigOffConvVir(
            @RequestParam String conv,
            @RequestParam String soc,
            @RequestParam String off,
            @RequestParam String mat,
            @RequestParam Long seq,
            @RequestParam LocalDate mois,
            @RequestParam String vir) {

        CleLigOffDemandeConv id = new CleLigOffDemandeConv(conv, soc, mat, off, seq,mois);

        Optional<LigOffDemandeConv> optionalEntity = ligOffDemandeConvRepository.findById(id);
        if (!optionalEntity.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        LigOffDemandeConv entity = optionalEntity.get();
        entity.setMod_p(vir);
        LigOffDemandeConv updated = ligOffDemandeConvRepository.save(entity);

        return ResponseEntity.ok(updated);
    }
    @GetMapping("/getDiskPret")
    List<DiskPret> getDiskPret(@RequestParam String corps, @RequestParam String mois) {
        return diskPretRepository.getDiskPret(corps, mois);
    }


    @GetMapping("/maj_convention")
    void maj_convention(@RequestParam String soc, @RequestParam String mois, @RequestParam(required = false) String corps, @RequestParam(required = false) String mat) {
        conventionService.maj_convention(soc, mois, corps, mat);
    }

    @GetMapping("/getConvention")
    List<Convention> getConvention() {
        return conventionRepository.findAll();
    }

    @GetMapping("/getOffConvention")
    List<OffConv> getConvention(@RequestParam String conv) {
        return offConvRepository.getOffConv(conv);
    }

    @PostMapping("/addConvention")
    Convention addConvention(@RequestBody Convention convention) {
        return conventionRepository.save(convention);
    }

    @PostMapping("/addOffConvention")
    List<OffConv> addOffConvention(@RequestBody List<OffConv> offConvs) {
        return offConvRepository.saveAll(offConvs);
    }


    @DeleteMapping("/deleteConv/{conv}")
    void DeleteConvention(@PathVariable String conv) {
        conventionRepository.deleteById(conv);
    }

    @DeleteMapping("/deleteOffConv")
    void DeleteConvention(@RequestBody CleOffConv offConv) {
        offConvRepository.deleteById(offConv);
    }


    @GetMapping("/dateRetr")
    public ResponseEntity<?> getDateRetraite(
            @RequestParam String codSoc,
            @RequestParam String matPers) {

        String dateRetraite =
                demandeConvRepository.getDateRetraite(codSoc, matPers);

        if (dateRetraite == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Date de retraite introuvable");
        }

        return ResponseEntity.ok(dateRetraite);
    }
}