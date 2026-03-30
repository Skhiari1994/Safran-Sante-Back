package com.arabsoft.gestionconvention.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Map;

@Service
public class ConventionService {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public void maj_convention(String soc, String mois, String corps, String mat) {

        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL pk_convention.maj_convention(?, ?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, mois);
                    callableStatement.setString(3, corps);
                    callableStatement.setString(4, mat);

                    return callableStatement;
                },
                Collections.emptyList()
        );



    }


    private Date convertToSqlDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null; // Return null if the date string is null or empty
        }

        try {
            // Define the date format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Parse the string into a LocalDate
            LocalDate localDate = LocalDate.parse(dateStr, formatter);

            // Convert to java.sql.Date
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr + ". Expected format: dd/MM/yyyy", e);
        }
    }

}
