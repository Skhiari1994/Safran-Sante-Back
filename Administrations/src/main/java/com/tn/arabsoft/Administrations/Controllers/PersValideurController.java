package com.tn.arabsoft.Administrations.Controllers;

import com.tn.arabsoft.Administrations.Entities.PERS_VALIDEUR;
import com.tn.arabsoft.Administrations.Entities.Role;
import com.tn.arabsoft.Administrations.Projections.PersValideurProjection;
import com.tn.arabsoft.Administrations.Repositories.PERS_VALIDEUR_Dao;
import com.tn.arabsoft.Administrations.Repositories.RoleDao;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/PERS_VALIDEUR")
public class PersValideurController {
    @Autowired
    PERS_VALIDEUR_Dao dao;
     @Autowired
     RoleDao roleDao;
    @GetMapping("/getpersvalideur/{cod}/{mat}")
    public List<PersValideurProjection> getPERS_VALIDEURbyId(@PathVariable("cod")String cod, @PathVariable("mat")String mat){
        return dao.getall(cod,mat);}
    @GetMapping("/matchefbyniv/{matChef}/{mat}")
    public PersValideurProjection matchefbyniv(@PathVariable("matChef")String matChef, @PathVariable("mat")String mat) {
        return dao.matchefbyniv(matChef,mat);
    }

    @GetMapping("/getPersValid")
    public List<PersValideurProjection> getPersValid() {
        return dao.getPersValid();
    }

    @PostMapping("/addPersValid")
    public void addPers(@RequestBody PERS_VALIDEUR pers)
    {
        try {
            String soc=pers.getCod_soc();
            String mat=pers.getMat_pers();
            String mat_resp=pers.getMat_resp();
            Long niv=pers.getNiveau();

            dao.addPersValid(soc, mat, mat_resp, niv);
            dao.commit();
        } catch (Exception e) {
            System.out.println(e);
        }



    }

    @PutMapping("/updatePersValid")
    public void updatePers(@RequestBody PERS_VALIDEUR pers)
    {
        try {
            String soc=pers.getCod_soc();
            String mat=pers.getMat_pers();
            String mat_resp=pers.getMat_resp();
            Long niv=pers.getNiveau();
            dao.updatePersValid(soc, mat, mat_resp, niv);
            dao.commit();
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    @PostMapping("/AddRole")
    public Role addRole(@RequestBody Role role)
    {
        return roleDao.save(role);

    }
//    @Transactional
//    @DeleteMapping("/DeletePersValide/{cod_soc}/{mat_pers}/{mat_resp}")
//    public ResponseEntity<Void> DeletePersValide(@PathVariable String cod_soc, @PathVariable String mat_pers, @PathVariable String mat_resp) {
//        dao.DeletePersValideur(cod_soc, mat_pers, mat_resp);
//        return ResponseEntity.ok("OK");
//    }
    @Transactional
    @DeleteMapping("/DeletePersValide/{cod_soc}/{mat_pers}/{mat_resp}")
    public ResponseEntity<Void> deletePersValide(@PathVariable String cod_soc,
                                                 @PathVariable String mat_pers,
                                                 @PathVariable String mat_resp) {
        try {
            // Perform the delete operation
            dao.DeletePersValideur(cod_soc, mat_pers, mat_resp);

            // Return an HTTP 200 OK status without a body (since ResponseEntity<Void>)
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Log the error (optional) and return HTTP 500 Internal Server Error without a body
            System.out.println("Error during deletion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/DeleteRole/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId) {
        roleDao.deleteById(roleId);
        return ResponseEntity.ok().build();
    }

}
