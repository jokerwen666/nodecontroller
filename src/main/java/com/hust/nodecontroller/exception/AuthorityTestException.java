package com.hust.nodecontroller.exception;

import com.hust.nodecontroller.enums.AuthorityResultEnum;

/**
 * @author Zhang Bowen
 * @Description
 * 该异常类用于记录控制子系统中权限校验相关的异常
 *
 * @ClassName AuthorityTestException
 * @date 2020.09.13 14:34
 */
public class AuthorityTestException extends Exception {
    private final AuthorityResultEnum authorityResultEnum;

    public AuthorityTestException(AuthorityResultEnum authorityResultEnum) {
        this.authorityResultEnum = authorityResultEnum;
    }

    public AuthorityTestException(String message, AuthorityResultEnum authorityResultEnum) {
        super(message);
        this.authorityResultEnum = authorityResultEnum;
    }

    public AuthorityTestException(String message, Throwable cause, AuthorityResultEnum authorityResultEnum) {
        super(message, cause);
        this.authorityResultEnum = authorityResultEnum;
    }

    public AuthorityTestException(Throwable cause, AuthorityResultEnum authorityResultEnum) {
        super(cause);
        this.authorityResultEnum = authorityResultEnum;
    }

    public AuthorityResultEnum getAuthorityResultEnum() {
        return authorityResultEnum;
    }
}
