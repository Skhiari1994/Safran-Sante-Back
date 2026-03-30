package com.arabsoft.Gestion_adherent.Services;

import com.arabsoft.Gestion_adherent.Entities.Famille;
import com.arabsoft.Gestion_adherent.Repositories.FamilleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FamilleServiceImpl implements FamilleService {
    @Autowired
    FamilleRepository familleRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public List<Famille> getFamille(String matpers, String codSoc) {
        return familleRepository.getFamille(codSoc,matpers);
    }

    @Transactional
    public void deleteCertif(String soc, Long num, String mat, String annee) {
        Query query = entityManager.createNativeQuery(
                "DELETE FROM certif_famille WHERE cod_soc = :soc AND num_fam = :num AND mat_pers = :mat AND annee_certif = :annee"
        );
        query.setParameter("soc", soc);
        query.setParameter("num", num);
        query.setParameter("mat", mat);
        query.setParameter("annee", annee);
        query.executeUpdate();
    }
}
