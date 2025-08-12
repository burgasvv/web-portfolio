package org.burgas.portfolioservice.repository;

import org.burgas.portfolioservice.entity.Project;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Override
    @EntityGraph(value = "project-with-portfolio", type = EntityGraph.EntityGraphType.FETCH)
    @NotNull Optional<Project> findById(@NotNull UUID uuid);
}
