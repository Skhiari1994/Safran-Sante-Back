package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseDatNais;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ResponseProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

@Service
public class AdherentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

/*

    public ResponseProcedure cal_plafond_mutuelle(String wcodSoc, Long annee, String matDeb, String matFin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call plafond_pkg.cal_plafond_mutuelle(?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc);
                callableStatement.setLong(2, annee);
                callableStatement.setString(3, matDeb);
                callableStatement.setString(4, matFin);
                callableStatement.registerOutParameter(5, Types.VARCHAR);

                callableStatement.execute();
                ResponseProcedure reponse=new ResponseProcedure();
                String message=callableStatement.getString(5);
                reponse.setReponse(message);
                return reponse;
            }
        });
    }

    public ResponseProcedure cal_plafond_cnam(String wcodSoc, Long annee, String matDeb, String matFin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call plafond_pkg.cal_plafond_cnam(?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc);
                callableStatement.setLong(2, annee);
                callableStatement.setString(3, matDeb);
                callableStatement.setString(4, matFin);
                callableStatement.registerOutParameter(5, Types.VARCHAR);

                callableStatement.execute();
                ResponseProcedure reponse=new ResponseProcedure();
                String message=callableStatement.getString(5);
                reponse.setReponse(message);
                return reponse;
            }
        });
    }

    public ResponseProcedure maj_pec_enf(String wcodSoc, Long annee, String matDeb, String matFin) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call plafond_pkg.maj_pec_enf(?, ?, ?, ?,?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc);
                callableStatement.setLong(2, annee);
                callableStatement.setString(3, matDeb);
                callableStatement.setString(4, matFin);
                callableStatement.registerOutParameter(5, Types.VARCHAR);

                callableStatement.execute();
                ResponseProcedure reponse=new ResponseProcedure();
                String message=callableStatement.getString(5);
                reponse.setReponse(message);
                return reponse;
            }
        });
    }*/


    public ResponseProcedure cal_plafond_mutuelle(String wcodSoc, Long annee, String matDeb, String matFin) {
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

    public ResponseProcedure cal_plafond_cnam(String wcodSoc, String annee, String matDeb, String matFin) {
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

    public ResponseProcedure maj_pec_enf(String wcodSoc, String annee, String matDeb, String matFin) {
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
