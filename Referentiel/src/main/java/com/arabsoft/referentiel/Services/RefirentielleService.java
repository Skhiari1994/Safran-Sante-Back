package com.arabsoft.referentiel.services;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefirentielleService {

    private final DataSource dataSource;

    public void callStoredProcedure(String fileName) throws SQLException {

        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall("{call REFERENTIELS.TRANS_REF_MED(?)}")) {
            stmt.setString(1, fileName);
            stmt.execute();
        }

    }

}
