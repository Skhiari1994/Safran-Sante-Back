package com.tn.arabsoft.administrations.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tn.arabsoft.administrations.repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.administrations.entities.Admeventtype;
import com.tn.arabsoft.administrations.entities.Admstorageautorisation;
import com.tn.arabsoft.administrations.entities.Admsubmodule;
import com.tn.arabsoft.administrations.entities.JsonResponse;
import com.tn.arabsoft.administrations.entities.Role;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final AdmstorageautorisationDAO admstorageautorisationDAO;

	public List<JsonResponse> convertAdmEntities(List<Admsubmodule> admSubmodules, List<Admeventtype> admEventTypes,
			Role role) {
		Map<Long, List<Admeventtype>> eventTypeMap = groupEventTypesBySubmodule(admEventTypes);
		List<JsonResponse> jsonResponses = new ArrayList<>();

		for (Admsubmodule submodule : admSubmodules) {
			if (hasAuthorization(role, submodule)) {
				JsonResponse jsonResponse = createJsonResponse(submodule, eventTypeMap, role);
				jsonResponses.add(jsonResponse);
			}
		}

		return jsonResponses;
	}

	public List<JsonResponse> convertAdmEntities(List<Admsubmodule> admSubmodules, List<Admeventtype> admEventTypes) {
		List<JsonResponse> jsonResponses = new ArrayList<>();
		Map<Long, List<Admeventtype>> eventTypeMap = new HashMap<>();

		// Group AdmEventType entities by sum_id
		for (Admeventtype eventType : admEventTypes) {
			Long sumId = eventType.getSum_id();
			eventTypeMap.putIfAbsent(sumId, new ArrayList<>());
			eventTypeMap.get(sumId).add(eventType);
		}

		// Convert AdmSubmodule entities along with associated AdmEventType entities
		for (Admsubmodule submodule : admSubmodules) {
			JsonResponse jsonResponse = createJsonResponse(submodule, eventTypeMap);
			jsonResponses.add(jsonResponse);
		}

		return jsonResponses;
	}

	private Map<Long, List<Admeventtype>> groupEventTypesBySubmodule(List<Admeventtype> admEventTypes) {
		Map<Long, List<Admeventtype>> eventTypeMap = new HashMap<>();
		for (Admeventtype eventType : admEventTypes) {
			Long sumId = eventType.getSum_id();
			eventTypeMap.computeIfAbsent(sumId, k -> new ArrayList<>()).add(eventType);
		}
		return eventTypeMap;
	}

	private boolean hasAuthorization(Role role, Admsubmodule submodule) {
		List<Admstorageautorisation> authorisations = admstorageautorisationDAO.getMenuRole(submodule.getSum_id(),
				role.getId());
		return !authorisations.isEmpty();
	}

	private JsonResponse createJsonResponse(Admsubmodule submodule, Map<Long, List<Admeventtype>> eventTypeMap,
			Role role) {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setId(submodule.getSum_id());
		jsonResponse.setLabel(submodule.getSum_name());
		jsonResponse.setIcon(submodule.getMdl_icon1()); // Assuming icon is stored in mdl_icon as Base64 encoded string

		// Vérifier si sum_rout n'est pas null avant de le concaténer
		String sumRout = submodule.getSum_rout();
		jsonResponse.setLink(sumRout != null ? "/" + sumRout : "");

		jsonResponse.setCollapseid(submodule.getCollapseid());
		jsonResponse.setParentId(99L);

		// Obtenir les autorisations pour le rôle et le sous-module
		List<Admstorageautorisation> authorisations = admstorageautorisationDAO.getMenuRole(submodule.getSum_id(),
				role.getId());

		// Gestion des enfants (sub-items) et de IsCollapsed pour le nœud parent
		if (authorisations != null && !authorisations.isEmpty()) {
			List<JsonResponse> children = new ArrayList<>();

			for (Admstorageautorisation auth : authorisations) {
				Admeventtype eventType = auth.getAdmeventtype();
				if (eventType != null) {
					JsonResponse child = createChildJsonResponse(eventType, eventTypeMap);
					children.add(child);
				}
			}

			// Vérifier si des enfants existent pour configurer IsCollapsed
			jsonResponse.setSubItems(children.isEmpty() ? null : children);
			jsonResponse.setCollapsed(!children.isEmpty()); // Collapsed = true si des enfants existent
		} else {
			jsonResponse.setSubItems(null);
			jsonResponse.setCollapsed(false); // Pas d'enfants, donc Collapsed = false
		}

		return jsonResponse;
	}

	private JsonResponse createChildJsonResponse(Admeventtype eventType, Map<Long, List<Admeventtype>> eventTypeMap) {
		JsonResponse child = new JsonResponse();
		child.setId(eventType.getEvt_id());
		child.setLabel(eventType.getEvt_name());
		child.setIcon("");
		child.setLink(eventType.getEvt_action() != null ? eventType.getEvt_action() : "");
		child.setParentId(eventType.getEvt_evt_id());

		// Check for nested event types and create sub-items if present
		List<Admeventtype> nestedEventTypes = eventTypeMap.get(eventType.getEvt_id());
		if (nestedEventTypes != null && !nestedEventTypes.isEmpty()) {
			List<JsonResponse> nestedChildren = new ArrayList<>();
			for (Admeventtype nestedEventType : nestedEventTypes) {
				JsonResponse nestedChild = createChildJsonResponse(nestedEventType, eventTypeMap);
				nestedChildren.add(nestedChild);
			}
			child.setSubItems(nestedChildren);
			child.setCollapsed(true); // Node has children, set IsCollapsed = true
		} else {
			child.setSubItems(null);
			child.setCollapsed(false); // No children, set IsCollapsed = false
		}

		return child;
	}

	private JsonResponse createJsonResponse(Admsubmodule submodule, Map<Long, List<Admeventtype>> eventTypeMap) {
		JsonResponse jsonResponse = new JsonResponse();
		jsonResponse.setId(submodule.getSum_id());
		jsonResponse.setLabel(submodule.getSum_name());
		jsonResponse.setIcon(submodule.getMdl_icon1()); // Suppose que l'icône est stockée dans mdl_icon
		jsonResponse.setLink(submodule.getSum_rout() != null ? "/" + submodule.getSum_rout() : ""); // Si sum_rout est
																									// null, le lien
																									// reste vide
		jsonResponse.setParentId(99L);

		// Vérifier les types d'événements associés à ce sous-module
		if (eventTypeMap.containsKey(submodule.getSum_id())) {
			List<JsonResponse> children = new ArrayList<>();
			Map<Long, JsonResponse> childMap = new HashMap<>();

			// Créer d'abord tous les enfants sans imbriquer
			for (Admeventtype eventType : eventTypeMap.get(submodule.getSum_id())) {
				JsonResponse child = createChildJsonResponse(eventType, submodule.getSum_rout(), eventTypeMap);
				children.add(child);
				childMap.put(eventType.getEvt_id(), child);
			}

			// Imbriquer correctement les enfants basés sur evt_evt_id
			List<JsonResponse> topLevelChildren = new ArrayList<>();
			for (JsonResponse child : children) {
				if (child.getParentId() != null) {
					JsonResponse parent = childMap.get(child.getParentId());
					if (parent != null) {
						if (parent.getSubItems() == null) {
							parent.setSubItems(new ArrayList<>());
						}
						parent.getSubItems().add(child);
					}
				} else {
					child.setParentId(submodule.getSum_id());
					topLevelChildren.add(child);
				}
			}
			jsonResponse.setSubItems(topLevelChildren.isEmpty() ? null : topLevelChildren);
			jsonResponse.setCollapsed(topLevelChildren.isEmpty() ? false : true); // Définir IsCollapsed en fonction des
																					// enfants
		} else {
			jsonResponse.setSubItems(null);
			jsonResponse.setCollapsed(false);
		}

		return jsonResponse;
	}

	private JsonResponse createChildJsonResponse(Admeventtype eventType, String submoduleRoute,
			Map<Long, List<Admeventtype>> eventTypeMap) {
		JsonResponse child = new JsonResponse();
		child.setId(eventType.getEvt_id());
		child.setLabel(eventType.getEvt_name());
		child.setIcon("");

		// Construire le lien uniquement si submoduleRoute et evt_action ne sont pas
		// null
		String link = "";
		if (submoduleRoute != null && eventType.getEvt_action() != null) {
			link = "/" + submoduleRoute + "/" + eventType.getEvt_action();
		} else if (submoduleRoute != null) {
			link = "/" + submoduleRoute;
		} else if (eventType.getEvt_action() != null) {
			link = "/" + eventType.getEvt_action();
		}
		child.setLink(link);

		child.setParentId(eventType.getEvt_evt_id());

		// Vérification des types d'événements imbriqués
		if (eventTypeMap.containsKey(eventType.getEvt_id())) {
			List<JsonResponse> nestedChildren = new ArrayList<>();
			for (Admeventtype nestedEventType : eventTypeMap.get(eventType.getEvt_id())) {
				JsonResponse nestedChild = createChildJsonResponse(nestedEventType, submoduleRoute, eventTypeMap);
				nestedChildren.add(nestedChild);
			}
			child.setSubItems(nestedChildren.isEmpty() ? null : nestedChildren);
			child.setCollapsed(true); // Noeud avec des enfants imbriqués, IsCollapsed = true
		} else {
			child.setSubItems(null);
			child.setCollapsed(false); // Pas d'enfants imbriqués, IsCollapsed = false
		}

		return child;
	}

}
