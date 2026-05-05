package com.tn.arabsoft.remboursement_frais_medicaux.service;

import org.springframework.stereotype.Service;

import com.tn.arabsoft.remboursement_frais_medicaux.repositories.BordArriverRepository;
import com.tn.arabsoft.remboursement_frais_medicaux.repositories.BordEnvoiRepository;

@Service

public class BordArriverCnamService {
    private final BordArriverRepository repository;
    private final BordEnvoiRepository bordEnvoiRepository;

    public BordArriverCnamService(BordArriverRepository repository, BordEnvoiRepository bordEnvoiRepository) {
        this.repository = repository;
        this.bordEnvoiRepository = bordEnvoiRepository;
    }

    public String getGeneratedCodBord(String codSoc) {
        return repository.generateCodBord(codSoc);
    }

    public int validateBord(String codSoc, String codAssur, String codBord) {
        return bordEnvoiRepository.markValidBord(codSoc, codAssur, codBord);
    }
}
