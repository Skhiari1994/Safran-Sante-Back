package com.tn.arabsoft.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GatewayCorsConfiguration {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "http://localhost:4200",
            "http://localhost:8040",
            "http://172.16.30.206:4200",
            "http://172.16.30.206:8040",
            "http://192.168.2.93:4200",
            "http://192.168.2.93:8222",
            "http://172.16.20.111:8222",
            "http://192.168.137.74:8222"



    );

    @Bean
    public WebFilter corsWebFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();

            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                String origin = request.getHeaders().getOrigin();

                if (origin != null && ALLOWED_ORIGINS.contains(origin)) {
                    HttpHeaders headers = response.getHeaders();
                    headers.add("Access-Control-Allow-Origin", origin);
                    headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
                    headers.add("Access-Control-Expose-Headers", "Authorization");
                    headers.add("Access-Control-Max-Age", "3600");
                }

                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }

            return chain.filter(ctx);
        };
    }
}
