package com.hhplus.hhplus_special_course.global.common.lock;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

@Component
public class MariaDBJpaNamedLockManager implements NamedLockManager {

    private static final String LOCK_NAME = "lockName";
    private static final String TIMEOUT_SECONDS = "timeoutSeconds";

    private static final String GET_LOCK = "SELECT GET_LOCK(:%s, :%s)".formatted(LOCK_NAME, TIMEOUT_SECONDS);
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:%s)".formatted(LOCK_NAME);

    @PersistenceContext
    private EntityManager em;

    @Override
    public <T> T executeWithLock(
            final String lockName,
            final int timeoutSeconds,
            final Supplier<T> supplier
    ) {
        try {
            getLock(lockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(lockName);
        }
    }

    private void getLock(final String lockName, final int timeoutSeconds) {
        Query query = em.createNativeQuery(GET_LOCK);
        query.setParameter(LOCK_NAME, lockName);
        query.setParameter(TIMEOUT_SECONDS, timeoutSeconds);
        checkLock(query.getSingleResult(), LockOperation.GET);
    }

    private void releaseLock(final String lockName) {
        Query query = em.createNativeQuery(RELEASE_LOCK);
        query.setParameter(LOCK_NAME, lockName);
        checkLock(query.getSingleResult(), LockOperation.RELEASE);
    }

    private void checkLock(Object result, LockOperation operation) {
        if (!Objects.equals(result, 1)) {
            throw new NamedLockException(operation);
        }
    }
}
