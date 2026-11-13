package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.*;
import com.arabsoft.referentiel.entities.cle.CleAgence;
import com.arabsoft.referentiel.entities.cle.ClePoste;
import com.arabsoft.referentiel.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Comparator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/paramAdherent")
public class ParamAdherents {

    private final NationaliteRepository nationaliteRepository;
    private final GouvernoratRepository gouvernoratRepository;
    private final PosteRepository posteRepository;
    private final BanqueRepository banqueRepository;
    private final AgenceRepository agenceRepository;
    private final PrmLieuGeogRepository prmLieuGeogRepository;
    private final SocieteRepository societeRepository;
    private final AffectationRepository affectationRepository;
    private final TypeDepartRepository typeDepartRepository;
    private final ActiviteFamilleRepository activiteFamilleRepository;
    private final JdbcTemplate jdbcTemplate;

    @GetMapping("getNationalite")
    List<Nationalite> getNationalites() {
        return nationaliteRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Nationalite::getCod_natp))
                .toList();
    }

    @PostMapping("/addNationalite")
    Nationalite savePersonnel(@RequestBody Nationalite personnel) {
        return nationaliteRepository.save(personnel);
    }

    @DeleteMapping("/{codNatp}")
    public void deleteNationalite(@PathVariable String codNatp) {
        nationaliteRepository.deleteById(codNatp);
    }

    @GetMapping("/getGouvernorat")
    List<Gouvernorat> getGouvernorat() {
        return gouvernoratRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Gouvernorat::getCod_gouv))
                .toList();
    }

    @GetMapping("/getPoste/{gouv}")
    List<Poste> getPoste(@PathVariable String gouv) {
        return posteRepository.getPoste(gouv);
    }

    @PostMapping("/addGouv")
    Gouvernorat addGouv(@RequestBody Gouvernorat gouvernorat) {
        return gouvernoratRepository.save(gouvernorat);
    }

    @PostMapping("/addPoste")
    public void addPoste(@RequestBody List<Poste> poste) {
        posteRepository.saveAll(poste);
    }

    @PostMapping("/deletePoste")
    public void deletePoste(@RequestBody ClePoste poste) {
        posteRepository.deleteById(poste);
    }

    @PostMapping("/deleteAgence")
    public void deleteAgence(@RequestBody CleAgence agence) {
        agenceRepository.deleteById(agence);
    }

    @DeleteMapping("/deleteBanque/{codBanq}")
    public ResponseEntity<String> deleteBanque(@PathVariable String codBanq) {
        if (!agenceRepository.getAgence(codBanq).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette banque contient des agences : supprimez-les d'abord.");
        }
        try {
            banqueRepository.deleteById(codBanq);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette banque est utilisée ailleurs et ne peut pas être supprimée.");
        }
    }

    @DeleteMapping("/deleteGouv/{codGouv}")
    public ResponseEntity<String> deleteGouv(@PathVariable String codGouv) {
        if (!posteRepository.getPoste(codGouv).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce gouvernorat contient des postes : supprimez-les d'abord.");
        }
        try {
            gouvernoratRepository.deleteById(codGouv);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce gouvernorat est utilisé ailleurs et ne peut pas être supprimé.");
        }
    }

    @GetMapping("/getBanque")
    List<Banque> getBanque() {
        return banqueRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Banque::getCod_banq))
                .toList();
    }

    @GetMapping("/getAgence/{codBanq}")
    List<Agence> getAgence(@PathVariable String codBanq) {
        return agenceRepository.getAgence(codBanq);
    }

    @PostMapping("/addBanq")
    Banque addBanque(@RequestBody Banque banque) {
        return banqueRepository.save(banque);
    }

    @PostMapping("/addAgence")
    public void addAgence(@RequestBody List<Agence> f) {
        agenceRepository.saveAll(f);
    }

    @PostMapping("/addSociete")
    public void addSociete(@RequestBody Societe f) {
        societeRepository.save(f);
    }

    @GetMapping("/getSociete")
    List<Societe> getSociete() {
        return societeRepository.findAll();
    }

    /**
     * Supprime une société uniquement si son code n'est utilisé nulle part ailleurs.
     * Le contrôle parcourt toutes les tables qui possèdent une colonne COD_SOC
     * (personnel, bulletins, bordereaux, utilisateurs...), sans dépendre d'une liste figée.
     */
    @DeleteMapping("/deleteSociete/{codSoc}")
    public ResponseEntity<String> deleteSociete(@PathVariable String codSoc) {
        if (!societeRepository.existsById(codSoc)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette société n'existe pas.");
        }
        List<String> tablesLiees = tablesUtilisantSociete(codSoc);
        if (!tablesLiees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Impossible de supprimer la société " + codSoc
                            + " : son code est utilisé par d'autres données ("
                            + String.join(", ", tablesLiees.stream().limit(5).toList())
                            + (tablesLiees.size() > 5 ? "…" : "") + ").");
        }
        try {
            societeRepository.deleteById(codSoc);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Cette société est utilisée ailleurs et ne peut pas être supprimée.");
        }
    }

    /** Tables (autres que SOCIETE) contenant au moins une ligne pour ce code société. */
    private List<String> tablesUtilisantSociete(String codSoc) {
        List<String[]> colonnes = jdbcTemplate.query(
                "select c.TABLE_SCHEMA, c.TABLE_NAME, c.COLUMN_NAME "
                        + "from INFORMATION_SCHEMA.COLUMNS c "
                        + "join INFORMATION_SCHEMA.TABLES t on t.TABLE_SCHEMA = c.TABLE_SCHEMA "
                        + "and t.TABLE_NAME = c.TABLE_NAME and t.TABLE_TYPE = 'BASE TABLE' "
                        + "where upper(c.COLUMN_NAME) = 'COD_SOC' and upper(c.TABLE_NAME) <> 'SOCIETE' "
                        + "order by c.TABLE_NAME",
                (rs, i) -> new String[] { rs.getString(1), rs.getString(2), rs.getString(3) });
        return colonnes.stream()
                .filter(t -> {
                    // les noms viennent du catalogue de la base, jamais de l'utilisateur
                    String sql = "select top 1 1 from [" + t[0] + "].[" + t[1] + "] where [" + t[2] + "] = ?";
                    return !jdbcTemplate.queryForList(sql, codSoc).isEmpty();
                })
                .map(t -> t[1])
                .toList();
    }

    @PostMapping("/addDepartement")
    public void addDepartement(@RequestBody PrmLieuGeographique f) {
        prmLieuGeogRepository.save(f);
    }

    @PostMapping("/deleteDep")
    public void deleteDep(@RequestBody String f) {
        prmLieuGeogRepository.deleteById(f);
    }

    @GetMapping("/getDepartement")
    List<PrmLieuGeographique> getDepartement() {
        return prmLieuGeogRepository.findAll();
    }

    @PostMapping("/addAffectation")
    public void addAffectation(@RequestBody Affectation f) {
        affectationRepository.save(f);
    }

    @PostMapping("/delAffectation")
    public void delAffectation(@RequestBody String f) {
        affectationRepository.deleteById(f);
    }

    @GetMapping("/getAffectation")
    List<Affectation> getAffectation() {
        return affectationRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Affectation::getCod_affect))
                .toList();
    }

    @PostMapping("/addTypDepart")
    public void addTypDepart(@RequestBody TypeDepart f) {
        typeDepartRepository.save(f);
    }

    @PostMapping("/deleTypeDepart")
    public void deleTypeDepart(@RequestBody String f) {
        typeDepartRepository.deleteById(f);
    }

    @GetMapping("/getTypDepart")
    List<TypeDepart> getTypDepart() {
        return typeDepartRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(TypeDepart::getCod_typ_depart))
                .toList();
    }

    @PostMapping("/addActiviteEnf")
    public void addActiviteEnf(@RequestBody ActiviteFamille f) {
        activiteFamilleRepository.save(f);
    }

    @PostMapping("/deletActiviteEnf")
    public void deletActiviteEnf(@RequestBody String f) {
        activiteFamilleRepository.deleteById(f);
    }

    @GetMapping("/getActiviteEnf")
    List<ActiviteFamille> getActiviteEnf() {
        return activiteFamilleRepository.findAll();
    }

}