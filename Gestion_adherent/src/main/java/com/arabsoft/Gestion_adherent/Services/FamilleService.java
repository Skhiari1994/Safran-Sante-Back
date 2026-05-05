package com.arabsoft.gestion_adherent.services;

import java.util.List;

import com.arabsoft.gestion_adherent.entities.Famille;

public interface FamilleService {

    List<Famille> getFamille(String matpers, String codSoc);

}
