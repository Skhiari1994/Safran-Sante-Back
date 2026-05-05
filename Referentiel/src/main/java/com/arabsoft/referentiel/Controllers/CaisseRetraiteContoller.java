package com.arabsoft.referentiel.controllers;

import com.arabsoft.referentiel.entities.MotifRembour;
import com.arabsoft.referentiel.entities.ParamCaisse;

import com.arabsoft.referentiel.repositories.MotifRembourRepository;
import com.arabsoft.referentiel.repositories.ParamCaisseRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ParamCaisse")
public class CaisseRetraiteContoller {

    private final ParamCaisseRepository paramCaisseRepository;
    private final MotifRembourRepository motifRembourRepository;

    @GetMapping("/getAllParamCaisse")
    List<ParamCaisse> getAllParamCaisses() {
        return paramCaisseRepository.findAll();
    }

    @PostMapping("/addParamCaisse")
    ParamCaisse addParamCaisse(@RequestBody ParamCaisse paramCaisse) {
        return paramCaisseRepository.save(paramCaisse);
    }

    @DeleteMapping("/deleteParamCaisse")
    void deleteParamCaisse(@RequestParam String codParam) {
        paramCaisseRepository.deleteById(codParam);
    }

    @GetMapping("/getAllMotifRembour")
    List<MotifRembour> getAllMotifRembour() {
        return motifRembourRepository.findAll();
    }

    @PostMapping("/addMotifRembour")
    MotifRembour addMotifRembour(@RequestBody MotifRembour paramCaisse) {
        return motifRembourRepository.save(paramCaisse);
    }

    @DeleteMapping("/deleteMotifRembour")
    void deleteMotifRembour(@RequestParam String codRemb) {
        motifRembourRepository.deleteById(codRemb);
    }
}
