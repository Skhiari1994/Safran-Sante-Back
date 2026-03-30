package com.arabsoft.Gestion_adherent.Services;

import com.arabsoft.Gestion_adherent.Entities.Famille;

import java.util.List;

public interface FamilleService {

    List<Famille> getFamille(String matpers,String codSoc);

}
