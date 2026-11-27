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

        extraireAnneesTable("bult_soin", List.of("dat_soin", "ann_plaf_imp"), codSoc, matPers, annees);
        extraireAnneesTable("bult_arriver", List.of("dat_soin", "ann_plaf_imp"), codSoc, matPers, annees);
        extraireAnneesTable("plafond_assur", List.of("annee_assur"), codSoc, matPers, annees);
        extraireAnneesTable("plafond_cnam", List.of("annee_cnam"), codSoc, matPers, annees);

        // Filtrage en Java des années valides et tri décroissant
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
     * Extrait les années distinctes d'une table donnée en filtrant directement en SQL
     * sur cod_soc et mat_pers lorsqu'ils sont renseignés.
     */
    private void extraireAnneesTable(
            String table,
            List<String> colonnes,
            String codSoc,
            String matPers,
            Set<Integer> annees
    ) {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
        sql.append(String.join(", ", colonnes));
        sql.append(" FROM ").append(table);

        List<Object> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        if (codSoc != null && !codSoc.isBlank()) {
            conditions.add("cod_soc = ?");
            params.add(codSoc.trim());
        }
        if (matPers != null && !matPers.isBlank()) {
            conditions.add("mat_pers = ?");
            params.add(matPers.trim());
        }
        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql.toString(), params.toArray());
            int countAvant = annees.size();
            for (Map<String, Object> row : rows) {
                for (String colonne : colonnes) {
                    extraireAnneeObjet(getValeur(row, colonne)).ifPresent(annees::add);
                }
            }
            log.info("{} : {} années extraites (total cumulé : {})",
                    table.toUpperCase(), annees.size() - countAvant, annees);
        } catch (Exception e) {
            log.warn("Erreur lecture {} : {}", table.toUpperCase(), e.getMessage());
        }
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
