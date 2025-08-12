package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum VideoMessages {

    VIDEO_NOT_FOUND("Video not found");

    private final String message;

    VideoMessages(String message) {
        this.message = message;
    }
}
