package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S1141", "java:S6909" })
public class FileProcessingService {

    private final JdbcTemplate jdbcTemplate;

    public void loadFile(String[] lines, String codSoc) throws SQLException {
        DataSource dataSource = jdbcTemplate.getDataSource();

        if (dataSource == null) {
            throw new IllegalStateException("DataSource is not configured");
        }

        try (Connection conn = dataSource.getConnection()) {
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