package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.burgas.portfolioservice.dto.project.ProjectRequest;
import org.burgas.portfolioservice.dto.project.ProjectResponse;
import org.burgas.portfolioservice.entity.Document;
import org.burgas.portfolioservice.entity.Image;
import org.burgas.portfolioservice.entity.Project;
import org.burgas.portfolioservice.entity.Video;
import org.burgas.portfolioservice.exception.*;
import org.burgas.portfolioservice.mapper.ProjectMapper;
import org.burgas.portfolioservice.message.DocumentMessages;
import org.burgas.portfolioservice.message.ImageMessages;
import org.burgas.portfolioservice.message.ProjectMessages;
import org.burgas.portfolioservice.message.VideoMessages;
import org.burgas.portfolioservice.repository.ProjectRepository;
import org.burgas.portfolioservice.service.contract.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ProjectServiceImpl implements ProjectService<ProjectRequest, ProjectResponse> {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final ImageService imageService;
    private final VideoService videoService;
    private final DocumentService documentService;

    @Override
    public ProjectResponse findById(UUID projectId) {
        return this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .map(this.projectMapper::toResponse)
                .orElseThrow(
                        () -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage())
                );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public ProjectResponse createOrUpdate(ProjectRequest projectRequest, UUID identityId) {
        return this.projectMapper.toResponse(
                this.projectRepository.save(this.projectMapper.toEntity(projectRequest))
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(UUID projectId, UUID identityId) {
        return this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .map(
                        project -> {
                            this.projectRepository.delete(project);
                            return ProjectMessages.PROJECT_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage())
                );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImages(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        multipartFiles.forEach(
                multipartFile -> {
                    Image image = this.imageService.upload(multipartFile);
                    project.addImage(image);
                }
        );
        return ProjectMessages.PROJECT_IMAGES_UPLOADED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(UUID identityId, UUID projectId, UUID imageId, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Pair<Project, Image> projectAndImage = this.getProjectAndImage(projectId, imageId);
        this.imageService.change(projectAndImage.b.getId(), multipartFile);
        return ProjectMessages.PROJECT_IMAGE_CHANGED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(UUID identityId, UUID projectId, UUID imageId) {
        Pair<Project, Image> projectAndImage = this.getProjectAndImage(projectId, imageId);
        projectAndImage.a.removeImage(projectAndImage.b);
        this.imageService.delete(projectAndImage.b.getId());
        return ProjectMessages.PROJECT_IMAGE_DELETED.getMessage();
    }

    private Pair<Project, Image> getProjectAndImage(final UUID projectId, final UUID imageId) {
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        Image projectImage = project.getImages()
                .stream()
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage()));
        return new Pair<>(project, projectImage);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadVideos(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        multipartFiles.forEach(
                multipartFile -> {
                    Video video = this.videoService.upload(multipartFile);
                    project.addVideo(video);
                }
        );
        return ProjectMessages.PROJECT_VIDEOS_UPLOADED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeVideo(UUID identityId, UUID projectId, UUID videoId, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Pair<Project, Video> projectAndVideo = this.getProjectAndVideo(projectId, videoId);
        this.videoService.change(projectAndVideo.b.getId(), multipartFile);
        return ProjectMessages.PROJECT_VIDEO_CHANGED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteVideo(UUID identityId, UUID projectId, UUID videoId) {
        Pair<Project, Video> projectAndVideo = this.getProjectAndVideo(projectId, videoId);
        projectAndVideo.a.removeVideo(projectAndVideo.b);
        this.videoService.delete(projectAndVideo.b.getId());
        return ProjectMessages.PROJECT_VIDEO_DELETED.getMessage();
    }

    private Pair<Project, Video> getProjectAndVideo(final UUID projectId, final UUID videoId) {
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        Video projectVideo = project.getVideos()
                .stream()
                .filter(video -> video.getId().equals(videoId))
                .findFirst()
                .orElseThrow(() -> new VideoNotFoundException(VideoMessages.VIDEO_NOT_FOUND.getMessage()));
        return new Pair<>(project, projectVideo);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadDocuments(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null || multipartFiles.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        multipartFiles.forEach(
                multipartFile -> {
                    Document document = this.documentService.upload(multipartFile);
                    project.addDocument(document);
                }
        );
        return ProjectMessages.PROJECT_DOCUMENTS_UPLOADED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeDocument(UUID identityId, UUID projectId, UUID documentId, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(ProjectMessages.MULTIPART_FILE_EMPTY.getMessage());
        Pair<Project, Document> projectAndDocument = this.getProjectAndDocument(projectId, documentId);
        this.documentService.change(projectAndDocument.b.getId(), multipartFile);
        return ProjectMessages.PROJECT_DOCUMENT_CHANGED.getMessage();
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteDocument(UUID identityId, UUID projectId, UUID documentId) {
        Pair<Project, Document> projectAndDocument = this.getProjectAndDocument(projectId, documentId);
        projectAndDocument.a.removeDocument(projectAndDocument.b);
        this.documentService.delete(projectAndDocument.b.getId());
        return ProjectMessages.PROJECT_DOCUMENT_DELETED.getMessage();
    }

    private Pair<Project, Document> getProjectAndDocument(final UUID projectId, final UUID documentId) {
        Project project = this.projectRepository.findById(
                        projectId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : projectId
                )
                .orElseThrow(() -> new ProjectNotFoundException(ProjectMessages.PROJECT_NOT_FOUND.getMessage()));
        Document projectDocument = project.getDocuments()
                .stream()
                .filter(document -> document.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new DocumentNotFoundException(DocumentMessages.DOCUMENT_NOT_FOUND.getMessage()));
        return new Pair<>(project, projectDocument);
    }
}
