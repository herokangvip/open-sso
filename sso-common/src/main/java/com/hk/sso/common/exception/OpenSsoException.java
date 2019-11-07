package com.hk.sso.common.exception;

/**
 * @author heroking.
 * @version 1.0
 * @date 2019/10/29 14:12
 */
public class OpenSsoException extends RuntimeException {
    public OpenSsoException() {
        super();
    }

    public OpenSsoException(String message) {
        super(message);
    }

    public OpenSsoException(String message, Throwable cause) {
        super(message, cause);
    }
}
