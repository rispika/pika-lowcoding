package com.pika.gateway.exception;


public class NotFoundToken extends RuntimeException {

    public NotFoundToken() {
    }

    public NotFoundToken(String message) {
        super(message);
    }

    public NotFoundToken(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundToken(Throwable cause) {
        super(cause);
    }

    public NotFoundToken(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
