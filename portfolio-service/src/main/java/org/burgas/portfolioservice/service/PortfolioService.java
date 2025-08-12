package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.portfolioservice.dto.portfolio.PortfolioRequest;
import org.burgas.portfolioservice.dto.portfolio.PortfolioResponse;
import org.burgas.portfolioservice.entity.Portfolio;
import org.burgas.portfolioservice.exception.PortfolioNotFoundException;
import org.burgas.portfolioservice.mapper.PortfolioMapper;
import org.burgas.portfolioservice.message.PortfolioMessages;
import org.burgas.portfolioservice.repository.PortfolioRepository;
import org.burgas.portfolioservice.service.contract.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PortfolioService implements EntityService<PortfolioRequest, PortfolioResponse> {

    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    @Override
    public List<PortfolioResponse> findAll() {
        return this.portfolioRepository.findAll()
                .stream()
                .map(this.portfolioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PortfolioResponse findById(UUID portfolioId) {
        return this.portfolioRepository.findById(
                        portfolioId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioId
                )
                .map(this.portfolioMapper::toResponse)
                .orElseThrow(
                        () -> new PortfolioNotFoundException(PortfolioMessages.PORTFOLIO_NOT_FOUND.getMessage())
                );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public PortfolioResponse createOrUpdate(PortfolioRequest portfolioRequest) {
        return this.portfolioMapper.toResponse(
                this.portfolioRepository.save(this.portfolioMapper.toEntity(portfolioRequest))
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(UUID portfolioId) {
        Portfolio portfolio = this.portfolioRepository.findById(portfolioId == null ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : portfolioId)
                .orElseThrow(
                        () -> new PortfolioNotFoundException(PortfolioMessages.PORTFOLIO_NOT_FOUND.getMessage())
                );

        this.portfolioRepository.delete(portfolio);
        return PortfolioMessages.PORTFOLIO_DELETED.getMessage();
    }
}
