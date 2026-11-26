package com.tn.arabsoft.administrations.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PARAM_NOTIF")
public class ParamNotif {

    @Id
    @Column(name = "ID_NOTIF")
    private Integer idNotif;

    @Column(name = "TEXT_NOTIF", nullable = false, length = 500)
    private String textNotif;

    @Column(name = "PARAM1", length = 30)
    private String param1;

    @Column(name = "PARAM2", length = 30)
    private String param2;

    @Column(name = "PARAM3", length = 30)
    private String param3;

    @Column(name = "PARAM4", length = 30)
    private String param4;

    @Column(name = "PARAM5", length = 30)
    private String param5;

    @Column(name = "CORPS_NOTIF", length = 4)
    private String corpsNotif;

    @Column(name = "TYPE_NOTIF", nullable = false, length = 20)
    private String typeNotif;

    @Column(name = "COD_SOC", length = 4)
    private String codSoc;

    @Column(name = "NAT_NOTIF", length = 20)
    private String natNotif;

}
