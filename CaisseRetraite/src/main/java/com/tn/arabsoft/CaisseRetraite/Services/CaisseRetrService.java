package com.tn.arabsoft.CaisseRetraite.Services;

import com.tn.arabsoft.CaisseRetraite.DTO.ResponseProcedure;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.RepCalculCotis;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ReponseCalculPrimeRetr;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ReponseProcedure;
import jakarta.transaction.Transactional;
import oracle.jdbc.OracleConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaisseRetrService {

    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ReponseProcedure verif_lig_cotisation_retr(String wcodSoc, String mat, String datDeb, String datFin) {
        Date sqlWdatDeb = (datDeb != null && !datDeb.isEmpty()) ? convertToSqlDate(datDeb) : null;
        Date sqlWdatFin = (datFin != null && !datFin.isEmpty()) ? convertToSqlDate(datFin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_retraite.verif_lig_cotisation_retr(?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, mat); // IN parameter
                callableStatement.setDate(3, sqlWdatDeb); // IN parameter
                callableStatement.setDate(4, sqlWdatFin); // IN parameter
                callableStatement.registerOutParameter(5, Types.VARCHAR); // OUT parameter for message

                // Execute the procedure
                callableStatement.execute();

                String message = callableStatement.getString(5);

                ReponseProcedure responseProcedure = new ReponseProcedure();
                 responseProcedure.setMessage(message);

                return responseProcedure;
            } catch (SQLException e) {
                throw new RuntimeException("Error while executing stored procedure: " + e.getMessage(), e);
            }
        });
    }

    public RepCalculCotis calcul_cotisation(String wcodSoc, String mat,String datDeb, String datFin, BigDecimal brut_) {

        Date sqlWdatDeb = (datDeb != null && !datDeb.isEmpty()) ? convertToSqlDate(datDeb) : null;
        Date sqlWdatFin = (datFin != null && !datFin.isEmpty()) ? convertToSqlDate(datFin) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_retraite.calcul_cotisation_retr(?, ?, ?, ?,?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, mat);
                callableStatement.setDate(3, sqlWdatDeb);
                callableStatement.setDate(4, sqlWdatFin);
                callableStatement.setBigDecimal(5, brut_);

                callableStatement.registerOutParameter(6, Types.INTEGER); // OUT parameter for message

                // Execute the procedure
                callableStatement.execute();

                BigDecimal cot = callableStatement.getBigDecimal(6);

                RepCalculCotis responseProcedure = new RepCalculCotis();
                responseProcedure.setCot(cot);

                return responseProcedure;
            } catch (SQLException e) {
                throw new RuntimeException("Error while executing stored procedure: " + e.getMessage(), e);
            }
        });
    }

    public void maj_lig_cotisation_retr(String wcodSoc, String mat, Long num, String datDeb, String datFin, BigDecimal cot_) {

        Date sqlWdatDeb = (datDeb != null && !datDeb.isEmpty()) ? convertToSqlDate(datDeb) : null;
        Date sqlWdatFin = (datFin != null && !datFin.isEmpty()) ? convertToSqlDate(datFin) : null;

        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_retraite.maj_lig_cotisation_retr(?, ?, ?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, mat);
                callableStatement.setLong(3, num);
                callableStatement.setDate(4, sqlWdatDeb);
                callableStatement.setDate(5, sqlWdatFin);
                callableStatement.setBigDecimal(6, cot_);

                callableStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'exécution de la procédure : " + e.getMessage(), e);
            }
            return null; // obligatoire pour respecter la signature de la lambda (type `ConnectionCallback<Void>`)
        });
    }

    public void chargement_pers_prime(String wcodSoc, String mois, String mat) {


        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_retraite.chargement_pers_prime(?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                callableStatement.setString(1, wcodSoc);
                callableStatement.setString(2, mois);
                callableStatement.setString(3, mat);

                callableStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'exécution de la procédure : " + e.getMessage(), e);
            }
            return null;
        });
    }

    public int maj_cotisation(String soc, String mat, String mois, String corps) {

        return jdbcTemplate.execute((Connection connection) -> {

            String sql = "{call pk_retraite.maj_cotisation(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = connection.prepareCall(sql)) {

                cs.setString(1, soc);
                cs.setString(2, mat);
                cs.setString(3, mois);
                cs.setString(4, corps);
                cs.registerOutParameter(5, Types.INTEGER);

                cs.execute();

                return cs.getInt(5);
            }
        });
    }

    public ReponseCalculPrimeRetr calcul_prime_retraite(String benef_prime, String benef_droi,BigDecimal taux_prime,String dat_remb, String mat_pers, String soc) {

        Date sqlWdatDeb = (dat_remb != null && !dat_remb.isEmpty()) ? convertToSqlDate(dat_remb) : null;

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call pk_retraite.calcul_prime_retraite(?, ?, ?, ?,?,?,?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                // Set input parameters
                callableStatement.setString(1, benef_prime);
                callableStatement.setString(2, benef_droi);
                callableStatement.setBigDecimal(3, taux_prime);
                callableStatement.setDate(4, sqlWdatDeb);
                callableStatement.setString(5, mat_pers);
                callableStatement.setString(6, soc);
                callableStatement.registerOutParameter(7, Types.NUMERIC);
                callableStatement.registerOutParameter(8, Types.NUMERIC);
                callableStatement.registerOutParameter(9, Types.NUMERIC);
                callableStatement.registerOutParameter(10, Types.DATE);
                callableStatement.registerOutParameter(11, Types.DATE);
                callableStatement.execute();

                BigDecimal mntPrime = callableStatement.getBigDecimal(7);
                BigDecimal mntCotis = callableStatement.getBigDecimal(8);
                BigDecimal mntTotal = callableStatement.getBigDecimal(9);
                LocalDate dat_deb = callableStatement.getDate(10) != null ? callableStatement.getDate(10).toLocalDate() : null;
                LocalDate dat_fin = callableStatement.getDate(11) != null ? callableStatement.getDate(11).toLocalDate() : null;

                ReponseCalculPrimeRetr responseProcedure = new ReponseCalculPrimeRetr();
                responseProcedure.setP_montant_prime(mntPrime);
                responseProcedure.setP_montant_cotis(mntCotis);
                responseProcedure.setP_montant_total(mntTotal);
                responseProcedure.setP_dat_deb_ret(dat_deb);
                responseProcedure.setP_dat_fin_ret(dat_fin);

                return responseProcedure;
            } catch (SQLException e) {
                throw new RuntimeException("Error while executing stored procedure: " + e.getMessage(), e);
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

    @Transactional
    public ResponseProcedure lireFichierEtAppelerProcedure(
            String soc,
            String mois,
            MultipartFile file
    ) throws Exception {

        ResponseProcedure resp = new ResponseProcedure();
        int totalLines = 0;

        // Liste pour stocker toutes les lignes du CSV
        List<String> lignes = new ArrayList<>();

        // Lecture du fichier CSV
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            // Ignorer l'entête
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("FIN")) {
                    break;
                }
                lignes.add(line);
                totalLines++;
            }
        }

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            // 1 Initialisation (équivalent DELETE + COMMIT)
            try (CallableStatement csInit =
                         conn.prepareCall("{ call PK_GESTION_CREDIT.init_lecture_disk_pret(?) }")) {
                csInit.setString(1, mois);
                csInit.execute();
            }

            // 2 Préparer l’ARRAY Oracle pour le PL/SQL
            OracleConnection oracleConn = conn.unwrap(OracleConnection.class);
            Array oracleArray = oracleConn.createOracleArray(
                    "SYS.ODCIVARCHAR2LIST",
                    lignes.toArray(new String[0])
            );

            // 3 Appel de la procédure lecture_disk_pret
            try (CallableStatement cs =
                         conn.prepareCall("{ call PK_GESTION_CREDIT.lecture_disk_pret(?,?,?,?) }")) {

                cs.setString(1, soc);
                cs.setString(2, mois);
                cs.setArray(3, oracleArray);
                cs.registerOutParameter(4, Types.INTEGER); // Nombre de lignes insérées

                cs.execute();

                int insertedLines = cs.getInt(4);
                resp.setInsertedLines(insertedLines);
            }

            // 4 Commit final
            conn.commit();

            // 5 Préparer la réponse
            resp.setMessage("Import terminé avec succès");
            resp.setTotalLines(totalLines);

            return resp;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'import du fichier", e);
        }
    }



}
