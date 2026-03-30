package com.arabsoft.referentiel.Controllers;


import com.arabsoft.referentiel.Entities.*;
import com.arabsoft.referentiel.Projections.PiecePretProjection;
import com.arabsoft.referentiel.Projections.PositionPretProjection;
import com.arabsoft.referentiel.Projections.TypPretProjection;
import com.arabsoft.referentiel.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paramCredit")
public class ParamCredit {

    @Autowired
    GroupePretRepository groupePretRepository;
    @Autowired
    TypePretRepository typePretRepository;
    @Autowired
    PositionPretRepository positionPretRepository;
    @Autowired
    ParamPiecePretRepository paramPiecePretRepository;
    @Autowired
    PiecesPretRepository piecesPretRepository;
    @Autowired
    EtatPretRepository etatPretRepository;
    @Autowired
    OrgPretRepository orgPretRepository;
    @GetMapping("/getGroupPret")
    List<GroupePret> getGroupPret(){
        return groupePretRepository.findAll();
    }

    @GetMapping("/getTypePret")
    List<TypePret> getTypePret(@RequestParam String grpPret){
        return typePretRepository.getTypePret(grpPret);
    }

    @GetMapping("/getAllTypePret")
    List<TypPretProjection> getAllType(){
        return typePretRepository.getAllTypePret();
    }
    @PostMapping("/addGroupe")
    GroupePret addGroupe(@RequestBody GroupePret groupePret){
        return groupePretRepository.save(groupePret);
    }
    @PostMapping("/addType")
    TypePret AddType(@RequestBody TypePret typPret){
        return typePretRepository.save(typPret);
    }
    @PostMapping("/addPosition")
    PositionPret addPosition(@RequestBody PositionPret positionPret){
        return positionPretRepository.save(positionPret);
    }

    @DeleteMapping("/deleteGroupe/{soc}/{grpPret}")
    public void deleteGroupe(@PathVariable String soc,@PathVariable String grpPret) {
        groupePretRepository.deleteGroupPret(soc,grpPret);
    }
    @DeleteMapping("/deleteType/{soc}/{grpPret}/{typ}")
    public void deleteType(@PathVariable String soc,@PathVariable String grpPret,@PathVariable String typ) {
        typePretRepository.deleteTypPret(soc,grpPret,typ);
    }
    @DeleteMapping("/deletePosition/{grpPret}/{typ}/{motif}")
    public void deletePosition(@PathVariable String grpPret,@PathVariable String typ,@PathVariable String motif) {
        positionPretRepository.deletePositionPret(grpPret,typ,motif);
    }
    @DeleteMapping("/deletePiece/{codPiece}")
    public void deletePiece(@PathVariable String codPiece) {
        paramPiecePretRepository.deleteById(codPiece);
    }
    @GetMapping("/getPositionPret")
    List<PositionPretProjection> getPositionPret(@RequestParam String grpPret, @RequestParam String typ){
        return positionPretRepository.getPositionPret(grpPret,typ);
    }

    @GetMapping("/findAllPiece")
    List<ParamPiecesPret> getPositionPret(){
        return paramPiecePretRepository.findAll();
    }
    @PostMapping("/addPiece")
    ParamPiecesPret addPiece(@RequestBody ParamPiecesPret paramPiecePret){
        return paramPiecePretRepository.save(paramPiecePret);
    }

    @GetMapping("/getPiecesPret")
    List<PiecePretProjection> getPiecePret(@RequestParam String grp,@RequestParam String typ){
        return piecesPretRepository.getListPieces(grp,typ);
    }
    @PostMapping("/addPiecePret")
    PiecesPret addPiecePret(@RequestBody PiecesPret piecesPret){
        return piecesPretRepository.save(piecesPret);
    }

    @DeleteMapping("/deletePiece/{grp}/{typ}/{codPiece}")
    public void deletePiece(@PathVariable String grp,@PathVariable String typ,@PathVariable String codPiece) {
        piecesPretRepository.deletePiecePret(grp,typ,codPiece);
    }

    @GetMapping("/getEtatPret")
    List<EtatPret> getEtatPret(){
        return etatPretRepository.findAll();
    }

    @PostMapping("/addEtatPret")
    EtatPret addPiecePret(@RequestBody EtatPret etatPret){
        return etatPretRepository.save(etatPret);
    }

    @DeleteMapping("/deleteEtatPret/{typ}/{etat}")
    public void deleteEtatPret(@PathVariable String typ,@PathVariable String etat) {
        etatPretRepository.deleteEtatPret(typ,etat);
    }

    @GetMapping("/getOrgPret")
    List<OrgPret> getOrgPret(){
        return orgPretRepository.findAll();
    }

    @PostMapping("/addOrgPRet")
    OrgPret addOrgPRet(@RequestBody OrgPret orgPret){
        return orgPretRepository.save(orgPret);
    }

    @DeleteMapping("/deleteOrgPret/{orgPret}")
    public void deleteOrgPret(@PathVariable String orgPret) {
        orgPretRepository.deleteById(orgPret);
    }
}
