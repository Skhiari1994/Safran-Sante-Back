package com.tn.arabsoft.RemboursementFraisMedicaux.Service;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.CnamFileGen;
import com.tn.arabsoft.RemboursementFraisMedicaux.Repositories.CnamFileGenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
public class CnamFileGenService {
    private final CnamFileGenRepository fileGenRepository;

    public CnamFileGenService(CnamFileGenRepository fileGenRepository) {
        this.fileGenRepository = fileGenRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CnamFileGen> getFileByCodSocAndCodBord(String cod_soc, String cod_bord) {
        return fileGenRepository.findByCodSocAndCodBord(cod_soc, cod_bord);
    }

    // Convert CLOB to byte array for download
    public byte[] convertClobToBytes(String clobData) {
        return clobData.getBytes(StandardCharsets.UTF_8);
    }
}
