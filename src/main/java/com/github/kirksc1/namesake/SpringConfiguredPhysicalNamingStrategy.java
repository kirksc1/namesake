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

    private static final PhysicalNamingStrategy NO_OP_STRATEGY = new NoOpPhyscialNamingStrategy();

    private final Properties properties;
    private final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}", ":", true);
    private final PhysicalNamingStrategy delegate;

    public SpringConfiguredPhysicalNamingStrategy(Environment environment) {
        this(environment, NO_OP_STRATEGY);
    }

    public SpringConfiguredPhysicalNamingStrategy(Environment environment, PhysicalNamingStrategy delegate) {
        Properties tempProperties = new Properties();
        MutablePropertySources propertySources = ((AbstractEnvironment) environment).getPropertySources();
        StreamSupport.stream(propertySources.spliterator(), false)
                .filter(propertySource -> propertySource instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource) ps).getPropertyNames())
                .flatMap(Arrays::<String>stream)
                .forEach(propName -> tempProperties.setProperty(propName, environment.getProperty(propName)));
        this.properties = tempProperties;
        this.delegate = delegate;
    }

    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return delegate.toPhysicalCatalogName(convertExpression(identifier), jdbcEnv);
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return delegate.toPhysicalColumnName(convertExpression(identifier), jdbcEnv);
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return delegate.toPhysicalSchemaName(convertExpression(identifier), jdbcEnv);
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return delegate.toPhysicalSequenceName(convertExpression(identifier), jdbcEnv);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        return delegate.toPhysicalTableName(convertExpression(identifier), jdbcEnv);
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
