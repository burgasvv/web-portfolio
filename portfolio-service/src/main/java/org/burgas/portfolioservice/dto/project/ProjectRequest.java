package org.burgas.portfolioservice.dto.project;

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
public final class ProjectRequest extends Request {

    private UUID id;
    private String name;
    private String description;
    private UUID portfolioId;
}
