package com.arabsoft.gestioncotisation.Services;

import com.arabsoft.gestioncotisation.Entities.Cle.CleLigCotisMutPers;
import com.arabsoft.gestioncotisation.Entities.LigCotisMutPers;
import com.arabsoft.gestioncotisation.Projections.CotisMutPersProjection;
import com.arabsoft.gestioncotisation.Repositories.LigCotisMutPersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.time.LocalDate;
@Service
public class LigCotisMutPersService {

    @Autowired
    private LigCotisMutPersRepository repository;


    public Optional<LigCotisMutPers> findById(CleLigCotisMutPers id) {
        return repository.findById(id);
    }

    // Method to save a new LigCotisMutPers entity
    public LigCotisMutPers saveCotisMutPers(LigCotisMutPers cotisMutPers) {
        return repository.save(cotisMutPers);  // Save the entity and return it
    }


    private final LigCotisMutPersRepository ligCotisMutPersRepository;

    @Autowired
    public LigCotisMutPersService(LigCotisMutPersRepository ligCotisMutPersRepository) {
        this.ligCotisMutPersRepository = ligCotisMutPersRepository;
    }

    public List<Object[]> getFilteredLigCotis(String codSoc, String matPers, LocalDate datDeb, LocalDate datFin) {
        return ligCotisMutPersRepository.findExistingCotisMutPersSummary(codSoc,matPers, datDeb, datFin);
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public LigCotisMutPers majLigCotisation(String soc, String mat, Long num, LocalDate datd, LocalDate datf, BigDecimal cot) {
        jdbcTemplate.execute((ConnectionCallback<Object>) connection -> {
            try (CallableStatement callableStatement = connection.prepareCall("{call pk_cotisation_mut.maj_lig_cotisation(?, ?, ?, ?, ?, ?)}")) {
                callableStatement.setString(1, soc);
                callableStatement.setString(2, mat);
                callableStatement.setLong(3, num);
                callableStatement.setDate(4, Date.valueOf(datd)); // Convert LocalDate to java.sql.Date
                callableStatement.setDate(5, Date.valueOf(datf)); // Convert LocalDate to java.sql.Date
                callableStatement.setBigDecimal(6, cot);
                callableStatement.execute();
            }
            return null;
        });

        return null;
    }



    public int checkCotisation(String soc, String mat, LocalDate datd, LocalDate datf) {
        String sql = "{call pk_cotisation_mut.verif_lig_cotisation(?, ?, ?, ?, ?)}";

        // Define parameters for the stored procedure
        List<SqlParameter> parameters = new ArrayList<>();
        parameters.add(new SqlParameter(Types.VARCHAR));  // soc
        parameters.add(new SqlParameter(Types.VARCHAR));  // mat
        parameters.add(new SqlParameter(Types.DATE));     // datd
        parameters.add(new SqlParameter(Types.DATE));     // datf
        parameters.add(new SqlOutParameter("result", Types.INTEGER)); // OUT parameter

        // Execute the procedure
        Map<String, Object> result = jdbcTemplate.call(
                new org.springframework.jdbc.core.CallableStatementCreator() {
                    @Override
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        CallableStatement cs = con.prepareCall(sql);
                        cs.setString(1, soc);
                        cs.setString(2, mat);
                        cs.setDate(3, Date.valueOf(datd));
                        cs.setDate(4, Date.valueOf(datf));
                        cs.registerOutParameter(5, Types.INTEGER); // Register OUT parameter as INTEGER
                        return cs;
                    }
                },
                parameters
        );

        // Retrieve the OUT parameter result correctly
        Object output = result.get("result");
        if (output != null) {
            return (Integer) output;
        } else {
            // Handle null case appropriately (e.g., log a warning or throw a custom exception)
            throw new IllegalStateException("Stored procedure did not return a result");
        }
    }


}