package com.arabsoft.auth.repository;

import com.arabsoft.auth.model.User;
import com.arabsoft.auth.projections.SocieteProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUselogin(String email);

  Optional<User> findByEmail(String email);

  Optional<User> findByUseloginAndCodSoc(String uselogin, String codSoc);

  @Query(value = """
      SELECT a.use_id
      FROM PERSONNEL AS p
      INNER JOIN SOCIETE AS s
          ON s.COD_SOC = p.COD_SOC
      INNER JOIN ADMUSER AS a
          ON a.COD_SOC = p.COD_SOC
         AND a.USE_LOGIN = p.MAT_PERS
      WHERE p.MAT_PERS = :matPers
        AND p.CIN = :cin
      """, nativeQuery = true)
  Long findUseIdByMatriculeAndCin(
      @Param("matPers") String matPers,
      @Param("cin") String cin);

  @Query(value = """
      select roles_id from ADMUSER_ROLES as a where a.users_use_id = :useId
      """, nativeQuery = true)
  Long findRoleByUseId(@Param("useId") Long useId);

  @Query(value = """
      SELECT s.COD_SOC AS codSoc,
             s.LIB_SOC AS libSoc
      FROM SOCIETE AS s
      ORDER BY s.COD_SOC
      """, nativeQuery = true)
  List<SocieteProjection> findAllSocietes();

  @Query(value = """
      SELECT DISTINCT
             p.COD_SOC AS codSoc,
             s.LIB_SOC AS libSoc
      FROM PERSONNEL AS p
      INNER JOIN SOCIETE AS s
          ON s.COD_SOC = p.COD_SOC
      INNER JOIN ADMUSER AS a
          ON a.COD_SOC = p.COD_SOC
         AND a.USE_LOGIN = p.MAT_PERS
      WHERE p.MAT_PERS = :matPers
        AND p.CIN = :cin
      """, nativeQuery = true)
  List<SocieteProjection> findSocieteByMatriculeAndCin(
      @Param("matPers") String matPers,
      @Param("cin") String cin);

}
