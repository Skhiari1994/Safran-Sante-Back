package com.arabsoft.Gestion_adherent.Services;

import com.arabsoft.Gestion_adherent.Entities.AffilMutuelle;
import com.arabsoft.Gestion_adherent.Repositories.AffilMutuelleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Service
public class AffilPersService {
    @PersistenceContext
    private EntityManager entityManager;
     @Autowired
    AffilMutuelleRepository mutuelleRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Transactional
    public void deleteAffilPers(String soc, String mat,String dat_ass) {
        Query query = entityManager.createNativeQuery(
                "DELETE FROM affil_mutuelle WHERE cod_soc = :soc  AND mat_pers = :mat AND dat_ass = :dat_ass"
        );
        query.setParameter("soc", soc);
        query.setParameter("mat", mat);
        query.setParameter("dat_ass", dat_ass);
        query.executeUpdate();
    }

    @Transactional
    public AffilMutuelle updateEtat(AffilMutuelle affil, String etat) {
        AffilMutuelle aff =  mutuelleRepository.getAffilPersAffiliation(affil.getCod_soc(),affil.getMat_pers(),affil.getDat_ass());
        aff.setEtat_aff(etat);
        return mutuelleRepository.save(aff); // Persiste les modifications
    }


    public void ValiderAff(String soc, String etat_aff, String typ_aff, String mat_pers, String dat_ass) {
        Map<String, Object> result = jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{CALL valider_aff(?, ?,?,?,?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, etat_aff);
                    callableStatement.setString(3, typ_aff);
                    callableStatement.setString(4, mat_pers);
                   callableStatement.setString(5, dat_ass);
                    return callableStatement;
                },
                Collections.emptyList()
        );

        // Récupérer la valeur du paramètre OUT

    }
}
