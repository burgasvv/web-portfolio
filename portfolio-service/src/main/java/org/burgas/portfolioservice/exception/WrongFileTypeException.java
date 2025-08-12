package org.burgas.portfolioservice.exception;

public class WrongFileTypeException extends RuntimeException {

    public WrongFileTypeException(String message) {
        super(message);
    }
}
