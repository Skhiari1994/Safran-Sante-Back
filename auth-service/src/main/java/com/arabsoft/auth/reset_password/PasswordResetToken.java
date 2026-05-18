package com.arabsoft.auth.reset_password;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.arabsoft.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@NoArgsConstructor
@ToString
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
    @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_PASSWORDRESETTOKEN", allocationSize = 1)
    private Integer token_id;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        // Initialisation du calendrier avec l'heure actuelle
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());

        // Ajout du temps d'expiration en minutes
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);

        // Création d'un objet Date à partir du calendrier
        Date expirationDate = calendar.getTime();

        // Formatage et affichage de la date en "dd/MM/yyyy HH.mm.ss" pour le débogage
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Formatted Expiration Date: " + dateFormat.format(expirationDate));

        // Retourner la date (type Date)
        return expirationDate;
    }

}