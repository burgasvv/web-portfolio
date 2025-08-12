package org.burgas.portfolioservice.dto.identity;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.entity.Authority;
import org.burgas.portfolioservice.entity.Image;

import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class IdentityNoPortfoliosResponse extends Response {

    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Boolean enabled;
    private Image image;
    private Authority authority;
    private String createdAt;
    private String updatedAt;
}
