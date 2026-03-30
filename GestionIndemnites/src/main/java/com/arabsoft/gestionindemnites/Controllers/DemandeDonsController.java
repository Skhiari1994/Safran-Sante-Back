package com.arabsoft.gestionindemnites.Controllers;


import com.arabsoft.gestionindemnites.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.gestionindemnites.DTO.ChekFamProcScolDTO;
import com.arabsoft.gestionindemnites.DTO.FamProcScolDTO;
import com.arabsoft.gestionindemnites.DTO.TypDonsDTO;
import com.arabsoft.gestionindemnites.DTO.UpdateDemandeDTO;
import com.arabsoft.gestionindemnites.Entities.Cle.CleDemandeDons;
import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.arabsoft.gestionindemnites.Entities.PieceDemDons;
import com.arabsoft.gestionindemnites.Projections.*;
import com.arabsoft.gestionindemnites.Repositories.DemandeDonsRepository;
import com.arabsoft.gestionindemnites.Services.DemandeDonsService;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ind")
public class DemandeDonsController {
    @Autowired
    private DemandeDonsRepository demandeDonsRepository;
    @Autowired
    private DemandeDonsService demandeDonsService;
    @GetMapping("/all")
    List<DemandeDons> getPiece() {
        return demandeDonsRepository.findAll();
    }

    @PostMapping("/execProcTypDon")
    public ResponseEntity<Map<String, Object>> executeProcedure(@RequestBody TypDonsDTO request) {
        Map<String, Object> result = demandeDonsService.callNatDemandeDonsProc(
                request.getNatDon(),
                request.getCodAffect(),
                request.getTypDon(),
                request.getCodSoc(),
                request.getMatPers(),
                request.getNumFam(),
                request.getDatDemDon()
        );
        return ResponseEntity.ok(result);
    }
    @PostMapping("/execProcFamScol")
    public ResponseEntity<Map<String, Object>> callFAM_DEMANDE_DONS(@RequestBody FamProcScolDTO request) {
        Map<String, Object> result = demandeDonsService.callFAM_DEMANDE_DONS(
                request.getCodSoc(),
                request.getMatPers(),
                request.getNumFam(),
                request.getNatDon(),
                request.getDatDemDon()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/chekExisteDem")
    public ResponseEntity<Map<String, Object>> callCheckDemandeDonProc(@RequestBody ChekFamProcScolDTO request) {
        Map<String, Object> result = demandeDonsService.callCheckDemandeDonProc(
                request.getNatDon(),
                request.getCodSoc(),

                request.getMatPers(),
                request.getNumFam(),

                request.getDatDemDon()




        );
        return ResponseEntity.ok(result);
    }
    @GetMapping("/PersIndAff/{soc}/{aff}")
    List<DemandeDonsPers> getPersIndAffect(@PathVariable String soc,@PathVariable String aff) {
        return demandeDonsRepository.getPersIndAffect(soc,aff);
    }
    @GetMapping("/PersInd/{soc}")
    List<DemandeDonsPers> getPersInd(@PathVariable String soc) {
        return demandeDonsRepository.getPersInd(soc);
    }
    @GetMapping("/getDemInd/{soc}/{matPers}")
    List<DemDonsProjection> getDemInd(@PathVariable String soc, @PathVariable String matPers) {
        return demandeDonsRepository.getPersIndDemande(soc,matPers);
    }
    @GetMapping("/getFamille/{codSoc}/{matPers}")
    List<FamilleProjection> getFamille(@PathVariable String codSoc, @PathVariable String matPers){
        return this.demandeDonsRepository.getFamille(codSoc,matPers);
    }
    @GetMapping("/getFamAdherent/{codSoc}/{matPers}")
    List<FamilleProjection> getFamAdherent(@PathVariable String codSoc, @PathVariable String matPers){
        return this.demandeDonsRepository.getFamAdherent(codSoc,matPers);
    }
    @GetMapping("/getFameEnfant/{codSoc}/{matPers}")
    List<FamilleProjection> getFameEnfant(@PathVariable String codSoc, @PathVariable String matPers){
        return this.demandeDonsRepository.getFameEnfant(codSoc,matPers);
    }
    @GetMapping("/getFamConjoint/{codSoc}/{matPers}")
    List<FamilleProjection> getFamConjoint(@PathVariable String codSoc, @PathVariable String matPers){
        return this.demandeDonsRepository.getFamConjoint(codSoc,matPers);
    }

    @PostMapping("/save")
    public ResponseEntity<DemandeDons> saveDemandeDon(@RequestBody DemandeDons demandeDon) {
        DemandeDons savedDon = demandeDonsService.createDemandeDon(demandeDon);
        return ResponseEntity.ok(savedDon);
    }
    @GetMapping("/numFam")
    public ResponseEntity<Integer> getConcerneValue(@RequestParam String concerne) {
        Integer result = demandeDonsService.getConcerneValue(concerne);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/getDemandeIndScol")
    List<DemandeDonProjection> getDemandeVal(@RequestParam String codSoc, @RequestParam(required = false) String codAffect, @RequestParam(required = false) String natDon){
        return this.demandeDonsRepository.getDemandeVal(codSoc,codAffect,natDon);
    }

    @GetMapping("/getDemandeVal")
    List<DemandeDonProjection> getDemandeIndScol(@RequestParam String codSoc, @RequestParam String codAffect, @RequestParam  String natDon){
        return this.demandeDonsRepository.getDemandeIndScol(codSoc,codAffect,natDon);
    }


    @PostMapping("/updateValid")
    public ResponseEntity<List<DemandeDons>> updateDemandeDon(
            @RequestBody List<UpdateDemandeDTO> request) {

        List<DemandeDons> updatedDemandes = new ArrayList<>();

        for (UpdateDemandeDTO row : request) {
            System.out.println(" dem dons " + row.getDemandeDon().getMnt_acc_don());

            CleDemandeDons id = new CleDemandeDons(
                    row.getTyp_don(),
                    row.getCod_soc(),
                    row.getMat_pers(),
                    row.getNum_fam(),
                    row.getDat_dem_don()
            );
            DemandeDons updatedDemande = demandeDonsService.updateDemandeDonvalid(id, row.getDemandeDon());
            updatedDemandes.add(updatedDemande);
        }
        return ResponseEntity.ok(updatedDemandes);
    }

    @GetMapping("/getDemandeDeblocage")
    public ResponseEntity<List<DemandeDonProjection>> getDemandeDeblocage(
            @RequestParam String codSoc,
            @RequestParam(required = false) String codAffect,
            @RequestParam(required = false) String natDon,
            @RequestParam String datDeb,
            @RequestParam String datFin,
            @RequestParam(required = false)  String modPay
            ) {

        // Handle 'undefined' or empty values correctly for optional parameters
        if ("undefined".equals(codAffect) || codAffect == null || codAffect.isEmpty()) {
            codAffect = null;
        }
        if ("undefined".equals(natDon) || natDon == null || natDon.isEmpty()) {
            natDon = null;
        }

        List<DemandeDonProjection> result = demandeDonsRepository.getDemandeDeblocage(codSoc, codAffect, natDon, datDeb, datFin,modPay);

        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }


    @PutMapping("/updateDeblocage")
    public ResponseEntity<List<DemandeDons>> updateDemandeDonDeblocage(
            @RequestBody List<UpdateDemandeDTO> request) {

        List<DemandeDons> updatedDemandes = new ArrayList<>();

        for (UpdateDemandeDTO row : request) {
            CleDemandeDons id = new CleDemandeDons(
                    row.getTyp_don(),
                    row.getCod_soc(),
                    row.getMat_pers(),
                    row.getNum_fam(),
                    row.getDat_dem_don()
            );

            DemandeDons updatedDemande = demandeDonsService.updateDemandeDonDeblocage(id, row.getDemandeDon());
            updatedDemandes.add(updatedDemande);
        }

        return ResponseEntity.ok(updatedDemandes);
    }

    @PostMapping("/saveDelocage")
    public ResponseEntity<List<DemandeDons>> saveDelocage(@RequestBody List<DemandeDons> demandeDonsList) {
        List<DemandeDons> updatedDons = demandeDonsService.updateDemandeDonsDeblocage(demandeDonsList);
        return ResponseEntity.ok(updatedDons);
    }

    @PostMapping("/deleteDelocage")
    public ResponseEntity<?> deleteDelocage(@RequestBody List<DemandeDons> demandeDonsList) {
        try {
            for (DemandeDons id : demandeDonsList) {
                CleDemandeDons cle = new CleDemandeDons(
                        id.getTyp_don(),
                        id.getCod_soc(),
                        id.getMat_pers(),
                        id.getNum_fam(),
                        id.getDat_dem_don()
                );

                if (demandeDonsRepository.existsById(cle)) {
                    demandeDonsRepository.deleteById(cle);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Demande non trouvée pour l’ID : " + cle);
                }
            }

            return ResponseEntity.ok("Toutes les demandes ont été supprimées avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }



    @GetMapping("/PersIndVirementScol")
    public ResponseEntity<List<DemandeDonProjection>> PersIndVirementScol(@RequestParam String soc,
    @RequestParam String datDebloc,
    @RequestParam(required = false) String wcodLieuGeog,
    @RequestParam(required = false) String wnatDon) {



        List<DemandeDonProjection> result = demandeDonsRepository.getPersIndVirementScol(soc, datDebloc, wcodLieuGeog, wnatDon);

        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }

    @GetMapping("/getPersIndVirementScolCloture")
    public ResponseEntity<List<DemandeDonProjection>> getPersIndVirementScolCloture(@RequestParam String soc,
                                                                          @RequestParam String datDebloc,
                                                                          @RequestParam(required = false) String wcodLieuGeog,
                                                                          @RequestParam(required = false) String wnatDon) {



        List<DemandeDonProjection> result = demandeDonsRepository.getPersIndVirementScolCloture(soc, datDebloc, wcodLieuGeog, wnatDon);

        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result);
    }


    @GetMapping("/PersIndValidationCp")
    List<DemandeDonProjection> PersIndValidationCp(@RequestParam String codSoc, @RequestParam String codAffect, @RequestParam  String natDon,
                                                   @RequestParam(required = false) String datDeb,
                                                   @RequestParam(required = false) String datFin){
        return this.demandeDonsRepository.PersIndValidationCp(codSoc,codAffect,natDon,datDeb,datFin);
    }
}
