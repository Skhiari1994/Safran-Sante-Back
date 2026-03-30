package com.arabsoft.gestioncotisation.Controllers;

import com.arabsoft.gestioncotisation.DTO.ChargCotisMutPersDTO;
import com.arabsoft.gestioncotisation.DTO.CotisAddDTO;
import com.arabsoft.gestioncotisation.DTO.CotisDto;
import com.arabsoft.gestioncotisation.DTO.CotisationDTO;
import com.arabsoft.gestioncotisation.Entities.Cle.CleLigCotisMutPers;
import com.arabsoft.gestioncotisation.Entities.Cle.CotisMutPersID;
import com.arabsoft.gestioncotisation.Entities.CotisMutPers;
import com.arabsoft.gestioncotisation.Entities.LigCotisMutPers;
import com.arabsoft.gestioncotisation.Exception.ResourceNotFoundException;
import com.arabsoft.gestioncotisation.Projections.CotisMutPersProjection;
import com.arabsoft.gestioncotisation.Projections.PersCotisProjection;
import com.arabsoft.gestioncotisation.Repositories.CotisMutPersRepository;
import com.arabsoft.gestioncotisation.Repositories.LigCotisMutPersRepository;
import com.arabsoft.gestioncotisation.Services.CotisMutPersService;
import com.arabsoft.gestioncotisation.Services.LigCotisMutPersService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.DELETE;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/Cotisation")
public class CotisMutPersController {

    @Autowired
    CotisMutPersRepository cotisMutPersRepository;
    @Autowired
    private CotisMutPersService cotisMutPersService;
    @Autowired
    private LigCotisMutPersService ligCotisMutPersService;

    @Autowired
    LigCotisMutPersRepository ligCotisMutPersRepository;

    @GetMapping("/cot")
    List<CotisMutPers> getBanque() {
        return cotisMutPersRepository.findAll();
    }


    @PostMapping("/cotisation")
    public ResponseEntity<?> getCotisation(@RequestBody CotisMutPers request) {
        Optional<CotisMutPers> cotisMutPers = cotisMutPersRepository.findByCodSocAndMatPersAndNumCot(request.getCod_soc(),
                request.getMat_pers(),
                request.getNum_cot());
        return cotisMutPers
                .map(cotis -> ResponseEntity.ok(cotis))  // Return full object in the response
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cotisationMutPers")
    public ResponseEntity<?> getCotisationPers(@RequestBody CotisMutPers request) {
        // Fetch multiple results instead of a single one
        List<CotisMutPers> cotisMutPersList = cotisMutPersRepository.findByCodSocAndMatPers(request.getCod_soc(),
                request.getMat_pers());

        // Check if the list is not empty
        if (!cotisMutPersList.isEmpty()) {
            // Return the list of CotisMutPers if found
            return ResponseEntity.ok(cotisMutPersList);
        } else {
            // Return a 404 if no results are found
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/updateCotis")
    CotisMutPers updateCotis(@RequestBody CotisMutPers cotisMutPers){
        return cotisMutPersRepository.save(cotisMutPers);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addCotisations(@RequestBody CotisMutPers cotisation) {
        try {
            LocalDate startDate = cotisation.getDat_deb();
            LocalDate endDate = cotisation.getDat_fin();

            // 1. Validate date range
            if (startDate.isAfter(endDate)) {
                return ResponseEntity.badRequest().body("La date de début doit être antérieure ou égale à la date de fin.");
            }

            // 2. Check if cotisations already exist in that date range
            List<LigCotisMutPers> existingDates = cotisMutPersRepository.findExistingDatesInRange(
                    cotisation.getCod_soc(),
                    cotisation.getMat_pers(),
                    startDate,
                    endDate
            );

            if (!existingDates.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        String.format("Des cotisations existent déjà sur cette période : %s", existingDates)
                );
            }

            // 3. Save the cotisation once
            CotisMutPers savedCotisation = cotisMutPersService.saveCotisationSeq(cotisation);

            // 4. Call the procedure to generate all lines (lig_cotis_mut_pers)
            ligCotisMutPersService.majLigCotisation(
                    savedCotisation.getCod_soc(),
                    savedCotisation.getMat_pers(),
                    savedCotisation.getNum_cot(),
                    savedCotisation.getDat_deb(),
                    savedCotisation.getDat_fin(),
                    savedCotisation.getMnt_payer()
            );

            // 5. Return response
            return ResponseEntity.ok(savedCotisation);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de l'ajout de la cotisation : " + e.getMessage());
        }
    }

    @PostMapping("/checkLigCotis")
    public Object checkCotisation(@RequestBody CotisAddDTO request) {
        try {
            // Log received request details

            // Call the service method to check cotisation
            int result = ligCotisMutPersService.checkCotisation(
                    request.getCod_soc(),
                    request.getMat_pers(),
                    request.getDat_deb(),
                    request.getDat_fin()
            );

            // Check if the result indicates a cotisation issue
            if (result==1) {
                System.out.println("Received request:1" + request);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);

            } else {
                System.out.println("Received request:2 " + request);

                return ResponseEntity.ok(result);

            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return 0;
        }
    }


    @GetMapping("/PersCotis/{soc}")
    List<PersCotisProjection> getPersCotis(@PathVariable String soc) {
        return cotisMutPersRepository.getPersCotis(soc);
    }


    @GetMapping("/PersCotisSearch/{soc}")
    List<PersCotisProjection> getPersCotisSearch(@PathVariable String soc) {
        return cotisMutPersRepository.getPersCotisSearch(soc);
    }
    @PostMapping("/LigCotisPersMut")
    public ResponseEntity<?> getLigCotisMutPers(@RequestBody CleLigCotisMutPers request) {
        try {
            // Map the request to the composite key
            CleLigCotisMutPers id = new CleLigCotisMutPers();
            id.setCod_soc(request.getCod_soc());
            id.setMat_pers(request.getMat_pers());
            id.setNum_cot(request.getNum_cot());
            id.setDat_cot(request.getDat_cot());

            // Fetch the entity
            Optional<LigCotisMutPers> results = ligCotisMutPersService.findById(id);

            // Return the response based on the result
            return results.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/deleteLig")
    public ResponseEntity<?> deleteLig(@RequestBody CleLigCotisMutPers request) {
        try {
            // Construire la clé composite
            CleLigCotisMutPers id = new CleLigCotisMutPers();
            id.setCod_soc(request.getCod_soc());
            id.setMat_pers(request.getMat_pers());
            id.setNum_cot(request.getNum_cot());
            id.setDat_cot(request.getDat_cot());

            // Vérifier l'existence de l'entité
            if (ligCotisMutPersRepository.existsById(id)) {
                ligCotisMutPersRepository.deleteById(id);
                return ResponseEntity.ok("Ligne supprimée avec succès.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Ligne non trouvée pour suppression.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression : " + e.getMessage());
        }
    }



    @GetMapping("/test")
    public ResponseEntity<?> testExists(
            @RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate datDeb) {
        boolean exists = cotisMutPersRepository.existsByCodSocAndMatPersAndDatDeb(codSoc, matPers, datDeb);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getLig")
    public ResponseEntity<?> searchLigCotis(@RequestParam String codSoc,
            @RequestParam String matPers,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate datDeb,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate datFin
    ) {
        if (matPers == null || datDeb == null || datFin == null) {
            return ResponseEntity.badRequest().body("All parameters (matPers, datDeb, and datFin) are required.");
        }

        try {
            // Fetch the list of results where each result is an Object[]
            List<Object[]> results = ligCotisMutPersService.getFilteredLigCotis(codSoc,matPers, datDeb, datFin);

            if (results.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            // Transform Object[] to a more readable format, for example, a List of Maps
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Object[] row : results) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("cod_soc", row[0]);
                resultMap.put("mat_pers", row[1]);
                resultMap.put("num_cot", row[2]);
                resultMap.put("dat_mut", row[3]);
                resultMap.put("mnt_payer", row[4]);
                resultMap.put("dat_cot", row[5]);

                resultList.add(resultMap);
            }

            return ResponseEntity.ok(resultList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/PersCotisActif/{soc}")
    List<PersCotisProjection> getPersCotisActif(@PathVariable String soc) {
        return cotisMutPersRepository.getPersCotisActif(soc);
    }

    @PostMapping("/PersCotisActifCorps")
    public List<CotisMutPersProjection> getPersCotisActifCorps(@RequestBody CotisMutPers request) {

        return cotisMutPersRepository.getPersCotisActifCorps(request.getCod_soc(), request.getCorps(), request.getDat_deb());
    }

    @PostMapping("/chargActInx")
    public ResponseEntity<String> chargActInx(@RequestBody ChargCotisMutPersDTO request) {
        try {
            int result = cotisMutPersService.ChargActInx(request.getSoc(), request.getMois(), request.getMat());
            if (result == 1) {
                System.out.println("1"+result);
                return ResponseEntity.ok("1");

            } else {
                System.out.println("Procedure execution failed  :"+result);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Procedure execution failed");
            }
        } catch (Exception e) {
            System.out.println("error  :"+e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/updateEtatAndModPay")
    public ResponseEntity<?> updateEtatAndModPay(@RequestBody List<CotisMutPers> requestList) {
        try {
            // Initialize a list to hold records that were not found
            List<String> notFoundRecords = new ArrayList<>();

            // Loop through the provided list of CotisMutPers objects
            for (CotisMutPers request : requestList) {
                Optional<CotisMutPers> optionalCotis = cotisMutPersRepository.findByCodSocAndMatPersAndNumCot(
                        request.getCod_soc(),
                        request.getMat_pers(),
                        request.getNum_cot()
                );

                if (optionalCotis.isPresent()) {
                    CotisMutPers existingCotis = optionalCotis.get();

                    // Update the fields for each record
                    existingCotis.setEtat_cot(request.getEtat_cot());
                    existingCotis.setMod_pay(request.getMod_pay());

                    // Save the updated record
                    cotisMutPersRepository.save(existingCotis);
                } else {
                    // Record not found; add to the list
                    notFoundRecords.add("cod_soc: " + request.getCod_soc() +
                            ", mat_pers: " + request.getMat_pers() +
                            ", num_cot: " + request.getNum_cot());
                }
            }

            // If there are any records not found, include them in the response
            if (!notFoundRecords.isEmpty()) {
                return ResponseEntity.status(404).body("Some records were not found: " + notFoundRecords);
            }

            // Return success response after all updates
            return ResponseEntity.ok("All records updated successfully");

        } catch (Exception e) {
            // Log the exception for debugging
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating cotisations: " + e.getMessage());
        }
    }
    @PutMapping("/saveUpdatedRows")
    public ResponseEntity<?> saveUpdatedRows(@RequestBody List<CotisMutPers> updatedRows) {
        for (CotisMutPers row : updatedRows) {
            cotisMutPersRepository.save(row); // Assuming save will handle both insert and update
        }
        return ResponseEntity.ok("1");
    }

    @PostMapping("/saveUpdatedCotis")
    @Transactional
    public ResponseEntity<?> saveUpdatedCotis(@RequestBody CotisMutPers request) {
        try {

            CotisMutPersID id = new CotisMutPersID(request.getCod_soc(),request.getMat_pers(),request.getNum_cot());


            Optional<CotisMutPers> optional = cotisMutPersRepository.findById(id);

            if (optional.isPresent()) {
                CotisMutPers existingDemConv = optional.get();
                existingDemConv.setEtat_cot(request.getEtat_cot());
                CotisMutPers updatedDemConv = cotisMutPersRepository.save(existingDemConv);
                return ResponseEntity.ok(updatedDemConv);
            } else {
                System.out.println("No OffDemandeConv found for ID: " + id);
                return ResponseEntity.ok(null); // Indicate no update was performed
            }
        } catch (Exception e) {
            System.err.println("Error updating OffDemandeConv: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping("/getCotisationMatDate")
    List<CotisMutPers> getCotisationMatDate(@RequestParam String codSoc,
                                                     @RequestParam String matPers,
                                                     @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate datDeb,
                                                   @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate datFin) {
        return cotisMutPersRepository.getCotisationMatDate(codSoc,matPers,datDeb,datFin);
    }
    @GetMapping("/getCotisationMat")
    List<CotisMutPers> getCotisationMat(@RequestParam String codSoc,
                                        @RequestParam String matPers) {
        return cotisMutPersRepository.getCotisationMat(codSoc,matPers);
    }
    @PostMapping("/getCotisationMatDate")
    public ResponseEntity<?> getCotisationMatDate(@RequestBody CotisationDTO request) {
        // Process the request object
        return ResponseEntity.ok(cotisMutPersRepository.getCotisationMatDate(request.getCodSoc(),request.getMatPers(),request.getDatDeb(),request.getDatFin()));
    }
}