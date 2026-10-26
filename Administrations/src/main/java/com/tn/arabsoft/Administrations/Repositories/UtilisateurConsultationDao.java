package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Role;
import com.tn.arabsoft.administrations.projections.UtilisateurConsultationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UtilisateurConsultationDao extends JpaRepository<Role, Long> {

    @Query(value = "select u.use_id as useId, coalesce(u.mat_pers, u.use_login) as matricule, "
            + "u.use_lname as nom, u.use_fname as prenom, "
            + "(select listagg(r.name, ', ') within group (order by r.name) "
            + "from admuser_roles ur join role r on r.id = ur.roles_id "
            + "where ur.users_use_id = u.use_id) as role, "
            + "(select listagg(ur.roles_id, ',') within group (order by ur.roles_id) "
            + "from admuser_roles ur where ur.users_use_id = u.use_id) as roleIds "
            + "from admuser u order by to_number(regexp_substr(coalesce(u.mat_pers, u.use_login), '^[0-9]+$')), u.use_login",
            nativeQuery = true)
    List<UtilisateurConsultationProjection> findUtilisateurs();

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
