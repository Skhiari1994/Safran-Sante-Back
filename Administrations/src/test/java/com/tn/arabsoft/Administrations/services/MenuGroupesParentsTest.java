package com.tn.arabsoft.Administrations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import com.tn.arabsoft.administrations.entities.JsonResponse;
import com.tn.arabsoft.administrations.entities.Admsubmodule;
import com.tn.arabsoft.administrations.repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.administrations.services.MenuService;

/** Une page autorisée s'affiche même si l'autorisation de son groupe parent n'a pas été enregistrée. */
class MenuGroupesParentsTest {

	private final MenuService service = new MenuService(mock(AdmstorageautorisationDAO.class));

	private static Admeventtype evt(long id, Long parent, long rang, String action) {
		Admeventtype e = new Admeventtype();
		e.setEvt_id(id);
		e.setEvt_evt_id(parent);
		e.setEvt_rank(rang);
		e.setEvt_action(action);
		e.setEvt_name("evt" + id);
		e.setSum_id(61L);
		return e;
	}

	private static List<Long> ids(List<Admeventtype> liste) {
		return liste.stream().map(Admeventtype::getEvt_id).collect(Collectors.toList());
	}

	@Test
	void legroupeParentSansAutorisationEstAjoute() {
		Admeventtype groupe = evt(9030, null, 3, null);
		Admeventtype page1 = evt(9035, 9030L, 5, "Cons/Mut");
		Admeventtype page2 = evt(9036, 9030L, 6, "Cons/Cnam");
		List<Admeventtype> actifs = List.of(groupe, page1, page2, evt(9031, 9030L, 1, "Prise/Mut"));

		List<Admeventtype> resultat = service.avecGroupesParents(List.of(page1, page2), actifs);

		assertEquals(List.of(9030L, 9035L, 9036L), ids(resultat));
	}

	@Test
	void lesGroupesImbriquesRemontentJusqueAuSommet() {
		Admeventtype racine = evt(10, null, 1, null);
		Admeventtype milieu = evt(11, 10L, 1, null);
		Admeventtype page = evt(12, 11L, 1, "Page");

		List<Admeventtype> resultat = service.avecGroupesParents(List.of(page), List.of(racine, milieu, page));

		assertEquals(List.of(10L, 11L, 12L), ids(resultat));
	}

	@Test
	void unGroupeInactifOuQuiEstUnePageNEstJamaisRenduVisible() {
		Admeventtype page = evt(20, 19L, 1, "Page");
		// le parent 19 est absent des entrées actives (statut N) : la page reste écartée, comme avant
		assertEquals(List.of(20L), ids(service.avecGroupesParents(List.of(page), List.of(page))));

		Admeventtype parentAvecPage = evt(30, null, 1, "Parent/Page");
		Admeventtype enfant = evt(31, 30L, 1, "Enfant");
		assertEquals(List.of(31L), ids(service.avecGroupesParents(List.of(enfant), List.of(parentAvecPage, enfant))));
	}

	@Test
	void rienNEstAjouteSiLeGroupeEstDejaAutorise() {
		Admeventtype groupe = evt(1, null, 1, null);
		Admeventtype page = evt(2, 1L, 1, "P");
		assertEquals(List.of(1L, 2L), ids(service.avecGroupesParents(List.of(groupe, page), List.of(groupe, page))));
	}

	@Test
	void lOrdreSuitLeRang() {
		Admeventtype groupe = evt(9030, null, 3, null);
		Admeventtype autre = evt(9010, null, 1, null);
		Admeventtype page = evt(9035, 9030L, 5, "P");
		Admeventtype pageAutre = evt(9011, 9010L, 1, "Q");

		List<Admeventtype> resultat = service.avecGroupesParents(List.of(pageAutre, page),
				List.of(groupe, autre, page, pageAutre));

		assertEquals(List.of(9010L, 9011L, 9030L, 9035L), ids(resultat));
	}

	@Test
	void lesPagesDuProfilSontImbriqueesSousLeurGroupeDansLeMenu() {
		Admsubmodule module = new Admsubmodule();
		module.setSum_id(61L);
		module.setSum_name("Remboursements");
		Admeventtype groupe = evt(9030, null, 3, null);
		Admeventtype page = evt(9035, 9030L, 5, "Cons/Mut");

		List<JsonResponse> menu = service.convertAdmEntities(List.of(module),
				service.avecGroupesParents(List.of(page), List.of(groupe, page)));

		JsonResponse mod = menu.get(0);
		assertEquals(1, mod.getSubItems().size());
		assertEquals("evt9030", mod.getSubItems().get(0).getLabel());
		assertEquals("evt9035", mod.getSubItems().get(0).getSubItems().get(0).getLabel());
	}
}
