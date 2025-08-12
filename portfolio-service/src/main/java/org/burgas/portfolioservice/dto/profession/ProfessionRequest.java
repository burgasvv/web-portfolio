package org.burgas.portfolioservice.dto.profession;

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
public final class ProfessionRequest extends Request {

    private UUID id;
    private String name;
    private String description;
}
