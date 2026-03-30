package com.arabsoft.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admuser")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
    @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_ADMUSER", allocationSize = 1)
    @Column(name = "USE_ID")
    private Long use_id;

    @Column(name = "USE_MATRICULE")
    private String usematricule;

    @Column(name = "USE_LOGIN")
    private String uselogin;

    @Column(name = "USE_PSW")
    private String usepsw;

    @Column(name = "USE_LNAME")
    private String use_lname;

    @Column(name = "USE_FNAME")
    private String use_fname;

    @Column(name = "USE_NBESSAI")
    private Long use_nbessai;

    @Column(name = "USE_CPTESTATUS")
    private String use_cptestatus;

    @Column(name = "USE_CRTDT")
    private LocalDate use_crtdt;

    @Column(name = "USE_LASTCNX")
    private LocalDateTime use_lastcnx;

    @Column(name = "USE_NIVADM")
    private String use_nivadm;

    @Column(name = "USE_TYPE")
    private boolean use_type;

    @Column(name = "USE_STATUS")
    private boolean use_status;

    @Column(name = "USE_CREDT")
    @CreatedDate
    private LocalDate use_credt;

    @Column(name = "USE_UPDTDT")
    @LastModifiedDate
    private LocalDate use_updtdt;

    @Column(name = "USE_UPDTBY")
    private String use_updtby;

    @Column(name = "COD_SOC")
    private String cod_soc;

    @Column(name = "MAT_PERS")
    private String matpers;

    @Column(name = "COD_SITE")
    private String cod_site;

    @Column(name = "USE_PSWD")
    private String use_pswd;
    @Column(name = "email")
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    // Implémentations de UserDetails et Principal

    @Override
    public String getName() {
        return uselogin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return use_pswd;
    }

    @Override
    public String getUsername() {
        return uselogin;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !use_status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return use_type;
    }

    public String fullName(){
        return use_fname + " " + use_lname;
    }
}