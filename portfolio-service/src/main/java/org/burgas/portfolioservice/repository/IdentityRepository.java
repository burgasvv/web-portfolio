package org.burgas.portfolioservice.repository;

import org.burgas.portfolioservice.entity.Identity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, UUID> {

    @Override
    @EntityGraph(value = "identity-with-image-and-portfolios", type = EntityGraph.EntityGraphType.LOAD)
    @NotNull List<Identity> findAll();

    @Override
    @EntityGraph(value = "identity-with-image-and-portfolios", type = EntityGraph.EntityGraphType.LOAD)
    @NotNull Optional<Identity> findById(@NotNull UUID uuid);

    Optional<Identity> findIdentityByEmail(String email);
}
