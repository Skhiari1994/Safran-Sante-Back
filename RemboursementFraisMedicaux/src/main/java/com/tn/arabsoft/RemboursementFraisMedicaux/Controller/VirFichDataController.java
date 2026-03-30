package com.tn.arabsoft.RemboursementFraisMedicaux.Controller;
import com.tn.arabsoft.RemboursementFraisMedicaux.Service.VirFichDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/vir/fich")
public class VirFichDataController {
    @Autowired
    private VirFichDataService service;

    @PostMapping("/generate")
    public Map<String, Object> generateVirFile(@RequestBody Map<String, String> request) {
        String codSoc = request.get("codSoc");
        String codBord = request.get("codBord");
        return service.generateVirFile(codSoc, codBord);
    }

    @GetMapping("/download/{seq}")
    public ResponseEntity<ByteArrayResource> downloadVirFile(@PathVariable Long seq) {
        Map<String, Object> fileData = service.downloadVirFile(seq);
        ByteArrayResource resource = (ByteArrayResource) fileData.get("content");
        System.out.println("data is gen  :"+fileData);
        String fileName = (String) fileData.get("fileName");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @PostMapping("/generateCnam")
    public Map<String, Object> generateVirFileCnam(@RequestBody Map<String, String> request) {
        String codSoc = request.get("codSoc");
        String codBord = request.get("codBord");
        return service.generateVirFileCnam(codSoc, codBord);
    }

    @GetMapping("/downloadCnam/{seq}")
    public ResponseEntity<ByteArrayResource> downloadVirFileCnam(@PathVariable Long seq) {
        Map<String, Object> fileData = service.downloadVirFileCnam(seq);
        ByteArrayResource resource = (ByteArrayResource) fileData.get("content");
        String fileName = (String) fileData.get("fileName");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(resource.contentLength())
                .body(resource);
    }
}