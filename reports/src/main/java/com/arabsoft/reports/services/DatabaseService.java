package com.arabsoft.reports.services;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isSqlServer() {
        try (Connection connection = dataSource.getConnection()) {

            String databaseProduct = connection.getMetaData().getDatabaseProductName();

            return databaseProduct.toLowerCase().contains("sql server");

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Impossible de déterminer le type de base de données", e);
        }
    }

}
