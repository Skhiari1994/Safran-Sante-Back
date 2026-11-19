package com.tn.arabsoft.remboursement_frais_medicaux.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnneeService {

    private final JdbcTemplate jdbcTemplate;

    private static final Pattern PATTERN_ANNEE = Pattern.compile("\\b(19\\d{2}|20\\d{2})\\b");

    /**
     * Récupère la liste distincte et ordonnée (décroissante) de toutes les années
     * présentes dans BULT_SOIN, BULT_ARRIVER, PLAFOND_ASSUR et PLAFOND_CNAM.
     * Le traitement et l'agrégation sont réalisés entièrement en Java (Spring Boot).
     */
    public List<Integer> getAnneesDistinctes(String codSoc, String matPers) {
        log.info("Extraction des années cumulées pour codSoc='{}', matPers='{}'", codSoc, matPers);

        Set<Integer> annees = new HashSet<>();

        // 1. Extraction BULT_SOIN (dat_soin et ann_plaf_imp)
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT DISTINCT dat_soin, ann_plaf_imp, cod_soc, mat_pers FROM bult_soin"
            );
            int countAvant = annees.size();
            rows.stream()
                    .filter(r -> correspondCriteres(getValeur(r, "cod_soc"), getValeur(r, "mat_pers"), codSoc, matPers))
                    .forEach(r -> {
                        extraireAnneeObjet(getValeur(r, "dat_soin")).ifPresent(annees::add);
                        extraireAnneeObjet(getValeur(r, "ann_plaf_imp")).ifPresent(annees::add);
                    });
            log.info("BULT_SOIN : {} années extraites (total cumulé : {})", annees.size() - countAvant, annees);
        } catch (Exception e) {
            log.warn("Erreur lecture BULT_SOIN : {}", e.getMessage());
        }

        // 2. Extraction BULT_ARRIVER (dat_soin et ann_plaf_imp)
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT DISTINCT dat_soin, ann_plaf_imp, cod_soc, mat_pers FROM bult_arriver"
            );
            int countAvant = annees.size();
            rows.stream()
                    .filter(r -> correspondCriteres(getValeur(r, "cod_soc"), getValeur(r, "mat_pers"), codSoc, matPers))
                    .forEach(r -> {
                        extraireAnneeObjet(getValeur(r, "dat_soin")).ifPresent(annees::add);
                        extraireAnneeObjet(getValeur(r, "ann_plaf_imp")).ifPresent(annees::add);
                    });
            log.info("BULT_ARRIVER : {} années extraites (total cumulé : {})", annees.size() - countAvant, annees);
        } catch (Exception e) {
            log.warn("Erreur lecture BULT_ARRIVER : {}", e.getMessage());
        }

        // 3. Extraction PLAFOND_ASSUR (annee_assur)
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT DISTINCT annee_assur, cod_soc, mat_pers FROM plafond_assur"
            );
            int countAvant = annees.size();
            rows.stream()
                    .filter(r -> correspondCriteres(getValeur(r, "cod_soc"), getValeur(r, "mat_pers"), codSoc, matPers))
                    .forEach(r -> extraireAnneeObjet(getValeur(r, "annee_assur")).ifPresent(annees::add));
            log.info("PLAFOND_ASSUR : {} années extraites (total cumulé : {})", annees.size() - countAvant, annees);
        } catch (Exception e) {
            log.warn("Erreur lecture PLAFOND_ASSUR : {}", e.getMessage());
        }

        // 4. Extraction PLAFOND_CNAM (annee_cnam)
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                    "SELECT DISTINCT annee_cnam, cod_soc, mat_pers FROM plafond_cnam"
            );
            int countAvant = annees.size();
            rows.stream()
                    .filter(r -> correspondCriteres(getValeur(r, "cod_soc"), getValeur(r, "mat_pers"), codSoc, matPers))
                    .forEach(r -> extraireAnneeObjet(getValeur(r, "annee_cnam")).ifPresent(annees::add));
            log.info("PLAFOND_CNAM : {} années extraites (total cumulé : {})", annees.size() - countAvant, annees);
        } catch (Exception e) {
            log.warn("Erreur lecture PLAFOND_CNAM : {}", e.getMessage());
        }

        // 5. Filtrage en Java des années valides et tri décroissant
        List<Integer> resultat = annees.stream()
                .filter(a -> a != null && a >= 1900 && a <= 2100)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        // Si aucune donnée n'a été trouvée dans les 4 tables, proposer par défaut l'année en cours
        if (resultat.isEmpty()) {
            resultat.add(LocalDate.now().getYear());
        }

        log.info("Résultat final des années renvoyées : {}", resultat);
        return resultat;
    }

    /**
     * Surcharge sans filtre (toutes les années de toutes les tables).
     */
    public List<Integer> getAnneesDistinctes() {
        return getAnneesDistinctes(null, null);
    }

    /**
     * Recherche insensible à la casse dans les clés de la Map JDBC.
     */
    private Object getValeur(Map<String, Object> map, String cleRecherchee) {
        if (map == null || cleRecherchee == null) return null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (cleRecherchee.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private boolean correspondCriteres(Object socObj, Object matObj, String codSocFiltre, String matPersFiltre) {
        String socEntite = socObj != null ? socObj.toString().trim() : "";
        String matEntite = matObj != null ? matObj.toString().trim() : "";

        boolean socOk = codSocFiltre == null || codSocFiltre.isBlank() || codSocFiltre.trim().equalsIgnoreCase(socEntite);
        boolean matOk = matPersFiltre == null || matPersFiltre.isBlank() || matPersFiltre.trim().equalsIgnoreCase(matEntite);
        return socOk && matOk;
    }

    /**
     * Extrait l'année à partir de n'importe quel type :
     * java.sql.Date, java.time.LocalDate, java.time.LocalDateTime, java.util.Date, Timestamp, Number, ou String (avec regex).
     */
    private Optional<Integer> extraireAnneeObjet(Object obj) {
        if (obj == null) return Optional.empty();

        if (obj instanceof java.sql.Date sqlDate) {
            return Optional.of(sqlDate.toLocalDate().getYear());
        }
        if (obj instanceof java.time.LocalDate localDate) {
            return Optional.of(localDate.getYear());
        }
        if (obj instanceof java.time.LocalDateTime ldt) {
            return Optional.of(ldt.getYear());
        }
        if (obj instanceof java.sql.Timestamp ts) {
            return Optional.of(ts.toLocalDateTime().getYear());
        }
        if (obj instanceof java.util.Date utilDate) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(utilDate);
            return Optional.of(cal.get(Calendar.YEAR));
        }
        if (obj instanceof Number num) {
            int val = num.intValue();
            if (val >= 1900 && val <= 2100) {
                return Optional.of(val);
            }
        }

        // Cas chaîne ou représentation textuelle
        String str = obj.toString().trim();
        if (str.isEmpty()) return Optional.empty();

        Matcher matcher = PATTERN_ANNEE.matcher(str);
        if (matcher.find()) {
            try {
                int annee = Integer.parseInt(matcher.group(1));
                if (annee >= 1900 && annee <= 2100) {
                    return Optional.of(annee);
                }
            } catch (NumberFormatException ignored) {}
        }

        return Optional.empty();
    }
}
