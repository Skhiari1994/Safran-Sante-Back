package com.arabsoft.gestioncotisation.Services;

import com.arabsoft.gestioncotisation.DTO.CotisDto;
import com.arabsoft.gestioncotisation.Entities.CotisMutPers;
import com.arabsoft.gestioncotisation.Exception.ResourceNotFoundException;
import com.arabsoft.gestioncotisation.Projections.CotisMutPersProjection;
import com.arabsoft.gestioncotisation.Repositories.CotisMutPersRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CotisMutPersService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CotisMutPersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired
    private  CotisMutPersRepository cotisMutPersRepository;

    public CotisMutPers saveCotisation(CotisMutPers cotisation) {
        return cotisMutPersRepository.save(cotisation);
    }

    public CotisMutPers saveCotisationSeq(CotisMutPers cot) {
        // Calculate the next num_cot value
        Long nextNumCot = cotisMutPersRepository.findNextNumCot(cot.getCod_soc(), cot.getMat_pers());
        System.out.println("Next NumCot: " + nextNumCot);  // Debug logging

        cot.setNum_cot(nextNumCot);  // Set the num_cot value

        // Save the entity
        return cotisMutPersRepository.save(cot);
    }
    public int ChargActInx(String soc, LocalDate mois, String mat) {
        return jdbcTemplate.execute((ConnectionCallback<Integer>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call chargement_pers_actif(?, ?, ?, ?)}")) {
                callableStatement.setString(1, soc);
                callableStatement.setDate(2, java.sql.Date.valueOf(mois));
                callableStatement.setString(3, mat);
                callableStatement.registerOutParameter(4, java.sql.Types.INTEGER);

                callableStatement.execute();
                return callableStatement.getInt(4); // Retrieve the OUT parameter
            } catch (SQLException e) {
                e.printStackTrace();
                return 0; // Return 0 if an exception occurs
            }
        });
    }



}