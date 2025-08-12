package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum IdentityMessages {

    WRONG_FILE_TYPE("Wrong file type"),
    IDENTITY_IMAGE_UPLOADED("Identity image uploaded"),
    IDENTITY_IMAGE_CHANGED("Identity image changed"),
    IDENTITY_IMAGE_DELETED("Identity image deleted"),
    MULTIPART_FILE_EMPTY("Multipart file is empty"),
    IDENTITY_ENABLED("Identity enabled"),
    IDENTITY_DISABLED("Identity disabled"),
    ACTIVATION_FIELD_MATCHES("Activation field matches"),
    ACTIVATION_FIELD_EMPTY("Activation field is empty"),
    IDENTITY_PASSWORD_MATCHES("Identity new password and old password matches"),
    IDENTITY_PASSWORD_CHANGED("identity password was changed"),
    PASSWORD_IS_EMPTY("New password is empty"),
    IDENTITY_NOT_AUTHENTICATED("Identity not authenticated"),
    IDENTITY_NOT_AUTHORIZED("Identity not authorized"),
    IDENTITY_DELETED("Identity deleted"),
    IDENTITY_NOT_FOUND("Identity not found"),
    IDENTITY_FIELD_USERNAME_EMPTY("Identity field username is empty"),
    IDENTITY_FIELD_PASSWORD_EMPTY("Identity field password is empty"),
    IDENTITY_FIELD_EMAIL_EMPTY("Identity field email is empty"),
    IDENTITY_FIELD_PHONE_EMPTY("Identity field phone is empty");

    private final String message;

    IdentityMessages(String message) {
        this.message = message;
    }
}
