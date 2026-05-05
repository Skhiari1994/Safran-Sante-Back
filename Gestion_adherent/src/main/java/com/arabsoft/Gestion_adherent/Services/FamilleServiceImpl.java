package com.arabsoft.gestion_adherent.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.arabsoft.gestion_adherent.entities.Famille;
import com.arabsoft.gestion_adherent.repositories.FamilleRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilleServiceImpl implements FamilleService {

    private final FamilleRepository familleRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Famille> getFamille(String matpers, String codSoc) {
        return familleRepository.getFamille(codSoc, matpers);
    }

    @Transactional
    public void deleteCertif(String soc, Long num, String mat, String annee) {
        Query query = entityManager.createNativeQuery(
                "delete from certif_famille where cod_soc = :soc and num_fam = :num and mat_pers = :mat and annee_certif = :annee");
        query.setParameter("soc", soc);
        query.setParameter("num", num);
        query.setParameter("mat", mat);
        query.setParameter("annee", annee);
        query.executeUpdate();
    }
}
