package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;

import com.tn.arabsoft.RemboursementFraisMedicaux.DTO.CnamFichRequest;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.CnamFichDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cnam-fich")
public class CnamFichController {
    private static final Logger log = LoggerFactory.getLogger(CnamFichController.class);

    @Autowired
    private CnamFichDataService service;

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateFile(@RequestBody CnamFichRequest request) {
        log.debug("Generating file with request: codSoc={}, codBord={}", request.getCodSoc(), request.getCodBord());
        try {
            Map<String, Object> response = service.generateCnamFichFile(request.getCodSoc(), request.getCodBord());
            if (response.containsKey("errorDetails")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating file for codSoc={}, codBord={}", request.getCodSoc(), request.getCodBord(), e);
            Map<String, Object> errorResponse = Map.of(
                    "message", "Error generating file: " + e.getMessage(),
                    "errorDetails", e.getClass().getSimpleName()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/download/{seq}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long seq) {
        log.debug("Downloading file for seq: {}", seq);
        try {
            byte[] fileContent = service.downloadCnamFichFile(seq);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cnam_fich_" + seq + ".txt")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContent);
        } catch (RuntimeException e) {
            log.error("Error downloading file for seq: {}", seq, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(("Error downloading file: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("Unexpected error downloading file for seq: {}", seq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Unexpected error downloading file: " + e.getMessage()).getBytes());
        }
    }
}
