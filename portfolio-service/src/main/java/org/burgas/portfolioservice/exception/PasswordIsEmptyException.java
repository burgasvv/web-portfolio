package org.burgas.portfolioservice.exception;

public class PasswordIsEmptyException extends RuntimeException {

    public PasswordIsEmptyException(String message) {
        super(message);
    }
}
