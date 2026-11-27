package com.arabsoft.gestion_adherent.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Outils communs des fenêtres de recherche lues page par page (« Sélectionnez une valeur ») :
 * mêmes règles que la recherche faite jusqu'ici dans le navigateur.
 * - « contient », sans tenir compte des majuscules (motifContient, utilisé avec LIKE ... escape '!') ;
 * - matricule : égalité exacte (101 ne ramène pas 1101) ;
 * - recherche globale : chaque mot (3 au plus) doit se retrouver dans au moins une colonne.
 */
public final class LovRecherche {

    /** Taille de page acceptée : 1 à 200 lignes. */
    public static final int TAILLE_MAX = 200;

    private LovRecherche() {
    }

    /** Paramètre vide ou absent : critère ignoré (null). */
    public static String vide(String valeur) {
        return valeur == null || valeur.trim().isEmpty() ? null : valeur.trim();
    }

    /** Texte recherché en minuscules, tel quel (égalité exacte : matricule). */
    public static String exact(String valeur) {
        String v = vide(valeur);
        return v == null ? null : v.toLowerCase();
    }

    /** « contient » pour LIKE ... escape '!' : les caractères spéciaux saisis (! % _ [) sont pris tels quels. */
    public static String motifContient(String texte) {
        String v = vide(texte);
        if (v == null) {
            return null;
        }
        return "%" + v.toLowerCase().replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![") + "%";
    }

    /** Les 3 premiers mots de la recherche globale (null si absent). */
    public static String[] mots(String recherche) {
        String[] resultat = new String[3];
        String v = vide(recherche);
        if (v != null) {
            List<String> liste = Arrays.stream(v.split("\\s+")).filter(m -> !m.isEmpty()).limit(3).toList();
            for (int i = 0; i < liste.size(); i++) {
                resultat[i] = liste.get(i);
            }
        }
        return resultat;
    }

    /**
     * Colonnes affichées par l'écran (liste « a,b,c »), limitées à celles que la requête connaît.
     * Sans liste : les colonnes par défaut (celles de la fenêtre d'origine).
     */
    public static java.util.Set<String> colonnes(String cols, String... parDefaut) {
        String v = vide(cols);
        if (v == null) {
            return java.util.Set.of(parDefaut);
        }
        return Arrays.stream(v.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .collect(java.util.stream.Collectors.toSet());
    }

    public static int taille(int taille) {
        return Math.min(Math.max(taille, 1), TAILLE_MAX);
    }

    public static int page(int page) {
        return Math.max(page, 0);
    }

    /** Colonnes « matricule » : comparées exactement, comme dans le navigateur (lov-match.ts). */
    private static final java.util.regex.Pattern MATRICULE = java.util.regex.Pattern.compile("^mat(_|ricule|pers|int|adm|$)",
            java.util.regex.Pattern.CASE_INSENSITIVE);

    /**
     * Paramètres d'une fenêtre de recherche lus depuis la requête : filtre de chaque colonne (matricule exact, sinon
     * « contient »), colonnes affichées (cols) pour la recherche unique q, page et taille.
     */
    public static final class Filtres {
        private final Map<String, String> params;
        private final java.util.Set<String> visibles;
        private final String[] mots;
        private final int page;
        private final int taille;

        public Filtres(Map<String, String> params, String... colonnesParDefaut) {
            this.params = params;
            this.visibles = colonnes(params.get("cols"), colonnesParDefaut);
            this.mots = mots(params.get("q"));
            this.page = LovRecherche.page(entier(params.get("page"), 0));
            this.taille = LovRecherche.taille(entier(params.get("taille"), 50));
        }

        private static int entier(String v, int parDefaut) {
            try {
                return v == null ? parDefaut : Integer.parseInt(v.trim());
            } catch (NumberFormatException e) {
                return parDefaut;
            }
        }

        /** Filtre de la colonne, prêt pour la requête (null = pas de filtre). */
        public String get(String colonne) {
            String v = params.get(colonne);
            return MATRICULE.matcher(colonne).find() ? exact(v) : motifContient(v);
        }

        /** 1 si la colonne est affichée par l'écran (la recherche unique la parcourt), sinon 0. */
        public int visible(String colonne) {
            return visibles.contains(colonne) ? 1 : 0;
        }

        public String[] motsExacts() {
            return new String[] { exact(mots[0]), exact(mots[1]), exact(mots[2]) };
        }

        public String[] motsContient() {
            return new String[] { motifContient(mots[0]), motifContient(mots[1]), motifContient(mots[2]) };
        }

        public int page() {
            return page;
        }

        public int taille() {
            return taille;
        }

        public int debut() {
            return page * taille;
        }
    }

    /** Réponse commune : lignes de la page, nombre total de lignes trouvées, page et taille réellement appliquées. */
    public static Map<String, Object> reponse(List<?> lignes, long total, int page, int taille) {
        Map<String, Object> r = new LinkedHashMap<>();
        r.put("content", lignes);
        r.put("total", total);
        r.put("page", page);
        r.put("taille", taille);
        return r;
    }
}
