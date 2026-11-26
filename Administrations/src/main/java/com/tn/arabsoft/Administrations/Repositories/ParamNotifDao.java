package com.tn.arabsoft.administrations.repositories;

import com.tn.arabsoft.administrations.entities.ParamNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParamNotifDao extends JpaRepository<ParamNotif, Integer> {

    @Query("select coalesce(max(p.idNotif), 0) + 1 from ParamNotif p")
    Integer getNextId();

    /** Même clé unique que la base (UK_PARAM_NOTIF1) : nature + type + texte, hors la notification elle-même. */
    @Query("select count(p) > 0 from ParamNotif p where p.natNotif = :nature and p.typeNotif = :type "
            + "and p.textNotif = :texte and p.idNotif <> :idExclu")
    boolean existeDoublon(@Param("nature") String nature, @Param("type") String type,
                          @Param("texte") String texte, @Param("idExclu") Integer idExclu);

}
