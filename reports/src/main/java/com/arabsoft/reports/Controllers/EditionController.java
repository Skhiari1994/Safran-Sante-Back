package com.arabsoft.reports.Controllers;

import com.arabsoft.reports.Entities.*;
import com.arabsoft.reports.Projections.PersonnelProjection;
import com.arabsoft.reports.Projections.PretProjection;
import com.arabsoft.reports.Repositories.*;
import com.arabsoft.reports.Services.PrintReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.print.PrintService;
import java.util.List;

@RestController
@RequestMapping("/personnel")
public class EditionController {
    @Autowired
    private PersonnelDao personnelDao;
    @Autowired
    NatureDonRepository natureDonRepository;
    @Autowired
    PrmLieuGeogRepository prmLieuGeogRepository;

    @Autowired
    OffConvRepository offConvRepository;
    @Autowired
    PretPersRepository pretPersRepository;
    @Autowired
    GroupePretRepository groupePretRepository;
    @Autowired
    TypePretRepository typePretRepository;
    @Autowired
    PrintReportsService printService;
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
    @GetMapping("/getNatDon")
    List<NatureDon> getNatDon(){
        return this.natureDonRepository.getNatureDon();
    }

    @GetMapping("/getAllLieu")
    List<PrmLieuGeographique> getAllLieu(){
        return this.prmLieuGeogRepository.findAll();
    }

    @GetMapping("/getOffConv")
    List<OffConv> getOffConv(){
        return this.offConvRepository.findAll();
    }

    @GetMapping("/getPretPers")
    List<PretProjection> getPretPers(@RequestParam String mat){
        return this.pretPersRepository.getPretPers(mat);
    }

    @GetMapping("/getGroupePret")
    List<GroupePret> getGroupePret(){
        return this.groupePretRepository.getGroupePret();
    }

    @GetMapping("/getTypePret")
    List<TypePret> getTypePret(){
        return this.typePretRepository.getTypePret();
    }

    @GetMapping("/genererFich")
    ReponseGenererFich genererFich(@RequestParam String soc,@RequestParam String mois,@RequestParam(required = false) String corps,@RequestParam(required = false) String grpPret,@RequestParam (required = false)String typPret){
        return this.printService.generer_fich(soc,mois,corps,grpPret,typPret);
    }

    @GetMapping("/genererFichExel")
    ResponseEntity<Resource> genererFichExel(@RequestParam String fileName) throws Exception {
        return this.printService.exporterExcel(fileName);
    }

    @GetMapping("/getPersCArteSoinDeb")
    List<PersonnelProjection> getPersCArteSoinDeb(@RequestParam String soc,@RequestParam(required = false) String mat_fin) {
        return this.personnelDao.getPersBultMutDeb(soc,mat_fin);
    }

    @GetMapping("/getPersCArteSoinFin")
    List<PersonnelProjection> getPersCArteSoinFin(@RequestParam String soc,@RequestParam(required = false) String mat_deb) {
        return this.personnelDao.getPersBultMutFin(soc,mat_deb);
    }


    @GetMapping("/getPersBultCnamDeb")
    List<PersonnelProjection> getPersBultCnamDeb(@RequestParam String soc,@RequestParam(required = false) String mat_fin) {
        return this.personnelDao.getPersBultCnamDeb(soc,mat_fin);
    }

    @GetMapping("/getPersBultCnamFin")
    List<PersonnelProjection> getPersBultCnamFin(@RequestParam String soc,@RequestParam(required = false) String mat_deb) {
        return this.personnelDao.getPersBultCnamFin(soc,mat_deb);
    }
}
