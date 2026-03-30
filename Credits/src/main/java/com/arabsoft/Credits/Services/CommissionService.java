package com.arabsoft.Credits.Services;

import com.arabsoft.Credits.Entities.Response.ResponseProcedure;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Map;

@Service
public class CommissionService {
    @Autowired
    private JdbcTemplate jdbcTemplate;



    public ResponseProcedure prepar_commission(String wcodSoc, String wdatDebComm, String wdatFinComm, Integer  wnumComm) {

       /* java.sql.Date sqlWdatDebComm = (wdatDebComm != null && !wdatDebComm.isEmpty()) ? java.sql.Date.valueOf(wdatDebComm) : null;
        java.sql.Date sqlWdatFinComm = (wdatFinComm != null && !wdatFinComm.isEmpty()) ? java.sql.Date.valueOf(wdatFinComm) : null;
        System.out.println("sqlWdatDebComm :"+sqlWdatDebComm+"sqlWdatFinComm :"+sqlWdatFinComm);*/
        Date sqlWdatDebComm = convertToSqlDate(wdatDebComm);
        Date sqlWdatFinComm = convertToSqlDate(wdatFinComm);

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_gestion_credit.prepar_commission(?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setDate(2, sqlWdatDebComm); // IN parameter
                callableStatement.setDate(3, sqlWdatFinComm); // IN parameter
                callableStatement.setInt(4, wnumComm); // IN parameter
                callableStatement.registerOutParameter(5, Types.VARCHAR); // OUT parameter

                callableStatement.execute();
                String message=callableStatement.getString(5);
                ResponseProcedure responseProcedure= new ResponseProcedure();
                responseProcedure.setMessage(message);
                return responseProcedure;
            }
        });
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
