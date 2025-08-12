package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum DocumentMessages {

    DOCUMENT_NOT_FOUND("Document not found");

    private final String message;

    DocumentMessages(String message) {
        this.message = message;
    }
}
