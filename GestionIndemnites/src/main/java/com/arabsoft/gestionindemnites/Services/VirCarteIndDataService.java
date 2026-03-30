package com.arabsoft.gestionindemnites.Services;

import com.arabsoft.gestionindemnites.Entities.VirCarteIndData;
import com.arabsoft.gestionindemnites.Repositories.VirCarteIndDataRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
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
public class VirCarteIndDataService {
    private static final Logger log = LoggerFactory.getLogger(VirCarteService.class);
    @Autowired
    private VirCarteIndDataRepository repository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> generateVirCarteFile(String soc, String datDebloc, String wcodLieuGeog, String wnatDon) {
        String procedureCall = "{call INDEMNITE.VIR_CARTE_IND(?, ?, ?, ?, ?)}"; // 4 IN, 1 OUT
        Map<String, Object> response = new HashMap<>();

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Set input parameters
            callableStatement.setString(1, soc);
            callableStatement.setString(2, datDebloc);
            callableStatement.setString(3, wcodLieuGeog);
            callableStatement.setString(4, wnatDon);

            // Register output parameter
            callableStatement.registerOutParameter(5, Types.VARCHAR); // message

            // Execute the procedure
            log.debug("Calling INDEMNITE.VIR_CARTE_IND with params: soc={}, datDebloc={}, wcodLieuGeog={}, wnatDon={}",
                    soc, datDebloc, wcodLieuGeog, wnatDon);
            callableStatement.execute();

            // Retrieve output parameter
            String message = callableStatement.getString(5);
            log.debug("Stored procedure output: message={}", message);

            // Query VIR_CARTE_IND_DATA for the latest seq_ if successful
            Long seq = null;
            if (message != null && message.toLowerCase().contains("fichier")) {

                // FIX query to match your real status (or remove filter first)
                String sql = "SELECT MAX(seq_) FROM VIR_CARTE_IND_DATA";

                seq = jdbcTemplate.queryForObject(sql, Long.class);

                log.debug("Retrieved seq: {}", seq);
            }


            // Populate response
            response.put("seq", seq);
            response.put("message", message != null ? message : "No message returned from stored procedure");

            return response;

        } catch (SQLException e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.toString();
            log.error("Error calling stored procedure INDEMNITE.VIR_CARTE_IND with params: soc={}, datDebloc={}, wcodLieuGeog={}, wnatDon={}",
                    soc, datDebloc, wcodLieuGeog, wnatDon, e);
            response.put("message", "Error calling stored procedure: " + errorMessage);
            response.put("errorDetails", e.getClass().getSimpleName());
            return response;
        }
    }
    public byte[] downloadVirCarteFile(Long seq) {
        log.debug("Fetching data for seq: {}", seq);
        String sql = "SELECT ligne, status FROM VIR_CARTE_IND_DATA WHERE seq_ = ? ORDER BY ligne";
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

        // Update status to PROCESSED
        String updateSql = "UPDATE VIR_CARTE_IND_DATA SET status = 'PROCESSED' WHERE seq_ = ?";
        int updated = jdbcTemplate.update(updateSql, seq);
        log.debug("Updated {} records to PROCESSED for seq: {}", updated, seq);

        return content.toString().getBytes(StandardCharsets.UTF_8);
    }
    @Transactional
    public void deleteAllVirCarteData() {
        repository.deleteAllData();
    }
}
