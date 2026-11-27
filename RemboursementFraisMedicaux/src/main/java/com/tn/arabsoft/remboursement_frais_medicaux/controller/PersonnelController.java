package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import java.util.Map;
import com.tn.arabsoft.remboursement_frais_medicaux.util.LovRecherche;
import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.*;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.*;
import com.tn.arabsoft.remboursement_frais_medicaux.service.AdherentService;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Personnel")
@SuppressWarnings({ "java:S117", "java:S4684" })
public class PersonnelController {

    private final PersonnelRepository personnelRepository;
    private final AdherentService adherentService;
    private final PersAffilRepository persAffilRepository;
    private final RefFiliereRepository refFiliereRepository;
    private final DossierMldRepository dossierMldRepository;
    private final MaladieRepository maladieRepository;
    private final PlafondAssurRepository plafondAssurRepository;
    private final PlafondCnamRepository plafondCnamRepository;
    private final PriseChargeRepository priseChargeRepository;

    @GetMapping("/getPersonnelBultSoin")
    List<PersonnelBultSoinProjection> getPersonnelBultSoin(@RequestParam String soc) {
        return personnelRepository.getPersonnelBultSoin(soc);
    }

    @GetMapping("/getAdherent")
    List<AdherentProjection> getAdherent(@RequestParam String soc, @RequestParam String mat) {

        AdherentProjection self = personnelRepository.getAdherentLui(soc, mat); // real name + birth date

        List<AdherentProjection> list = new ArrayList<>(personnelRepository.getListAdherent(soc, mat));

        // synthetic row: the adherent himself, always first
        list.add(0, new AdherentProjection() {
            @Override
            public Integer getNum_fam() {
                return 0;
            }

            @Override
            public String getNom_pren() {
                return self != null ? self.getNom_pren() : "Adherent";
            }

            @Override
            public LocalDate getDat_nais() {
                return self != null ? self.getDat_nais() : null;
            }
        });

        return list;
    }

    @GetMapping("/getListAdherentDossMld")
    List<AdherentProjection> getListAdherentDossMld(@RequestParam String soc, @RequestParam String mat) {
        List<AdherentProjection> list = personnelRepository.getListAdherentDossMld(soc, mat);

        // Add synthetic row in Java
        list.add(new AdherentProjection() {
            @Override
            public Integer getNum_fam() {
                return 0;
            }

            @Override
            public String getNom_pren() {
                return "Adhérent";
            }

            @Override
            public LocalDate getDat_nais() {
                return null;
            }
        });

        return list;
    }

    @GetMapping("/getPersonnelBultSoinSaisie")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisie(@RequestParam String cod_soc) {
        return personnelRepository.getPersonnelBultSoinSaisie(cod_soc);
    }

    @GetMapping("/getListFamillePersonnelBultSoinLibre")
    List<FamillePersonnelBultSoin> getListFamillePersonnelBultSoinLibre(@RequestParam String cod_soc,
            @RequestParam String pers) {
        return personnelRepository.getListFamillePersonnelBultSoinLibre(cod_soc, pers);
    }

    @GetMapping("/getPersonnelBultSoinSaisieLibre")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieLibre(@RequestParam String cod_soc,
            @RequestParam String ass) {
        return personnelRepository.getPersonnelBultSoinSaisieLibre(cod_soc, ass);
    }

    @GetMapping("/getPersonnelBultSoinSaisieCnam")
    List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieCnam(@RequestParam String cod_soc,
            @RequestParam String ass, @RequestParam String cod_bord) {
        return personnelRepository.getPersonnelBultSoinSaisieCnam(cod_soc, ass, cod_bord);
    }

    @GetMapping("/getFamillePersonnelBultSoinSaisie")
    List<FamillePersonnelBultSoin> getPrestat(@RequestParam String cod_soc, @RequestParam String mat_pers,
            @RequestParam(required = false) String cod_bord) {
        return personnelRepository.getPrestat(cod_soc, mat_pers, cod_bord);
    }

    @GetMapping("/calPlafondMutuelle")
    ResponseProcedure calPlafondMutuelle(@RequestParam String wcodSoc, @RequestParam Long annee,
            @RequestParam String matDeb, @RequestParam String matFin) {
        return adherentService.calPlafondMutuelle(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/calPlafondCnam")
    ResponseProcedure calPlafondCnam(@RequestParam String wcodSoc, @RequestParam String annee,
            @RequestParam String matDeb, @RequestParam String matFin) {
        return adherentService.calPlafondCnam(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/majPecEnf")
    ResponseProcedure majPecEnf(@RequestParam String wcodSoc, @RequestParam String annee, @RequestParam String matDeb,
            @RequestParam String matFin) {
        return adherentService.majPecEnf(wcodSoc, annee, matDeb, matFin);
    }

    @GetMapping("/GetAllPers")
    List<Personnel> getAllPers(@RequestParam("soc") String soc) {
        return personnelRepository.getPersActifBySoc(soc);
    }

    @GetMapping("/getPersAffil")
    List<PersAffilProjection> getPersAffil(@RequestParam String soc, @RequestParam String mat) {
        return persAffilRepository.getPersAffil(soc, mat);
    }

    @GetMapping("/getRefFilliere")
    List<FiliereProjection> getRefFilliere() {
        return refFiliereRepository.getListFiliere();
    }

    @PostMapping("/addPersAffil")
    List<PersAffil> addPersAffil(@RequestBody List<PersAffil> persAffil) {
        return persAffilRepository.saveAll(persAffil);
    }

    @GetMapping("/getRefEtablis")
    List<RefEtablisProjection> getRefEtablis() {
        return persAffilRepository.getRefEtablis();
    }

    @GetMapping("/getPersActif")
    List<PersonnelProjection> getPersActif(@RequestParam String soc) {
        return personnelRepository.getPersActif(soc);
    }

    @GetMapping("/getDossierMll")
    List<DossierMldProjection> getDossierMll(@RequestParam String soc, @RequestParam String mat) {
        return dossierMldRepository.getDossierMll(soc, mat);
    }

    @GetMapping("/getNumDoss")
    Long getNumDoss(@RequestParam String soc, @RequestParam String mat) {
        return dossierMldRepository.getNumDoss(soc, mat);
    }

    @GetMapping("/getMaladie")
    List<Maladie> getMaladie() {
        return maladieRepository.getMaladie();
    }

    @PostMapping("/addDossMld")
    List<DossierMld> addDossApci(@RequestBody List<DossierMld> dossierMlds) {
        return dossierMldRepository.saveAll(dossierMlds);
    }

    @GetMapping("/getPlafondAssur")
    List<PlafondAssurProjection> getPlafondAssur(@RequestParam String soc, @RequestParam String mat) {
        return plafondAssurRepository.getPlafondAssur(soc, mat);
    }

    @GetMapping("/getPlafondCnam")
    List<PlafondCnam> getPlafondCnam(@RequestParam String mat) {
        return plafondCnamRepository.getPlafondCnam(mat);
    }

    @PostMapping("/addPlafAssur")
    PlafondAssur addPlafAssur(@RequestBody PlafondAssur plafondAssur) {
        return plafondAssurRepository.save(plafondAssur);
    }

    @PostMapping("/addPlafCnam")
    PlafondCnam addPlafCnam(@RequestBody PlafondCnam plafondCnam) {
        return plafondCnamRepository.save(plafondCnam);
    }

    @GetMapping("/getPriseCharge")
    List<PriseChargeProjection> getPriseCharge(@RequestParam String soc, @RequestParam String mat) {
        return priseChargeRepository.getPriseCharge(soc, mat);
    }

    @PostMapping("/addPriseCharge")
    List<PriseCharge> addPriseCharge(@RequestBody List<PriseCharge> priseCharge) {
        return priseChargeRepository.saveAll(priseCharge);
    }

    @DeleteMapping("/deletePriseCharge")
    void deletePriseCharge(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("pec") String pec) {
        priseChargeRepository.deletePriseCharge(soc, mat, pec);
    }

    @GetMapping("/getPersBultCnamDeb")
    List<PersonnelProjection> getPersBultCnamDeb(@RequestParam String soc) {
        return this.personnelRepository.getPersBultCnamDeb(soc);
    }

    @GetMapping("/getPersBultCnamFin")
    List<PersonnelProjection> getPersBultCnamFin(@RequestParam String soc) {
        return this.personnelRepository.getPersBultCnamFin(soc);
    }

    @GetMapping("/getListAdherentFamille")
    List<AdherentProjection> getListAdherentFamille(@RequestParam String soc, @RequestParam("mat") String mat,
            @RequestParam("fam") String fam) {
        Integer famInt = fam != null ? Integer.parseInt(fam) : null;
        return this.personnelRepository.getListAdherentFamille(soc, mat, famInt);
    }


    // ---------- fenêtres de recherche « personnel », page par page (au lieu de ~2 300 lignes d'un coup) ----------
    // Paramètres communs : code (matricule exact saisi dans le champ), un filtre par colonne (mat_pers exact,
    // dat_nais jj/mm/aaaa, autres « contient »), q (recherche unique, 3 mots), cols (colonnes affichées), page, taille.

    /** Fenêtre de recherche du personnel, page par page : même liste que /getPersActif (personnel actif + libellé assurance). */
    @GetMapping("/lovPersActif")
    public Map<String, Object> lovPersActif(@RequestParam String soc, @RequestParam Map<String, String> params) {
        LovRecherche.Filtres f = new LovRecherche.Filtres(params, "mat_pers", "dat_nais", "num_retr", "nom_pers", "pren_pers");
        String code = LovRecherche.exact(params.get("code"));
        String[] m = f.motsExacts();
        String[] l = f.motsContient();
        long total = personnelRepository.compterPersActif(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("num_retr"), f.get("nom_pers"), f.get("pren_pers"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("num_retr"), f.visible("nom_pers"), f.visible("pren_pers"), m[0], l[0], m[1], l[1], m[2], l[2]);
        List<PersonnelProjection> lignes = total == 0 ? List.of()
                : personnelRepository.pagePersActif(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("num_retr"), f.get("nom_pers"), f.get("pren_pers"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("num_retr"), f.visible("nom_pers"), f.visible("pren_pers"), m[0], l[0], m[1], l[1], m[2], l[2], f.debut(), f.taille());
        return LovRecherche.reponse(lignes, total, f.page(), f.taille());
    }

    /** Fenêtre de recherche du personnel, page par page : même liste que /GetAllPers (fiche personnel complète). */
    @GetMapping("/lovPersActifEntite")
    public Map<String, Object> lovPersActifEntite(@RequestParam String soc, @RequestParam Map<String, String> params) {
        LovRecherche.Filtres f = new LovRecherche.Filtres(params, "mat_pers", "dat_nais", "num_retr", "nom_pers", "pren_pers");
        String code = LovRecherche.exact(params.get("code"));
        String[] m = f.motsExacts();
        String[] l = f.motsContient();
        long total = personnelRepository.compterPersActifEntite(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("num_retr"), f.get("nom_pers"), f.get("pren_pers"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("num_retr"), f.visible("nom_pers"), f.visible("pren_pers"), m[0], l[0], m[1], l[1], m[2], l[2]);
        List<Personnel> lignes = total == 0 ? List.of()
                : personnelRepository.pagePersActifEntite(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("num_retr"), f.get("nom_pers"), f.get("pren_pers"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("num_retr"), f.visible("nom_pers"), f.visible("pren_pers"), m[0], l[0], m[1], l[1], m[2], l[2], f.debut(), f.taille());
        return LovRecherche.reponse(lignes, total, f.page(), f.taille());
    }

    /** Fenêtre de recherche du personnel, page par page : même liste que /getPersBultCnamDeb et /getPersBultCnamFin. */
    @GetMapping("/lovPersBultCnam")
    public Map<String, Object> lovPersBultCnam(@RequestParam String soc, @RequestParam Map<String, String> params) {
        LovRecherche.Filtres f = new LovRecherche.Filtres(params, "mat_pers", "dat_nais", "nom", "num_retr");
        String code = LovRecherche.exact(params.get("code"));
        String[] m = f.motsExacts();
        String[] l = f.motsContient();
        long total = personnelRepository.compterPersBultCnam(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("nom"), f.get("num_retr"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("nom"), f.visible("num_retr"), m[0], l[0], m[1], l[1], m[2], l[2]);
        List<PersonnelProjection> lignes = total == 0 ? List.of()
                : personnelRepository.pagePersBultCnam(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("nom"), f.get("num_retr"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("nom"), f.visible("num_retr"), m[0], l[0], m[1], l[1], m[2], l[2], f.debut(), f.taille());
        return LovRecherche.reponse(lignes, total, f.page(), f.taille());
    }

    /** Fenêtre de recherche du personnel, page par page : même liste que /getPersonnelBultSoin. */
    @GetMapping("/lovPersonnelBultSoin")
    public Map<String, Object> lovPersonnelBultSoin(@RequestParam String soc, @RequestParam Map<String, String> params) {
        LovRecherche.Filtres f = new LovRecherche.Filtres(params, "mat_pers", "dat_nais", "nom", "num_retr", "lib_fill");
        String code = LovRecherche.exact(params.get("code"));
        String[] m = f.motsExacts();
        String[] l = f.motsContient();
        long total = personnelRepository.compterPersonnelBultSoin(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("nom"), f.get("num_retr"), f.get("lib_fill"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("nom"), f.visible("num_retr"), f.visible("lib_fill"), m[0], l[0], m[1], l[1], m[2], l[2]);
        List<PersonnelBultSoinProjection> lignes = total == 0 ? List.of()
                : personnelRepository.pagePersonnelBultSoin(soc, code, f.get("mat_pers"), f.get("dat_nais"), f.get("nom"), f.get("num_retr"), f.get("lib_fill"), f.visible("mat_pers"), f.visible("dat_nais"), f.visible("nom"), f.visible("num_retr"), f.visible("lib_fill"), m[0], l[0], m[1], l[1], m[2], l[2], f.debut(), f.taille());
        return LovRecherche.reponse(lignes, total, f.page(), f.taille());
    }
}
