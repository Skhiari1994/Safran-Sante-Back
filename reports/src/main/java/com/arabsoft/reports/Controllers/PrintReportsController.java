package com.arabsoft.reports.Controllers;

import com.arabsoft.reports.Services.PrintReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/Print")
public class PrintReportsController {

    @Autowired
    PrintReportsService printReportsService;

    @PostMapping("/pdf")
    public ResponseEntity<Resource> genererPDF(@RequestParam Long numRap, @RequestBody Map<String, String> params) {
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
