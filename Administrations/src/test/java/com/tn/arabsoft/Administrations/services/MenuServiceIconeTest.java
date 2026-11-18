package com.tn.arabsoft.Administrations.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.tn.arabsoft.administrations.entities.Admeventtype;
import com.tn.arabsoft.administrations.entities.Admstorageautorisation;
import com.tn.arabsoft.administrations.entities.Admsubmodule;
import com.tn.arabsoft.administrations.entities.JsonResponse;
import com.tn.arabsoft.administrations.entities.Role;
import com.tn.arabsoft.administrations.repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.administrations.services.MenuService;

/** Le menu renvoyé au front transmet l'icône des modules (mdl_icon1) et des entrées de niveau 1 (evt_icon). */
class MenuServiceIconeTest {

	private static Admeventtype evt(long id, String nom, Long parent, String icone) {
		Admeventtype e = new Admeventtype();
		e.setEvt_id(id);
		e.setEvt_name(nom);
		e.setEvt_evt_id(parent);
		e.setSum_id(1L);
		e.setEvt_icon(icone);
		return e;
	}

	private static Admstorageautorisation autorisation(Admeventtype e) {
		Admstorageautorisation a = new Admstorageautorisation();
		a.setAdmeventtype(e);
		return a;
	}

	@Test
	void lesIconesDuModuleEtDesEntreesSontTransmises() {
		AdmstorageautorisationDAO dao = mock(AdmstorageautorisationDAO.class);
		Admeventtype avecIcone = evt(10, "Edition", null, "ri-printer-line");
		Admeventtype sansIcone = evt(11, "Fiche adherent", null, null);
		when(dao.getMenuRole(anyLong(), anyLong()))
				.thenReturn(List.of(autorisation(avecIcone), autorisation(sansIcone)));

		Admsubmodule module = new Admsubmodule();
		module.setSum_id(1L);
		module.setSum_name("Gestion des adhesions");
		module.setMdl_icon1("ri-team-line");
		Role role = new Role();
		role.setId(5L);

		List<JsonResponse> menu = new MenuService(dao).convertAdmEntities(List.of(module),
				List.of(avecIcone, sansIcone), role);

		assertEquals(1, menu.size());
		assertEquals("ri-team-line", menu.get(0).getIcon());
		List<JsonResponse> enfants = menu.get(0).getSubItems();
		assertEquals(2, enfants.size());
		assertEquals("ri-printer-line", enfants.get(0).getIcon());
		assertEquals("", enfants.get(1).getIcon(), "sans icône en base : chaîne vide, le front choisit l'icône par défaut");
	}
}
