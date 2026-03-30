package com.tn.arabsoft.Administrations.Controllers;


import com.tn.arabsoft.Administrations.Repositories.AdmeventtypeDAO;

import com.tn.arabsoft.Administrations.Repositories.AdmstorageautorisationDAO;
import com.tn.arabsoft.Administrations.Repositories.AdmsubmoduleDAO;
import com.tn.arabsoft.Administrations.Repositories.RoleDao;
import com.tn.arabsoft.Administrations.Services.MenuService;
import com.tn.arabsoft.Administrations.Entities.Admeventtype;
import com.tn.arabsoft.Administrations.Entities.Admstorageautorisation;
import com.tn.arabsoft.Administrations.Entities.Admsubmodule;
import com.tn.arabsoft.Administrations.Entities.JsonResponse;
import com.tn.arabsoft.Administrations.Entities.Role;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Menu")
public class MenuController {

    @Autowired   
    private AdmsubmoduleDAO submoduleRepository;

    @Autowired
    private AdmeventtypeDAO eventTypeRepository;

    @Autowired
    private MenuService jsonConverter;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    
    private AdmstorageautorisationDAO admstorageautorisationDAO;


    
    
    @GetMapping("/menu/{roleId}")
    public ResponseEntity<List<JsonResponse>> getMenuForRole(@PathVariable Long roleId) {
         //Role roleEntity = roleDao.findByName(roleId);

         List<Admsubmodule> submodules = submoduleRepository.getListSubmoduleByRole(roleId);
        List<Admeventtype> eventTypes = eventTypeRepository.getListAdmeventtypeByRole(roleId);

        // Generate menu based on role
        List<JsonResponse> menu = jsonConverter.convertAdmEntities(submodules, eventTypes);

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @GetMapping("/menu")
    public ResponseEntity<List<JsonResponse>> getMenu() {

         List<Admsubmodule> submodules = submoduleRepository.getListSubmoduleAdminis();  
        List<Admeventtype> eventTypes = eventTypeRepository.getListAdmeventtype();  

        // Generate menu based on role
        List<JsonResponse> menu = jsonConverter.convertAdmEntities(submodules, eventTypes);

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

    @GetMapping("/suModule")
    public List<Admsubmodule> getAdmSubModule() {
        return submoduleRepository.getListSubmoduleAdminis(); 
    }
    @GetMapping("/evtTypeNiv/{sum}")
    public List<Admeventtype> getEvtTypeNiv(@PathVariable String sum) {
        return eventTypeRepository.getListAdmeventtypeNiv(sum); 
    }

    @GetMapping("/evtTypeNiv1/{evt}")
    public List<Admeventtype> getEvtTypeNiv1(@PathVariable String evt) {
        return eventTypeRepository.getListAdmeventtypeNiv1(evt); 
    }

    @GetMapping("/evtTypeNiv2/{evt}")
    public List<Admeventtype> getEvtTypeNiv2(@PathVariable String evt) {
        return eventTypeRepository.getListAdmeventtypeNiv2(evt); 
    }

    @PostMapping("/addSubModule")
    public Admsubmodule saveSubModule(@RequestBody Admsubmodule adm) {
        adm.setMod_id(1L); 
        return submoduleRepository.save(adm);
    }

    @PostMapping("/addEvntType1")
    public Admeventtype saveAddEvntType(@RequestBody Admeventtype adm) {
        return eventTypeRepository.save(adm);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        try {
             if (!submoduleRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

             submoduleRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/evt/{id}")
    public ResponseEntity<Void> deleteDemandeEvt(@PathVariable Long id) {
        try {
            // Check if the entity exists
            if (!eventTypeRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Delete the entity
            eventTypeRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/role")
    public List<Role> getRole()
    {
    	return roleDao.findAll();
    }
    @GetMapping("/roles/{roleId}/modules")
    public List<Admstorageautorisation> getModukeByRole(@PathVariable("roleId") Long roleId)
    {
    	return admstorageautorisationDAO.getAdmStorageByRole(roleId);
    }
    
    @GetMapping("/roles/{roleId}")
    public List<Admstorageautorisation> getModukeByR(@PathVariable("roleId") Long roleId)
    {
    	Role role =new Role();
    	role.setId(roleId);
    	return admstorageautorisationDAO.findByRole(role);
    }
    @GetMapping("/findAll/{sum_id}/{role}")
    public List<Admstorageautorisation> findAllMenu(@PathVariable("sum_id") Long sum_id,@PathVariable("role") Long role)
    {
    	return admstorageautorisationDAO.getMenuRole(sum_id,role);
    }
    @PostMapping("/saveAuto")
    public Admstorageautorisation saveAutorisation(@RequestBody Admstorageautorisation admstorageautorisation) {
        Admeventtype admeventtype = admstorageautorisation.getAdmeventtype();
        
        if (admeventtype != null) {
            if (admeventtype.getEvt_id() == null) {
                
                eventTypeRepository.save(admeventtype);
            } else {
                admeventtype = eventTypeRepository.findById(admeventtype.getEvt_id()).orElse(null);
                admstorageautorisation.setAdmeventtype(admeventtype);
            }
        }
        
        return admstorageautorisationDAO.save(admstorageautorisation);
    }

    
    @DeleteMapping("/Autorisation")
    public ResponseEntity<Void> deleteAuthorization(@RequestParam Long sum_id, @RequestParam Long role, @RequestParam(required = false) Long evt) {
        try {
        	admstorageautorisationDAO.deleteFromAdmautorisation(sum_id, role, evt);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/getActionProfil/{profil}")
    public List<Admeventtype> findAllMenu(@PathVariable("profil") Long profil)
    {
        return eventTypeRepository.getActionsByRole(profil);
    }

}    
    
