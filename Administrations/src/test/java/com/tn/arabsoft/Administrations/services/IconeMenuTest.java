package com.tn.arabsoft.Administrations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.tn.arabsoft.administrations.services.IconeMenu;

class IconeMenuTest {

	@Test
	void uneIconeVideOuAbsenteDevientNull() {
		assertNull(IconeMenu.normaliser(null));
		assertNull(IconeMenu.normaliser(""));
		assertNull(IconeMenu.normaliser("   "));
	}

	@Test
	void uneIconeValideEstConserveeSansEspacesAutour() {
		assertEquals("ri-team-line", IconeMenu.normaliser("ri-team-line"));
		assertEquals("ri-team-line", IconeMenu.normaliser("  ri-team-line \t"));
		assertEquals("bx bx-registered", IconeMenu.normaliser("bx bx-registered"));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			"RI-team-line", // majuscules
			"ri_team_line", // souligné
			"ri-team-line\"><script>alert(1)</script>", // injection dans l'attribut class
			"ri-team-line' onmouseover='x", // injection d'attribut
			"a", // trop court
			"-ri-team", // commence par un tiret
			"ri-team-", // finit par un tiret
			"ri-équipe-line", // accent
			"ri/team" // caractère interdit
	})
	void uneIconeInvalideEstRefusee(String icone) {
		ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> IconeMenu.normaliser(icone));
		assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
	}

	@Test
	void uneIconeDePlusDeCentCaracteresEstRefusee() {
		String trop = "ri-" + "a".repeat(100);
		assertThrows(ResponseStatusException.class, () -> IconeMenu.normaliser(trop));
		String limite = "ri-" + "a".repeat(97); // 100 caractères
		assertEquals(limite, IconeMenu.normaliser(limite));
	}
}
