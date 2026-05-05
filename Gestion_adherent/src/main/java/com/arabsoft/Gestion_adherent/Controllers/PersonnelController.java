package com.arabsoft.gestion_adherent.controllers;

import com.arabsoft.gestion_adherent.services.AffilPersService;
import com.arabsoft.gestion_adherent.services.ChargFichierService;
import com.arabsoft.gestion_adherent.services.FamilleServiceImpl;
import com.arabsoft.gestion_adherent.dto.ResponseProcedureCharge;
import com.arabsoft.gestion_adherent.entities.*;
import com.arabsoft.gestion_adherent.projections.*;
import com.arabsoft.gestion_adherent.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Personnel")
@SuppressWarnings({ "java:S117" })
public class PersonnelController {

    private final PersonnelRepository personnelRepository;
    private final NationaliteRepository nationaliteRepository;
    private final PrmLieuGeogRepository prmLieuGeogRepository;
    private final AffectationRepository affectationRepository;
    private final TypeDepartRepository typeDepartRepository;
    private final BanqueRepository banqueRepository;
    private final AgenceRepository agenceRepository;
    private final GouvernoratRepository gouvernoratRepository;
    private final PosteRepository posteRepository;
    private final AdrPersRepository adrPersRepository;
    private final PhotoPersRepository photoPersRepository;
    private final CertifFamilleRepository familleRepository;
    private final FamilleServiceImpl familleService;
    private final AffilMutuelleRepository affilMutuelleRepository;
    private final CorpsRepository corpsRepository;
    private final AffilPersService affilPersService;
    private final DepartPersRepository departPersRepository;
    private final DiskPretRepository diskPretRepository;
    private final ChargFichierService chargFichierService;

    private final TrsPersRepository trsPersRepository;

    @PostMapping("/addUser")
    Personnel savePersonnel(@RequestBody Personnel personnel) {
        return personnelRepository.save(personnel);
    }

    @PostMapping(value = "/addPhoto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public PhotoPers addPhotoPersonnel(
            @RequestPart("file") MultipartFile file,
            @RequestPart("photoPers") String photoPers) throws Exception {

        PhotoPers photo = new ObjectMapper().readValue(photoPers, PhotoPers.class);
        photo.setPhoto(file.getBytes());
        photo.setFile_name(file.getOriginalFilename());
        photo.setContent_type(file.getContentType());
        photo.setPath("DB");

        try {
            return photoPersRepository.save(photo);
        } catch (Exception e) {
            throw new Exception("Could not save File: " + e.getMessage());
        }
    }

    @PostMapping("/addAdresse")
    AdrPers addAdresse(@RequestBody AdrPers adresse) {
        return adrPersRepository.save(adresse);
    }

    @GetMapping("/getPersonnels")
    public List<PersonnelPrejection> getPersonnels() {
        return this.personnelRepository.getPersonnels();

    }

    @GetMapping("/getPersonnelles/{soc}/{mat}")
    public List<PersonnelPrejection> getPersonelles(@PathVariable("soc") String soc, @PathVariable("mat") String mat) {
        return this.personnelRepository.getPersonnelByMat(soc, mat);

    }

    @GetMapping("/getPersonnellesEnInstance")
    public List<PersonnelPrejection> getPersonnellesEnInstance() {
        return this.personnelRepository.getPersonnelEnInstance();

    }

    @GetMapping("/getPersonnelles")
    public List<Personnel> getAllPersonelles() {
        return this.personnelRepository.findAll();

    }

    @GetMapping("/getNationalite")
    List<Nationalite> getNationalite() {
        return nationaliteRepository.findAll();
    }

    @GetMapping("/getLieu")
    List<PrmLieuGeographique> getLieu() {
        return prmLieuGeogRepository.findAll();
    }

    @GetMapping("/getAffectation")
    List<Affectation> getAffectation() {
        return affectationRepository.findAll();
    }

    @GetMapping("/getBanque")
    List<Banque> getBanque() {
        return banqueRepository.findAll();
    }

    @GetMapping("/getTypeDepart")
    List<TypeDepart> getTypeDepart() {
        return typeDepartRepository.findAll();
    }

    @GetMapping("/getAgence/{codBanq}")
    List<Agence> getAgence(@PathVariable String codBanq) {
        return agenceRepository.getAgence(codBanq);
    }

    @GetMapping("/getGouvernorat")
    List<Gouvernorat> getGouvernorat() {
        return gouvernoratRepository.findAll();
    }

    @GetMapping("/getPoste/{gouv}")
    List<Poste> getPoste(@PathVariable String gouv) {
        return posteRepository.getPoste(gouv);
    }

    @GetMapping("/getAdresse/{soc}/{mat}")
    List<AdrPersProjection> getAdresse(@PathVariable String soc, @PathVariable String mat) {
        return adrPersRepository.getAdresse(soc, mat);
    }

    @GetMapping("/getMaxNumAdr/{soc}/{mat}")
    Long getMaxNumAdr(@PathVariable String soc, @PathVariable String mat) {
        return adrPersRepository.getMaxNumAdr(soc, mat);
    }

    @GetMapping("/getPhoto/{soc}/{mat}")
    PhotoPers getPhoto(@PathVariable String soc, @PathVariable String mat) {
        return photoPersRepository.getPhotoPersonnel(soc, mat);
    }

    @GetMapping("/getMatInt")
    List<MatIntProjection> getMatInt() {
        return personnelRepository.getMatInt();
    }

    @GetMapping("/getCertifEnf/{mat}/{num_fam}")
    List<CertifFamille> getCertifEnf(@PathVariable String mat, @PathVariable String num_fam) {
        return familleRepository.getCertifEnf(mat, num_fam);
    }

    @PostMapping("/AddCertifEnf")
    public void addCertifEnf(@RequestBody List<CertifFamille> f) {
        familleRepository.saveAll(f);
    }

    @DeleteMapping("/{soc}/{num}/{mat}/{annee}")
    public void deleteCertif(@PathVariable String soc, @PathVariable Long num, @PathVariable String mat,
            @PathVariable String annee) {
        familleService.deleteCertif(soc, num, mat, annee);
    }

    @GetMapping("/getAffilMutuelle")
    List<AffilMutuelle> getAffilMutuelle() {
        return affilMutuelleRepository.getAffilMutuelle();
    }

    @GetMapping("/getCorps")
    List<Corps> getCorps() {
        return corpsRepository.findAll();
    }

    @GetMapping("/getAffilPers/{soc}")
    List<AffilPersProjection> getAffilPers(@PathVariable String soc) {
        return affilMutuelleRepository.getAffilPers(soc);
    }

    @GetMapping("/getAffilPersMut/{soc}")
    List<AffilPersProjection> getAffilPersMut(@PathVariable String soc) {
        return affilMutuelleRepository.getAffilPersMut(soc);
    }

    @GetMapping("/getAffilPersByMat/{soc}/{mat}")
    AffilPersProjection getAffilPers(@PathVariable String soc, @PathVariable String mat) {
        return affilMutuelleRepository.getAffilPersByMat(soc, mat);
    }

    @GetMapping("/getAffilPersRenouvAdhesion/{soc}")
    List<AffilMutuelle> getAffilPersRenouvAdhesion(@PathVariable String soc) {
        return affilMutuelleRepository.getAffilPersRenouvAdhesion(soc);
    }

    @GetMapping("/getAffilPersEnInstance/{soc}")
    List<AffilPersValid> getAffilPersEnInstance(@PathVariable String soc) {
        return affilMutuelleRepository.getAffilPersEnInstance(soc);
    }

    @PostMapping("/AddAffiliation")
    public void addCertifEnf(@RequestBody AffilMutuelle affilMutuelle) {
        affilMutuelleRepository.save(affilMutuelle);
    }

    @DeleteMapping("/deleteAffil")
    public void deleteAffil(@RequestParam("soc") String soc, @RequestParam("mat") String mat,
            @RequestParam("dat") String dat) {
        affilPersService.deleteAffilPers(soc, mat, dat);
    }

    @PutMapping("/updateAffil/{etat}")
    public ResponseEntity<Void> updateEtat(
            @RequestBody AffilMutuelle aff,
            @PathVariable String etat) {
        affilPersService.updateEtat(aff, etat);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/callValiderAff")
    public void validerAff(@RequestParam("soc") String soc, @RequestParam("etat_aff") String etat_aff,
            @RequestParam("typ_aff") String typ_aff, @RequestParam("mat") String mat, @RequestParam("dat") String dat) {
        affilPersService.validerAff(soc, etat_aff, typ_aff, mat, dat);
    }

    @GetMapping("/listDepart/{soc}")
    List<DepartPersProjection> getListDepart(@PathVariable("soc") String soc) {
        return departPersRepository.getListDepart(soc);
    }

    @GetMapping("/listReinteg/{soc}")
    List<DepartPersProjection> listReinteg(@PathVariable("soc") String soc) {
        return departPersRepository.getListReinteg(soc);
    }

    @GetMapping("/listDepartReintegInstance/{soc}")
    List<DepartPersProjection> listDepartReintegInstance(@PathVariable("soc") String soc) {
        return departPersRepository.getListDepartReintegInstance(soc);
    }

    @GetMapping("/listPersonnelDepart/{soc}")
    List<ListPersonnelDepartProjection> getListPersonnelDepart(@PathVariable("soc") String soc) {
        return departPersRepository.getListPersonnelDepart(soc);
    }

    @GetMapping("/listPersonnelReinteg/{soc}")
    List<ListPersonnelDepartProjection> getListPersonnelReinteg(@PathVariable("soc") String soc) {
        return departPersRepository.getListPersonnelReinteg(soc);
    }

    @PostMapping("/AddDepartPers")
    public void addDepartPers(@RequestBody DepartPers departPers) {
        departPersRepository.save(departPers);
    }

    @GetMapping("/getDiskPret")
    List<DiskPret> getDiskPret(@RequestParam String mois) {
        YearMonth ym = parseYearMonth(mois);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.plusMonths(1).atDay(1);
        return diskPretRepository.getDiskPret(start, end);
    }

    private YearMonth parseYearMonth(String input) {

        input = input.trim();

        List<DateTimeFormatter> ymFormatters = List.of(
                DateTimeFormatter.ofPattern("MM/yyyy"),
                DateTimeFormatter.ofPattern("M/yyyy"),
                DateTimeFormatter.ofPattern("MM-yyyy"),
                DateTimeFormatter.ofPattern("M-yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM"),
                DateTimeFormatter.ofPattern("yyyy-MM"),
                DateTimeFormatter.ofPattern("yyyyMM"),
                DateTimeFormatter.ofPattern("MMyyyy"));

        for (DateTimeFormatter formatter : ymFormatters) {
            try {
                return YearMonth.parse(input, formatter);
            } catch (DateTimeParseException ignored) {
                // ignore and try next
            }
        }

        // 2. Full date formats → convert to YearMonth
        List<DateTimeFormatter> dateFormatters = List.of(
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyyMMdd"),
                DateTimeFormatter.ofPattern("ddMMyyyy"));

        for (DateTimeFormatter formatter : dateFormatters) {
            try {
                LocalDate date = LocalDate.parse(input, formatter);
                return YearMonth.from(date);
            } catch (DateTimeParseException ignored) {
                // ignore and try next
            }
        }

        throw new IllegalArgumentException(
                "Format invalide. Formats acceptés: yyyy-MM, MM/yyyy, MMyyyy");

    }

    @GetMapping("/getDiskPretImport")
    List<DiskPretProjection> getDiskPretImport(@RequestParam String mois) {
        YearMonth ym = parseYearMonth(mois);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.plusMonths(1).atDay(1);
        return diskPretRepository.getDiskPretImport(start, end);
    }

    @PostMapping("/import-fichier")
    public ResponseProcedureCharge importFichier(@RequestParam("soc") String soc, @RequestParam("mois") String mois,
            @RequestParam("file") MultipartFile file) throws IOException, SQLException {
        return chargFichierService.lireFichierEtAppelerProcedure(soc, mois, file);
    }

    @GetMapping("/fichier_salarie")
    public ResponseProcedure virBord(@RequestParam String soc, @RequestParam String nomFichier,
            @RequestParam String etat_act) {
        return chargFichierService.fichierSalarie(soc, nomFichier, etat_act);

    }

    @GetMapping("/download-file")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam(defaultValue = "virements.txt") String fileName) {
        byte[] fileBytes = chargFichierService.getVirementFileBytes();

        if (fileBytes == null || fileBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + fileName + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileBytes.length)
                .contentType(MediaType.TEXT_PLAIN)
                .body(fileBytes);
    }

    @GetMapping("/getTrsPers")
    List<TrsPers> getTrsPers() {
        return trsPersRepository.findAll();
    }

    @PostMapping("/addTrsPers")
    TrsPers addTrsPers(@RequestBody TrsPers trsPers) {
        return trsPersRepository.save(trsPers);
    }

    @DeleteMapping("/deleteTrsPers")
    @Transactional
    public ResponseEntity<Void> deleteFromTrs() {

        List<TrsPers> list = trsPersRepository.findAll();

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        for (TrsPers trs : list) {
            if (trs == null) {
                continue; // 🛡 ABSOLUTE PROTECTION
            }
            trsPersRepository.delete(trs);
        }

        return ResponseEntity.ok().build();
    }

}
