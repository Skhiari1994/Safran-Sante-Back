package com.arabsoft.referentiel.Controllers;

import com.arabsoft.referentiel.Entities.MotifRembour;
import com.arabsoft.referentiel.Entities.ParamCaisse;
import com.arabsoft.referentiel.Repositories.MotifRembourRepository;
import com.arabsoft.referentiel.Repositories.ParamCaisseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ParamCaisse")
public class CaisseRetraiteContoller {

    @Autowired
    ParamCaisseRepository paramCaisseRepository;
    @Autowired
    MotifRembourRepository motifRembourRepository;

    @GetMapping("/getAllParamCaisse")
    List<ParamCaisse> getAllParamCaisses(){
        return paramCaisseRepository.findAll();
    }

    @PostMapping("/addParamCaisse")
    ParamCaisse addParamCaisse(@RequestBody ParamCaisse paramCaisse){
        return paramCaisseRepository.save(paramCaisse);
    }

    @DeleteMapping("/deleteParamCaisse")
    void deleteParamCaisse(@RequestParam String codParam){
        paramCaisseRepository.deleteById(codParam);
    }

    @GetMapping("/getAllMotifRembour")
    List<MotifRembour> getAllMotifRembour(){
        return motifRembourRepository.findAll();
    }

    @PostMapping("/addMotifRembour")
    MotifRembour addMotifRembour(@RequestBody MotifRembour paramCaisse){
        return motifRembourRepository.save(paramCaisse);
    }

    @DeleteMapping("/deleteMotifRembour")
    void deleteMotifRembour(@RequestParam String codRemb){
        motifRembourRepository.deleteById(codRemb);
    }
}
