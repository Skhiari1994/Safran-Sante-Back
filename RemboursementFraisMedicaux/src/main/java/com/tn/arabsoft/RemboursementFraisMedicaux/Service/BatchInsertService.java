package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchInsertService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int insertActes(List<LigActArriver> actes) {
        int count = 0;
        for (LigActArriver acte : actes) {
            entityManager.createNativeQuery(
                    "INSERT INTO lig_act_arriver (cod_soc, mat_pers, num_fam, dat_soin, indice, abrv_act, cod_act, num_lig, let_cod, cot_act, act_prix, mnt_honor, mnt_remb, accord_act, mnt_net, dat_act, prf_typ, prf_cod, mut_mnt_net, num_pec_act, decis_act) "
                            +
                            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21)")
                    .setParameter(1, acte.getCod_soc())
                    .setParameter(2, acte.getMat_pers())
                    .setParameter(3, acte.getNum_fam())
                    .setParameter(4, acte.getDat_soin())
                    .setParameter(5, acte.getIndice())
                    .setParameter(6, acte.getAbrv_act())
                    .setParameter(7, acte.getCod_act())
                    .setParameter(8, acte.getNum_lig())
                    .setParameter(9, acte.getLet_cod())
                    .setParameter(10, acte.getCot_act())
                    .setParameter(11, acte.getAct_prix())
                    .setParameter(12, acte.getMnt_honor())
                    .setParameter(13, acte.getMnt_remb())
                    .setParameter(14, acte.getAccord_act())
                    .setParameter(15, acte.getMnt_net())
                    .setParameter(16, acte.getDat_act())
                    .setParameter(17, acte.getPrf_typ())
                    .setParameter(18, acte.getPrf_cod())
                    .setParameter(19, acte.getMut_mnt_net())
                    .setParameter(20, acte.getNum_pec_act())
                    .setParameter(21, acte.getDecis_act())
                    .executeUpdate();
            count++;
        }
        return count;
    }

    @Transactional
    public int insertVisites(List<LigVisitArriver> visites) {
        int count = 0;
        for (LigVisitArriver visite : visites) {
            entityManager.createNativeQuery(
                    "INSERT INTO lig_visit_arriver (cod_soc, mat_pers, num_fam, dat_soin, indice, abrv_act, cod_visit, num_lig, mnt_honor, mnt_remb, mnt_net, dat_act, prf_typ, prf_cod, prix_visit, taux_remb, mut_mnt_net, decis_med) "
                            +
                            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18)")
                    .setParameter(1, visite.getCod_soc())
                    .setParameter(2, visite.getMat_pers())
                    .setParameter(3, visite.getNum_fam())
                    .setParameter(4, visite.getDat_soin())
                    .setParameter(5, visite.getIndice())
                    .setParameter(6, visite.getAbrv_act())
                    .setParameter(7, visite.getCod_visit())
                    .setParameter(8, visite.getNum_lig())
                    .setParameter(9, visite.getMnt_honor())
                    .setParameter(10, visite.getMnt_remb())
                    .setParameter(11, visite.getMnt_net())
                    .setParameter(12, visite.getDat_act())
                    .setParameter(13, visite.getPrf_typ())
                    .setParameter(14, visite.getPrf_cod())
                    .setParameter(15, visite.getPrix_visit())
                    .setParameter(16, visite.getTaux_remb())
                    .setParameter(17, visite.getMut_mnt_net())
                    .setParameter(18, visite.getDecis_med())
                    .executeUpdate();
            count++;
        }
        return count;
    }

    @Transactional
    public int insertMedicaments(List<LigMedArriver> medicaments) {
        int count = 0;
        for (LigMedArriver med : medicaments) {
            entityManager.createNativeQuery(
                    "INSERT INTO lig_med_arriver (cod_soc, mat_pers, num_fam, dat_soin, abrv_act, cod_med, num_lig, num_lig_med, indice, mnt_honor, mnt_remb, mnt_net, accord_med, dat_act, prf_typ, prf_cod, num_pec_med, mdc_prix, med_prix, prix_remb, mut_mnt_net, nbr_j, decis_med) "
                            +
                            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18, ?19, ?20, ?21, ?22, ?23)")
                    .setParameter(1, med.getCod_soc())
                    .setParameter(2, med.getMat_pers())
                    .setParameter(3, med.getNum_fam())
                    .setParameter(4, med.getDat_soin())
                    .setParameter(5, med.getAbrv_act())
                    .setParameter(6, med.getCod_med())
                    .setParameter(7, med.getNum_lig())
                    .setParameter(8, med.getNum_lig_med())
                    .setParameter(9, med.getIndice())
                    .setParameter(10, med.getMnt_honor())
                    .setParameter(11, med.getMnt_remb())
                    .setParameter(12, med.getMnt_net())
                    .setParameter(13, med.getAccord_med())
                    .setParameter(14, med.getDat_act())
                    .setParameter(15, med.getPrf_typ())
                    .setParameter(16, med.getPrf_cod())
                    .setParameter(17, med.getNum_pec_med())
                    .setParameter(18, med.getMdc_prix())
                    .setParameter(19, med.getMed_prix())
                    .setParameter(20, med.getPrix_remb())
                    .setParameter(21, med.getMut_mnt_net())
                    .setParameter(22, med.getNbr_j())
                    .setParameter(23, med.getDecis_med())
                    .executeUpdate();
            count++;
        }
        return count;
    }

    @Transactional
    public int insertAppareils(List<LigAppArriver> appareils) {
        int count = 0;
        for (LigAppArriver app : appareils) {
            entityManager.createNativeQuery(
                    "INSERT INTO lig_app_arriver (cod_soc, mat_pers, num_fam, dat_soin, abrv_act, cod_app, num_lig, num_lig_app, mnt_honor, mnt_remb, mnt_net, accord_app, indice, dat_act, prf_typ, prf_cod, num_pec_app, mut_mnt_net) "
                            +
                            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, ?15, ?16, ?17, ?18)")
                    .setParameter(1, app.getCod_soc())
                    .setParameter(2, app.getMat_pers())
                    .setParameter(3, app.getNum_fam())
                    .setParameter(4, app.getDat_soin())
                    .setParameter(5, app.getAbrv_act())
                    .setParameter(6, app.getCod_app())
                    .setParameter(7, app.getNum_lig())
                    .setParameter(8, app.getNum_lig_app())
                    .setParameter(9, app.getMnt_honor())
                    .setParameter(10, app.getMnt_remb())
                    .setParameter(11, app.getMnt_net())
                    .setParameter(12, app.getAccord_app())
                    .setParameter(13, app.getIndice())
                    .setParameter(14, app.getDat_act())
                    .setParameter(15, app.getPrf_typ())
                    .setParameter(16, app.getPrf_cod())
                    .setParameter(17, app.getNum_pec_app())
                    .setParameter(18, app.getMut_mnt_net())
                    .executeUpdate();
            count++;
        }
        return count;
    }
}
