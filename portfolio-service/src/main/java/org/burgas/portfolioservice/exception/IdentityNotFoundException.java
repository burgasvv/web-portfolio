package org.burgas.portfolioservice.exception;

public class IdentityNotFoundException extends RuntimeException {

    public IdentityNotFoundException(String message) {
        super(message);
    }
}
