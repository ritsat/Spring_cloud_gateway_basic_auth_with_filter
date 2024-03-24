package com.ecl.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Component
public class BasicAuthGatewayFilter extends AbstractGatewayFilterFactory<BasicAuthGatewayFilter.Config> {

    private static final String EMPTY_STR = "";

    private static final String COLON_STR = ":";

    private static final String BASIC_STR = "Basic ";


    /*public BasicAuthGatewayFilter() {
        super(Config.class);
    }*/

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {

                String basicAuthValue = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                basicAuthValue = basicAuthValue != null ? basicAuthValue.replace(BASIC_STR, EMPTY_STR) : EMPTY_STR;

                basicAuthValue = new String(Base64.getDecoder().decode(basicAuthValue.getBytes()));
                String[] credentials = basicAuthValue.split(COLON_STR);
                if (credentials.length == 2) {
                    String userName = credentials[0];
                    String password = credentials[1];

                    //Check credentials with difference sources like database, LDAP, static files, etc
                    if (userName.equals("test-user") && password.equals("test-pwd")) {
                        return chain.filter(exchange);
                    }
                }

            }

            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();

            // return returnUnAuthorizedMessage(exchange);

        };
    }

    private Mono<Void> returnUnAuthorizedMessage(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String data = "{\"code\":" + HttpStatus.UNAUTHORIZED + ", \"message\":\"You are unable to authorize.\"}";
        DataBuffer buffer = response.bufferFactory().wrap(data.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public Class<Config> getConfigClass() {
        return Config.class;
    }

    static class Config {

    }
}
