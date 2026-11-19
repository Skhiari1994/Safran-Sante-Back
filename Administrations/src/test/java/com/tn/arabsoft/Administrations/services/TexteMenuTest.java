package com.tn.arabsoft.Administrations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.tn.arabsoft.administrations.services.TexteMenu;

class TexteMenuTest {

	@Test
	void unTexteAbsentVideOuBlancDevientNull() {
		assertNull(TexteMenu.videEnNull(null));
		assertNull(TexteMenu.videEnNull(""));
		assertNull(TexteMenu.videEnNull("   "));
		assertNull(TexteMenu.videEnNull("\t\n"));
	}

	@Test
	void unTexteRenseigneEstConserveInchange() {
		assertEquals("Cotis/CotisMutuelle", TexteMenu.videEnNull("Cotis/CotisMutuelle"));
		assertEquals(" Admin/AddProfile ", TexteMenu.videEnNull(" Admin/AddProfile "));
	}
}
