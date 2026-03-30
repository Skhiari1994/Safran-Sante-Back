package com.arabsoft.gestionindemnites.Controllers;
import com.arabsoft.gestionindemnites.DTO.VirCarteFileResponse;
import com.arabsoft.gestionindemnites.DTO.VirCarteIndRequest;
import com.arabsoft.gestionindemnites.Services.VirCarteIndDataService;
import com.arabsoft.gestionindemnites.Services.VirCarteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/virement")
public class VirCarteController {

    private final VirCarteService virCarteService;
    private static final Logger log = LoggerFactory.getLogger(VirCarteService.class);

    public VirCarteController(VirCarteService virCarteService) {
        this.virCarteService = virCarteService;
    }
    @Autowired
    private VirCarteIndDataService service;

    @PostMapping("/generate")
    public VirCarteFileResponse generateVirCarteFile(@RequestBody VirCarteIndRequest request) {
        Map<String, Object> result = service.generateVirCarteFile(
                request.getSoc(),
                request.getDatDebloc(),
                request.getCodLieuGeog(),
                request.getNatDon()
        );

        byte[] fileBytes = service.downloadVirCarteFile((Long) result.get("seq"));
        String base64Content = Base64.getEncoder().encodeToString(fileBytes);

        VirCarteFileResponse response = new VirCarteFileResponse();
        response.setMessage((String) result.get("message"));
        response.setSeq((Long) result.get("seq"));
        response.setFileContent(base64Content);

        return response;
    }


    @GetMapping("/download/{seq}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long seq) {
        log.debug("Downloading file for seq: {}", seq);
        try {
            byte[] fileContent = service.downloadVirCarteFile(seq);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vir_carte_" + seq + ".txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContent);
        } catch (Exception e) {
            log.error("Error downloading file for seq: {}", seq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error downloading file: " + e.getMessage()).getBytes());
        }
    }
    @GetMapping("/vir-scol")
   public void callVirCarte(@RequestParam String soc,
                               @RequestParam String datDebloc,
                            @RequestParam(required = false) String wcodLieuGeog,
                            @RequestParam(required = false) String wnatDon) {
          virCarteService.processVirCarte(soc, datDebloc, wcodLieuGeog, wnatDon);
    }
    @GetMapping("/download-file")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam(defaultValue = "F:/virements.txt") String filePath) {
        // Générer le fichier
        virCarteService.generateVirementFile(filePath);

        // Vérifier si le fichier existe
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Préparer la réponse HTTP avec le fichier
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllVirCarteData() {
        service.deleteAllVirCarteData();
        return ResponseEntity.ok("Toutes les données de VIR_CARTE_DATA ont été supprimées avec succès.");
    }
}
