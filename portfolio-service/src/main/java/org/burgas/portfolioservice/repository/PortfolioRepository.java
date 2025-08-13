package org.burgas.portfolioservice.repository;

import org.burgas.portfolioservice.entity.Portfolio;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, UUID> {

    @Override
    @EntityGraph(value = "portfolio-with-identity-and-profession", type = EntityGraph.EntityGraphType.FETCH)
    @NotNull List<Portfolio> findAll();

    @Override
    @EntityGraph(value = "portfolio-with-identity-and-profession", type = EntityGraph.EntityGraphType.FETCH)
    @NotNull Optional<Portfolio> findById(@NotNull UUID uuid);

    @EntityGraph(value = "portfolio-with-identity-and-profession", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Portfolio> findPortfolioByName(String name);
}
