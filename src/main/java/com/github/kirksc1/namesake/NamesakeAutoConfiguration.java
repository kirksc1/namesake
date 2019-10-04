package com.github.kirksc1.namesake;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class NamesakeAutoConfiguration {

    @Bean
    public PhysicalNamingStrategy namesakePhysicalNamingStrategy(Environment environment) {
        return new SpringConfiguredPhysicalNamingStrategy(environment);
    }
}
