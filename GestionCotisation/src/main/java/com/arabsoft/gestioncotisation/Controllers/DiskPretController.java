package com.arabsoft.gestioncotisation.Controllers;

import com.arabsoft.gestioncotisation.DTO.ProcActifValDTO;
import com.arabsoft.gestioncotisation.Entities.CotisMutPers;
import com.arabsoft.gestioncotisation.Entities.DiskPret;
import com.arabsoft.gestioncotisation.Projections.DiskPretProjection;
import com.arabsoft.gestioncotisation.Repositories.DiskPretRepository;
import com.arabsoft.gestioncotisation.Services.DiskPretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diskPret")
public class DiskPretController {

    private final DiskPretService diskPretService;

    public DiskPretController(DiskPretService diskPretService) {
        this.diskPretService = diskPretService;
    }

    @GetMapping("/getDiskPret")
    public List<DiskPretProjection> getFilteredDiskPrets(
            @RequestParam(required = false) String matPers,
            @RequestParam(required = false) String corps,
            @RequestParam String mois) {
        return diskPretService.getFilteredDiskPrets(matPers, corps, mois);
    }

    @PostMapping("/maj-cotisation")
    public ResponseEntity<Map<String, Object>> updateCotisation(@RequestBody ProcActifValDTO request) {
        Map<String, Object> response = new HashMap<>();
        try {
            int rowsUpdated = diskPretService.callMajCotisationProc(
                    request.getP_mat_pers(),
                    request.getP_corps(),
                    request.getP_mois(),
                    request.getP_cod_soc()
            );

            response.put("status", "success");
            response.put("message", "Procédure exécutée avec succès");
            response.put("rowsUpdated", rowsUpdated);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // <-- ajoute ça pour voir la vraie erreur
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Erreur lors de l'exécution de la procédure");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);


    }
    }

}
