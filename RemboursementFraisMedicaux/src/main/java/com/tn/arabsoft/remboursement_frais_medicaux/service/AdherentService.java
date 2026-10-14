
package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;

import lombok.RequiredArgsConstructor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

@Service
@RequiredArgsConstructor
public class AdherentService {

    private final JdbcTemplate jdbcTemplate;

    private static final String PKG_PLAFOND = "plafond_pkg";

    public ResponseProcedure calPlafondMutuelle(
            String wcodSoc,
            Long annee,
            String matDeb,
            String matFin) {

        return jdbcTemplate.execute((Connection connection) -> {

            String call = getCallSyntax(connection, PKG_PLAFOND, "cal_plafond_mutuelle", 5);
            System.out.println("wcodSoc: " + wcodSoc);
            System.out.println("annee: " + annee);
            System.out.println("matDeb: " + matDeb);
            System.out.println("matFin: " + matFin);
            System.out.println("connection: " + connection);
            System.out.println("call: " + call);

            try (CallableStatement cs = connection.prepareCall(call)) {

                cs.setString(1, wcodSoc);
                cs.setLong(2, annee);
                cs.setString(3, matDeb);
                cs.setString(4, matFin);
                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                ResponseProcedure response = new ResponseProcedure();
                response.setReponse(cs.getString(5));

                return response;
            }
        });
    }

    public ResponseProcedure calPlafondCnam(String wcodSoc, String annee, String matDeb, String matFin) {

        return jdbcTemplate.execute((Connection connection) -> {

            String call = getCallSyntax(connection, PKG_PLAFOND, "cal_plafond_cnam", 5);
            System.out.println("wcodSoc: " + wcodSoc);
            System.out.println("annee: " + annee);
            System.out.println("matDeb: " + matDeb);
            System.out.println("matFin: " + matFin);
            System.out.println("connection: " + connection);
            System.out.println("call: " + call);
            try (CallableStatement cs = connection.prepareCall(call)) {

                cs.setString(1, wcodSoc);
                cs.setString(2, annee);
                cs.setString(3, matDeb);
                cs.setString(4, matFin);
                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                ResponseProcedure response = new ResponseProcedure();
                response.setReponse(cs.getString(5));
                return response;
            }
        });
    }

    public ResponseProcedure majPecEnf(
            String wcodSoc,
            String annee,
            String matDeb,
            String matFin) {

        return jdbcTemplate.execute((Connection connection) -> {

            String call = getCallSyntax(connection, PKG_PLAFOND, "maj_pec_enf", 5);

            try (CallableStatement cs = connection.prepareCall(call)) {

                cs.setString(1, wcodSoc);
                cs.setString(2, annee);
                cs.setString(3, matDeb);
                cs.setString(4, matFin);

                cs.registerOutParameter(5, Types.VARCHAR);

                cs.execute();

                ResponseProcedure response = new ResponseProcedure();
                response.setReponse(cs.getString(5));

                return response;
            }
        });
    }

    private String getCallSyntax(Connection connection, String packageName, String procedureName, int paramsCount)
            throws SQLException {

        String dbProduct = connection.getMetaData().getDatabaseProductName().toLowerCase();

        String procedure;

        if (dbProduct.contains("oracle")) {
            procedure = packageName + "." + procedureName;
        } else if (dbProduct.contains("microsoft") || dbProduct.contains("sql server")) {
            procedure = "dbo." + procedureName;
        } else {
            procedure = procedureName;
        }

        StringBuilder call = new StringBuilder("{call ");
        call.append(procedure).append("(");

        for (int i = 0; i < paramsCount; i++) {
            if (i > 0) {
                call.append(",");
            }
            call.append("?");
        }

        call.append(")}");

        return call.toString();
    }

}
