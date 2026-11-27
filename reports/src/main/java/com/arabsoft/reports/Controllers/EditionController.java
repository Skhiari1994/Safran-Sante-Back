package com.arabsoft.reports.controllers;

import com.arabsoft.reports.entities.*;
import com.arabsoft.reports.projections.PersonnelProjection;
import com.arabsoft.reports.repositories.*;
import com.arabsoft.reports.services.PrintReportsService;

import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/personnel")
@SuppressWarnings({ "java:S101", "java:S116", "java:S117" })
public class EditionController {

    private final PersonnelDao personnelDao;

    private final PrmLieuGeogRepository prmLieuGeogRepository;

    private final PrintReportsService printService;

    @GetMapping
    public List<PersonnelProjection> getAllPersonnel(@RequestParam("soc") String soc) {
        return personnelDao.getPersonnel(soc, null);
    }

    @GetMapping("/by-mat")
    public List<PersonnelProjection> getByMatPers(
            @RequestParam("soc") String soc,
            @RequestParam("mat_pers") String mat_pers) {
        return personnelDao.getPersonnel(soc, mat_pers);
    }

    @GetMapping("/getAllLieu")
    List<PrmLieuGeographique> getAllLieu() {
        return this.prmLieuGeogRepository.findAll();
    }

    @GetMapping("/genererFich")
    ReponseGenererFich genererFich(@RequestParam String soc, @RequestParam String mois,
            @RequestParam(required = false) String corps, @RequestParam(required = false) String grpPret,
            @RequestParam(required = false) String typPret) {
        return this.printService.genererFich(soc, mois, corps, grpPret, typPret);
    }

    @GetMapping("/genererFichExel")
    ResponseEntity<Resource> genererFichExel(@RequestParam String fileName) throws Exception {
        return this.printService.exporterExcel(fileName);
    }

    /**
     * Personnel ayant des bulletins imputés sur l'année (fenêtre de l'édition « Récap remboursement adhérent »).
     * La requête existait (PersonnelDao.getPersRecap) mais n'était exposée nulle part : l'écran recevait une 404.
     */
    @GetMapping("/getPersRecap")
    List<PersonnelProjection> getPersRecap(@RequestParam String soc, @RequestParam String annee) {
        return this.personnelDao.getPersRecap(soc, annee);
    }

    @GetMapping("/getPersCArteSoinDeb")
    List<PersonnelProjection> getPersCArteSoinDeb(@RequestParam String soc,
            @RequestParam(required = false) String mat_fin) {
        return this.personnelDao.getPersBultMutDeb(soc, mat_fin);
    }

    @GetMapping("/getPersCArteSoinFin")
    List<PersonnelProjection> getPersCArteSoinFin(@RequestParam String soc,
            @RequestParam(required = false) String mat_deb) {
        return this.personnelDao.getPersBultMutFin(soc, mat_deb);
    }

    @GetMapping("/getPersBultCnamDeb")
    List<PersonnelProjection> getPersBultCnamDeb(@RequestParam String soc,
            @RequestParam(required = false) String mat_fin) {
        return this.personnelDao.getPersBultCnamDeb(soc, mat_fin);
    }

    @GetMapping("/getPersBultCnamFin")
    List<PersonnelProjection> getPersBultCnamFin(@RequestParam String soc,
            @RequestParam(required = false) String mat_deb) {
        return this.personnelDao.getPersBultCnamFin(soc, mat_deb);
    }

}
