package com.github.kirksc1.namesake;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChainedPhyscialNamingStrategy implements PhysicalNamingStrategy {

    private final List<PhysicalNamingStrategy> physicalNamingStrategies = new ArrayList<>();

    public ChainedPhyscialNamingStrategy(PhysicalNamingStrategy ... strategies) {
        Arrays.stream(strategies)
                .filter(physicalNamingStrategy -> physicalNamingStrategy != null)
                .forEach(physicalNamingStrategy -> physicalNamingStrategies.add(physicalNamingStrategy));

        if (physicalNamingStrategies.isEmpty()) {
            //default to spring default
            physicalNamingStrategies.add(new SpringPhysicalNamingStrategy());
        }
    }

    @Override
    public Identifier toPhysicalCatalogName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Identifier retVal = identifier;

        for (PhysicalNamingStrategy strategy: physicalNamingStrategies) {
            retVal = strategy.toPhysicalCatalogName(retVal, jdbcEnv);
        }

        return retVal;
    }

    @Override
    public Identifier toPhysicalColumnName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Identifier retVal = identifier;

        for (PhysicalNamingStrategy strategy: physicalNamingStrategies) {
            retVal = strategy.toPhysicalColumnName(retVal, jdbcEnv);
        }

        return retVal;
    }

    @Override
    public Identifier toPhysicalSchemaName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Identifier retVal = identifier;

        for (PhysicalNamingStrategy strategy: physicalNamingStrategies) {
            retVal = strategy.toPhysicalSchemaName(retVal, jdbcEnv);
        }

        return retVal;
    }

    @Override
    public Identifier toPhysicalSequenceName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Identifier retVal = identifier;

        for (PhysicalNamingStrategy strategy: physicalNamingStrategies) {
            retVal = strategy.toPhysicalSequenceName(retVal, jdbcEnv);
        }

        return retVal;
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        Identifier retVal = identifier;

        for (PhysicalNamingStrategy strategy: physicalNamingStrategies) {
            retVal = strategy.toPhysicalTableName(retVal, jdbcEnv);
        }

        return retVal;
    }

}
