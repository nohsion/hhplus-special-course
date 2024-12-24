package com.hhplus.hhplus_special_course.global.common.lock;

public class NamedLockException extends RuntimeException {

    private static final String EXCEPTION_MESSAGE = "NamedLock을 수행하는 도중 오류가 발생했습니다.";

    public NamedLockException(LockOperation operation) {
        super(EXCEPTION_MESSAGE + " - " + operation);
    }
}
