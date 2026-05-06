package com.arabsoft.gestionindemnites.Services;

import com.arabsoft.gestionindemnites.Projections.VarCartDataProjection;
import com.arabsoft.gestionindemnites.Repositories.VirCarteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Service
public class VirCarteService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final VirCarteRepository virCarteRepository;

    public VirCarteService(VirCarteRepository virCarteRepository) {
        this.virCarteRepository = virCarteRepository;
    }

    public String processVirCarte(String soc, String datDebloc, String wcodLieuGeog, String wnatDon) {
        String procedureCall = "{call INDEMNITE.vir_carte(?, ?, ?, ?, ?)}"; // Ajout du paramètre de sortie

        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
                CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

            // Définition des paramètres d'entrée
            callableStatement.setString(1, soc);
            callableStatement.setString(2, datDebloc);
            callableStatement.setString(3, wcodLieuGeog);
            callableStatement.setString(4, wnatDon);

            // Définition du paramètre de sortie (message)
            callableStatement.registerOutParameter(5, Types.VARCHAR);

            // Exécuter la procédure
            callableStatement.execute();

            // Récupérer la valeur du paramètre de sortie
            callableStatement.getString(5);
            System.out.println("procedure" + callableStatement.getString(5));

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'appel de la procédure : " + e.getMessage(), e);
        }
        return procedureCall;
    }

    public String generateVirementFile(String filePath) {
        List<VarCartDataProjection> virements = virCarteRepository.findAllByOrderByOrdreAsc();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (VarCartDataProjection virement : virements) {
                writer.write(virement.getLigne());
                writer.newLine(); // Nouvelle ligne
            }
            return "Fichier généré avec succès : " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la génération du fichier";
        }
    }

    @Transactional
    public void deleteAllVirCarteData() {
        virCarteRepository.deleteAllData();
    }
}
