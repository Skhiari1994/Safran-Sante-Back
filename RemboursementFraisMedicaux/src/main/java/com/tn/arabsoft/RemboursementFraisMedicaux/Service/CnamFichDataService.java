package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.BordEnvoiRepository;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.CnamFichDataRepository;
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
public class CnamFichDataService {
    private static final Logger log = LoggerFactory.getLogger(CnamFichDataService.class);

    @Autowired
    private CnamFichDataRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BordEnvoiRepository bordEnvoiRepository;


    public Map<String, Object> generateCnamFichFile(String codSoc, String codBord) {
        String procedureCall = "{call PK_BORD_ARRIVER.PREP_FICH_CNAM(?, ?, ?, ?)}"; // 2 IN, 2 OUT
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, codSoc);
            callableStatement.setString(2, codBord);

            // Register output parameters
            callableStatement.registerOutParameter(3, Types.BIGINT); // seq_
            callableStatement.registerOutParameter(4, Types.VARCHAR); // message

            // Execute the procedure
            log.debug("Calling CNAM.PREP_FICH_CNAM with params: codSoc={}, codBord={}", codSoc, codBord);
            callableStatement.execute();

            // Retrieve output parameters
            Long seq = callableStatement.getLong(3);
            String message = callableStatement.getString(4);
            log.debug("Stored procedure output: seq={}, message={}", seq, message);

            // Update ENVOI_BORD from 'N' to 'O' after successful procedure execution
            updateEnvoiBord(codSoc, codBord);

            // Populate response
            response.put("seq", seq);
            response.put("message", message != null ? message : "No message returned from stored procedure");

            return response;

        } catch (SQLException e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.toString();
            log.error("Error calling stored procedure CNAM.PREP_FICH_CNAM with params: codSoc={}, codBord={}",
                    codSoc, codBord, e);
            response.put("message", "Error calling stored procedure: " + errorMessage);
            response.put("errorDetails", e.getClass().getSimpleName());
            return response;
        }
    }

    private void updateEnvoiBord(String codSoc, String codBord) {
        String updateSql = "UPDATE BORD_ENVOI SET ENVOI_BORD = 'O' WHERE COD_SOC = ? AND COD_BORD = ?";
        try {
            int updated = jdbcTemplate.update(updateSql, codSoc, codBord);
            log.debug("Updated ENVOI_BORD to 'O' for {} record(s) with codSoc={}, codBord={}",
                    updated, codSoc, codBord);

            if (updated == 0) {
                log.warn("No records found to update ENVOI_BORD for codSoc={}, codBord={}", codSoc, codBord);
            }
        } catch (Exception e) {
            log.error("Error updating ENVOI_BORD for codSoc={}, codBord={}", codSoc, codBord, e);
            // Depending on your requirements, you might want to throw this exception
            // or handle it differently
        }
    }
    public byte[] downloadCnamFichFile(Long seq) {
        log.debug("Fetching data for seq: {}", seq);
        String sql = "SELECT ligne, status FROM CNAM_FICH_DATA WHERE seq_ = ? ORDER BY ligne";
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

        String updateSql = "UPDATE CNAM_FICH_DATA SET status = 'PROCESSED' WHERE seq_ = ?";
        int updated = jdbcTemplate.update(updateSql, seq);
        log.debug("Updated {} records to PROCESSED for seq: {}", updated, seq);

        return content.toString().getBytes(StandardCharsets.UTF_8);
    }
}
