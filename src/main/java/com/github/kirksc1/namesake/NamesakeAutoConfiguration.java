package com.github.kirksc1.namesake;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@ConditionalOnProperty(name = "namesake.enabled", havingValue = "true", matchIfMissing = true)
public class NamesakeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(name="namesakePhysicalNamingStrategy")
    public PhysicalNamingStrategy namesakePhysicalNamingStrategy(Environment environment) {
        return new ChainedPhyscialNamingStrategy(new SpringConfiguredPhysicalNamingStrategy(environment), new SpringPhysicalNamingStrategy());
    }

}
