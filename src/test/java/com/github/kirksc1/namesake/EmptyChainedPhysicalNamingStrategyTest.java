package com.github.kirksc1.namesake;

import com.github.kirksc1.namesake.entity.Goo;
import com.github.kirksc1.namesake.entity.GooRepository;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.internal.SessionImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class EmptyChainedPhysicalNamingStrategyTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    GooRepository gooRepository;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PhysicalNamingStrategy namesakePhysicalNamingStrategy() {
            return new ChainedPhyscialNamingStrategy();
        }
    }

    @Test
    public void testGooCanBeSavedAndRetrieved() {
        Goo goo = new Goo();
        goo.setName("test");

        gooRepository.save(goo);

        assertTrue(gooRepository.findByName("test").isPresent());
    }

    @Test
    @Transactional
    public void testTableNameIsCorrect() {
        String[] tableNames = getTableNames(entityManager, Goo.class);

        assertEquals(2, tableNames.length);
        assertEquals("standard_table", tableNames[0]); //Table name
        assertEquals("standard_table", tableNames[1]); //Root table name
    }

    @Test
    @Transactional
    public void testFieldNameIsCorrect() {
        String[] fieldNames = getFieldNames(entityManager, Goo.class, "name");

        assertEquals(1, fieldNames.length);
        assertEquals("standard_field", fieldNames[0]);
    }

    public String[] getTableNames(EntityManager em, Class entityClass) {
        Object entityExample;
        try {
            entityExample = entityClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        SessionImpl session = em.unwrap(SessionImpl.class);
        EntityPersister persister = session.getEntityPersister(null, entityExample);

        if (persister instanceof AbstractEntityPersister) {
            AbstractEntityPersister persisterImpl = (AbstractEntityPersister) persister;

            String tableName = persisterImpl.getTableName();
            String rootTableName = persisterImpl.getRootTableName();

            return new String[] {rootTableName, tableName};

        } else {
            throw new RuntimeException("Unexpected persister type; a subtype of AbstractEntityPersister expected.");
        }
    }

    public String[] getFieldNames(EntityManager em, Class entityClass, String property) {
        Object entityExample;
        try {
            entityExample = entityClass.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

        SessionImpl session = em.unwrap(SessionImpl.class);
        EntityPersister persister = session.getEntityPersister(null, entityExample);

        if (persister instanceof AbstractEntityPersister) {
            AbstractEntityPersister persisterImpl = (AbstractEntityPersister) persister;

            return persisterImpl.getPropertyColumnNames(property);

        } else {
            throw new RuntimeException("Unexpected persister type; a subtype of AbstractEntityPersister expected.");
        }
    }
}
