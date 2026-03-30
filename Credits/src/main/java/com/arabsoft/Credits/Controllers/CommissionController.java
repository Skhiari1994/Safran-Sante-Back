package com.arabsoft.Credits.Controllers;

import com.arabsoft.Credits.Entities.Commission;
import com.arabsoft.Credits.Entities.DetailsCommission;
import com.arabsoft.Credits.Entities.Response.ResponseProcedure;
import com.arabsoft.Credits.Projections.DetailsCommissionProjection;
import com.arabsoft.Credits.Projections.PersonnelPrejection;
import com.arabsoft.Credits.Repositories.CommissionRepository;
import com.arabsoft.Credits.Repositories.DetailsCommissionRepository;
import com.arabsoft.Credits.Repositories.PersonnelRepository;
import com.arabsoft.Credits.Services.CommissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Commission")
public class CommissionController {

    @Autowired
    CommissionService commissionService;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    DetailsCommissionRepository detailsCommissionRepository;
    @Autowired
    PersonnelRepository personnelRepository;
    @GetMapping("/preparComm")
    ResponseProcedure preparComm(@RequestParam String codSoc, @RequestParam String datDebComm, @RequestParam String datFinComm, @RequestParam Integer numComm)
    {
        return commissionService.prepar_commission(codSoc,datDebComm,datFinComm,numComm);
    }

    @GetMapping("/getCommission")
    List<Commission> getCommission()
    {
        return commissionRepository.getCommissionDESC();
    }
    @PostMapping("/addCommission")
    Commission addCommission(@RequestBody Commission commission){
       return commissionRepository.save(commission);
    }
    @GetMapping("/getDetailCommission/{soc}/{num}")
    List<DetailsCommissionProjection> getDetailCommission(@PathVariable String soc , @PathVariable Long num)
    {
        return detailsCommissionRepository.getDetailComm(soc,num);
    }
    @GetMapping("/getCommissionPrepare/{soc}")
    List<Commission> getCommissionPreparer(@PathVariable String soc )
    {
        return commissionRepository.getCommissionPreparer(soc);
    }
    @GetMapping("/getCommissionValider/{soc}")
    List<Commission> getCommissionValider(@PathVariable String soc )
    {
        return commissionRepository.getCommissionValider(soc);
    }
    @GetMapping("/getPersonnelCredit/{soc}")
    List<PersonnelPrejection> getPersonnelCredit(@PathVariable String soc )
    {
        return personnelRepository.getPersonnelCredit(soc);
    }
    @GetMapping("/getPersonnelCreditByMat/{soc}/{mat}")
    PersonnelPrejection getPersonnelCreditByMat(@PathVariable String soc,@PathVariable String mat )
    {
        return personnelRepository.getPersonnelCreditByMat(soc,mat);
    }
}
