package com.arabsoft.auth.service;

import com.arabsoft.auth.dto.DelaisSessionDto;
import com.arabsoft.auth.model.ParamGrh;
import com.arabsoft.auth.repository.ParamGrhDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParamGrhService {

    private final ParamGrhDao paramGrhDao;

    // Pour délais d'inactivité
    public static final String DELAI_INACTIVITE_MS = "DELAI_INACTIVITE_MS";
    // Pour délais d'avertissement
    public static final String DELAI_AVERTISSEMENT_MS = "DELAI_AVERTISSEMENT_MS";

    public DelaisSessionDto getDelaisSession() {
        Map<String, String> valeurs = paramGrhDao
                .findByLibParamIn(List.of(DELAI_INACTIVITE_MS, DELAI_AVERTISSEMENT_MS))
                .stream()
                .collect(Collectors.toMap(ParamGrh::getLibParam, ParamGrh::getValeur1));

        System.out.println("valeurs des delais : " + valeurs);

        return new DelaisSessionDto(
                lire(valeurs, DELAI_INACTIVITE_MS, 3 * 60_000L),
                lire(valeurs, DELAI_AVERTISSEMENT_MS, 2 * 60_000L));
    }

    private long lire(Map<String, String> valeurs, String cle, long defaut) {
        try {
            long v = Long.parseLong(valeurs.get(cle).trim());
            return v > 0 ? v : defaut;
        } catch (Exception e) { // absent, null ou non numérique
            return defaut;
        }
    }

}
