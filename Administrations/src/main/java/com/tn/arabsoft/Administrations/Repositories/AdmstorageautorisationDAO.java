package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.Admstorageautorisation;
import com.tn.arabsoft.administrations.entities.Admsubmodule;
import com.tn.arabsoft.administrations.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@SuppressWarnings("java:S117")
public interface AdmstorageautorisationDAO extends JpaRepository<Admstorageautorisation, Long> {
    List<Admstorageautorisation> findByRoleAndAdmsubmodule(Role role, Admsubmodule admsubmodule);

    List<Admstorageautorisation> findByRole(Role role);

    @Query(value = "SELECT a.id_autho, a.role_id, a.sum_id, a.evt_id from Admstorageautorisation a where a.role_id=:id", nativeQuery = true)
    public List<Admstorageautorisation> getAdmStorageByRole(@Param("id") Long id);

    @Query(value = "SELECT a.id_autho, a.role_id, a.sum_id, a.evt_id " +
            "FROM ADMSTORAGEAUTORISATION a " +
            "WHERE a.sum_id = :sum_id AND a.role_id = :role", nativeQuery = true)
    public List<Admstorageautorisation> getMenuRole(@Param("sum_id") Long sum_id, @Param("role") Long role);

    @Transactional
    @Modifying
    @Query(value = "delete from Admstorageautorisation where role_id=:role and sum_id=:sum_id and nvl(evt_id,-1)=nvl(:evt,-1)", nativeQuery = true)
    void deleteFromAdmautorisation(@Param("sum_id") Long sum_id, @Param("role") Long role, @Param("evt") Long evt);

}
