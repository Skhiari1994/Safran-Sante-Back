package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.VirBord;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseDatNais;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ReponseReglerBord;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.reponses.ResponseProcedure;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.VirBordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;

@Service
public class ReglementService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    VirBordRepository virBordRepository;

    @Transactional
    public ReponseReglerBord reglerBord(String soc, String codBord, String codAssur) {
        return jdbcTemplate.execute((Connection con) -> {
            CallableStatement cs =
                    con.prepareCall("{call PK_REGLEMENT.regler_bord(?,?,?,?,?,?)}");

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

    public ResponseProcedure vir_bord(String wcodSoc, String cod_bord_, String nomFichier) {
        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.vir_bord(?, ?, ?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, cod_bord_); // IN parameter
                callableStatement.setString(3, nomFichier); // IN parameter
                callableStatement.registerOutParameter(4, Types.VARCHAR);
                callableStatement.execute(); // Exécuted la procédure stockée
                ResponseProcedure reponse=new ResponseProcedure();

                String message=callableStatement.getString(4);

                reponse.setReponse(message);
                return reponse; // Récupération de la date de naissance
            }
        });
    }
    public void majRegAdh(String wcodSoc, String cod_bord_) {
        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.majRegAdh(?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, cod_bord_); // IN parameter
                callableStatement.execute(); // Exécuter la procédure stockée
            }
            return null; // Nécessaire car execute() attend un retour
        });
    }
    public void majRegAdhVirCnam(String wcodSoc, String cod_bord_) {
        jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call PK_REGLEMENT.majRegAdhVirCnam(?, ?)}";
            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                callableStatement.setString(1, wcodSoc); // IN parameter
                callableStatement.setString(2, cod_bord_); // IN parameter
                callableStatement.execute(); // Exécuter la procédure stockée
            }
            return null; // Nécessaire car execute() attend un retour
        });
    }
    public String generateVirementFile(String filePath,String fileName) {
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
