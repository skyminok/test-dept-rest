package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class EntityMappingTest {

    @Autowired
    private transient EntityManager em;

    @Test
    void test() {
        Metamodel metamodel = em.getMetamodel();
        Set<EntityType<?>> entities = metamodel.getEntities();
        for (EntityType<?> entity : entities) {
            String query = String.format("select e from %s e where 1 = 0", entity.getName());
            List<?> list = em.createQuery(query, entity.getJavaType())
                    .setMaxResults(0)
                    .getResultList();
            assertTrue(list.isEmpty());
        }
    }
}
