package org.burgas.portfolioservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.portfolio.PortfolioRequest;
import org.burgas.portfolioservice.dto.portfolio.PortfolioResponse;
import org.burgas.portfolioservice.entity.Portfolio;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.burgas.portfolioservice.repository.PortfolioRepository;
import org.burgas.portfolioservice.repository.ProfessionRepository;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.burgas.portfolioservice.message.PortfolioMessages.*;

@Component
@RequiredArgsConstructor
public final class PortfolioMapper implements EntityMapper<PortfolioRequest, Portfolio, PortfolioResponse> {

    private final PortfolioRepository portfolioRepository;

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;

    private final ProfessionRepository professionRepository;
    private final ProfessionMapper professionMapper;

    @Override
    public Portfolio toEntity(PortfolioRequest portfolioRequest) {
        UUID portfolioId = this.handleData(portfolioRequest.getId(), UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)));
        return this.portfolioRepository.findById(portfolioId)
                .map(
                        portfolio -> {
                            UUID professionId = portfolioRequest.getProfessionId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioRequest.getProfessionId();
                            UUID identityId = portfolioRequest.getIdentityId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioRequest.getIdentityId();

                            return Portfolio.builder()
                                    .id(portfolio.getId())
                                    .name(this.handleData(portfolioRequest.getName(), portfolio.getName()))
                                    .description(this.handleData(portfolioRequest.getDescription(), portfolio.getDescription()))
                                    .identity(
                                            this.handleData(
                                                    this.identityRepository.findById(identityId).orElse(null),
                                                    portfolio.getIdentity()
                                            )
                                    )
                                    .profession(
                                            this.handleData(
                                                    this.professionRepository.findById(professionId).orElse(null),
                                                    portfolio.getProfession()
                                            )
                                    )
                                    .opened(this.handleData(portfolioRequest.getOpened(), portfolio.getOpened()))
                                    .createdAt(portfolio.getCreatedAt())
                                    .updateAt(LocalDateTime.now())
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            UUID professionId = portfolioRequest.getProfessionId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioRequest.getProfessionId();
                            UUID identityId = portfolioRequest.getIdentityId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioRequest.getIdentityId();

                            return Portfolio.builder()
                                    .name(this.handleDataThrowable(portfolioRequest.getName(), PORTFOLIO_FIELD_NAME_EMPTY.getMessage()))
                                    .description(this.handleDataThrowable(portfolioRequest.getDescription(), PORTFOLIO_FIELD_DESCRIPTION_EMPTY.getMessage()))
                                    .identity(
                                            this.handleDataThrowable(
                                                    this.identityRepository.findById(identityId).orElse(null),
                                                    PORTFOLIO_FIELD_IDENTITY_EMPTY.getMessage()
                                            )
                                    )
                                    .profession(
                                            this.handleDataThrowable(
                                                    this.professionRepository.findById(professionId).orElse(null),
                                                    PORTFOLIO_FIELD_PROFESSION_EMPTY.getMessage()
                                            )
                                    )
                                    .opened(this.handleDataThrowable(portfolioRequest.getOpened(), PORTFOLIO_FIELD_OPENED_EMPTY.getMessage()))
                                    .createdAt(LocalDateTime.now())
                                    .updateAt(LocalDateTime.now())
                                    .build();
                        }
                );
    }

    @Override
    public PortfolioResponse toResponse(Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .name(portfolio.getName())
                .description(portfolio.getDescription())
                .identity(
                        Optional.ofNullable(portfolio.getIdentity())
                                .map(this.identityMapper::toIdentityNoPortfolioResponse)
                                .orElse(null)
                )
                .profession(
                        Optional.ofNullable(portfolio.getProfession())
                                .map(this.professionMapper::toResponse)
                                .orElse(null)
                )
                .opened(portfolio.getOpened())
                .createdAt(portfolio.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .updatedAt(portfolio.getUpdateAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .build();
    }
}
