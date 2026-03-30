package com.arabsoft.gestionconvention.Entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class MotifSuspConv {
     @Id
    private String cod_mot_susp;
    private String  lib_mot_susp;

}
