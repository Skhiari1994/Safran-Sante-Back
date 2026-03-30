package com.arabsoft.gestionindemnites.Services;

import com.arabsoft.gestionindemnites.Entities.Cle.CleDemandeDons;
import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.arabsoft.gestionindemnites.Entities.PieceDemDons;
import com.arabsoft.gestionindemnites.Repositories.PieceDemDonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PieceDemandeDonsService {
    @Autowired
    private PieceDemDonsRepository repository;


    public Optional<PieceDemDons> findById(ClePieceDemDons id) {
        return repository.findById(id);
    }
    public PieceDemDons updateDemandeDon(ClePieceDemDons id, PieceDemDons demandeDon) {
        PieceDemDons existing = repository.findById(id).orElseThrow(() -> new RuntimeException("pieces Don not found"));
        existing.setTyp_don(demandeDon.getTyp_don());
        existing.setCod_soc(demandeDon.getCod_soc());
        existing.setMat_pers(demandeDon.getMat_pers());
        existing.setNum_fam(demandeDon.getNum_fam());
        existing.setDat_dem_don(demandeDon.getDat_dem_don());
        existing.setCod_piece(demandeDon.getCod_piece());

        // Set other fields as required
        return repository.save(existing);
    }
    public PieceDemDons createPieceDemandeDon(PieceDemDons piecedemandeDon) {
        return repository.save(piecedemandeDon);
    }

}