package com.apigateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
public class CustomFilter implements GlobalFilter {
    Logger logger = LoggerFactory.getLogger(CustomFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info("Pre Filter -> Authorization = " + request.getHeaders().getFirst("Authorization"));

        detectServiceFilter(request.getURI().toString());
        return chain.filter(exchange).then(Mono.fromRunnable(()-> {
            ServerHttpResponse response =  exchange.getResponse();
            logger.info("Post Filter = " + response.getStatusCode());
        }));
    }

    private void detectServiceFilter(String uri) {
        if(uri.contains("/auth")) {
            logger.info("REDIRECTING TO  -> Authentication service");
        }
        else if(uri.contains("/bank")) {
            logger.info("REDIRECTING TO  -> Bank card service");
        }
        else if(uri.contains("/qr")) {
            logger.info("REDIRECTING TO  -> QR code service");
        }
        else if(uri.contains("/paypal")) {
            logger.info("REDIRECTING TO  -> PayPal service");
        }
        else if(uri.contains("/bitcoin")) {
            logger.info("REDIRECTING TO  -> Bitcoin service");
        }
        else if(uri.contains("/webshop")) {
            logger.info("REDIRECTING TO  -> Web shop service");
        }
        else {
            logger.info("REDIRECTING TO  -> unknown");
        }
    }


}
