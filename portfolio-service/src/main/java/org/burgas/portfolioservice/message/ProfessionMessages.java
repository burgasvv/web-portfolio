package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum ProfessionMessages {

    PROFESSION_DELETED("Profession deleted"),
    PROFESSION_NOT_FOUND("Profession not found");

    private final String message;

    ProfessionMessages(String message) {
        this.message = message;
    }
}
