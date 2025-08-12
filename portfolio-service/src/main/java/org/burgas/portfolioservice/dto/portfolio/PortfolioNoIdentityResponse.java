package org.burgas.portfolioservice.dto.portfolio;

import lombok.*;
import org.burgas.portfolioservice.dto.profession.ProfessionResponse;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.dto.project.ProjectNoPortfolioResponse;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortfolioNoIdentityResponse extends Response {

    private UUID id;
    private String name;
    private String description;
    private ProfessionResponse profession;
    private Boolean opened;
    private String createdAt;
    private String updatedAt;
    private List<ProjectNoPortfolioResponse> projects;
}
