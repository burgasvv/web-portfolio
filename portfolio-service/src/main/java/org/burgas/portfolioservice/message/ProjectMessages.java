package org.burgas.portfolioservice.message;

import lombok.Getter;

@Getter
public enum ProjectMessages {

    PROJECT_DOCUMENTS_UPLOADED("Project documents uploaded"),
    PROJECT_DOCUMENT_CHANGED("Project document changed"),
    PROJECT_DOCUMENT_DELETED("Project document deleted"),
    PROJECT_VIDEOS_UPLOADED("Project videos uploaded"),
    PROJECT_VIDEO_CHANGED("Project video changed"),
    PROJECT_VIDEO_DELETED("Project video deleted"),
    WRONG_FILE_TYPE("Wrong file type"),
    PROJECT_IMAGES_UPLOADED("Project images uploaded"),
    PROJECT_IMAGE_CHANGED("Project image changed"),
    PROJECT_IMAGE_DELETED("Project image deleted"),
    MULTIPART_FILE_EMPTY("Multipart file is empty"),
    PROJECT_DELETED("Project deleted"),
    PROJECT_NOT_FOUND("Project not found"),
    PROJECT_FIELD_NAME_EMPTY("Project field name is empty"),
    PROJECT_FIELD_DESCRIPTION_EMPTY("Project field description is empty"),
    PROJECT_FIELD_PORTFOLIO_EMPTY("Project field portfolio is empty");

    private final String message;

    ProjectMessages(String message) {
        this.message = message;
    }
}
