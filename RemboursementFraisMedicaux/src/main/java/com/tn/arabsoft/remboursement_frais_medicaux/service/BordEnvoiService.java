package com.tn.arabsoft.remboursement_frais_medicaux.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.dao.DuplicateKeyException;

import java.util.Objects;
import java.util.List;

@Service
public class BordEnvoiService {
    private static final int MAX_TENTATIVES = 5;
    private static final String PREFIXE_DEFAUT = "B";

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate tx;

    public BordEnvoiService(JdbcTemplate jdbcTemplate, PlatformTransactionManager txManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.tx = new TransactionTemplate(txManager);
        // Équivalent du COMMIT de la procédure : transaction indépendante
        this.tx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * Génère le code bordereau, insère la ligne dans bord_envoi et renvoie le code.
     */
    public String genererBord(String codAssur, String codSoc, LocalDate datDeb, LocalDate datFin,
            int nbrBult, double totHonor, double totNet) {
        for (int tentative = 1;; tentative++) {
            try {
                // Une nouvelle transaction par tentative
                return tx.execute(status -> creerBord(codAssur, codSoc, datDeb, datFin, nbrBult, totHonor, totNet));
            } catch (DuplicateKeyException e) {
                // Un autre utilisateur a pris le même numéro entre le calcul et l'insert
                if (tentative >= MAX_TENTATIVES) {
                    throw new IllegalStateException("Impossible de générer un code bordereau unique", e);
                }
            }
        }
    }

    private String creerBord(String codAssur, String codSoc, LocalDate datDeb, LocalDate datFin,
            int nbrBult, double totHonor, double totNet) {

        LocalDate aujourdhui = LocalDate.now();
        LocalDate debutAnnee = aujourdhui.withDayOfYear(1);

        String prefixe = lirePrefixe(codAssur);
        int numero = prochainNumero(codSoc, debutAnnee);
        if (numero > 999) {
            throw new IllegalStateException("Plus de 999 bordereaux CNAM pour l'année " + aujourdhui.getYear());
        }
        String codBord = prefixe + aujourdhui.getYear() + String.format("%03d", numero);

        jdbcTemplate.update("""
                insert into bord_envoi
                       (cod_bord, cod_assur, dat_bord, dat_deb, dat_fin,
                        nbr_bult, tot_honor, tot_net, typ_bord, cod_soc, envoi_bord)
                values (?, ?, current_timestamp, ?, ?, ?, ?, ?, 'C', ?, 'O')
                """,
                codBord, codAssur, datDeb, datFin, nbrBult, totHonor, totNet, codSoc);

        return codBord;
    }

    /**
     * Préfixe de l'assurance, 'B' par défaut (comme le NVL + WHEN OTHERS de la
     * procédure).
     */
    private String lirePrefixe(String codAssur) {
        List<String> res = jdbcTemplate.queryForList(
                "select prefixe from assurance where cod_assur = ?", String.class, codAssur);
        String p = res.isEmpty() || res.get(0) == null ? "" : res.get(0).trim();
        return (p.length() == 1) ? p : PREFIXE_DEFAUT;
    }

    /** max(3 derniers chiffres du code) + 1 pour l'année en cours. */
    private int prochainNumero(String codSoc, LocalDate debutAnnee) {
        List<String> codes = jdbcTemplate.queryForList("""
                select cod_bord
                  from bord_envoi
                 where cod_soc = ?
                   and typ_bord = 'C'
                   and dat_bord >= ?
                   and dat_bord < ?
                """, String.class, codSoc, debutAnnee, debutAnnee.plusYears(1));

        return codes.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(c -> c.length() >= 7)
                .map(c -> c.substring(c.length() - 3))
                .filter(s -> s.matches("\\d{3}"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0) + 1;
    }
}
