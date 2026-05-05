package com.arabsoft.reports.controllers;

import com.arabsoft.reports.services.PrintReportsService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Print")
@SuppressWarnings({ "java:S106" })
public class PrintReportsController {

    private final PrintReportsService printReportsService;

    @PostMapping("/pdf")
    public ResponseEntity<Resource> genererPDF(@RequestParam Long numRap, @RequestBody Map<String, String> params) {
        System.out.println("genererPDF @@@@@@ ");
        try {
            params.forEach((key, value) -> {
                System.out.println("Parametre : " + key + ", Valeur : " + value);
            });
            byte[] pdf = printReportsService.genererRapportPDF(numRap, params);
            if (pdf == null) {
                return ResponseEntity.internalServerError().build();
            }

            ByteArrayResource resource = new ByteArrayResource(pdf);
            String fileName = "rapport_" + System.currentTimeMillis() + ".pdf";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(pdf.length)
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/excel")
    public ResponseEntity<byte[]> genererExcel(@RequestParam Long numRap, @RequestBody Map<String, String> params) {
        try {
            byte[] excel = printReportsService.genererRapportExcel(numRap, params);
            if (excel == null || excel.length == 0) {
                return ResponseEntity.internalServerError().build();
            }

            String fileName = "rapport_" + System.currentTimeMillis() + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType
                            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .contentLength(excel.length)
                    .body(excel);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
