package com.arabsoft.referentiel.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class RefirentielleService {
    @Autowired
    private DataSource dataSource;

    public void callStoredProcedure(String fileName) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmt = conn.prepareCall("{call REFERENTIELS.TRANS_REF_MED(?)}");
            stmt.setString(1, fileName); // pass just the file name
            stmt.execute();
        }
    }
}
