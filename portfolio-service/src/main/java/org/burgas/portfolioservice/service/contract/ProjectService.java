package org.burgas.portfolioservice.service.contract;

import org.burgas.portfolioservice.dto.project.ProjectRequest;
import org.burgas.portfolioservice.dto.project.ProjectResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public interface ProjectService<T extends ProjectRequest, V extends ProjectResponse> {

    V findById(UUID projectId);

    V createOrUpdate(T t, UUID identityId);

    String deleteById(UUID projectId, UUID identityId);

    String uploadImages(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles);

    String changeImage(UUID identityId, UUID projectId, UUID imageId, MultipartFile multipartFile);

    String deleteImage(UUID identityId, UUID projectId, UUID imageId);

    String uploadVideos(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles);

    String changeVideo(UUID identityId, UUID projectId, UUID videoId, MultipartFile multipartFile);

    String deleteVideo(UUID identityId, UUID projectId, UUID videoId);

    String uploadDocuments(UUID identityId, UUID projectId, List<MultipartFile> multipartFiles);

    String changeDocument(UUID identityId, UUID projectId, UUID documentId, MultipartFile multipartFile);

    String deleteDocument(UUID identityId, UUID projectId, UUID documentId);
}
