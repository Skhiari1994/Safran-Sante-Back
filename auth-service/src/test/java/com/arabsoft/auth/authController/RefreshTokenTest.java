package com.arabsoft.auth.authController;

import com.arabsoft.auth.model.Role;
import com.arabsoft.auth.model.User;
import com.arabsoft.auth.repository.TokenRepository;
import com.arabsoft.auth.repository.UserRepository;
import com.arabsoft.auth.security.JwtService;
import com.arabsoft.auth.service.ParamGrhService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Renouvellement du jeton court : POST /api/auth/refresh. */
class RefreshTokenTest {

    // clé HMAC de test (base64, 256 bits) ; le vrai secret vient de JWT_SECRET_KEY
    private static final String CLE = "dGVzdC1zZWNyZXQta2V5LXRlc3Qtc2VjcmV0LWtleS0xMjM0NTY=";

    private JwtService jwtService;
    private UserRepository userRepository;
    private MockMvc mvc;
    private User utilisateur;

    @BeforeEach
    void init() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", CLE);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 420_000L);

        userRepository = mock(UserRepository.class);
        utilisateur = User.builder()
                .uselogin("1001").use_fname("Aziz").use_lname("Jemli").codSoc("01").matpers("1001")
                .use_pswd("x").use_type(true).use_status(false)
                .roles(List.of(Role.builder().name("ADMIN").build()))
                .build();
        when(userRepository.findByUseloginAndCodSoc(anyString(), anyString())).thenAnswer(inv ->
                "1001".equals(inv.getArgument(0)) && "01".equals(inv.getArgument(1)) ? Optional.of(utilisateur) : Optional.empty());

        var controller = new AuthenticationController(mock(PasswordEncoder.class), mock(AuthenticationService.class),
                userRepository, mock(TokenRepository.class), jwtService, mock(ParamGrhService.class));
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private String jetonValide() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", "Aziz Jemli");
        claims.put("matricule", "1001");
        claims.put("codSoc", "01");
        claims.put("libSoc", "Société Test");
        return jwtService.generateToken(claims, utilisateur);
    }

    private String jetonExpire() {
        return Jwts.builder().setSubject("1001").claim("codSoc", "01")
                .setIssuedAt(new Date(System.currentTimeMillis() - 600_000))
                .setExpiration(new Date(System.currentTimeMillis() - 1_000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(CLE)), SignatureAlgorithm.HS256).compact();
    }

    private Claims claims(String jeton) {
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(CLE))).build()
                .parseClaimsJws(jeton).getBody();
    }

    @Test
    void jetonValide_donneUnNouveauJetonAvecLesMemesClaimsMetier() throws Exception {
        String ancien = jetonValide();
        Thread.sleep(1100); // le champ iat est à la seconde : on laisse passer une seconde pour distinguer les jetons

        MvcResult res = mvc.perform(post("/api/auth/refresh").header("Authorization", "Bearer " + ancien))
                .andExpect(status().isOk()).andReturn();

        String corps = res.getResponse().getContentAsString();
        assertTrue(corps.contains("\"token\""), corps);
        String nouveau = corps.replaceAll(".*\"token\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        Claims avant = claims(ancien), apres = claims(nouveau);
        assertNotEquals(ancien, nouveau);
        assertEquals("1001", apres.getSubject());
        assertEquals("Aziz Jemli", apres.get("fullName"));
        assertEquals("1001", apres.get("matricule"));
        assertEquals("01", apres.get("codSoc"));
        assertEquals("Société Test", apres.get("libSoc"));
        assertEquals(List.of("ADMIN"), apres.get("authorities"));
        assertTrue(apres.getExpiration().after(avant.getExpiration()), "l'échéance doit être repoussée");
        assertEquals(420_000L, apres.getExpiration().getTime() - apres.getIssuedAt().getTime(), 1000);
    }

    @Test
    void jetonExpire_estRefuse() throws Exception {
        mvc.perform(post("/api/auth/refresh").header("Authorization", "Bearer " + jetonExpire()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sansJeton_estRefuse() throws Exception {
        mvc.perform(post("/api/auth/refresh")).andExpect(status().isUnauthorized());
    }

    @Test
    void jetonAltere_estRefuse() throws Exception {
        mvc.perform(post("/api/auth/refresh").header("Authorization", "Bearer " + jetonValide() + "x"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void compteDesactive_estRefuse() throws Exception {
        String jeton = jetonValide();
        utilisateur.setUse_type(false); // compte désactivé depuis l'émission du jeton
        mvc.perform(post("/api/auth/refresh").header("Authorization", "Bearer " + jeton))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void compteVerrouille_estRefuse() throws Exception {
        String jeton = jetonValide();
        utilisateur.setUse_status(true); // use_status = verrouillé
        mvc.perform(post("/api/auth/refresh").header("Authorization", "Bearer " + jeton))
                .andExpect(status().isUnauthorized());
    }
}
