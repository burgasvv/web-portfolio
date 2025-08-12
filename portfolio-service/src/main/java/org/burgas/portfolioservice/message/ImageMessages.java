package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum ImageMessages {

    IMAGE_NOT_FOUND("Image not found");

    private final String message;

    ImageMessages(String message) {
        this.message = message;
    }
}
