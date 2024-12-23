package com.hhplus.hhplus_special_course.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Table;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * 테스트용 DB 데이터를 명시적으로 초기화하는 클래스.
 * <p>따라서 @Transactional 롤백 테스트는 사용하지 않는다.</p>
 */
@Component
@ActiveProfiles("test")
public class TestDataCleaner {

    private final EntityManager entityManager;

    public TestDataCleaner(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void cleanUp() {
        entityManager.getMetamodel().getEntities().forEach(entityType -> {
            Class<?> entityClazz = entityType.getJavaType();
            if (entityClazz.isAnnotationPresent(Table.class)) {
                Table tableAnnotation = entityClazz.getAnnotation(Table.class);
                String tableName = tableAnnotation.name();
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            }
        });
    }
}
