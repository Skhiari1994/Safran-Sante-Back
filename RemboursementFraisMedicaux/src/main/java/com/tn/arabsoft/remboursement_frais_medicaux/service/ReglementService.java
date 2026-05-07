package com.tn.arabsoft.remboursement_frais_medicaux.service;

import jakarta.transaction.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.VirBord;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ReponseReglerBord;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.reponses.ResponseProcedure;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.VirBordRepository;

import lombok.RequiredArgsConstructor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReglementService {

    private final JdbcTemplate jdbcTemplate;
    private final VirBordRepository virBordRepository;

    @Transactional
    public ReponseReglerBord reglerBord(String soc, String codBord, String codAssur) {
        return jdbcTemplate.execute((Connection con) -> {
            CallableStatement cs = con.prepareCall("{call PK_REGLEMENT.regler_bord(?,?,?,?,?,?)}");

            cs.setString(1, soc);
            cs.setString(2, codBord);
            cs.setString(3, codAssur);

            cs.registerOutParameter(4, Types.NUMERIC);
            cs.registerOutParameter(5, Types.VARCHAR);
            cs.registerOutParameter(6, Types.VARCHAR);

            cs.execute();

            ReponseReglerBord r = new ReponseReglerBord();
            r.setWtot_remb(cs.getBigDecimal(4));
            r.setWreg_bord(cs.getString(5));
            r.setMessage(cs.getString(6));
            return r;
        });
    }

    public ResponseProcedure virBord(String wcodSoc, String codBord, String nomFichier) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.vir_bord(?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, codBord); // IN parameter
                callableStatement.setString(3, nomFichier); // IN parameter
                callableStatement.registerOutParameter(4, Types.VARCHAR);
                callableStatement.execute(); // Exécuted la procédure stockée
                ResponseProcedure reponse = new ResponseProcedure();

                String message = callableStatement.getString(4);

                reponse.setReponse(message);
                return reponse; // Récupération de la date de naissance
            }
        });
    }

    public void majRegAdh(String wcodSoc, String codBord) {
        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.majRegAdh(?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, codBord); // IN parameter
                callableStatement.execute(); // Exécuter la procédure stockée
            }
            return null; // Nécessaire car execute() attend un retour
        });
    }

    public void majRegAdhVirCnam(String wcodSoc, String codBord) {
        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.majRegAdhVirCnam(?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, codBord); // IN parameter
                callableStatement.execute(); // Exécuter la procédure stockée
            }
            return null; // Nécessaire car execute() attend un retour
        });
    }

    public String generateVirementFile(String filePath) {
        List<VirBord> virements = virBordRepository.findAllByOrderByOrdreAsc();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (VirBord virement : virements) {
                writer.write(virement.getLigne());
                writer.newLine(); // Nouvelle ligne
            }
            return "Fichier généré avec succès : " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier";
        }
    }
}
