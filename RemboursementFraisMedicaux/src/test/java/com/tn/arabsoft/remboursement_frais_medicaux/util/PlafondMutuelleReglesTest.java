package com.tn.arabsoft.remboursement_frais_medicaux.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PlafondMutuelleReglesTest {

    private static final LocalDate AUJOURDHUI = LocalDate.of(2026, 11, 18);
    private static final LocalDate NAISSANCE = LocalDate.of(1988, 10, 18);

    @Test
    void ageCompletALaDateDeSoin() {
        var age = PlafondMutuelleRegles.ageALaDateDeSoin(NAISSANCE, LocalDate.of(2026, 11, 16));
        assertEquals(38, age.years());
        assertEquals(0, age.months());

        var apres = PlafondMutuelleRegles.ageALaDateDeSoin(NAISSANCE, LocalDate.of(2026, 11, 18));
        assertEquals(38, apres.years());
        assertEquals(1, apres.months());
    }

    @Test
    void ageNulSiDatesInutilisables() {
        assertEquals(0, PlafondMutuelleRegles.ageALaDateDeSoin(null, AUJOURDHUI).years());
        assertEquals(0, PlafondMutuelleRegles.ageALaDateDeSoin(NAISSANCE, null).months());
        assertEquals(0, PlafondMutuelleRegles.ageALaDateDeSoin(AUJOURDHUI, NAISSANCE).years());
    }

    @Test
    void dateValideDansLaDureeDeVie() {
        assertNull(PlafondMutuelleRegles.controleDateSoin(LocalDate.of(2026, 11, 16), NAISSANCE, AUJOURDHUI,
                AUJOURDHUI, 120.0));
        assertNull(PlafondMutuelleRegles.controleDateSoin(AUJOURDHUI, NAISSANCE, AUJOURDHUI, AUJOURDHUI, 120.0));
    }

    @Test
    void dureeDeVieEpuisee() {
        assertEquals(PlafondMutuelleRegles.MSG_DUREE_EPUISEE, PlafondMutuelleRegles.controleDateSoin(
                AUJOURDHUI.minusDays(121), NAISSANCE, AUJOURDHUI, AUJOURDHUI, 120.0));
        assertNull(PlafondMutuelleRegles.controleDateSoin(AUJOURDHUI.minusDays(120), NAISSANCE, AUJOURDHUI,
                AUJOURDHUI, 120.0));
    }

    @Test
    void dureeAbsenteEquivautAZeroJour() {
        assertEquals(PlafondMutuelleRegles.MSG_DUREE_EPUISEE, PlafondMutuelleRegles.controleDateSoin(
                AUJOURDHUI.minusDays(1), NAISSANCE, AUJOURDHUI, AUJOURDHUI, null));
        assertNull(PlafondMutuelleRegles.controleDateSoin(AUJOURDHUI, NAISSANCE, AUJOURDHUI, AUJOURDHUI, null));
    }

    @Test
    void datesInvalides() {
        assertEquals(PlafondMutuelleRegles.MSG_DATE_INVALIDE, PlafondMutuelleRegles.controleDateSoin(
                AUJOURDHUI.plusDays(1), NAISSANCE, AUJOURDHUI.plusDays(1), AUJOURDHUI, 120.0));
        assertEquals(PlafondMutuelleRegles.MSG_DATE_INVALIDE, PlafondMutuelleRegles.controleDateSoin(
                NAISSANCE.minusDays(1), NAISSANCE, AUJOURDHUI, NAISSANCE.minusDays(1), 120.0));
        assertEquals(PlafondMutuelleRegles.MSG_DATE_INVALIDE, PlafondMutuelleRegles.controleDateSoin(null,
                NAISSANCE, AUJOURDHUI, AUJOURDHUI, 120.0));
    }
}
