package org.burgas.portfolioservice.dto.identity;

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
public final class IdentityRequest extends Request {

    private UUID id;
    private String username;
    private String password;
    private String email;
    private String phone;
}
