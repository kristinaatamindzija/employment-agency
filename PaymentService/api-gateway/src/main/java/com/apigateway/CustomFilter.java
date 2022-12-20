package com.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
@Slf4j
public class CustomFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String ipAddress = Objects.requireNonNull(request.getRemoteAddress()).getAddress().getHostAddress();
        String userAgent = request.getHeaders().getFirst("User-Agent");
        String authorization = request.getHeaders().getFirst("Authorization");
        String requestURI = request.getURI().toString();
        String method = request.getMethodValue();
        log.info("Incoming request from IP: {}, User-Agent: {}, URI: {}, Method: {}, Authorization: {}",
                ipAddress, userAgent, requestURI, method, authorization);

        detectServiceFilter(request.getURI().toString());
        return chain.filter(exchange).then(Mono.fromRunnable(()-> {
            ServerHttpResponse response =  exchange.getResponse();
            int statusCode = Objects.requireNonNull(response.getStatusCode()).value();
            log.info("Outgoing response from IP: {}, User-Agent: {}, URI: {}, Method: {}, Authorization: {}, Status Code: {}",
                    ipAddress, userAgent, requestURI, method, authorization, statusCode);
        }));
    }

    private void detectServiceFilter(String uri) {
        if(uri.contains("/auth")) {
            log.info("REDIRECTING TO  -> Authentication service");
        }
        else if(uri.contains("/bank")) {
            log.info("REDIRECTING TO  -> Bank card service");
        }
        else if(uri.contains("/qr")) {
            log.info("REDIRECTING TO  -> QR code service");
        }
        else if(uri.contains("/paypal")) {
            log.info("REDIRECTING TO  -> PayPal service");
        }
        else if(uri.contains("/bitcoin")) {
            log.info("REDIRECTING TO  -> Bitcoin service");
        }
        else if(uri.contains("/webshop")) {
            log.info("REDIRECTING TO  -> Web shop service");
        }
        else {
            log.info("REDIRECTING TO  -> unknown");
        }
    }


}
