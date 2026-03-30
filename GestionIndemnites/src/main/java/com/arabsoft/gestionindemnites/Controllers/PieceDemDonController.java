package com.arabsoft.gestionindemnites.Controllers;

import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.arabsoft.gestionindemnites.Entities.PieceDemDons;
import com.arabsoft.gestionindemnites.Projections.PieceDemDonsProjection;
import com.arabsoft.gestionindemnites.Projections.PieceProjection;
import com.arabsoft.gestionindemnites.Repositories.PieceDemDonsRepository;
import com.arabsoft.gestionindemnites.Services.PieceDemandeDonsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/piece")
public class PieceDemDonController {

    @Autowired
    private PieceDemDonsRepository pieceDemDonsRepository;
    @Autowired
    private PieceDemandeDonsService pieceDemDonsService;

    @GetMapping("/all")
    List<PieceProjection> getPiece() {
        return pieceDemDonsRepository.getListPiece();
    }

    @PostMapping("/getAddedPiece")
    public ResponseEntity<?> getLigCotisMutPers(@RequestBody ClePieceDemDons request) {
        try {
            // Map the request to the composite key
            ClePieceDemDons id = new ClePieceDemDons();
            id.setTyp_don(request.getTyp_don());
            id.setCod_soc(request.getCod_soc());
            id.setMat_pers(request.getMat_pers());
            id.setDat_dem_don(request.getDat_dem_don());
            id.setCod_piece(request.getCod_piece());
            id.setNum_fam(request.getNum_fam());
            // Fetch the entity
            Optional<PieceDemDons> results = pieceDemDonsService.findById(id);
            // Return the response based on the result
            return results.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }
    @GetMapping("/getListTypDon/{typDon}/{codAffect}")
    List<PieceDemDonsProjection> getListTypDon(@PathVariable String typDon, @PathVariable String codAffect){
        return this.pieceDemDonsRepository.getListTypDons(typDon,codAffect);
    }
    @GetMapping("/getListNatDons")
    List<PieceDemDonsProjection> getListNatDons(){
        return this.pieceDemDonsRepository.getListNatDons();
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveDemandeDon(@RequestBody List<PieceDemDons> pieceDemDons) {

        for (PieceDemDons row : pieceDemDons) {
            pieceDemDonsService.createPieceDemandeDon(row); // Assuming save will handle both insert and update
        }
        return ResponseEntity.ok("1");

    }


}
