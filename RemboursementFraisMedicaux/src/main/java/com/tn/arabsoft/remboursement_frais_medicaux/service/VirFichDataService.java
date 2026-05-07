package com.tn.arabsoft.remboursement_frais_medicaux.service;

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

import javax.sql.DataSource;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S112", "java:S4144" })
public class VirFichDataService {

    private final JdbcTemplate jdbcTemplate;
    private final VirFichDataRepository repository;

    @Transactional
    public Map<String, Object> generateVirFile(String codSoc, String codBord) {

        Map<String, Object> result = new HashMap<>();

        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured");
        }

        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall("{call pk_bord_arriver.vir_bord_download(?, ?, ?, ?)}")) {

            stmt.setString(1, codSoc);
            stmt.setString(2, codBord);
            stmt.registerOutParameter(3, Types.NUMERIC);
            stmt.registerOutParameter(4, Types.VARCHAR);
            stmt.execute();
            Long seq = stmt.getLong(3);
            String message = stmt.getString(4);
            result.put("seq", seq);
            result.put("message", message);

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("error", "Error generating file: " + e.getMessage());
        }

        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> downloadVirFile(Long seq) {

        List<VirFichData> records = repository.findByIdSeqOrderByLigne(seq);

        if (records.isEmpty()) {
            throw new RuntimeException("No file found for seq: " + seq);
        }

        StringBuilder content = new StringBuilder();
        String fileName = records.get(0).getFileName();

        for (VirFichData virRecord : records) {
            content.append(virRecord.getId().getLigne()).append("\n");
        }

        byte[] contentBytes = content.toString().getBytes();
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("content", new ByteArrayResource(contentBytes));
        return response;
    }

    @Transactional
    public Map<String, Object> generateVirFileCnam(String codSoc, String codBord) {
        Map<String, Object> result = new HashMap<>();
        String procedureName = codBord != null && codBord.trim().toUpperCase().startsWith("L")
                ? "vir_bord_libre"
                : "vir_bord";
        String callStmt = "{call PK_BORD_ARRIVER." + procedureName + "(?, ?, ?, ?)}";

        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured");
        }

        try (Connection conn = dataSource.getConnection();
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
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result.put("error", "Error generating file: " + e.getMessage());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> downloadVirFileCnam(Long seq) {
        List<VirFichData> records = repository.findByIdSeqOrderByLigne(seq);

        if (records.isEmpty()) {
            throw new RuntimeException("No file found for seq: " + seq);
        }

        StringBuilder content = new StringBuilder();
        String fileName = records.get(0).getFileName();
        for (VirFichData virRecord : records) {
            content.append(virRecord.getId().getLigne()).append("\n");
        }

        byte[] contentBytes = content.toString().getBytes();
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("content", new ByteArrayResource(contentBytes));
        return response;
    }
}