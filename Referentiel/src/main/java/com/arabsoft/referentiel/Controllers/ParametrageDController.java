package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.*;
import com.arabsoft.referentiel.entities.cle.CleRefFillAct;
import com.arabsoft.referentiel.projections.RefFillActProjection;
import com.arabsoft.referentiel.repositories.*;
import com.arabsoft.referentiel.services.RefirentielleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParametrageD")
@SuppressWarnings({ "java:S100", "java:S117" })
public class ParametrageDController {

    private final RefVisitRepository refVisitRepository;

    private final ActeRepository acteRepository;

    private final RefMedRepository refMedRepository;

    private final RefActRepository refActRepository;

    private final RefAppareilRepository refAppareilRepository;

    private final MaladieRepository maladieRepository;

    private final ActiviteEtablisRepository activiteEtablisRepository;

    private final RefEtablisRepository refEtablisRepository;

    private final GouvernoratRepository gouvernoratRepository;

    private final PosteRepository posteRepository;

    private final RegimeRembRepository regimeRembRepository;

    private final RefFiliereRepository refFiliereRepository;

    private final RefFillActRepository refFillActRepository;

    private final RefirentielleService refirentielleService;

    @GetMapping("/getAllRef")
    List<RefVisit> getAllRef() {
        return refVisitRepository.findAll();
    }

    @PostMapping("/addRef")
    RefVisit addRef(@RequestBody RefVisit refVisit) {
        return refVisitRepository.save(refVisit);
    }

    @DeleteMapping("/deteRef/{codVisit}")
    public void deletePiece(@PathVariable String codVisit) {
        refVisitRepository.deleteById(codVisit);
    }

    @GetMapping("/getAllActe")
    List<Acte> getAllActe() {
        return acteRepository.findAll();
    }

    @GetMapping("/getRefMed")
    List<RefMed> getRefMed() {
        return refMedRepository.findAll();
    }

    @PostMapping("/addRefMed")
    RefMed addRefMed(@RequestBody RefMed refMed) {
        return refMedRepository.save(refMed);
    }

    @DeleteMapping("/deteRefMed/{cod_med}")
    public void deleteMed(@PathVariable String cod_med) {
        refMedRepository.deleteById(cod_med);
    }

    @GetMapping("/getRefAct")
    List<RefAct> getRefAct() {
        return refActRepository.findAll();
    }

    @PostMapping("/addRefAct")
    RefAct addRefAct(@RequestBody RefAct refAct) {
        return refActRepository.save(refAct);
    }

    @DeleteMapping("/deteRefaAct/{cod_act}")
    public void deleteRefAct(@PathVariable String cod_act) {
        refActRepository.deleteById(cod_act);
    }

    @GetMapping("/getRefAppareil")
    List<RefAppareil> getRefAppareil() {
        return refAppareilRepository.findAll();
    }

    @PostMapping("/addRefAppareil")
    RefAppareil addRefAct(@RequestBody RefAppareil refAppareil) {
        return refAppareilRepository.save(refAppareil);
    }

    @DeleteMapping("/deteRefAppareil/{cod_app}")
    public void deleteRefAppareil(@PathVariable String cod_app) {
        refAppareilRepository.deleteById(cod_app);
    }

    @GetMapping("/getMaladie")
    List<Maladie> getMaladie() {
        return maladieRepository.findAll();
    }

    @PostMapping("/addMaladie")
    Maladie addMaladie(@RequestBody Maladie maladie) {
        return maladieRepository.save(maladie);
    }

    @DeleteMapping("/deleteMaladie/{cod_malad}")
    public void deleteMaladie(@PathVariable String cod_malad) {
        maladieRepository.deleteById(cod_malad);
    }

    @GetMapping("/getRefEtablis")
    public List<RefEtablis> getRefEtablis() {
        return refEtablisRepository.getRefEtablis();
    }

    @GetMapping("/getPersPhysique")
    public List<RefEtablis> getPersPhysique() {
        return refEtablisRepository.getPersPhysique();
    }

    @PostMapping("/addRefEtablis")
    RefEtablis addRefEtablis(@RequestBody RefEtablis refEtablis) {
        return refEtablisRepository.save(refEtablis);
    }

    @GetMapping("/getActiviteEtablis/{cod_activ}")
    public ResponseEntity<ActiviteEtablis> getActiviteEtablis(@PathVariable String cod_activ) {
        return activiteEtablisRepository.findById(cod_activ)
                .map(ResponseEntity::ok) // Retourne 200 OK avec l'entité
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Activité non trouvée"));
    }

    @GetMapping("/getAllActiviteEtablis")
    public List<ActiviteEtablis> getAllActiviteEtablis() {
        return activiteEtablisRepository.findAll();
    }

    @GetMapping("/getGouv/{cod_gouv}")
    public ResponseEntity<Gouvernorat> getGouv(@PathVariable String cod_gouv) {
        return gouvernoratRepository.findById(cod_gouv)
                .map(ResponseEntity::ok) // Retourne 200 OK avec l'entité
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gouv non trouvée"));
    }

    @GetMapping("/getAllGouv")
    public List<Gouvernorat> getAllGouv() {

        return gouvernoratRepository.findAll();
    }

    @GetMapping("/getAllPoste")
    public List<Poste> getAllPoste() {

        return posteRepository.findAll();
    }

    @GetMapping("/getPoste/{cod_gouv}/{cod_post}")
    public Poste getPoste(@PathVariable String cod_gouv, @PathVariable String cod_post) {
        return posteRepository.getPosteById(cod_gouv, cod_post);
    }

    @GetMapping("/getPoste/{cod_gouv}")
    public ResponseEntity<List<Poste>> getPosteByGouv(@PathVariable String cod_gouv) {
        List<Poste> postes = posteRepository.getPoste(cod_gouv);

        if (postes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(postes);
    }

    @GetMapping("/getRegimeRemb")
    List<RegimeRemb> getRegimeRemb() {
        return regimeRembRepository.findAll();
    }

    @PostMapping("/addRegimeRemb")
    RegimeRemb addRegimeRemb(@RequestBody RegimeRemb regimeRemb) {
        return regimeRembRepository.save(regimeRemb);
    }

    @DeleteMapping("/deleteRegimeRemb/{reg_remb}")
    public void deleteRegimeRemb(@PathVariable String reg_remb) {
        regimeRembRepository.deleteById(reg_remb);
    }

    @GetMapping("/getRefFiliere")
    List<RefFiliere> getRefFiliere() {
        return refFiliereRepository.findAll();
    }

    @PostMapping("/addRefFiliere")
    RefFiliere addRefFiliere(@RequestBody RefFiliere refFiliere) {
        return refFiliereRepository.save(refFiliere);
    }

    @DeleteMapping("/deleteRefFiliere/{cod_fil}")
    public void deleteRefFiliere(@PathVariable String cod_fil) {
        refFiliereRepository.deleteById(cod_fil);
    }

    @GetMapping("/getRefFillAct/{codFil}")
    List<RefFillActProjection> getRefFillAct(@PathVariable String codFil) {
        return refFillActRepository.getFillAct(codFil);
    }

    @PostMapping("/addRefFillAct")
    RefFillAct addRefFillAct(@RequestBody RefFillAct refFillAct) {
        return refFillActRepository.save(refFillAct);
    }

    @PostMapping("/deleteRefFillAct")
    public void deleteRefFillAct(@RequestBody CleRefFillAct cod_fil) {
        refFillActRepository.deleteById(cod_fil);
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String content = new String(file.getBytes(), StandardCharsets.UTF_8);
            refirentielleService.callStoredProcedure(content);
            return ResponseEntity.ok("Upload successful and stored procedure called.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
