package com.arabsoft.referentiel.Controllers;


import com.arabsoft.referentiel.Entities.*;
import com.arabsoft.referentiel.Entities.Cle.AssurActivCle;
import com.arabsoft.referentiel.Entities.Cle.AssurFilCle;
import com.arabsoft.referentiel.Entities.Cle.BaremRembCle;
import com.arabsoft.referentiel.Projections.AssurActivProjection;
import com.arabsoft.referentiel.Projections.AssurFilProjection;
import com.arabsoft.referentiel.Projections.BaremeRembProjection;
import com.arabsoft.referentiel.Repositories.*;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/Parametrage")
public class ParametrageController {

    @Autowired
    private AssuranceRepository assuranceRepository;

    @Autowired
    private AssurActivRepository assurActivRepository;

    @Autowired
    private AssurFilRepository assurFilRepository;

    @Autowired
    private RefFiliereRepository refFiliereRepository;

    @Autowired
    private ActiviteFamilleRepository activiteFamilleRepository;

    @Autowired
    private TypesActesRepository typesActesRepository;

    @Autowired
    private ActeRepository acteRepository;

    @Autowired
    private BaremRembRepository repository;


    @Autowired
    private ActiviteEtablisRepository activiteEtablisRepository;
    @Autowired
    RefEtablisRepository refEtablisRepository;
    @PostMapping("/Assurance")
    public ResponseEntity<Assurance> createOrUpdateAssurance(@RequestBody Assurance assurance) {
        Assurance savedAssurance = assuranceRepository.save(assurance);
        return ResponseEntity.ok(savedAssurance);
    }


    @GetMapping("/Assurance/{cod_assur}")
    public ResponseEntity<Assurance> getAssuranceById(@PathVariable String cod_assur) {
        Optional<Assurance> assurance = assuranceRepository.findById(cod_assur);
        return assurance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/Assurance")
    public ResponseEntity<List<Assurance>> getAllAssurances() {
        List<Assurance> assurances = assuranceRepository.findAll();
        return ResponseEntity.ok(assurances);
    }


    @DeleteMapping("/Assurance/{cod_assur}")
    public ResponseEntity<Void> deleteAssurance(@PathVariable String cod_assur) {
        assuranceRepository.deleteById(cod_assur);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/AssureActive/{codActivite}/{codAssur}")
    public ResponseEntity<AssurActiv> getAssurActiv(@PathVariable String codActivite, @PathVariable String codAssur) {
        AssurActivCle cle = new AssurActivCle();
        cle.setCod_activite(codActivite);
        cle.setCod_assur(codAssur);

        Optional<AssurActiv> assurActiv = assurActivRepository.findById(cle);

        return assurActiv.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/AssureActive")
    public ResponseEntity<Iterable<AssurActiv>> getAllAssurActiv() {
        Iterable<AssurActiv> allAssurActiv = assurActivRepository.findAll();
        return ResponseEntity.ok(allAssurActiv);
    }

    @PostMapping("/AssureActive")
    public ResponseEntity<AssurActiv> createAssurActiv(@RequestBody AssurActiv assurActiv) {
        AssurActiv savedAssurActiv = assurActivRepository.save(assurActiv);
        return new ResponseEntity<>(savedAssurActiv, HttpStatus.CREATED);
    }


    @PutMapping("/AssureActive/{codActivite}/{codAssur}")
    public ResponseEntity<AssurActiv> updateAssurActiv(@PathVariable String codActivite, @PathVariable String codAssur,
                                                       @RequestBody AssurActiv updatedAssurActiv) {
        AssurActivCle cle = new AssurActivCle();
        cle.setCod_activite(codActivite);
        cle.setCod_assur(codAssur);

        Optional<AssurActiv> existingAssurActiv = assurActivRepository.findById(cle);

        if (existingAssurActiv.isPresent()) {
            updatedAssurActiv.setCod_activite(codActivite);
            updatedAssurActiv.setCod_assur(codAssur);
            AssurActiv savedAssurActiv = assurActivRepository.save(updatedAssurActiv);
            return ResponseEntity.ok(savedAssurActiv);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/AssureActive/{codActivite}/{codAssur}")
    public ResponseEntity<Void> deleteAssurActiv(@PathVariable String codActivite, @PathVariable String codAssur) {
        AssurActivCle cle = new AssurActivCle();
        cle.setCod_activite(codActivite);
        cle.setCod_assur(codAssur);

        Optional<AssurActiv> existingAssurActiv = assurActivRepository.findById(cle);

        if (existingAssurActiv.isPresent()) {
            assurActivRepository.deleteById(cle);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



    //AssurFil
// Create
    @PostMapping("/AssurFil")
    public ResponseEntity<AssurFil> createAssurFil(@RequestBody AssurFil filiere) {
        AssurFil savedAssurFil = assurFilRepository.save(filiere);
        return new ResponseEntity<>(savedAssurFil, HttpStatus.CREATED);
    }


    @GetMapping("/AssurFilMutuelle/{cod_assur}")
    public List<AssurFilProjection> getAssurFilDetails(@PathVariable String cod_assur) {
        return assurFilRepository.GetListAssurFil(cod_assur);
    }

    @GetMapping("/AssurActivMutuelle/{cod_assur}")
    public List<AssurActivProjection> getAssurActivDetails(@PathVariable String cod_assur) {
        return assurActivRepository.GetListAssurActiv(cod_assur);
    }
    // Read
    @GetMapping("/AssurFil/{codAssur}/{codFil}")
    public ResponseEntity<AssurFil> getAssurFil(@PathVariable String codAssur, @PathVariable String codFil) {
        AssurFilCle cle = new AssurFilCle();
        cle.setCod_assur(codAssur);
        cle.setCod_fil(codFil);

        Optional<AssurFil> assurFil = assurFilRepository.findById(cle);

        return assurFil.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update
    @PutMapping("/AssurFil/{codAssur}/{codFil}")
    public ResponseEntity<AssurFil> updateAssurFil(@PathVariable String codAssur, @PathVariable String codFil,
                                                   @RequestBody AssurFil updatedAssurFil) {
        AssurFilCle cle = new AssurFilCle();
        cle.setCod_assur(codAssur);
        cle.setCod_fil(codFil);

        Optional<AssurFil> existingAssurFil = assurFilRepository.findById(cle);

        if (existingAssurFil.isPresent()) {
            updatedAssurFil.setCod_assur(codAssur);
            updatedAssurFil.setCod_fil(codFil);
            AssurFil savedAssurFil = assurFilRepository.save(updatedAssurFil);
            return ResponseEntity.ok(savedAssurFil);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete
    @DeleteMapping("/AssurFil/{codAssur}/{codFil}")
    public ResponseEntity<Void> deleteAssurFil(@PathVariable String codAssur, @PathVariable String codFil) {
        AssurFilCle cle = new AssurFilCle();
        cle.setCod_assur(codAssur);
        cle.setCod_fil(codFil);

        Optional<AssurFil> existingAssurFil = assurFilRepository.findById(cle);

        if (existingAssurFil.isPresent()) {
            assurFilRepository.deleteById(cle);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/Filiere")
    public ResponseEntity<List<RefFiliere>> getAllRefFiliere() {
        List<RefFiliere> refFilieres = refFiliereRepository.findAll();
        return new ResponseEntity<>(refFilieres, HttpStatus.OK);
    }

    // Récupérer un enregistrement par son code
    @GetMapping("/Filiere/{cod_fil}")
    public ResponseEntity<RefFiliere> getRefFiliereById(@PathVariable String cod_fil) {
        Optional<RefFiliere> refFiliere = refFiliereRepository.findById(cod_fil);
        return refFiliere.map(filiere -> new ResponseEntity<>(filiere, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Créer ou mettre à jour un enregistrement
    @PostMapping("/Filiere")
    public ResponseEntity<RefFiliere> createOrUpdateRefFiliere(@RequestBody RefFiliere refFiliere) {
        RefFiliere savedRefFiliere = refFiliereRepository.save(refFiliere);
        return new ResponseEntity<>(savedRefFiliere, HttpStatus.CREATED);
    }

    // Supprimer un enregistrement
    @DeleteMapping("/Filiere/{cod_fil}")
    public ResponseEntity<Void> deleteRefFiliere(@PathVariable String cod_fil) {
        if (refFiliereRepository.existsById(cod_fil)) {
            refFiliereRepository.deleteById(cod_fil);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ActiviteFamille")
    public ResponseEntity<List<ActiviteFamille>> getAllActiviteFamille() {
        List<ActiviteFamille> ActiviteFamille = activiteFamilleRepository.findAll();
        return ResponseEntity.ok(ActiviteFamille);
    }

    @PostMapping("/TypesActes")
    public ResponseEntity<TypesActes> createOrUpdateTypesActes(@RequestBody TypesActes typesActes) {
        TypesActes savedTypesActes = typesActesRepository.save(typesActes);
        return ResponseEntity.ok(savedTypesActes);
    }


    @GetMapping("/TypesActes/{type_acte}")
    public ResponseEntity<TypesActes> getTypesActesById(@PathVariable String type_acte) {
        Optional<TypesActes> typesActes = typesActesRepository.findById(type_acte);
        return typesActes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/TypesActes")
    public ResponseEntity<List<TypesActes>> getAllTypesActes() {
        List<TypesActes> typesActes = typesActesRepository.findAll();
        return ResponseEntity.ok(typesActes);
    }


    @DeleteMapping("/TypesActes/{type_acte}")
    public ResponseEntity<Void> deleteTypesActes(@PathVariable String type_acte) {
        typesActesRepository.deleteById(type_acte);
        return ResponseEntity.noContent().build();
    }

    // Controller Acte


    @PostMapping("/Acte")
    public ResponseEntity<Acte> createOrUpdateActe(@RequestBody Acte acte) {
        Acte savedActe = acteRepository.save(acte);
        return ResponseEntity.ok(savedActe);
    }


    @GetMapping("/Acte/{abrv_act}")
    public ResponseEntity<Acte> getActesById(@PathVariable String abrv_act) {
        Optional<Acte> acte = acteRepository.findById(abrv_act);
        return acte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/Acte")
    public ResponseEntity<List<Acte>> getAllActes() {
        List<Acte> acte = acteRepository.findAll();
        return ResponseEntity.ok(acte);
    }


    @DeleteMapping("/Acte/{abrv_act}")
    public ResponseEntity<Void> deleteActes(@PathVariable String abrv_act) {
        acteRepository.deleteById(abrv_act);
        return ResponseEntity.noContent().build();
    }

    //BaremeRemb
    // Récupérer tous les enregistrements
    @GetMapping("/bareme-remb")
    public ResponseEntity<List<BaremeRemb>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/bareme-remb-projection")
    public ResponseEntity<List<BaremeRembProjection>> getAllBareme() {
        return ResponseEntity.ok(repository.getAllBareme());
    }



    // Récupérer un enregistrement par clé composite
    @GetMapping("/bareme-remb/{cod_fil}/{abrv_act}/{cod_assur}")
    public ResponseEntity<BaremeRemb> getById(
            @PathVariable String cod_fil,
            @PathVariable String abrv_act,
            @PathVariable String cod_assur) {

        BaremRembCle id = new BaremRembCle(cod_fil, abrv_act, cod_assur);
        Optional<BaremeRemb> bareme = repository.findById(id);

        return bareme.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Ajouter un nouvel enregistrement
    @PostMapping("/bareme-remb")
    public ResponseEntity<BaremeRemb> create(@RequestBody BaremeRemb bareme) {
        return ResponseEntity.ok(repository.save(bareme));
    }

    // Mettre à jour un enregistrement
    @PutMapping("/bareme-remb/{cod_fil}/{abrv_act}/{cod_assur}")
    public ResponseEntity<BaremeRemb> update(
            @PathVariable String cod_fil,
            @PathVariable String abrv_act,
            @PathVariable String cod_assur,
            @RequestBody BaremeRemb updatedBareme) {

        BaremRembCle id = new BaremRembCle(cod_fil, abrv_act, cod_assur);
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        updatedBareme.setCod_fil(cod_fil);
        updatedBareme.setAbrv_act(abrv_act);
        updatedBareme.setCod_assur(cod_assur);

        return ResponseEntity.ok(repository.save(updatedBareme));
    }

    // Supprimer un enregistrement
    @DeleteMapping("/bareme-remb/{cod_fil}/{abrv_act}/{cod_assur}")
    public ResponseEntity<Void> delete(
            @PathVariable String cod_fil,
            @PathVariable String abrv_act,
            @PathVariable String cod_assur) {

        BaremRembCle id = new BaremRembCle(cod_fil, abrv_act, cod_assur);
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //activité établiss controllerrr

    @PostMapping("/ActiviteEtabliss")
    public ResponseEntity<ActiviteEtablis> createOrUpdateActiviteEtabliss(@RequestBody ActiviteEtablis activiteEtablis) {
        ActiviteEtablis savedActiviteEtablis = activiteEtablisRepository.save(activiteEtablis);
        return ResponseEntity.ok(savedActiviteEtablis);
    }


    @GetMapping("/ActiviteEtabliss/{cod_activ}")
    public ResponseEntity<ActiviteEtablis> getActiviteEtablisById(@PathVariable String cod_activ) {
        Optional<ActiviteEtablis> activiteEtablis = activiteEtablisRepository.findById(cod_activ);
        return activiteEtablis.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/ActiviteEtabliss")
    public ResponseEntity<List<ActiviteEtablis>> getAllActiviteEtablis() {
        List<ActiviteEtablis> ActiviteEtabliss = activiteEtablisRepository.findAll();
        return ResponseEntity.ok(ActiviteEtabliss);
    }


    @DeleteMapping("/ActiviteEtabliss/{cod_activ}")
    public ResponseEntity<Void> deleteActiviteEtablis(@PathVariable String cod_activ) {
        activiteEtablisRepository.deleteById(cod_activ);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getPersPhysiqueParamMutuelle")
    public List<RefEtablis> getPersPhysique(){
        return refEtablisRepository.getPersPhysiqueParamMutuelle();
    }

    @GetMapping("/getEtablissParamMutuelle")
    public List<RefEtablis> getEtabliss(){
        return refEtablisRepository.getEtablissParamMutuelle();
    }
}
