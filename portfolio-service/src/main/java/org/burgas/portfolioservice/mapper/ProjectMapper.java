package org.burgas.portfolioservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.project.ProjectRequest;
import org.burgas.portfolioservice.dto.project.ProjectResponse;
import org.burgas.portfolioservice.entity.Project;
import org.burgas.portfolioservice.repository.PortfolioRepository;
import org.burgas.portfolioservice.repository.ProjectRepository;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;

import static org.burgas.portfolioservice.message.ProjectMessages.*;

@Component
@RequiredArgsConstructor
public final class ProjectMapper implements EntityMapper<ProjectRequest, Project, ProjectResponse> {

    private final ProjectRepository projectRepository;
    private final PortfolioRepository portfolioRepository;
    private final PortfolioMapper portfolioMapper;

    @Override
    public Project toEntity(ProjectRequest projectRequest) {
        UUID projectId = this.handleData(projectRequest.getId(), UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)));
        return this.projectRepository.findById(projectId)
                .map(
                        project -> {
                            UUID portfolioId = projectRequest.getPortfolioId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectRequest.getPortfolioId();
                            return Project.builder()
                                    .id(project.getId())
                                    .name(this.handleData(projectRequest.getName(), project.getName()))
                                    .description(this.handleData(projectRequest.getDescription(), project.getDescription()))
                                    .portfolio(
                                            this.handleData(
                                                    this.portfolioRepository.findById(portfolioId).orElse(null),
                                                    project.getPortfolio()
                                            )
                                    )
                                    .createdAt(project.getCreatedAt())
                                    .updatedAt(LocalDateTime.now())
                                    .build();
                        }
                )
                .orElseGet(
                        () -> {
                            UUID portfolioId = projectRequest.getPortfolioId() == null ?
                                    UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectRequest.getPortfolioId();
                            return Project.builder()
                                    .name(this.handleDataThrowable(projectRequest.getName(), PROJECT_FIELD_NAME_EMPTY.getMessage()))
                                    .description(this.handleDataThrowable(projectRequest.getDescription(), PROJECT_FIELD_DESCRIPTION_EMPTY.getMessage()))
                                    .portfolio(
                                            this.handleDataThrowable(
                                                    this.portfolioRepository.findById(portfolioId).orElse(null),
                                                    PROJECT_FIELD_PORTFOLIO_EMPTY.getMessage()
                                            )
                                    )
                                    .createdAt(LocalDateTime.now())
                                    .updatedAt(LocalDateTime.now())
                                    .build();
                        }
                );
    }

    @Override
    public ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .portfolio(
                        Optional.of(project.getPortfolio())
                                .map(this.portfolioMapper::toResponse)
                                .orElse(null)
                )
                .createdAt(project.getCreatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .updatedAt(project.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .images(project.getImages())
                .videos(project.getVideos())
                .documents(project.getDocuments())
                .build();
    }
}
