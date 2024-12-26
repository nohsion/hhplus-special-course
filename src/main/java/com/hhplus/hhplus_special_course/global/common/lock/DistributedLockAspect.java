package com.hhplus.hhplus_special_course.global.common.lock;

import com.hhplus.hhplus_special_course.domain.course.exception.CourseFullCapacityException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class DistributedLockAspect {

    private final NamedLockManager namedLockManager;

    public DistributedLockAspect(final NamedLockManager namedLockManager) {
        this.namedLockManager = namedLockManager;
    }

    @Around("@annotation(distributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint, final DistributedLock distributedLock) {
        String key = distributedLock.key();
        long timeout = distributedLock.timeout();
        TimeUnit timeUnit = distributedLock.timeUnit();
        int timeoutSeconds = (int) timeUnit.toSeconds(timeout);

        return namedLockManager.executeWithLock(key, timeoutSeconds, () -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof CourseFullCapacityException ex) {
                    throw ex;
                }
                throw new DistributedLockException(throwable);
            }
        });
    }
}
