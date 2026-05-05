package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.VirFichData;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.VirFichDataRepository;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class VirFichDataService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private VirFichDataRepository repository;

    @Transactional
    public Map<String, Object> generateVirFile(String codSoc, String codBord) {
        System.out.println("Generating file with codSoc=" + codSoc + ", codBord=" + codBord);
        Map<String, Object> result = new HashMap<>();
        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                CallableStatement stmt = conn.prepareCall("{call PK_BORD_ARRIVER.vir_bord_download(?, ?, ?, ?)}")) {
            stmt.setString(1, codSoc);
            stmt.setString(2, codBord);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            Long seq = stmt.getLong(3);
            String message = stmt.getString(4);
            result.put("seq", seq);
            result.put("message", message);
            System.out.println("Generated: seq=" + seq + ", message=" + message);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("error", "Error generating file: " + e.getMessage());
            System.out.println("Generation error: " + sw.toString());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> downloadVirFile(Long seq) {
        System.out.println("Downloading file for seq=" + seq);
        Long rowCount = repository.countByIdSeq(seq);
        System.out.println("Found " + rowCount + " rows for seq=" + seq);
        List<VirFichData> records = repository.findByIdSeqOrderByLigne(seq);
        System.out.println("Retrieved " + records.size() + " records");
        for (VirFichData record : records) {
            System.out.println("Record: "
                    + record.getId().getLigne().substring(0, Math.min(50, record.getId().getLigne().length())));
        }
        if (records.isEmpty()) {
            throw new RuntimeException("No file found for seq: " + seq);
        }
        StringBuilder content = new StringBuilder();
        String fileName = records.get(0).getFileName();
        for (VirFichData record : records) {
            content.append(record.getId().getLigne()).append("\n");
        }
        byte[] contentBytes = content.toString().getBytes();
        System.out.println("Prepared file with " + records.size() + " lines, " + contentBytes.length + " bytes");
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("content", new ByteArrayResource(contentBytes));
        return response;
    }

    @Transactional
    public Map<String, Object> generateVirFileCnam(String codSoc, String codBord) {
        System.out.println("Generating file with codSoc=" + codSoc + ", codBord=" + codBord);
        Map<String, Object> result = new HashMap<>();
        String procedureName = codBord != null && codBord.trim().toUpperCase().startsWith("L")
                ? "vir_bord_libre"
                : "vir_bord";
        String callStmt = "{call PK_BORD_ARRIVER." + procedureName + "(?, ?, ?, ?)}";

        try (Connection conn = jdbcTemplate.getDataSource().getConnection();
                CallableStatement stmt = conn.prepareCall(callStmt)) {
            stmt.setString(1, codSoc);
            stmt.setString(2, codBord);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            Long seq = stmt.getLong(3);
            String message = stmt.getString(4);
            result.put("seq", seq);
            result.put("message", message);
            System.out.println("Generated using " + procedureName + ": seq=" + seq + ", message=" + message);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("error", "Error generating file: " + e.getMessage());
            System.out.println("Generation error: " + sw.toString());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> downloadVirFileCnam(Long seq) {
        System.out.println("Downloading file for seq=" + seq);
        Long rowCount = repository.countByIdSeq(seq);
        System.out.println("Found " + rowCount + " rows for seq=" + seq);
        List<VirFichData> records = repository.findByIdSeqOrderByLigne(seq);
        System.out.println("Retrieved " + records.size() + " records");
        for (VirFichData record : records) {
            System.out.println("Record: "
                    + record.getId().getLigne().substring(0, Math.min(50, record.getId().getLigne().length())));
        }
        if (records.isEmpty()) {
            throw new RuntimeException("No file found for seq: " + seq);
        }
        StringBuilder content = new StringBuilder();
        String fileName = records.get(0).getFileName();
        for (VirFichData record : records) {
            content.append(record.getId().getLigne()).append("\n");
        }
        byte[] contentBytes = content.toString().getBytes();
        System.out.println("Prepared file with " + records.size() + " lines, " + contentBytes.length + " bytes");
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("content", new ByteArrayResource(contentBytes));
        return response;
    }
}