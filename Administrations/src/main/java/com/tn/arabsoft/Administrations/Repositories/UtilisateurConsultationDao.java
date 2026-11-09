package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Role;
import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import com.tn.arabsoft.administrations.projections.UtilisateurRoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtilisateurConsultationDao extends JpaRepository<Role, Long> {

    @Query(value = "select u.use_id as useId, coalesce(u.mat_pers, u.use_login) as matricule, "
            + "u.use_lname as nom, u.use_fname as prenom, p.cin as cin, s.lib_soc as libSoc "
            + "from admuser u "
            // left join : les comptes admin n'ont ni fiche personnel (CIN) ni société
            + "left join personnel p on p.cod_soc = u.cod_soc and p.mat_pers = coalesce(u.mat_pers, u.use_login) "
            + "left join societe s on s.cod_soc = u.cod_soc "
            + "where (:codSoc = '' or u.cod_soc = :codSoc) order by "
            + "case when try_cast(coalesce(u.mat_pers, u.use_login) as bigint) is null then 1 else 0 end, "
            + "try_cast(coalesce(u.mat_pers, u.use_login) as bigint), u.use_login",
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
