package com.tn.arabsoft.gateway;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.regex.Pattern;

/**
 * Point d'entrée unique : toute requête vers un service doit porter un jeton JWT valide (signature correcte et non
 * expiré). Cela protège aussi les services qui n'ont pas de sécurité propre (Administrations, Remboursement,
 * Reports, communication). Un jeton expiré donne un 401, ce qui déclenche la déconnexion côté front.
 *
 * Exceptions (le navigateur ne peut pas envoyer d'en-tête Authorization dans ces cas) :
 * <ul>
 * <li>/api/auth/** : connexion, renouvellement… (le service d'authentification vérifie lui-même le jeton) ;</li>
 * <li>/communication-service/ws/** : poignée de main WebSocket/SockJS du chat ;</li>
 * <li>GET /communication-service/files/(view|download)/{fichier} : pièces jointes du chat affichées par des balises
 * &lt;img&gt;, &lt;audio&gt; ou des liens (noms en UUID, non devinables).</li>
 * </ul>
 */
@Component
public class JwtGlobalFilter implements GlobalFilter, Ordered {

    private static final Pattern PIECE_JOINTE_CHAT = Pattern
            .compile("^/communication-service/files/(view|download)/[^/]+$");

    private final Key cle;

    public JwtGlobalFilter(@Value("${application.security.jwt.secret-key}") String secretKey) {
        this.cle = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (request.getMethod() == HttpMethod.OPTIONS || estPublique(request)) {
            return chain.filter(exchange);
        }

        String autorisation = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (autorisation == null || !autorisation.startsWith("Bearer ")) {
            return refuser(exchange);
        }
        try {
            Jwts.parserBuilder().setSigningKey(cle).build().parseClaimsJws(autorisation.substring(7));
        } catch (JwtException | IllegalArgumentException e) {
            // expiré, altéré, mal formé…
            return refuser(exchange);
        }
        return chain.filter(exchange);
    }

    /** Routes accessibles sans jeton (voir la liste dans la description de la classe). */
    static boolean estPublique(ServerHttpRequest request) {
        String chemin = request.getURI().getRawPath();
        // un chemin « remonté » (..) ou encodé (%2e) pourrait sortir de l'exception une fois normalisé par le service
        if (chemin.contains("..") || chemin.toLowerCase().contains("%2e") || chemin.contains("\\")) {
            return false;
        }
        if (chemin.equals("/api/auth") || chemin.startsWith("/api/auth/")) {
            return true;
        }
        if (chemin.equals("/communication-service/ws") || chemin.startsWith("/communication-service/ws/")) {
            return true;
        }
        return request.getMethod() == HttpMethod.GET && PIECE_JOINTE_CHAT.matcher(chemin).matches();
    }

    private Mono<Void> refuser(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] corps = "{\"message\":\"Session expirée. Reconnectez-vous.\"}".getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(corps);
        return response.writeWith(Mono.just(buffer));
    }

    /** Avant le routage et les filtres de route. */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}
