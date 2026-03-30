package com.arabsoft.Gestion_adherent.Controllers;

import com.arabsoft.Gestion_adherent.DTO.ResponseProcedureCharge;
import com.arabsoft.Gestion_adherent.Entities.*;
import com.arabsoft.Gestion_adherent.Projections.*;
import com.arabsoft.Gestion_adherent.Repositories.*;
import com.arabsoft.Gestion_adherent.Services.AffilPersService;
import com.arabsoft.Gestion_adherent.Services.ChargFichierService;
import com.arabsoft.Gestion_adherent.Services.FamilleService;
import com.arabsoft.Gestion_adherent.Services.FamilleServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/Personnel")
public class PersonnelController {

    @Autowired
    PersonnelRepository personnelRepository;
    @Autowired
    NationaliteRepository nationaliteRepository;
    @Autowired
    PrmLieuGeogRepository prmLieuGeogRepository;
    @Autowired
    AffectationRepository affectationRepository;
    @Autowired
    TypeDepartRepository typeDepartRepository;
    @Autowired
    BanqueRepository banqueRepository;
    @Autowired
    AgenceRepository agenceRepository;
    @Autowired
    GouvernoratRepository gouvernoratRepository;
    @Autowired
    PosteRepository posteRepository;
    @Autowired
    AdrPersRepository adrPersRepository;
    @Autowired
    PhotoPersRepository photoPersRepository;
    @Autowired
    CertifFamilleRepository familleRepository;
    @Autowired
    FamilleServiceImpl familleService;
    @Autowired
    AffilMutuelleRepository affilMutuelleRepository;
    @Autowired
    CorpsRepository corpsRepository;
    @Autowired
    AffilPersService affilPersService;
    @Autowired
    DepartPersRepository departPersRepository;
    @Autowired
    DiskPretRepository diskPretRepository;
    @Autowired
    ChargFichierService chargFichierService;

    @Autowired
    TrsPersRepository trsPersRepository;

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

    @GetMapping("/getPersonnelles/{mat}")
    public List<PersonnelPrejection> getPersonelles(@PathVariable("mat") String mat) {
        return this.personnelRepository.getPersonnelByMat(mat);

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
    public void AddCertifEnf(@RequestBody List<CertifFamille> f) {

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
    public void AddCertifEnf(@RequestBody AffilMutuelle affilMutuelle) {

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
    public void ValiderAff(@RequestParam("soc") String soc, @RequestParam("etat_aff") String etat_aff,
            @RequestParam("typ_aff") String typ_aff, @RequestParam("mat") String mat, @RequestParam("dat") String dat) {
        affilPersService.ValiderAff(soc, etat_aff, typ_aff, mat, dat);
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
    public void AddDepartPers(@RequestBody Depart_Pers departPers) {

        departPersRepository.save(departPers);

    }

    @GetMapping("/getDiskPret")
    List<DiskPret> getDiskPret(@RequestParam String mois) {
        return diskPretRepository.getDiskPret(mois);

    }

    @GetMapping("/getDiskPretImport")
    List<DiskPretProjection> getDiskPretImport(@RequestParam String mois) {
        return diskPretRepository.getDiskPretImport(mois);

    }

    @PostMapping("/import-fichier")
    public ResponseProcedureCharge importFichier(@RequestParam("soc") String soc, @RequestParam("mois") String mois,
            @RequestParam("file") MultipartFile file) throws Exception {

        ResponseProcedureCharge resp = chargFichierService.lireFichierEtAppelerProcedure(soc, mois, file);
        return resp;

    }

    @GetMapping("/fichier_salarie")
    public ResponseProcedure vir_bord(@RequestParam String soc, @RequestParam String nomFichier,
            @RequestParam String etat_act) {
        return chargFichierService.fichier_salarie(soc, nomFichier, etat_act);

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

        if (list == null || list.isEmpty()) {
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

    /*
     * private final JdbcTemplate jdbcTemplate;
     * 
     * @Value("${file.upload-dir}")
     * private String uploadDir;
     * 
     * public PersonnelController(JdbcTemplate jdbcTemplate) {
     * this.jdbcTemplate = jdbcTemplate;
     * }
     * 
     * 
     * @GetMapping("/download")
     * public ResponseEntity<Resource> downloadEmployeeFile(
     * 
     * @RequestParam String codSoc,
     * 
     * @RequestParam String etatAct) {
     * String fileName = "employees_" + System.currentTimeMillis() + ".txt";
     * String fullFilePath = uploadDir + File.separator + fileName;
     * String message = "";
     * String filePathOut = "";
     * 
     * try (Connection conn = jdbcTemplate.getDataSource().getConnection();
     * CallableStatement stmt = conn.
     * prepareCall("{call ASSUR.pk_adhesions.GENERATE_EMPLOYEE_FILE(?, ?, ?, ?, ?)}"
     * )) {
     * stmt.setString(1, fileName);
     * stmt.setString(2, codSoc);
     * stmt.setString(3, etatAct);
     * stmt.registerOutParameter(4, Types.VARCHAR);
     * stmt.registerOutParameter(5, Types.VARCHAR);
     * stmt.execute();
     * 
     * message = stmt.getString(4);
     * filePathOut = uploadDir + File.separator + stmt.getString(5);
     * 
     * File file = new File(filePathOut);
     * if (!file.exists()) {
     * throw new RuntimeException("Fichier non trouvé: " + filePathOut);
     * }
     * 
     * Path path = Paths.get(filePathOut);
     * Resource resource = new UrlResource(path.toUri());
     * 
     * return ResponseEntity.ok()
     * .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
     * file.getName() + "\"")
     * .header(HttpHeaders.CONTENT_TYPE, "text/plain")
     * .body(resource);
     * } catch (Exception e) {
     * throw new RuntimeException("Erreur lors de la génération du fichier: " +
     * message + "; " + e.getMessage());
     * }
     * }
     */

}
