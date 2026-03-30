package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseCloture;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseRegBord;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseReglerBord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class RemboursementService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void InitialtionPlafond(String soc, String annee, String mat_deb, String mat_fin) {

        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection
                            .prepareCall("{CALL plafond_pkg.cal_plafond_mutuelle(?, ?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, annee);
                    callableStatement.setString(3, mat_deb);
                    callableStatement.setString(4, mat_fin);

                    return callableStatement;
                },
                Collections.emptyList());

    }

    public ReponseRegBord regler_bord(String wcodSoc, String cod_bord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call PK_BORD_ARRIVER.regler_bord(?, ?, ?, ?,?)}";
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    callableStatement.setString(1, wcodSoc);
                    callableStatement.setString(2, cod_bord);
                    callableStatement.registerOutParameter(3, Types.VARCHAR);
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // FIX: Patch missing reg_remb by copying from bult_soin
                    String fixSql = "UPDATE bult_arriver ba " +
                            "SET ba.reg_remb = ( " +
                            "    SELECT bs.reg_remb " +
                            "    FROM bult_soin bs " +
                            "    WHERE bs.cod_soc = ba.cod_soc " +
                            "    AND bs.mat_pers = ba.mat_pers " +
                            "    AND bs.num_fam = ba.num_fam " +
                            "    AND bs.dat_soin = ba.dat_soin " + // Corrected typo from os to bs
                            ") " +
                            "WHERE ba.cod_bord = ? " +
                            "AND ba.reg_remb IS NULL";

                    jdbcTemplate.update(fixSql, cod_bord);

                    return new ReponseRegBord(
                            callableStatement.getString(3),
                            callableStatement.getString(4),
                            callableStatement.getString(5));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de regler_bord : " + e.getMessage(), e);
        }
    }

    public ReponseCloture cloture_bord(String cod_soc, String cod_assur, String cod_bord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call pk_bord_arriver.cloture_bord(?, ?, ?, ?, ?)}"; // 3 IN + 2 OUT
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    // Paramètres IN
                    callableStatement.setString(1, cod_soc);
                    callableStatement.setString(2, cod_assur);
                    callableStatement.setString(3, cod_bord);

                    // Paramètres OUT
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // Récupération des paramètres OUT
                    String message = callableStatement.getString(4);
                    String valid_bord = callableStatement.getString(5);

                    return new ReponseCloture(message, valid_bord);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de cloture_bord : " + e.getMessage(), e);
        }
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
    public ReponseCloture cloture_bord_vir(String cod_soc, String cod_assur, String cod_bord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call pk_bord_arriver.cloture_bord(?, ?, ?, ?, ?)}"; // 3 IN + 2 OUT
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    // Paramètres IN
                    callableStatement.setString(1, cod_soc);
                    callableStatement.setString(2, cod_assur);
                    callableStatement.setString(3, cod_bord);

                    // Paramètres OUT
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // Récupération des paramètres OUT
                    String message = callableStatement.getString(4);
                    String valid_bord = callableStatement.getString(5);

                    return new ReponseCloture(message, valid_bord);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de cloture_bord : " + e.getMessage(), e);
        }
    }
}
