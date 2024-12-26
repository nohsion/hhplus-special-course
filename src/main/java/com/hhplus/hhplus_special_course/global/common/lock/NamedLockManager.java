package com.hhplus.hhplus_special_course.global.common.lock;

import java.util.function.Supplier;

public interface NamedLockManager {

    <T> T executeWithLock(String lockName, int timeoutSeconds, Supplier<T> supplier);
}
