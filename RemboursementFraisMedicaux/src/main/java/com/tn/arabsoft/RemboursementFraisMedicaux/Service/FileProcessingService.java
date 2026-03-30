package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class FileProcessingService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void loadFile(String[] lines, String codSoc) throws SQLException {
        try (Connection conn = jdbcTemplate.getDataSource().getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Process each line
                for (String line : lines) {
                    try (CallableStatement stmt = conn.prepareCall("{call PK_BORD_ARRIVER.charger_fich_cnam(?, ?)}")) {
                        stmt.setString(1, line);
                        stmt.setString(2, codSoc);
                        stmt.execute();
                    } catch (SQLException e) {
                        throw new SQLException("Error loading line: " + line + " - " + e.getMessage(), e);
                    }
                }
                // Process staging data
                try (CallableStatement stmt = conn.prepareCall("{call process_staging_data}")) {
                    stmt.execute();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}