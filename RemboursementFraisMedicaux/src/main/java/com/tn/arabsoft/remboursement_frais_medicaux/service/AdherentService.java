
package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;

import lombok.RequiredArgsConstructor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Service
@RequiredArgsConstructor
public class AdherentService {

    private final JdbcTemplate jdbcTemplate;

    public ResponseProcedure calPlafondMutuelle(String wcodSoc, Long annee, String matDeb, String matFin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call plafond_pkg.cal_plafond_mutuelle(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = connection.prepareCall(procedureCall)) {
                cs.setString(1, wcodSoc);
                cs.setLong(2, annee); // OK: NUMBER
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
            String procedureCall = "{call plafond_pkg.cal_plafond_cnam(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = connection.prepareCall(procedureCall)) {
                cs.setString(1, wcodSoc);
                cs.setString(2, annee); // ⚠️ VARCHAR2 in PL/SQL
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

    public ResponseProcedure majPecEnf(String wcodSoc, String annee, String matDeb, String matFin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call plafond_pkg.maj_pec_enf(?, ?, ?, ?, ?)}";
            try (CallableStatement cs = connection.prepareCall(procedureCall)) {
                cs.setString(1, wcodSoc);
                cs.setString(2, annee); // ⚠️ VARCHAR2 in PL/SQL
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
}
