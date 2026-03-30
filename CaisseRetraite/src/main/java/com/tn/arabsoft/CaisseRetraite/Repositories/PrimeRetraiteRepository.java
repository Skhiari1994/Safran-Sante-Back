package com.tn.arabsoft.CaisseRetraite.Repositories;

import com.tn.arabsoft.CaisseRetraite.Entities.PrimeRetraite;
import com.tn.arabsoft.CaisseRetraite.Entities.Reponse.ClePrimeRetraite;
import com.tn.arabsoft.CaisseRetraite.Projections.PrimeRetraiteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface PrimeRetraiteRepository extends JpaRepository<PrimeRetraite, ClePrimeRetraite> {

        @Query(value = "select nvl(max(nvl(num_remb,0)),0)  \n" +
                        "from prime_retraite\n" +
                        "where cod_soc=:soc\n" +
                        "and mat_pers=:mat", nativeQuery = true)
        Long getNumRemb(@Param("soc") String soc, @Param("mat") String mat);

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_remb,\n" +
                        "       t.num_retr,\n" +
                        "       t.cod_remb,\n" +
                        "       t.dat_remb,\n" +
                        "       t.benef_prime,\n" +
                        "       t.benef_droi,\n" +
                        "       t.taux_prime,\n" +
                        "       t.dat_deb_ret,\n" +
                        "       t.dat_fin_ret,\n" +
                        "       t.etat_prime,\n" +
                        "       t.montant_cotis,\n" +
                        "       t.montant_prime,\n" +
                        "       t.montant_total,\n" +
                        "       t.mod_pay,\n" +
                        "       t.obs_retraite,\n" +
                        "       t.imput_cpt,\n" +
                        "       t.seq_ecrt,\n" +
                        "       t.cheq_remb," +
                        "(select p.nom_pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers)nom," +
                        "(select p.mat_int from personnel p where p.mat_pers=t.mat_pers)mat_int," +
                        "(select p.dat_nais from personnel p where p.mat_pers=t.mat_pers)dat_nais " +
                        "from prime_retraite t where etat_prime in ('S','V')", nativeQuery = true)
        List<PrimeRetraiteProjection> getPrimeRetr();

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_remb,\n" +
                        "       t.num_retr,\n" +
                        "       t.cod_remb,\n" +
                        "       t.dat_remb,\n" +
                        "       t.benef_prime,\n" +
                        "       t.benef_droi,\n" +
                        "       t.taux_prime,\n" +
                        "       t.dat_deb_ret,\n" +
                        "       t.dat_fin_ret,\n" +
                        "       t.etat_prime,\n" +
                        "       t.montant_cotis,\n" +
                        "       t.montant_prime,\n" +
                        "       t.montant_total,\n" +
                        "       t.mod_pay,\n" +
                        "       t.obs_retraite,\n" +
                        "       t.imput_cpt,\n" +
                        "       t.seq_ecrt,\n" +
                        "       t.cheq_remb,\n" +
                        "       (select p.nom_pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers) nom,\n"
                        +
                        "       (select p.mat_int from personnel p where p.mat_pers=t.mat_pers) mat_int,\n" +
                        "       (select p.dat_nais from personnel p where p.mat_pers=t.mat_pers) dat_nais\n" +
                        "from prime_retraite t where etat_prime='S'", nativeQuery = true)
        List<PrimeRetraiteProjection> getPrimeRetrSaisie();

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_remb,\n" +
                        "       t.num_retr,\n" +
                        "       t.cod_remb,\n" +
                        "       t.dat_remb,\n" +
                        "       t.benef_prime,\n" +
                        "       t.benef_droi,\n" +
                        "       t.taux_prime,\n" +
                        "       t.dat_deb_ret,\n" +
                        "       t.dat_fin_ret,\n" +
                        "       t.etat_prime,\n" +
                        "       t.montant_cotis,\n" +
                        "       t.montant_prime,\n" +
                        "       t.montant_total,\n" +
                        "       t.mod_pay,\n" +
                        "       t.obs_retraite,\n" +
                        "       t.imput_cpt,\n" +
                        "       t.seq_ecrt,\n" +
                        "       t.cheq_remb,\n" +
                        "       (select p.nom_pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers) nom,\n"
                        +
                        "       (select p.mat_int from personnel p where p.mat_pers=t.mat_pers) mat_int,\n" +
                        "       (select p.dat_nais from personnel p where p.mat_pers=t.mat_pers) dat_nais\n" +
                        "from prime_retraite t where etat_prime='S' and t.mat_pers=:mat", nativeQuery = true)
        List<PrimeRetraiteProjection> getPrimeRetrSaisMat(@Param("mat") String mat);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO prime_retraite (" +
                        "cod_soc, mat_pers, num_remb, num_retr, cod_remb, dat_remb, " +
                        "benef_prime, benef_droi, taux_prime, dat_deb_ret, dat_fin_ret, " +
                        "etat_prime, montant_cotis, montant_prime, montant_total, " +
                        "mod_pay, obs_retraite, imput_cpt, seq_ecrt, cheq_remb" +
                        ") VALUES (" +
                        ":codSoc, :matPers, :numRemb, :numRetr, :codRemb, TO_DATE(:datRemb, 'YYYY-MM-DD'), " +
                        ":benefPrime, :benefDroi, :tauxPrime, TO_DATE(:datDebRet, 'YYYY-MM-DD'), TO_DATE(:datFinRet, 'YYYY-MM-DD'), "
                        +
                        ":etatPrime, :montantCotis, :montantPrime, :montantTotal, " +
                        ":modPay, :obsRetraite, :imputCpt, :seqEcrt, :cheqRemb" +
                        ")", nativeQuery = true)
        void insertPrimeRetr(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numRemb") Long numRemb,
                        @Param("numRetr") String numRetr,
                        @Param("codRemb") String codRemb,
                        @Param("datRemb") String datRemb,
                        @Param("benefPrime") String benefPrime,
                        @Param("benefDroi") String benefDroi,
                        @Param("tauxPrime") BigDecimal tauxPrime,
                        @Param("datDebRet") String datDebRet,
                        @Param("datFinRet") String datFinRet,
                        @Param("etatPrime") String etatPrime,
                        @Param("montantCotis") BigDecimal montantCotis,
                        @Param("montantPrime") BigDecimal montantPrime,
                        @Param("montantTotal") BigDecimal montantTotal,
                        @Param("modPay") String modPay,
                        @Param("obsRetraite") String obsRetraite,
                        @Param("imputCpt") String imputCpt,
                        @Param("seqEcrt") Long seqEcrt,
                        @Param("cheqRemb") String cheqRemb);

        @Modifying
        @Transactional
        @Query(value = "UPDATE prime_retraite SET " +
                        "num_retr = :numRetr, " +
                        "cod_remb = :codRemb, " +
                        "dat_remb = TO_DATE(:datRemb, 'YYYY-MM-DD'), " +
                        "benef_prime = :benefPrime, " +
                        "benef_droi = :benefDroi, " +
                        "taux_prime = :tauxPrime, " +
                        "dat_deb_ret = TO_DATE(:datDebRet, 'YYYY-MM-DD'), " +
                        "dat_fin_ret = TO_DATE(:datFinRet, 'YYYY-MM-DD'), " +
                        "etat_prime = :etatPrime, " +
                        "montant_cotis = :montantCotis, " +
                        "montant_prime = :montantPrime, " +
                        "montant_total = :montantTotal, " +
                        "mod_pay = :modPay, " +
                        "obs_retraite = :obsRetraite, " +
                        "imput_cpt = :imputCpt, " +
                        "seq_ecrt = :seqEcrt, " +
                        "cheq_remb = :cheqRemb " +
                        "WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_remb = :numRemb", nativeQuery = true)
        void updatePrimeRetr(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numRemb") Long numRemb,
                        @Param("numRetr") String numRetr,
                        @Param("codRemb") String codRemb,
                        @Param("datRemb") String datRemb,
                        @Param("benefPrime") String benefPrime,
                        @Param("benefDroi") String benefDroi,
                        @Param("tauxPrime") BigDecimal tauxPrime,
                        @Param("datDebRet") String datDebRet,
                        @Param("datFinRet") String datFinRet,
                        @Param("etatPrime") String etatPrime,
                        @Param("montantCotis") BigDecimal montantCotis,
                        @Param("montantPrime") BigDecimal montantPrime,
                        @Param("montantTotal") BigDecimal montantTotal,
                        @Param("modPay") String modPay,
                        @Param("obsRetraite") String obsRetraite,
                        @Param("imputCpt") String imputCpt,
                        @Param("seqEcrt") Long seqEcrt,
                        @Param("cheqRemb") String cheqRemb);
}
