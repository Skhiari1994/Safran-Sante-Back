package com.tn.arabsoft.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class JwtGlobalFilterTest {

    private static final String CLE = "dGVzdC1zZWNyZXQta2V5LXRlc3Qtc2VjcmV0LWtleS0xMjM0NTY=";
    private static final Key KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(CLE));

    private JwtGlobalFilter filtre;
    private AtomicBoolean transmis;

    @BeforeEach
    void init() {
        filtre = new JwtGlobalFilter(CLE);
        transmis = new AtomicBoolean(false);
    }

    private static String jeton(long expireDans) {
        return Jwts.builder().setSubject("1001")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000))
                .setExpiration(new Date(System.currentTimeMillis() + expireDans))
                .signWith(KEY, SignatureAlgorithm.HS256).compact();
    }

    /** Envoie la requête dans le filtre ; renvoie le statut posé (null si la requête a été transmise au service). */
    private HttpStatus appeler(HttpMethod methode, String chemin, String authorization) {
        // URI construite à la main : MockServerHttpRequest.get(String) interprète {…} et normalise moins fidèlement
        var builder = MockServerHttpRequest.method(methode, URI.create("http://localhost:8222" + chemin));
        if (authorization != null) builder.header("Authorization", authorization);
        var exchange = MockServerWebExchange.from(builder);
        filtre.filter(exchange, ex -> {
            transmis.set(true);
            return Mono.empty();
        }).block();
        return transmis.get() ? null : (HttpStatus) exchange.getResponse().getStatusCode();
    }

    @ParameterizedTest
    @ValueSource(strings = { "/admin/roles", "/Remb/BultSoin/all", "/Print/report", "/Communication/users",
            "/communication-service/users", "/communication-service/files/upload", "/adherent/Personnel/all",
            "/referentiel/nationalite" })
    void routesProtegees_sansJeton_401(String chemin) {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, chemin, null));
    }

    @Test
    void jetonValide_laRequeteEstTransmise() {
        assertNull(appeler(HttpMethod.GET, "/admin/roles", "Bearer " + jeton(60_000)));
        assertNull(appeler(HttpMethod.POST, "/Remb/BultSoin/save", "Bearer " + jeton(60_000)));
    }

    @Test
    void jetonExpire_401() {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Bearer " + jeton(-5_000)));
    }

    @Test
    void jetonAltere_ouSigneAvecUneAutreCle_401() {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Bearer " + jeton(60_000) + "x"));
        String autreCle = Jwts.builder().setSubject("1001").setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.secretKeyFor(SignatureAlgorithm.HS256)).compact();
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Bearer " + autreCle));
    }

    @Test
    void enTeteMalFormeOuVide_401() {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Basic abc"));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Bearer "));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/admin/roles", "Bearer pas.un.jeton"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "/api/auth/authenticate", "/api/auth/refresh", "/api/auth/getSocieteByLogin/1/2",
            "/communication-service/ws/info", "/communication-service/ws/123/abc/websocket",
            "/communication-service/files/view/3f2a9c1e-aaaa-bbbb-cccc-0123456789ab.png",
            "/communication-service/files/download/3f2a9c1e-aaaa-bbbb-cccc-0123456789ab.pdf" })
    void routesPubliques_sansJeton_sontTransmises(String chemin) {
        assertNull(appeler(HttpMethod.GET, chemin, null), chemin);
    }

    @Test
    void preflightCors_estTransmis() {
        assertNull(appeler(HttpMethod.OPTIONS, "/admin/roles", null));
    }

    @Test
    void lExceptionDesPiecesJointes_nEstQueEnLecture_surUnSeulFichier() {
        // dépôt de fichier : protégé
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.POST, "/communication-service/files/upload", null));
        // écrire sur l'URL de lecture : protégé
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.POST, "/communication-service/files/view/x.png", null));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.DELETE, "/communication-service/files/view/x.png", null));
        // liste/sous-dossier : protégé
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/communication-service/files/view/", null));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/communication-service/files/view/a/b", null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/communication-service/ws/../users",
            "/communication-service/files/view/../../users",
            "/communication-service/files/view/%2e%2e/users",
            "/communication-service/files/view/%2E%2E",
            "/api/auth/../../admin/roles",
            "/api/auth/..%5cadmin" })
    void tentativesDeContournement_parRemonteeDeChemin_401(String chemin) {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, chemin, null), chemin);
    }

    @Test
    void unPrefixeVoisinDeLExceptionNEstPasPublic() {
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/api/authentik/x", null));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/communication-service/wsx", null));
        assertEquals(HttpStatus.UNAUTHORIZED, appeler(HttpMethod.GET, "/communication-service/ws-admin/x", null));
    }

    @Test
    void laReponse401_estUnJsonLisible() {
        var exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/admin/roles"));
        filtre.filter(exchange, ex -> Mono.empty()).block();
        assertEquals(HttpStatus.UNAUTHORIZED, exchange.getResponse().getStatusCode());
        assertEquals("application/json", exchange.getResponse().getHeaders().getContentType().toString());
        assertTrue(exchange.getResponse().getBodyAsString().block().contains("Session expir"));
    }
}
