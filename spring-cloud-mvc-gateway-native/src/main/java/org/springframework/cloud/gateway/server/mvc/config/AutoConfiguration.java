package org.springframework.cloud.gateway.server.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportRuntimeHints;

@Configuration
@ImportRuntimeHints(Hints.class)
@Import(GatewayMvcPropertiesBeanDefinitionRegistrar.class)
class AutoConfiguration {

    @Bean
    GatewayRouteHelper gatewayRouteHelper() {
        return new GatewayRouteHelper();
    }

    AutoConfiguration() {
        System.out.println("auto config awaaaaaayyy!");
    }
}