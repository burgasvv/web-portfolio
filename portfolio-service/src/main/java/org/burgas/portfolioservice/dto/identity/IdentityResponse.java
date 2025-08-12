package org.burgas.portfolioservice.dto.identity;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.dto.portfolio.PortfolioNoIdentityResponse;
import org.burgas.portfolioservice.entity.Authority;
import org.burgas.portfolioservice.entity.Image;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class IdentityResponse extends Response {

    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Boolean enabled;
    private Image image;
    private Authority authority;
    private List<PortfolioNoIdentityResponse> portfolios;
    private String createdAt;
    private String updatedAt;
}
