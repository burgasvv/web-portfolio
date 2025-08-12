package org.burgas.portfolioservice.dto.project;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.dto.portfolio.PortfolioResponse;
import org.burgas.portfolioservice.entity.Document;
import org.burgas.portfolioservice.entity.Image;
import org.burgas.portfolioservice.entity.Video;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ProjectResponse extends Response {

    private UUID id;
    private String name;
    private String description;
    private PortfolioResponse portfolio;
    private String createdAt;
    private String updatedAt;
    private List<Image> images;
    private List<Video> videos;
    private List<Document> documents;
}
