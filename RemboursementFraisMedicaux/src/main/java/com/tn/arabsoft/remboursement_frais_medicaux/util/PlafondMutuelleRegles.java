package com.tn.arabsoft.remboursement_frais_medicaux.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import com.tn.arabsoft.remboursement_frais_medicaux.util.AgeUtil.Age;

/**
 * Règles du bulletin de soins mutuelle : validité de la date de soin et âge du bénéficiaire à cette date.
 * Même logique que {@code dbo.get_plafond_cnam}, mais l'âge est calculé à la date de soin.
 */
public final class PlafondMutuelleRegles {

    public static final String MSG_DUREE_EPUISEE = "Durée de vie du bulletin épuisée";
    public static final String MSG_DATE_INVALIDE = "Date de soin invalide";
    public static final String MSG_PLAFOND_NON_INITIALISE = "Plafond non encore initialisé pour cet adhérent";

    private PlafondMutuelleRegles() {
    }

    /**
     * @param dureeJours durée de vie du bulletin en jours ({@code null} = 0, comme pour le CNAM)
     * @return le message d'erreur, ou {@code null} si la date de soin est valide
     */
    public static String controleDateSoin(LocalDate soin, LocalDate naissance, LocalDate saisie, LocalDate aujourdhui,
            Double dureeJours) {
        if (soin == null) {
            return MSG_DATE_INVALIDE;
        }
        long ecart = ChronoUnit.DAYS.between(soin, aujourdhui);
        double duree = dureeJours == null ? 0 : dureeJours;
        if (ecart > duree) {
            return MSG_DUREE_EPUISEE;
        }
        if (ecart < 0) {
            return MSG_DATE_INVALIDE;
        }
        if ((naissance != null && soin.isBefore(naissance)) || (saisie != null && soin.isAfter(saisie))) {
            return MSG_DATE_INVALIDE;
        }
        return null;
    }

    /** Âge complet (années, mois) du bénéficiaire à la date de soin ; 0 an 0 mois si les dates sont inutilisables. */
    public static Age ageALaDateDeSoin(LocalDate naissance, LocalDate soin) {
        if (naissance == null || soin == null || naissance.isAfter(soin)) {
            return new Age(0, 0);
        }
        Period p = Period.between(naissance, soin);
        return new Age(p.getYears(), p.getMonths());
    }
}
