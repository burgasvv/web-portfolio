package org.burgas.portfolioservice.dto.portfolio;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.dto.identity.IdentityNoPortfoliosResponse;
import org.burgas.portfolioservice.dto.profession.ProfessionResponse;

import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortfolioResponse extends Response {

    private UUID id;
    private String name;
    private String description;
    private IdentityNoPortfoliosResponse identity;
    private ProfessionResponse profession;
    private Boolean opened;
    private String createdAt;
    private String updatedAt;
}
