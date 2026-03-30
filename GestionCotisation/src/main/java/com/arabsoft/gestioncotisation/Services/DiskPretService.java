package com.arabsoft.gestioncotisation.Services;

import com.arabsoft.gestioncotisation.Projections.DiskPretProjection;
import com.arabsoft.gestioncotisation.Repositories.DiskPretRepository;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Service
public class DiskPretService {

    private final DiskPretRepository diskPretRepository;
    private final JdbcTemplate jdbcTemplate;

    public DiskPretService(DiskPretRepository diskPretRepository, JdbcTemplate jdbcTemplate) {
        this.diskPretRepository = diskPretRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DiskPretProjection> getFilteredDiskPrets(String matPers, String corps, String mois) {
        return diskPretRepository.findFilteredDiskPrets(matPers, corps, mois);
    }

    // 🔹 Méthode pour appeler la procédure stockée "maj_cotisation"
   /* public void callMajCotisationProc(String matPers, String corps, String mois, String codSoc) {

        jdbcTemplate.execute((ConnectionCallback<Void>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call pk_cotisation_mut.maj_cotisation_agent_actifs(?, ?, ?, ?)}")) {
                callableStatement.setString(1, matPers);
                callableStatement.setString(2, corps);
                callableStatement.setString(3, mois);
                callableStatement.setString(4, codSoc);

                // Exécuter la procédure stockée
                callableStatement.execute();
            } catch (SQLException e) {
                throw new RuntimeException("Erreur lors de l'exécution de la procédure maj_cotisation", e);
            }
            return null;
        });
    }*/

    public int callMajCotisationProc(String matPers, String corps, String mois, String codSoc) {
        return jdbcTemplate.execute(
                "{call pk_cotisation_mut.maj_cotisation_agent_actifs(?, ?, ?, ?, ?)}",
                (CallableStatementCallback<Integer>) cs -> {
                    cs.setString(1, matPers);    // p_mat_pers
                    cs.setString(2, corps);      // p_corps
                    cs.setString(3, mois);       // p_mois
                    cs.setString(4, codSoc);     // p_cod_soc
                    cs.registerOutParameter(5, Types.INTEGER); // p_rows_updated

                    cs.execute();

                    return cs.getInt(5); // Retourne le nombre de lignes mises à jour
                }
        );
    }
}
