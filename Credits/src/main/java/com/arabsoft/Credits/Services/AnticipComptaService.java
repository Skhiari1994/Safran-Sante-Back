package com.arabsoft.Credits.Services;

import com.arabsoft.Credits.Entities.Response.ResponseProcedure;
import com.arabsoft.Credits.Projections.VirAnticipProjection;
import com.arabsoft.Credits.Repositories.VirAnticipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Service
public class AnticipComptaService {

    private static final Logger log = LoggerFactory.getLogger(AnticipComptaService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private VirAnticipRepository virAnticipRepository;


    public ResponseProcedure imputationAnticipation(
            String pCodSoc,
            String pMatPers,
            String pNumVir,
            String pRefMetier,
            BigDecimal pMontVir,
            BigDecimal pMntEsp,
            BigDecimal pRestVir) {

        return jdbcTemplate.execute((Connection connection) -> {
            String procedureCall = "{call anticip_cpt_prc(?, ?, ?, ?, ?, ?, ?, ?)}";

            try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {

                log.debug("Calling anticip_cpt_prc with params: soc={}, mat={}, numVir={}, ref={}, montVir={}, mntEsp={}, restVir={}",
                        pCodSoc, pMatPers, pNumVir, pRefMetier, pMontVir, pMntEsp, pRestVir);

                // Set input parameters
                callableStatement.setString(1, pCodSoc);      // p_cod_soc
                callableStatement.setString(2, pMatPers);     // p_mat_pers
                callableStatement.setString(3, pNumVir);      // p_num_vir
                callableStatement.setString(4, pRefMetier);   // p_ref_metier

                // Handle null values for numeric parameters
                if (pMontVir != null) {
                    callableStatement.setBigDecimal(5, pMontVir); // p_mont_vir
                } else {
                    callableStatement.setNull(5, Types.NUMERIC);
                }

                if (pMntEsp != null) {
                    callableStatement.setBigDecimal(6, pMntEsp);  // p_mnt_esp
                } else {
                    callableStatement.setNull(6, Types.NUMERIC);
                }

                if (pRestVir != null) {
                    callableStatement.setBigDecimal(7, pRestVir); // p_rest_vir
                } else {
                    callableStatement.setNull(7, Types.NUMERIC);
                }

                // Register output parameter
                callableStatement.registerOutParameter(8, Types.VARCHAR); // p_status

                // Execute the procedure
                callableStatement.execute();

                // Retrieve output parameter
                String status = callableStatement.getString(8);

                log.info("Procedure anticip_cpt_prc executed with status: {}", status);

                // Create response object
                ResponseProcedure response = new ResponseProcedure();
                response.setMessage(status);

                return response;

            } catch (SQLException e) {
                log.error("Error calling anticip_cpt_prc: {}", e.getMessage(), e);

                ResponseProcedure errorResponse = new ResponseProcedure();
                errorResponse.setMessage("ERROR: " + e.getMessage());

                return errorResponse;
            }
        });
    }

    public List<VirAnticipProjection> loadVirementsByMat(String codSoc, String matPers) {
        log.debug("Loading virements for codSoc={}, matPers={}", codSoc, matPers);

        try {
            List<VirAnticipProjection> virements = virAnticipRepository.getVirementsByMat(codSoc, matPers);
            log.info("Found {} virements for matPers={}", virements.size(), matPers);
            return virements;
        } catch (Exception e) {
            log.error("Error loading virements: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load virements: " + e.getMessage(), e);
        }
    }
}