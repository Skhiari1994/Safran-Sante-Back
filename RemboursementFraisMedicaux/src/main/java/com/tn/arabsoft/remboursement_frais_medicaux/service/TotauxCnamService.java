package com.tn.arabsoft.remboursement_frais_medicaux.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tn.arabsoft.remboursement_frais_medicaux.dto.TotauxCnamDto;

import java.util.List;

@Service
public class TotauxCnamService {

    private static final List<String> TABLES_LIGNES = List.of("lig_bult_visit", "lig_bult_act", "lig_bult_med",
            "lig_bult_app");

    private static final String SQL_TOTAUX_LIGNES = """
            select coalesce(sum(coalesce(mnt_honor, 0)), 0),
                   coalesce(sum(coalesce(mnt_net, 0)), 0)
              from %s
             where cod_soc = ?
               and mat_pers = ?
               and num_fam = ?
               and dat_soin = ?
               and num_soin = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public TotauxCnamService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public TotauxCnamDto calculateTotalHonor(String codSoc, String matPers,
            Integer numFam, LocalDate datSoin, Long numSoin) {

        // xmnt_honor et ymnt_tot : somme des 4 tables de lignes
        BigDecimal xmntHonor = BigDecimal.ZERO;
        BigDecimal ymntTot = BigDecimal.ZERO;
        for (String table : TABLES_LIGNES) { // noms de tables constants : pas d'injection possible
            BigDecimal[] s = jdbcTemplate.queryForObject(
                    SQL_TOTAUX_LIGNES.formatted(table), // table comes from the constant list, so no injection risk
                    (rs, i) -> new BigDecimal[] { rs.getBigDecimal(1), rs.getBigDecimal(2) },
                    codSoc, matPers, numFam, datSoin, numSoin);
            xmntHonor = xmntHonor.add(s[0]);
            ymntTot = ymntTot.add(s[1]);
        }

        // zMNT_TOT_MUT : somme de mnt_net dans lig_bult
        BigDecimal zMntTotMut = jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(mnt_net), 0) FROM lig_bult"
                        + " WHERE cod_soc = ? AND mat_pers = ? AND num_fam = ? AND dat_soin = ?",
                BigDecimal.class, codSoc, matPers, numFam, datSoin);

        // asold : plafond - solde consommé de l'année du soin (null si aucune ligne)
        BigDecimal asold = jdbcTemplate.query(
                "SELECT COALESCE(plafond, 0) - COALESCE(sold_plaf, 0) FROM plafond_cnam"
                        + " WHERE cod_soc = ? AND mat_pers = ? AND annee_cnam = ?",
                rs -> rs.next() ? rs.getBigDecimal(1) : null,
                codSoc, matPers, datSoin.getYear());

        return new TotauxCnamDto(xmntHonor, ymntTot, zMntTotMut, asold);

    }

}
