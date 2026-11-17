package com.arabsoft.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/** Un jeton expiré ou altéré doit donner un 401 (le front s'y déconnecte), pas une erreur 500/403. */
class JwtAuthFilterTest {

    private static final String CLE = "dGVzdC1zZWNyZXQta2V5LXRlc3Qtc2VjcmV0LWtleS0xMjM0NTY=";

    private JwtService jwtService;
    private JwtAuthFilter filtre;
    private FilterChain chaine;
    private final UserDetails utilisateur = User.withUsername("1001").password("x").authorities("ADMIN").build();

    @BeforeEach
    void init() {
        SecurityContextHolder.clearContext();
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", CLE);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 420_000L);
        UserDetailsService uds = mock(UserDetailsService.class);
        when(uds.loadUserByUsername("1001")).thenReturn(utilisateur);
        filtre = new JwtAuthFilter(jwtService, uds);
        chaine = mock(FilterChain.class);
    }

    private MockHttpServletResponse appeler(String jeton) throws Exception {
        var req = new MockHttpServletRequest("GET", "/adherent/personnes");
        req.setServletPath("/adherent/personnes");
        req.addHeader("Authorization", "Bearer " + jeton);
        var res = new MockHttpServletResponse();
        filtre.doFilter(req, res, chaine);
        return res;
    }

    @Test
    void jetonExpire_401_sansPoursuivreLaChaine() throws Exception {
        String expire = Jwts.builder().setSubject("1001")
                .setIssuedAt(new Date(System.currentTimeMillis() - 600_000))
                .setExpiration(new Date(System.currentTimeMillis() - 1_000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(CLE)), SignatureAlgorithm.HS256).compact();
        var res = appeler(expire);
        assertEquals(401, res.getStatus());
        assertTrue(res.getContentAsString().contains("Session expir"));
        verify(chaine, never()).doFilter(any(), any());
    }

    @Test
    void jetonAltere_401() throws Exception {
        var res = appeler(jwtService.generateToken(utilisateur) + "x");
        assertEquals(401, res.getStatus());
        verify(chaine, never()).doFilter(any(), any());
    }

    @Test
    void jetonValide_laChaineContinue_etLUtilisateurEstAuthentifie() throws Exception {
        var res = appeler(jwtService.generateToken(utilisateur));
        assertEquals(200, res.getStatus());
        verify(chaine).doFilter(any(), any());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("1001", SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
