package com.tn.arabsoft.remboursement_frais_medicaux.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tn.arabsoft.remboursement_frais_medicaux.service.VirFichDataService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vir/fich")
public class VirFichDataController {

    private final VirFichDataService service;

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