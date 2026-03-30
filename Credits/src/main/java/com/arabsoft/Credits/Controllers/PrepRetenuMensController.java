package com.arabsoft.Credits.Controllers;

import com.arabsoft.Credits.Entities.DetRetenueMens;
import com.arabsoft.Credits.Entities.Response.ResponseProcedure;
import com.arabsoft.Credits.Entities.RetenuMensuel;
import com.arabsoft.Credits.Projections.DetRetenueMensProjection;
import com.arabsoft.Credits.Projections.RetenuMensuelProjection;
import com.arabsoft.Credits.Repositories.DetRetenueMensRepository;
import com.arabsoft.Credits.Repositories.RetenuMensuelRepository;
import com.arabsoft.Credits.Services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/PrepRetMens")
public class PrepRetenuMensController {

    @Autowired
    RetenuMensuelRepository retenuMensuelRepository;
    @Autowired
    DetRetenueMensRepository detRetenueMensRepository;
    @Autowired
    CreditService creditService;

    @GetMapping("/getRetMens")
    List<RetenuMensuelProjection> getRetMens(@RequestParam String soc, @RequestParam String dat){
        return retenuMensuelRepository.getRetenuMens(soc,dat);
    }
    @GetMapping("/getRetMensVald")
    List<RetenuMensuelProjection> getRetMensVald(@RequestParam String soc, @RequestParam String dat){
        YearMonth ym = YearMonth.parse(dat, DateTimeFormatter.ofPattern("MM/yyyy"));
        LocalDate lastDay = ym.atEndOfMonth();
        Date sqlDate = java.sql.Date.valueOf(lastDay);

        return retenuMensuelRepository.getRetenuMensValdDate(soc, sqlDate);
    }
    @GetMapping("/getDetRetMens")
    List<DetRetenueMensProjection> getDetRetMens(@RequestParam String soc, @RequestParam String mat, @RequestParam String dat, @RequestParam String abrv){
        return detRetenueMensRepository.getDetRetenueMens(soc,mat,dat,abrv);
    }

    @GetMapping("/chargeRetMens")
    public void chargeRetMens(@RequestParam String soc,
                              @RequestParam String dat,
                              @RequestParam String mat) {
        creditService.CHARGEMENT_RETENUE_MENSUEL(soc, dat, mat);
    }

    @GetMapping("/validRetMens")
    void validRetMens(@RequestParam String dat){
        retenuMensuelRepository.validRetMens(dat);
    }

    @GetMapping("/vir_carte_ret")
    ResponseProcedure vir_carte_ret(@RequestParam String soc, @RequestParam String dat){
        return creditService.vir_carte_ret(soc,dat);
    }

    @GetMapping("/vir_carte_aux")
    ResponseProcedure vir_carte_aux(@RequestParam String soc, @RequestParam String dat){
        return creditService.vir_carte_aux(soc,dat);
    }
    @GetMapping("/download-file")
    public ResponseEntity<FileSystemResource> downloadFile(@RequestParam String fileName) {
        // 1. Define a temporary directory for generated files
        String tempDir = "Z:/temp_virements/";
        File dir = new File(tempDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = tempDir + fileName;

        // 2. Call service to generate the specific file filtered by category
        creditService.generateVirCarteFile(filePath, fileName);

        // 3. Verify file exists
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 4. Prepare Response with correct headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        headers.add(HttpHeaders.CONTENT_TYPE, "text/plain");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }
    @GetMapping("/getRetMensValdO")
    List<RetenuMensuelProjection> getRetMensValdO(@RequestParam String soc, @RequestParam String dat){
        return retenuMensuelRepository.getRetenuMensValdO(soc,dat);
    }


    @PostMapping("/generateRetenue")
    public ResponseEntity<byte[]> generateFiles(
            @RequestParam String soc,
            @RequestParam  LocalDate datRet
    ) throws IOException {

        byte[] zip = creditService.generateVirCarteFiles(soc, datRet);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=VIR_CARTE_" + datRet.format(DateTimeFormatter.ofPattern("MMyyyy")) + ".zip")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(zip);
    }
    @GetMapping("/getRetMensByType")
    List<RetenuMensuelProjection> getRetMensByType(@RequestParam String soc, @RequestParam String dat,
                                                   @RequestParam String type) {
        return retenuMensuelRepository.getRetenuMensByType(soc, dat, type);
    }
}
