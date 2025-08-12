package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.burgas.portfolioservice.entity.Video;
import org.burgas.portfolioservice.exception.MultipartFileEmptyException;
import org.burgas.portfolioservice.exception.VideoNotFoundException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.message.VideoMessages;
import org.burgas.portfolioservice.repository.VideoRepository;
import org.burgas.portfolioservice.service.contract.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class VideoService implements MediaService<Video> {

    private final VideoRepository videoRepository;

    @Override
    public Video findById(UUID uuid) {
        return this.videoRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(() -> new VideoNotFoundException(VideoMessages.VIDEO_NOT_FOUND.getMessage()));
    }

    @Override
    @SneakyThrows
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Video upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());

        Video video = Video.builder()
                .name(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .format(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1])
                .size(multipartFile.getSize())
                .data(multipartFile.getBytes())
                .build();

        return this.videoRepository.save(video);
    }

    @Override
    @SneakyThrows
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Video change(UUID uuid, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());

        Video video = this.videoRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(() -> new VideoNotFoundException(VideoMessages.VIDEO_NOT_FOUND.getMessage()));

        video.setName(multipartFile.getOriginalFilename());
        video.setContentType(multipartFile.getContentType());
        video.setFormat(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1]);
        video.setSize(multipartFile.getSize());
        video.setData(multipartFile.getBytes());

        return this.videoRepository.save(video);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public void delete(UUID uuid) {
        Video video = this.videoRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(() -> new VideoNotFoundException(VideoMessages.VIDEO_NOT_FOUND.getMessage()));
        this.videoRepository.delete(video);
    }
}
