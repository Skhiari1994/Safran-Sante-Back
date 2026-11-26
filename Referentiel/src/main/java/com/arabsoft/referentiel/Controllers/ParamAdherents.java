package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.*;
import com.arabsoft.referentiel.entities.cle.CleAgence;
import com.arabsoft.referentiel.entities.cle.ClePoste;
import com.arabsoft.referentiel.repositories.*;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.arabsoft.referentiel.services.LogoSociete;

import java.io.IOException;
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
    private final CorpsRepository corpsRepository;
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

    /** Enregistre ou remplace le logo d'une société (PNG, JPEG ou WebP, 5 Mo au plus). */
    @PutMapping(value = "/societe/{codSoc}/logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> setLogoSociete(@PathVariable String codSoc,
            @RequestParam("fichier") MultipartFile fichier) throws IOException {
        if (!societeRepository.existsById(codSoc)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cette société n'existe pas.");
        }
        byte[] octets = fichier.getBytes();
        String erreur = LogoSociete.erreur(octets);
        if (erreur != null) {
            return ResponseEntity.badRequest().body(erreur);
        }
        societeRepository.updateLogo(codSoc, octets);
        return ResponseEntity.noContent().build();
    }

    /** Image du logo d'une société ; 404 si elle n'en a pas. */
    @GetMapping("/societe/{codSoc}/logo")
    public ResponseEntity<byte[]> getLogoSociete(@PathVariable String codSoc) {
        byte[] logo = societeRepository.findLogo(codSoc);
        String type = LogoSociete.typeMime(logo);
        if (type == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(type))
                .cacheControl(CacheControl.noCache())
                .header("X-Content-Type-Options", "nosniff")
                .body(logo);
    }

    @DeleteMapping("/societe/{codSoc}/logo")
    public ResponseEntity<Void> supprimerLogoSociete(@PathVariable String codSoc) {
        if (!societeRepository.existsById(codSoc)) {
            return ResponseEntity.notFound().build();
        }
        societeRepository.supprimerLogo(codSoc);
        return ResponseEntity.noContent().build();
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

    /**
     * Longueur du code d'un corps. La table CORPS accepte 4 caractères, mais la colonne CORPS de PERSONNEL et de
     * AFFIL_MUTUELLE n'en accepte qu'un : un code plus long ne pourrait jamais être attribué à un adhérent.
     */
    private static final int CORPS_LONGUEUR_CODE = 1;
    private static final int CORPS_LONGUEUR_LIBELLE = 100;

    @GetMapping("/getCorps")
    List<Corps> getCorps() {
        return corpsRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Corps::getCod_corps))
                .toList();
    }

    /** Crée un corps. Un code déjà pris est refusé : la création ne remplace jamais un corps existant. */
    @PostMapping("/corps")
    public ResponseEntity<?> addCorps(@RequestBody Corps demande) {
        String code = texte(demande.getCod_corps());
        String erreur = verifierCodeCorps(code);
        if (erreur == null) {
            erreur = verifierLibellesCorps(demande);
        }
        if (erreur != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
        code = code.toUpperCase(java.util.Locale.ROOT);
        if (corpsRepository.existsById(code)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Le code « " + code + " » existe déjà.");
        }
        Corps corps = new Corps();
        corps.setCod_corps(code);
        corps.setLib_corps(texte(demande.getLib_corps()));
        corps.setLib_corps_a(texte(demande.getLib_corps_a()));
        return ResponseEntity.status(HttpStatus.CREATED).body(corpsRepository.save(corps));
    }

    /** Modifie les libellés d'un corps. Le code ne change jamais : il est référencé par les adhérents. */
    @PutMapping("/corps/{codCorps}")
    public ResponseEntity<?> updateCorps(@PathVariable String codCorps, @RequestBody Corps demande) {
        Corps corps = corpsRepository.findById(codCorps.trim()).orElse(null);
        if (corps == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce corps n'existe pas.");
        }
        String erreur = verifierLibellesCorps(demande);
        if (erreur != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erreur);
        }
        corps.setLib_corps(texte(demande.getLib_corps()));
        corps.setLib_corps_a(texte(demande.getLib_corps_a()));
        return ResponseEntity.ok(corpsRepository.save(corps));
    }

    /** Supprime un corps uniquement s'il n'est attribué à aucun adhérent (colonne CORPS des autres tables). */
    @DeleteMapping("/corps/{codCorps}")
    public ResponseEntity<String> deleteCorps(@PathVariable String codCorps) {
        String code = codCorps.trim();
        if (!corpsRepository.existsById(code)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ce corps n'existe pas.");
        }
        List<String> tablesLiees = tablesUtilisant("CORPS", "CORPS", code);
        if (!tablesLiees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "Impossible de supprimer le corps « " + code + " » : il est attribué à des adhérents ("
                            + String.join(", ", tablesLiees) + ").");
        }
        try {
            corpsRepository.deleteById(code);
            return ResponseEntity.ok().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Ce corps est utilisé ailleurs et ne peut pas être supprimé.");
        }
    }

    private static String texte(String valeur) {
        if (valeur == null) {
            return null;
        }
        String propre = valeur.trim();
        return propre.isEmpty() ? null : propre;
    }

    private static String verifierCodeCorps(String code) {
        if (code == null) {
            return "Le code est obligatoire.";
        }
        if (code.length() > CORPS_LONGUEUR_CODE) {
            return "Le code ne peut contenir que " + CORPS_LONGUEUR_CODE
                    + " caractère : c'est la longueur du champ Corps d'un adhérent.";
        }
        return null;
    }

    private static String verifierLibellesCorps(Corps corps) {
        String libelle = texte(corps.getLib_corps());
        if (libelle == null) {
            return "Le libellé est obligatoire.";
        }
        if (libelle.length() > CORPS_LONGUEUR_LIBELLE) {
            return "Le libellé ne peut pas dépasser " + CORPS_LONGUEUR_LIBELLE + " caractères.";
        }
        String arabe = texte(corps.getLib_corps_a());
        if (arabe != null && arabe.length() > CORPS_LONGUEUR_LIBELLE) {
            return "Le libellé arabe ne peut pas dépasser " + CORPS_LONGUEUR_LIBELLE + " caractères.";
        }
        return null;
    }

    /**
     * Tables (autres que celle indiquée) qui possèdent une colonne de ce nom et contiennent au moins une ligne
     * pour cette valeur. Les noms viennent du catalogue de la base, jamais de l'utilisateur.
     */
    private List<String> tablesUtilisant(String colonne, String tableExclue, String valeur) {
        List<String[]> colonnes = jdbcTemplate.query(
                "select c.TABLE_SCHEMA, c.TABLE_NAME, c.COLUMN_NAME "
                        + "from INFORMATION_SCHEMA.COLUMNS c "
                        + "join INFORMATION_SCHEMA.TABLES t on t.TABLE_SCHEMA = c.TABLE_SCHEMA "
                        + "and t.TABLE_NAME = c.TABLE_NAME and t.TABLE_TYPE = 'BASE TABLE' "
                        + "where upper(c.COLUMN_NAME) = upper(?) and upper(c.TABLE_NAME) <> upper(?) "
                        + "order by c.TABLE_NAME",
                (rs, i) -> new String[] { rs.getString(1), rs.getString(2), rs.getString(3) },
                colonne, tableExclue);
        return colonnes.stream()
                .filter(t -> {
                    String sql = "select top 1 1 from [" + t[0] + "].[" + t[1] + "] where [" + t[2] + "] = ?";
                    return !jdbcTemplate.queryForList(sql, valeur).isEmpty();
                })
                .map(t -> t[1])
                .toList();
    }

}