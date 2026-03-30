package com.arabsoft.Credits.Controllers;

import com.arabsoft.Credits.Projections.PersProjection;
import com.arabsoft.Credits.Projections.PersonnelAnticip;
import com.arabsoft.Credits.Repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Personnel")
@EnableCaching
public class PersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;

    @GetMapping("/getPersonnelAnticip/{soc}")
    List<PersonnelAnticip> getPersonnelAnticip(@PathVariable String soc )
    {
        return personnelRepository.getPersonnelsAnticipe(soc);
    }

    @GetMapping("/getPersonnelAnticipByMat/{soc}/{mat}")
    PersonnelAnticip getPersonnelAnticip(@PathVariable String soc,@PathVariable String mat )
    {
        return personnelRepository.getPersonnelsAnticipeByMat(soc,mat);
    }

    @GetMapping("/getPersonnels")
    @Cacheable("personnel")
    public List <PersProjection> getPersonnels(){
        return this.personnelRepository.getPersonnels();

    }
}
