package com.arabsoft.Credits.Services;


import com.arabsoft.Credits.Entities.VirCartePretData;
import com.arabsoft.Credits.Repositories.VirCartePretDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VirCartePretDataService {
    private static final Logger log = LoggerFactory.getLogger(VirCartePretDataService.class);

    @Autowired
    private VirCartePretDataRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public Map<String, Object> generateVirCartePretFile(String soc, String datDebloc, String numComm,String nameFile) {
        String procedureCall = "{call PK_GESTION_CREDIT.VIR_CARTE_PRET(?, ?, ?, ?, ?,?)}"; // 3 IN, 2 OUT
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, soc);
            callableStatement.setString(2, datDebloc);
            callableStatement.setString(3, numComm);
            callableStatement.setString(4,nameFile);

            // Register output parameters
            callableStatement.registerOutParameter(5, Types.BIGINT); // Changed to BIGINT for NUMBER
            callableStatement.registerOutParameter(6, Types.VARCHAR); // message

            // Execute the procedure
            log.debug("Calling PRET.VIR_CARTE_PRET with params: soc={}, datDebloc={}, numComm={}", soc, datDebloc, numComm);
            callableStatement.execute();

            // Retrieve output parameters
            Long seq = callableStatement.getLong(5); // Use getLong for NUMBER
            String message = callableStatement.getString(6);
            log.debug("Stored procedure output: seq={}, message={}", seq, message);

            // Populate response
            response.put("seq", seq);
            response.put("message", message != null ? message : "No message returned from stored procedure");

            return response;

        } catch (SQLException e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.toString();
            log.error("Error calling stored procedure PRET.VIR_CARTE_PRET with params: soc={}, datDebloc={}, numComm={}",
                    soc, datDebloc, numComm, e);
            response.put("message", "Error calling stored procedure: " + errorMessage);
            response.put("errorDetails", e.getClass().getSimpleName());
            return response;
        }
    }

    public byte[] downloadVirCartePretFile(Long seq) {
        log.debug("Fetching data for seq: {}", seq);
        String sql = "SELECT ligne, status FROM VIR_CARTE_PRET_DATA WHERE seq_ = ? ORDER BY ligne";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, seq);

        if (rows.isEmpty()) {
            log.warn("No data found for sequence: {}", seq);
            throw new RuntimeException("No data found for sequence: " + seq);
        }

        log.debug("Retrieved {} records for seq: {}", rows.size(), seq);
        StringBuilder content = new StringBuilder();
        for (Map<String, Object> row : rows) {
            String ligne = (String) row.get("ligne");
            if (ligne == null) {
                log.warn("Null ligne found for seq: {}, skipping record", seq);
                continue;
            }
            content.append(ligne).append("\n");
        }

        if (content.length() == 0) {
            log.error("No valid data to generate file for seq: {}", seq);
            throw new RuntimeException("No valid data to generate file for seq: " + seq);
        }

        // Update status using JdbcTemplate to avoid JPA issues
        String updateSql = "UPDATE VIR_CARTE_PRET_DATA SET status = 'PROCESSED' WHERE seq_ = ?";
        int updated = jdbcTemplate.update(updateSql, seq);
        log.debug("Updated {} records to PROCESSED for seq: {}", updated, seq);

        return content.toString().getBytes(StandardCharsets.UTF_8);
    }
}