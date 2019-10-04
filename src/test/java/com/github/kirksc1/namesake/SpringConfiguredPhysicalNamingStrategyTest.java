package com.github.kirksc1.namesake;

import com.github.kirksc1.namesake.entity.Foo;
import com.github.kirksc1.namesake.entity.FooRepository;
import org.hibernate.internal.SessionImpl;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"foo.name=CustomTable","foo.field.name=CustomField", "spring.jpa.show-sql=true"})
public class SpringConfiguredPhysicalNamingStrategyTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    FooRepository fooRepository;

    @Test
    public void testFooCanBeSavedAndRetrieved() {
        Foo foo = new Foo();
        foo.setName("test");

        fooRepository.save(foo);

        assertTrue(fooRepository.findById("test").isPresent());
    }

    @Test
    @Transactional
    public void testTableNameIsCorrect() {
        String[] tableNames = getTableNames(entityManager, Foo.class);

        assertEquals(2, tableNames.length);
        assertEquals("CustomTable", tableNames[0]); //Table name
        assertEquals("CustomTable", tableNames[1]); //Root table name
    }

    @Test
    @Transactional
    public void testFieldNameIsCorrect() {
        String[] fieldNames = getFieldNames(entityManager, Foo.class, "name");

        assertEquals(1, fieldNames.length);
        assertEquals("CustomField", fieldNames[0]);
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
