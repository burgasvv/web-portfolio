package org.burgas.portfolioservice.exception;

public class IdentityPasswordMatchesException extends RuntimeException {

    public IdentityPasswordMatchesException(String message) {
        super(message);
    }
}
