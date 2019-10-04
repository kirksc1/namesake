package com.github.kirksc1.namesake;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.StreamSupport;

public class SpringConfiguredPhysicalNamingStrategy implements PhysicalNamingStrategy {

    private final Properties properties;
    private final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);

    public SpringConfiguredPhysicalNamingStrategy(Environment environment) {
        Properties tempProperties = new Properties();
        MutablePropertySources propertySources = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propertySources.spliterator(), false)
                .filter(propertySource -> propertySource instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> tempProperties.setProperty(propName, environment.getProperty(propName)));
        this.properties = tempProperties;
    }

    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertExpression(identifier);
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertExpression(identifier);
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertExpression(identifier);
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertExpression(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return convertExpression(identifier);
    }

    private Identifier convertExpression(final Identifier identifier) {
        Identifier retVal = identifier;
        if (identifier != null) {
            final String newName = helper.replacePlaceholders(identifier.getText(), properties);
            retVal = Identifier.toIdentifier(newName);
        }
        return retVal;
    }
}
