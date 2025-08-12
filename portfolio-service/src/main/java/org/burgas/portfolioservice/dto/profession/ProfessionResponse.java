package org.burgas.portfolioservice.dto.profession;

import lombok.*;
import org.burgas.portfolioservice.dto.Response;

import java.util.UUID;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfessionResponse extends Response {

    private UUID id;
    private String name;
    private String description;
}
