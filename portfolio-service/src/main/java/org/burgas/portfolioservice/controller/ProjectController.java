package org.burgas.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.project.ProjectRequest;
import org.burgas.portfolioservice.dto.project.ProjectResponse;
import org.burgas.portfolioservice.exception.WrongFileTypeException;
import org.burgas.portfolioservice.message.ProjectMessages;
import org.burgas.portfolioservice.service.ProjectServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/projects")
public class ProjectController {

    private final ProjectServiceImpl projectService;

    @GetMapping(value = "/by-id")
    public ResponseEntity<ProjectResponse> getProjectById(@RequestParam UUID projectId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.projectService.findById(projectId));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest, @RequestParam UUID identityId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.projectService.createOrUpdate(projectRequest));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ProjectResponse> updateProject(@RequestBody ProjectRequest projectRequest, @RequestParam UUID identityId) {
        ProjectResponse projectResponse = this.projectService.createOrUpdate(projectRequest);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/api/v1/projects/by-id?projectId=" + projectResponse.getId()))
                .body(projectResponse);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteProject(@RequestParam UUID projectId, @RequestParam UUID identityId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.deleteById(projectId));
    }

    @PostMapping(value = "/upload-images")
    public ResponseEntity<String> uploadProjectImages(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestPart List<MultipartFile> file) {
        file = file.stream()
                .filter(multipartFile -> Objects.requireNonNull(multipartFile.getContentType()).split("/")[0].equals("image"))
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.uploadImages(identityId, projectId, file));
    }

    @PutMapping(value = "/change-image")
    public ResponseEntity<String> changeProjectImage(
            @RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID imageId, @RequestParam MultipartFile file
    ) {
        if (Objects.requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(this.projectService.changeImage(projectId, imageId, file));
        } else {
            throw new WrongFileTypeException(ProjectMessages.WRONG_FILE_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public ResponseEntity<String> deleteProjectImage(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID imageId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.deleteImage(projectId, imageId));
    }

    @PostMapping(value = "/upload-videos")
    public ResponseEntity<String> uploadProjectVideos(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestPart List<MultipartFile> file) {
        file = file.stream()
                .filter(multipartFile -> Objects.requireNonNull(multipartFile.getContentType()).split("/")[0].equals("video"))
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.uploadVideos(projectId, file));
    }

    @PutMapping(value = "/change-video")
    public ResponseEntity<String> changeProjectVideo(
            @RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID videoId, @RequestParam MultipartFile file
    ) {
        if (Objects.requireNonNull(file.getContentType()).split("/")[0].equals("video")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(this.projectService.changeVideo(projectId, videoId, file));
        } else {
            throw new WrongFileTypeException(ProjectMessages.WRONG_FILE_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-video")
    public ResponseEntity<String> deleteProjectVideo(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID videoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.deleteVideo(projectId, videoId));
    }

    @PostMapping(value = "/upload-documents")
    public ResponseEntity<String> uploadProjectDocuments(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestPart List<MultipartFile> file) {
        file = file.stream()
                .filter(
                        multipartFile -> Objects.requireNonNull(multipartFile.getContentType()).split("/")[0].equals("text") ||
                                         multipartFile.getContentType().split("/")[0].equals("application")
                )
                .toList();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.projectService.uploadDocuments(projectId, file));
    }

    @PutMapping(value = "/change-document")
    public ResponseEntity<String> changeProjectDocument(
            @RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID documentId, @RequestParam MultipartFile file
    ) {
        if (
                Objects.requireNonNull(file.getContentType()).split("/")[0].equals("text") ||
                file.getContentType().split("/")[0].equals("application")
        ) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(this.projectService.changeDocument(projectId, documentId, file));
        } else {
            throw new WrongFileTypeException(ProjectMessages.WRONG_FILE_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-document")
    public ResponseEntity<String> deleteProjectDocument(@RequestParam UUID identityId, @RequestParam UUID projectId, @RequestParam UUID documentId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.projectService.deleteDocument(projectId, documentId));
    }
}
