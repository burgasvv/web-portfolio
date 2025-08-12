package org.burgas.portfolioservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.portfolioservice.entity.Identity;
import org.burgas.portfolioservice.exception.IdentityNotAuthenticatedException;
import org.burgas.portfolioservice.exception.IdentityNotAuthorizedException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebFilter(
        urlPatterns = {
                "/api/v1/identities/by-id", "/api/v1/identities/update", "/api/v1/identities/delete", "/api/v1/identities/change-password",
                "/api/v1/identities/upload-image", "/api/v1/identities/change-image", "/api/v1/identities/delete-image",

                "/api/v1/portfolios/create", "/api/v1/portfolios/update", "/api/v1/portfolios/delete",

                "/api/v1/projects/create", "/api/v1/projects/update", "/api/v1/projects/delete",
                "/api/v1/projects/upload-images", "/api/v1/projects/change-image", "/api/v1/projects/delete-image",
                "/api/v1/projects/upload-videos", "/api/v1/projects/change-video", "/api/v1/projects/delete-video",
                "/api/v1/projects/upload-documents", "/api/v1/projects/change-document", "/api/v1/projects/delete-document"
        }
)
public class IdentityWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = (Authentication) request.getUserPrincipal();

        if (authentication.isAuthenticated()) {
            String identityIdParam = request.getParameter("identityId");
            Identity identity = (Identity) authentication.getPrincipal();
            UUID identityId = identityIdParam == null || identityIdParam.isBlank() ?
                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : UUID.fromString(identityIdParam);

            if (identity.getId().equals(identityId)) {
                filterChain.doFilter(request, response);

            } else {
                throw new IdentityNotAuthorizedException(IdentityMessages.IDENTITY_NOT_AUTHORIZED.getMessage());
            }

        } else {
            throw new IdentityNotAuthenticatedException(IdentityMessages.IDENTITY_NOT_AUTHENTICATED.getMessage());
        }
    }
}
