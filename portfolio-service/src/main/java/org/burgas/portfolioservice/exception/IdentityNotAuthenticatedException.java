package org.burgas.portfolioservice.exception;

public class IdentityNotAuthenticatedException extends RuntimeException {

    public IdentityNotAuthenticatedException(String message) {
        super(message);
    }
}
