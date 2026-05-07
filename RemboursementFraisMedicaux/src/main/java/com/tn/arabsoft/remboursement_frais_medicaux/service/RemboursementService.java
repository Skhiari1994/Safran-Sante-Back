package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseCloture;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseRegBord;

import lombok.RequiredArgsConstructor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S112" })
public class RemboursementService {

    private final JdbcTemplate jdbcTemplate;

    public void initialtionPlafond(String soc, String annee, String matDeb, String matFin) {

        jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection
                            .prepareCall("{call plafond_pkg.cal_plafond_mutuelle(?, ?, ?, ?)}");

                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, annee);
                    callableStatement.setString(3, matDeb);
                    callableStatement.setString(4, matFin);

                    return callableStatement;
                },
                Collections.emptyList());
    }

    public ReponseRegBord reglerBord(String wcodSoc, String codBord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call PK_BORD_ARRIVER.regler_bord(?, ?, ?, ?,?)}";
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    callableStatement.setString(1, wcodSoc);
                    callableStatement.setString(2, codBord);
                    callableStatement.registerOutParameter(3, Types.VARCHAR);
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // FIX: Patch missing reg_remb by copying from bult_soin
                    String fixSql = "update bult_arriver ba " +
                            "set ba.reg_remb = ( " +
                            "    select bs.reg_remb " +
                            "    from bult_soin bs " +
                            "    where bs.cod_soc = ba.cod_soc " +
                            "    and bs.mat_pers = ba.mat_pers " +
                            "    and bs.num_fam = ba.num_fam " +
                            "    and bs.dat_soin = ba.dat_soin " + // Corrected typo from os to bs
                            ") " +
                            "where ba.cod_bord = ? " +
                            "and ba.reg_remb is null";

                    jdbcTemplate.update(fixSql, codBord);

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

    public ReponseCloture clotureBord(String codSoc, String codAssur, String codBord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call pk_bord_arriver.cloture_bord(?, ?, ?, ?, ?)}"; // 3 IN + 2 OUT
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    // Paramètres IN
                    callableStatement.setString(1, codSoc);
                    callableStatement.setString(2, codAssur);
                    callableStatement.setString(3, codBord);

                    // Paramètres OUT
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // Récupération des paramètres OUT
                    String message = callableStatement.getString(4);
                    String validBord = callableStatement.getString(5);

                    return new ReponseCloture(message, validBord);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de cloture_bord : " + e.getMessage(), e);
        }
    }

    public ReponseCloture clotureBordVir(String codSoc, String codAssur, String codBord) {
        try {
            return jdbcTemplate.execute((Connection connection) -> {
                String procedureCall = "{call pk_bord_arriver.cloture_bord(?, ?, ?, ?, ?)}"; // 3 IN + 2 OUT
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    // Paramètres IN
                    callableStatement.setString(1, codSoc);
                    callableStatement.setString(2, codAssur);
                    callableStatement.setString(3, codBord);

                    // Paramètres OUT
                    callableStatement.registerOutParameter(4, Types.VARCHAR);
                    callableStatement.registerOutParameter(5, Types.VARCHAR);

                    callableStatement.execute();

                    // Récupération des paramètres OUT
                    String message = callableStatement.getString(4);
                    String validBord = callableStatement.getString(5);

                    return new ReponseCloture(message, validBord);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'exécution de cloture_bord : " + e.getMessage(), e);
        }
    }
}
