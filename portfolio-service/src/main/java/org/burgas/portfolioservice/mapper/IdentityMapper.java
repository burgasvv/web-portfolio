package org.burgas.portfolioservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.identity.IdentityNoPortfoliosResponse;
import org.burgas.portfolioservice.dto.identity.IdentityRequest;
import org.burgas.portfolioservice.dto.identity.IdentityResponse;
import org.burgas.portfolioservice.dto.portfolio.PortfolioNoIdentityResponse;
import org.burgas.portfolioservice.dto.project.ProjectNoPortfolioResponse;
import org.burgas.portfolioservice.entity.Authority;
import org.burgas.portfolioservice.entity.Identity;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.burgas.portfolioservice.message.IdentityMessages.*;

@Component
@RequiredArgsConstructor
public final class IdentityMapper implements EntityMapper<IdentityRequest, Identity, IdentityResponse> {

    private final IdentityRepository identityRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfessionMapper professionMapper;

    @Override
    public Identity toEntity(IdentityRequest identityRequest) {
        UUID identityId = this.handleData(identityRequest.getId(), UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)));
        return this.identityRepository.findById(identityId)
                .map(
                        identity -> Identity.builder()
                                .id(identity.getId())
                                .username(this.handleData(identityRequest.getUsername(), identity.getUsernameNotUserDetails()))
                                .password(identity.getPassword())
                                .email(this.handleData(identityRequest.getEmail(), identity.getEmail()))
                                .phone(this.handleData(identityRequest.getPhone(), identity.getPhone()))
                                .enabled(identity.getEnabled())
                                .authority(identity.getAuthority())
                                .createdAt(identity.getCreatedAt())
                                .updatedAt(LocalDateTime.now())
                                .build()
                )
                .orElseGet(
                        () -> {
                            String password = this.handleDataThrowable(identityRequest.getPassword(), IDENTITY_FIELD_PASSWORD_EMPTY.getMessage());
                            return Identity.builder()
                                    .username(this.handleDataThrowable(identityRequest.getUsername(), IDENTITY_FIELD_USERNAME_EMPTY.getMessage()))
                                    .password(this.passwordEncoder.encode(password))
                                    .email(this.handleDataThrowable(identityRequest.getEmail(), IDENTITY_FIELD_EMAIL_EMPTY.getMessage()))
                                    .phone(this.handleDataThrowable(identityRequest.getPhone(), IDENTITY_FIELD_PHONE_EMPTY.getMessage()))
                                    .enabled(true)
                                    .authority(Authority.USER)
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .build();
                        }
                );
    }

    @Override
    public IdentityResponse toResponse(Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .username(identity.getUsernameNotUserDetails())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .phone(identity.getPhone())
                .enabled(identity.getEnabled())
                .image(identity.getImage())
                .authority(identity.getAuthority())
                .portfolios(
                        identity.getPortfolios() == null ? null : identity.getPortfolios()
                                .stream()
                                .map(
                                        portfolio -> PortfolioNoIdentityResponse.builder()
                                                .id(portfolio.getId())
                                                .name(portfolio.getName())
                                                .description(portfolio.getDescription())
                                                .profession(
                                                        Optional.ofNullable(portfolio.getProfession())
                                                                .map(this.professionMapper::toResponse)
                                                                .orElse(null)
                                                )
                                                .opened(portfolio.getOpened())
                                                .createdAt(portfolio.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                                                .updatedAt(portfolio.getUpdateAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                                                .projects(
                                                        portfolio.getProjects() == null ? null : portfolio.getProjects()
                                                                .stream()
                                                                .map(
                                                                        project -> ProjectNoPortfolioResponse.builder()
                                                                                .id(project.getId())
                                                                                .name(project.getName())
                                                                                .description(project.getDescription())
                                                                                .createdAt(project.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                                                                                .updatedAt(project.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                                                                                .build()
                                                                )
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .createdAt(identity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .updatedAt(identity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .build();
    }

    public IdentityNoPortfoliosResponse toIdentityNoPortfolioResponse(Identity identity) {
        return IdentityNoPortfoliosResponse.builder()
                .id(identity.getId())
                .username(identity.getUsernameNotUserDetails())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .phone(identity.getPhone())
                .enabled(identity.getEnabled())
                .image(identity.getImage())
                .authority(identity.getAuthority())
                .createdAt(identity.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .updatedAt(identity.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .build();
    }
}
