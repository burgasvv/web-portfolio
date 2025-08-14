package org.burgas.gatewayserver.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProxyConfig {

    @Bean
    public RouteLocator routeLocator(final RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(
                        "portfolio-service", predicateSpec ->
                                predicateSpec
                                .path("/api/v1/**")
                                .uri("http://portfolio-service:9000")
                )
                .build();
    }
}
