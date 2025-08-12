package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.burgas.portfolioservice.entity.Image;
import org.burgas.portfolioservice.exception.ImageNotFoundException;
import org.burgas.portfolioservice.exception.MultipartFileEmptyException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.message.ImageMessages;
import org.burgas.portfolioservice.repository.ImageRepository;
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
public class ImageService implements MediaService<Image> {

    private final ImageRepository imageRepository;

    @Override
    public Image findById(UUID uuid) {
        return this.imageRepository.findById(
                        uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid
                )
                .orElseThrow(() -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Image upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());

        Image image = Image.builder()
                .name(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .format(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1])
                .size(multipartFile.getSize())
                .data(multipartFile.getBytes())
                .build();

        return this.imageRepository.save(image);
    }

    @SneakyThrows
    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Image change(UUID uuid, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());
        Image image = this.imageRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(
                        () -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage())
                );
        image.setName(multipartFile.getOriginalFilename());
        image.setContentType(multipartFile.getContentType());
        image.setFormat(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1]);
        image.setSize(multipartFile.getSize());
        image.setData(multipartFile.getBytes());
        return this.imageRepository.save(image);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public void delete(UUID uuid) {
        Image image = this.imageRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(
                        () -> new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage())
                );
        this.imageRepository.delete(image);
    }
}
