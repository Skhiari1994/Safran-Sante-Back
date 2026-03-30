package com.arabsoft.gestionindemnites.Services;

import com.arabsoft.gestionindemnites.Entities.Cle.CleDemandeDons;
import com.arabsoft.gestionindemnites.Entities.Cle.ClePieceDemDons;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;
import com.arabsoft.gestionindemnites.Entities.PieceDemDons;
import com.arabsoft.gestionindemnites.Repositories.DemandeDonsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DemandeDonsService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private DemandeDonsRepository demandeDonsRepository;

    @Autowired
    public DemandeDonsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired
    private DataSource dataSource;

    public DemandeDons createDemandeDon(DemandeDons demandeDon) {
        return demandeDonsRepository.save(demandeDon);
    }

    public List<DemandeDons> getAllDemandeDons() {
        return demandeDonsRepository.findAll();
    }

    public Optional<DemandeDons> getDemandeDonById(CleDemandeDons id) {
        return demandeDonsRepository.findById(id);
    }

    public DemandeDons updateDemandeDon(CleDemandeDons id, DemandeDons demandeDon) {
        DemandeDons existing = demandeDonsRepository.findById(id).orElseThrow(() -> new RuntimeException("DemandeDon not found"));
        existing.setTyp_don(demandeDon.getTyp_don());
        existing.setMnt_dem_don(demandeDon.getMnt_dem_don());
        existing.setMode_payement(demandeDon.getMode_payement());
        // Set other fields as required
        return demandeDonsRepository.save(existing);
    }

    public void deleteDemandeDon(CleDemandeDons id) {
        demandeDonsRepository.deleteById(id);
    }

    public Optional<DemandeDons> findById(CleDemandeDons id) {
        return demandeDonsRepository.findById(id);
    }



    public Map<String, Object> callNatDemandeDonsProc(
            String pNatDon,
            String pCodAffect,
            String pTypDon,
            String pCodSoc,
            String pMatPers,
            Long pNumFam,
            LocalDate pDatDemDon
    ) {
        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call indemnite.demande_dons_proc(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}")) {
                // Set input parameters
                callableStatement.setString(1, pNatDon);
                callableStatement.setString(2, pCodAffect);
                callableStatement.setString(3, pTypDon);
                callableStatement.setString(4, pCodSoc);
                callableStatement.setString(5, pMatPers);
                callableStatement.setLong(6, pNumFam); // Oracle NUMBER maps to Java Long
                callableStatement.setDate(7, java.sql.Date.valueOf(pDatDemDon)); // Convert LocalDate to SQL Date

                // Register output parameters
                callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR); // p_typ_benef
                callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR); // p_lib_nat_don
                callableStatement.registerOutParameter(10, java.sql.Types.NUMERIC); // p_max_mnt_don
                callableStatement.registerOutParameter(11, java.sql.Types.VARCHAR); // p_status
                callableStatement.registerOutParameter(12, java.sql.Types.NUMERIC); // valid

                // Execute the stored procedure
                callableStatement.execute();

                // Retrieve the output parameters
                Map<String, Object> result = new HashMap<>();
                result.put("p_typ_benef", callableStatement.getString(8));
                result.put("p_lib_nat_don", callableStatement.getString(9));
                result.put("p_max_mnt_don", callableStatement.getBigDecimal(10)); // Use getBigDecimal for NUMERIC
                result.put("p_status", callableStatement.getString(11));
                result.put("valid", callableStatement.getString(12));
                return result; // Return the result map
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.emptyMap(); // Return an empty map in case of an exception
            }
        });
    }


    public Map<String, Object> callFAM_DEMANDE_DONS(
            String p_cod_soc,
            String p_mat_pers,
            Long p_num_fam,
            String p_nat_don,
            LocalDate p_dat_dem_don
    ) {
        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call indemnite.FAM_DEMANDE_DONS(?, ?, ?, ?, ?, ?, ?)}")) {
                // Set input parameters
                callableStatement.setString(1, p_cod_soc);
                callableStatement.setString(2, p_mat_pers);
                callableStatement.setLong(3, p_num_fam); // Oracle NUMBER -> Java Long
                callableStatement.setString(4, p_nat_don);
                callableStatement.setDate(5, java.sql.Date.valueOf(p_dat_dem_don)); // Convert LocalDate to SQL Date

                // Register output parameters
                callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR); // p_status
                callableStatement.registerOutParameter(7, java.sql.Types.NUMERIC); // valid (NUMBER in Oracle)

                // Execute the stored procedure
                callableStatement.execute();

                // Retrieve output parameters correctly
                Map<String, Object> result = new HashMap<>();
                result.put("p_status", callableStatement.getString(6)); // Correct index
                result.put("valid", callableStatement.getInt(7)); // Retrieve as Integer (NUMBER in Oracle)

                return result;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error calling stored procedure FAM_DEMANDE_DONS", e);
            }
        });
    }

    public Integer getConcerneValue(String concerne) {
        Integer result = null;

        String functionCall = "{ ? = call indemnite.get_concerne_value(?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement callableStatement = connection.prepareCall(functionCall)) {

            // Register the first parameter as the return value
            callableStatement.registerOutParameter(1, Types.INTEGER);

            // Set the input parameter
            callableStatement.setString(2, concerne);

            // Execute the call
            callableStatement.execute();

            // Retrieve the result
            result = callableStatement.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions (e.g., logging, throwing custom exceptions, etc.)
        }

        return result;
    }

    public DemandeDons updateDemandeDonvalid(CleDemandeDons id, DemandeDons demandeDon) {
        DemandeDons existing = demandeDonsRepository.findById(id).orElseThrow(() -> new RuntimeException("DemandeDon not found"));
        existing.setEtat_dem(demandeDon.getEtat_dem());
        existing.setNum_piece(demandeDon.getNum_piece());
        
        // Update ref_metier (concatenated value: refMetier + "/" + dateOperation)
        if (demandeDon.getRef_metier() != null) {
            existing.setRef_metier(demandeDon.getRef_metier());
        }

        return demandeDonsRepository.save(existing);
    }

    public DemandeDons updateDemandeDonDeblocage(CleDemandeDons id, DemandeDons demandeDon) {
        DemandeDons existing = demandeDonsRepository.findById(id).orElseThrow(() -> new RuntimeException("DemandeDon not found"));

        existing.setEtat_dem(demandeDon.getEtat_dem());
        existing.setDat_deblocage(demandeDon.getDat_deblocage());

        return demandeDonsRepository.save(existing);
    }
    public List<DemandeDons> updateDemandeDonsDeblocage(List<DemandeDons> demandeDonsList) {
        for (DemandeDons don : demandeDonsList) {
            if (don.getDat_deblocage() != null && don.getEtat_dem().equals("D")) {
                // Convertir la date si nécessaire (dd/MM/yyyy → LocalDate)
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate date = don.getDat_deblocage();
                don.setDat_deblocage(date);
            }
            // Mettre à jour la base (save ou merge)
            demandeDonsRepository.save(don);
        }
        return demandeDonsList;
    }


    public Map<String, Object> callCheckDemandeDonProc(
            String pNatDon,
            String pCodSoc,
            String pMatPers,
            Long pNumFam,
            LocalDate pDatDemDon
    ) {
        return jdbcTemplate.execute((ConnectionCallback<Map<String, Object>>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call indemnite.CHECK_DEMANDE_DON(?, ?, ?, ?, ?, ?)}")) {
                // Définition des paramètres d'entrée
                callableStatement.setString(1, pNatDon);
                callableStatement.setString(2, pCodSoc);
                callableStatement.setString(3, pMatPers);
                callableStatement.setLong(4, pNumFam); // Oracle NUMBER -> Java Long
                callableStatement.setDate(5, java.sql.Date.valueOf(pDatDemDon)); // Conversion LocalDate -> SQL Date

                // Définition du paramètre de sortie
                callableStatement.registerOutParameter(6, java.sql.Types.NUMERIC); // p_status (0 ou 1)

                // Exécuter la procédure stockée
                callableStatement.execute();

                // Récupérer les résultats
                Map<String, Object> result = new HashMap<>();
                result.put("p_status", callableStatement.getInt(6)); // Récupérer en tant que Integer

                return result; // Retourner la map avec le statut
            } catch (SQLException e) {
                e.printStackTrace();
                return Collections.singletonMap("p_status", 1); // En cas d'erreur, retourner 1 (échec)
            }
        });
    }
}
