package org.burgas.portfolioservice.dto.project;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;

import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class ProjectNoPortfolioResponse extends Response {

    private UUID id;
    private String name;
    private String description;
    private String createdAt;
    private String updatedAt;
}
