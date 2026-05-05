package com.arabsoft.gestion_adherent.services;

import com.arabsoft.gestion_adherent.entities.AffilMutuelle;
import com.arabsoft.gestion_adherent.repositories.AffilMutuelleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.CallableStatement;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@SuppressWarnings({ "java:S117" })
public class AffilPersService {

    private final JdbcTemplate jdbcTemplate;

    private final AffilMutuelleRepository mutuelleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void deleteAffilPers(String soc, String mat, String dat_ass) {
        Query query = entityManager.createNativeQuery(
                "delete from affil_mutuelle where cod_soc = :soc and mat_pers = :mat and dat_ass = :dat_ass");
        query.setParameter("soc", soc);
        query.setParameter("mat", mat);
        query.setParameter("dat_ass", dat_ass);
        query.executeUpdate();
    }

    @Transactional
    public AffilMutuelle updateEtat(AffilMutuelle affil, String etat) {
        AffilMutuelle aff = mutuelleRepository.getAffilPersAffiliation(affil.getCod_soc(), affil.getMat_pers(),
                affil.getDat_ass());
        aff.setEtat_aff(etat);
        return mutuelleRepository.save(aff); // Persiste les modifications
    }

    public void validerAff(String soc, String etat_aff, String typ_aff, String mat_pers, String dat_ass) {
        jdbcTemplate.call(
                connection -> {
                    CallableStatement callableStatement = connection.prepareCall("{call valider_aff(?, ?, ?, ?, ?)}");
                    callableStatement.setString(1, soc);
                    callableStatement.setString(2, etat_aff);
                    callableStatement.setString(3, typ_aff);
                    callableStatement.setString(4, mat_pers);
                    callableStatement.setString(5, dat_ass);
                    return callableStatement;
                },
                Collections.emptyList());
    }
}