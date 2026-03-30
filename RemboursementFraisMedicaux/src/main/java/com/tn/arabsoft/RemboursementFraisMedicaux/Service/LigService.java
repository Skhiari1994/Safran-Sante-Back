package com.tn.arabsoft.RemboursementFraisMedicaux.Service;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.*;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LigService {

    private final LigVisitArriverRepository ligVisitArriverRepository;
    private final LigActArriverRepository ligActArriverRepository;
    private final LigMedArriverRepository ligMedArriverRepository;
    private final LigAppArriverRepository ligAppArriverRepository;
    private final BultArriverRepository bultArriverRepository;
    private final BordArriverRepository bordArriverRepository;

    // ---------------- SAVE COMPLET ----------------
    public void saveComplet(
            BordArriver bord,
            List<BultArriver> bulletins,
            List<LigActArriver> actes,
            List<LigVisitArriver> visites,
            List<LigAppArriver> appareils,
            List<LigMedArriver> medicaments
    ) {

        if (bord != null) bordArriverRepository.save(bord);
        if (bulletins != null && !bulletins.isEmpty()) bultArriverRepository.saveAll(bulletins);
        if (actes != null && !actes.isEmpty()) actes.forEach(this::saveOrUpdateActe);
        if (visites != null && !visites.isEmpty()) visites.forEach(this::saveOrUpdateVisite);
        if (appareils != null && !appareils.isEmpty()) appareils.forEach(this::saveOrUpdateAppareil);
        if (medicaments != null && !medicaments.isEmpty()) medicaments.forEach(this::saveOrUpdateMedicament);
    }

    // ---------------- SAVE OR UPDATE METHODS ----------------

    private void saveOrUpdateActe(LigActArriver acte) {
        LigActArriverCle id = new LigActArriverCle(
                acte.getCod_soc(), acte.getMat_pers(), acte.getNum_fam(),
                acte.getDat_soin(), acte.getNum_lig()
        );

        Optional<LigActArriver> existing = ligActArriverRepository.findById(id);
        if (existing.isPresent()) {
            var obj = existing.get();
            obj.setAbrv_act(acte.getAbrv_act());
            obj.setCod_act(acte.getCod_act());
            obj.setMnt_honor(acte.getMnt_honor());
            obj.setMnt_remb(acte.getMnt_remb());
            obj.setMnt_net(acte.getMnt_net());
            obj.setAccord_act(acte.getAccord_act());
            obj.setIndice(acte.getIndice());
            obj.setDat_act(acte.getDat_act());
            obj.setPrf_typ(acte.getPrf_typ());
            obj.setPrf_cod(acte.getPrf_cod());
            obj.setNum_pec_act(acte.getNum_pec_act());
            obj.setDecis_act(acte.getDecis_act());
            obj.setCot_act(acte.getCot_act());
            obj.setAct_prix(acte.getAct_prix());
            obj.setLet_cod(acte.getLet_cod());
            ligActArriverRepository.save(obj);
        } else {
            // assign num_lig automatique si besoin
            if (acte.getNum_lig() == null) {
                Integer maxNum = ligActArriverRepository.findMaxNumLig(acte.getCod_soc(), acte.getMat_pers(), acte.getNum_fam(), acte.getDat_soin());
                acte.setNum_lig(maxNum == null ? 1 : maxNum + 1);
            }
            ligActArriverRepository.save(acte);
        }
    }

    private void saveOrUpdateVisite(LigVisitArriver visite) {
        LigVisitArriverCle id = new LigVisitArriverCle(
                visite.getCod_soc(), visite.getMat_pers(), visite.getNum_fam(),
                visite.getDat_soin(), visite.getNum_lig()
        );
        Optional<LigVisitArriver> existing = ligVisitArriverRepository.findById(id);

        if (existing.isPresent()) {
            var obj = existing.get();
            obj.setAbrv_act(visite.getAbrv_act());
            obj.setCod_visit(visite.getCod_visit());
            obj.setMnt_honor(visite.getMnt_honor());
            obj.setMnt_remb(visite.getMnt_remb());
            obj.setMnt_net(visite.getMnt_net());
            obj.setIndice(visite.getIndice());
            obj.setDat_act(visite.getDat_act());
            obj.setPrf_typ(visite.getPrf_typ());
            obj.setPrf_cod(visite.getPrf_cod());
            obj.setPrix_visit(visite.getPrix_visit());
            obj.setTaux_remb(visite.getTaux_remb());
            obj.setMut_mnt_net(visite.getMut_mnt_net());
            obj.setDecis_med(visite.getDecis_med());
            ligVisitArriverRepository.save(obj);
        } else {
            if (visite.getNum_lig() == null) {
                Long maxNum = ligVisitArriverRepository.findMaxNumLig(visite.getCod_soc(), visite.getMat_pers(), visite.getNum_fam(), visite.getDat_soin());
                visite.setNum_lig(maxNum == null ? 1 : maxNum + 1);
            }
            ligVisitArriverRepository.save(visite);
        }
    }

    private void saveOrUpdateAppareil(LigAppArriver appareil) {
        LigAppArriverCle id = new LigAppArriverCle(
                appareil.getCod_soc(), appareil.getMat_pers(), appareil.getNum_fam(),
                appareil.getDat_soin(), appareil.getNum_lig()
        );
        Optional<LigAppArriver> existing = ligAppArriverRepository.findById(id);

        if (existing.isPresent()) {
            var obj = existing.get();
            obj.setAbrv_act(appareil.getAbrv_act());
            obj.setCod_app(appareil.getCod_app());
            obj.setMnt_honor(appareil.getMnt_honor());
            obj.setMnt_remb(appareil.getMnt_remb());
            obj.setMnt_net(appareil.getMnt_net());
            obj.setAccord_app(appareil.getAccord_app());
            obj.setIndice(appareil.getIndice());
            obj.setDat_act(appareil.getDat_act());
            obj.setPrf_typ(appareil.getPrf_typ());
            obj.setPrf_cod(appareil.getPrf_cod());
            obj.setNum_pec_app(appareil.getNum_pec_app());
            obj.setNum_lig_app(appareil.getNum_lig_app());
            obj.setMut_mnt_net(appareil.getMut_mnt_net());
            ligAppArriverRepository.save(obj);
        } else {
            if (appareil.getNum_lig() == null) {
                Integer maxNum = ligAppArriverRepository.findMaxNumLig(appareil.getCod_soc(), appareil.getMat_pers(), appareil.getNum_fam(), appareil.getDat_soin());
                appareil.setNum_lig(maxNum == null ? 1 : maxNum + 1);
            }
            ligAppArriverRepository.save(appareil);
        }
    }

    private void saveOrUpdateMedicament(LigMedArriver med) {
        LigMedArriverCle id = new LigMedArriverCle(
                med.getCod_soc(), med.getMat_pers(), med.getNum_fam(),
                med.getDat_soin(), med.getNum_lig()
        );
        Optional<LigMedArriver> existing = ligMedArriverRepository.findById(id);

        if (existing.isPresent()) {
            var obj = existing.get();
            obj.setAbrv_act(med.getAbrv_act());
            obj.setCod_med(med.getCod_med());
            obj.setMnt_honor(med.getMnt_honor());
            obj.setMnt_net(med.getMnt_net());
            obj.setMnt_remb(med.getMnt_remb());
            obj.setAccord_med(med.getAccord_med());
            obj.setDat_act(med.getDat_act());
            obj.setPrf_typ(med.getPrf_typ());
            obj.setPrf_cod(med.getPrf_cod());
            obj.setNum_pec_med(med.getNum_pec_med());
            obj.setMdc_prix(med.getMdc_prix());
            obj.setMed_prix(med.getMed_prix());
            obj.setPrix_remb(med.getPrix_remb());
            obj.setNum_lig_med(med.getNum_lig_med());
            obj.setMut_mnt_net(med.getMut_mnt_net());
            obj.setNbr_j(med.getNbr_j());
            obj.setDecis_med(med.getDecis_med());
            ligMedArriverRepository.save(obj);
        } else {
            if (med.getNum_lig() == null) {
                Integer maxNum = ligMedArriverRepository.findMaxNumLig(med.getCod_soc(), med.getMat_pers(), med.getNum_fam(), med.getDat_soin());
                med.setNum_lig(maxNum == null ? 1 : maxNum + 1);
            }
            ligMedArriverRepository.save(med);
        }
    }
}
