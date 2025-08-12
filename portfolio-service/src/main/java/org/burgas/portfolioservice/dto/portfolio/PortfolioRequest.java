package org.burgas.portfolioservice.dto.portfolio;

import lombok.*;
import org.burgas.portfolioservice.dto.Request;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PortfolioRequest extends Request {

    private UUID id;
    private String name;
    private String description;
    private UUID identityId;
    private UUID professionId;
    private Boolean opened;
}
