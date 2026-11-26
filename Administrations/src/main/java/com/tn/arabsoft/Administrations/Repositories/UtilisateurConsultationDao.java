package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Role;
import com.tn.arabsoft.administrations.projections.PersonnelSansCompteProjection;
import com.tn.arabsoft.administrations.projections.PersonnelUtilisateurProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtilisateurConsultationDao extends JpaRepository<Role, Long> {

    @Query(value = "select u.use_id as useId, coalesce(u.use_matricule, u.mat_pers, u.use_login) as matricule, "
            + "u.use_lname as nom, u.use_fname as prenom, p.cin as cin, s.lib_soc as libSoc "
            + "from admuser u "
            // left join : les comptes admin n'ont ni fiche personnel (CIN) ni société
            + "left join personnel p on p.cod_soc = u.cod_soc "
            + "and p.mat_pers = coalesce(u.use_matricule, u.mat_pers, u.use_login) "
            + "left join societe s on s.cod_soc = u.cod_soc "
            + "where (:codSoc = '' or u.cod_soc = :codSoc) order by "
            + "case when try_cast(coalesce(u.use_matricule, u.mat_pers, u.use_login) as bigint) is null then 1 else 0 end, "
            + "try_cast(coalesce(u.use_matricule, u.mat_pers, u.use_login) as bigint), u.use_login",
            nativeQuery = true)
    List<UtilisateurConsultationProjection> findUtilisateurs(@Param("codSoc") String codSoc);

    /**
     * Rôles des utilisateurs, regroupés ensuite en Java : la concaténation SQL (for xml path) était
     * ~100x plus lente (800 ms contre quelques ms pour 4 000 utilisateurs).
     */
    @Query(value = "select ur.users_use_id as useId, ur.roles_id as roleId, r.name as roleName "
            + "from admuser_roles ur join role r on r.id = ur.roles_id "
            + "join admuser u on u.use_id = ur.users_use_id "
            + "where (:codSoc = '' or u.cod_soc = :codSoc) order by ur.users_use_id, r.name",
            nativeQuery = true)
    List<UtilisateurRoleProjection> findRolesUtilisateurs(@Param("codSoc") String codSoc);

    /**
     * Personnel d'une societe qui peut recevoir un compte : pas encore de compte pour ce matricule, un CIN (il sert de
     * login) et ce CIN n'est pas deja le login d'un autre compte (UK_ADMUSER2 : login unique dans toute la base).
     */
    @Query(value = "select p.mat_pers as matPers, p.cin as cin, convert(varchar(10), p.dat_nais, 23) as datNais, "
            + "concat(p.pren_pers, concat(' ', p.nom_pers)) as fullName "
            + "from personnel p "
            + "where p.cod_soc = :codSoc and p.cin is not null and p.cin <> '' "
            + "and not exists (select 1 from admuser u where u.cod_soc = p.cod_soc and u.use_matricule = p.mat_pers) "
            + "and not exists (select 1 from admuser l where l.use_login = p.cin) "
            + "order by case when try_cast(p.mat_pers as bigint) is null then 1 else 0 end, "
            + "try_cast(p.mat_pers as bigint), p.mat_pers",
            nativeQuery = true)
    List<PersonnelSansCompteProjection> findPersonnelSansCompte(@Param("codSoc") String codSoc);

    /**
     * Fiche du personnel pour creer un compte : cod_soc, mat_pers, nom_pers, pren_pers, cin (PERSONNEL) et l'e-mail
     * de ADR_PERS. LEFT JOIN : presque aucune fiche n'a d'adresse (l'e-mail est alors vide et se saisit a l'ecran).
     */
    @Query(value = "select t.cod_soc as codSoc, t.mat_pers as matricule, t.nom_pers as nom, t.pren_pers as prenom, "
            + "t.cin as cin, a.email as email, "
            + "(select count(*) from admuser u where u.cod_soc = t.cod_soc and u.use_matricule = t.mat_pers) "
            + "as dejaUtilisateur "
            + "from personnel t "
            + "outer apply (select top 1 x.email from adr_pers x where x.cod_soc = t.cod_soc and x.mat_pers = t.mat_pers "
            + "and x.email is not null and x.email <> '' order by x.num_adr) a "
            + "where t.cod_soc = :codSoc and t.mat_pers = :mat",
            nativeQuery = true)
    PersonnelUtilisateurProjection findPersonnelPourUtilisateur(@Param("codSoc") String codSoc,
            @Param("mat") String mat);

    /**
     * Cree le compte a partir de la fiche du personnel :
     * cod_soc -> cod_soc, mat_pers -> use_matricule, nom_pers -> use_lname, pren_pers -> use_fname, cin -> use_login.
     * L'e-mail vient de l'ecran (pre-rempli avec ADR_PERS.email) ; vide = NULL. Compte actif (use_type = 1).
     */
    @Modifying
    @Query(value = "insert into admuser (use_id, cod_soc, use_matricule, use_lname, use_fname, use_login, email, "
            + "use_pswd, use_status, use_type, use_crtdt, use_credt) "
            + "select next value for seq_admuser, t.cod_soc, t.mat_pers, t.nom_pers, t.pren_pers, t.cin, "
            + "nullif(:email, ''), :pswd, 0, 1, sysdatetime(), sysdatetime() "
            + "from personnel t where t.cod_soc = :codSoc and t.mat_pers = :mat",
            nativeQuery = true)
    int insertUtilisateurDepuisPersonnel(@Param("codSoc") String codSoc, @Param("mat") String mat,
            @Param("email") String email, @Param("pswd") String motDePasseHache);

    @Query(value = "select use_id from admuser where cod_soc = :codSoc and use_matricule = :mat", nativeQuery = true)
    Long findUseIdParMatricule(@Param("codSoc") String codSoc, @Param("mat") String mat);

    @Query(value = "select count(*) from admuser where use_login = :login", nativeQuery = true)
    Integer countParLogin(@Param("login") String login);

    @Modifying
    @Query(value = "update admuser set use_lname = :nom, use_fname = :prenom where use_id = :id", nativeQuery = true)
    int updateNomPrenom(@Param("id") Long id, @Param("nom") String nom, @Param("prenom") String prenom);

    @Modifying
    @Query(value = "delete from admuser_roles where users_use_id = :id", nativeQuery = true)
    int deleteRolesUtilisateur(@Param("id") Long id);

    @Modifying
    @Query(value = "insert into admuser_roles (users_use_id, roles_id) values (:id, :roleId)", nativeQuery = true)
    int insertRoleUtilisateur(@Param("id") Long id, @Param("roleId") Long roleId);

    @Modifying
    @Query(value = "delete from token where userid = :id", nativeQuery = true)
    int deleteTokensUtilisateur(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from password_reset_token where user_id = :id", nativeQuery = true)
    int deleteResetTokensUtilisateur(@Param("id") Long id);

    @Modifying
    @Query(value = "delete from admuser where use_id = :id", nativeQuery = true)
    int deleteUtilisateur(@Param("id") Long id);

}
