package com.arabsoft.gestionconvention.Services;

import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class DemandeConvService {

    private final JdbcTemplate jdbcTemplate;

    public DemandeConvService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


  
    public Map<String, Object> callProcessDemandeConvProc(
            String pCodSoc,
            String pMatPers,
            String pEtatOffDem,
            LocalDate pDatDemConv,
            LocalDate pDatOffDem
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Convert LocalDate to java.sql.Date
        Date sqlDatDemConv = Date.valueOf(pDatDemConv);
        Date sqlDatOffDem = Date.valueOf(pDatOffDem);

        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall(
                    "{call pk_convention.process_demande_conv(?, ?, ?, ?, ?, ?, ?, ?)}")) {

                // Input parameters
                callableStatement.setString(1, pCodSoc);
                callableStatement.setString(2, pMatPers);
                callableStatement.setString(3, pEtatOffDem);
                callableStatement.setDate(4, sqlDatDemConv);
                callableStatement.setDate(5, sqlDatOffDem);

                // Output parameters
                callableStatement.registerOutParameter(6, Types.DATE);     // p_dat_min_fin
                callableStatement.registerOutParameter(7, Types.DATE);     // p_dat_fin_off
                callableStatement.registerOutParameter(8, Types.VARCHAR);  // p_message

                // Execute the procedure
                callableStatement.execute();

                // Retrieve the output parameters
                Map<String, Object> result = new HashMap<>();

                // Convert the SQL Date to LocalDate and format it as dd/MM/yyyy
                java.sql.Date pDatMinFin = callableStatement.getDate(6);
                java.sql.Date pDatFinOff = callableStatement.getDate(7);

                // Formatting the output dates
                String formattedPDatMinFin = (pDatMinFin != null) ? pDatMinFin.toLocalDate().format(formatter) : null;
                String formattedPDatFinOff = (pDatFinOff != null) ? pDatFinOff.toLocalDate().format(formatter) : null;

                // Add the formatted values to the result map
                result.put("p_dat_min_fin", formattedPDatMinFin);
                result.put("p_dat_fin_off", formattedPDatFinOff);
                result.put("p_message", callableStatement.getString(8));

                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        });
    }
    public Integer getNextSeqOffDemandeConv(
            String codSoc,
            String matPers,
            String codConv,
            String codOff) {

        return jdbcTemplate.execute((ConnectionCallback<Integer>) connection -> {
            try (CallableStatement cs = connection.prepareCall(
                    "{call pk_convention.GET_NEXT_SEQ_OFF_DEMANDE_CONV(?, ?, ?, ?, ?)}")) {

                // Input parameters
                cs.setString(1, codSoc);
                cs.setString(2, matPers);
                cs.setString(3, codConv);
                cs.setString(4, codOff);

                // Output parameter
                cs.registerOutParameter(5, Types.INTEGER);

                // Execute the stored procedure
                cs.execute();

                // Return the output value
                return cs.getInt(5);

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to call GET_NEXT_SEQ_OFF_DEMANDE_CONV", e);
            }
        });
    }


}
