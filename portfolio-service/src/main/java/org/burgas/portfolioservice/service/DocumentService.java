package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.burgas.portfolioservice.entity.Document;
import org.burgas.portfolioservice.exception.DocumentNotFoundException;
import org.burgas.portfolioservice.exception.MultipartFileEmptyException;
import org.burgas.portfolioservice.message.DocumentMessages;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.repository.DocumentRepository;
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
public class DocumentService implements MediaService<Document> {

    private final DocumentRepository documentRepository;

    @Override
    public Document findById(UUID uuid) {
        return this.documentRepository.findById(
                        uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid
                )
                .orElseThrow(() -> new DocumentNotFoundException(DocumentMessages.DOCUMENT_NOT_FOUND.getMessage()));
    }

    @Override
    @SneakyThrows
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Document upload(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());

        Document document = Document.builder()
                .name(multipartFile.getOriginalFilename())
                .contentType(multipartFile.getContentType())
                .format(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1])
                .size(multipartFile.getSize())
                .data(multipartFile.getBytes())
                .build();

        return this.documentRepository.save(document);
    }

    @Override
    @SneakyThrows
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public Document change(UUID uuid, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty())
            throw new MultipartFileEmptyException(IdentityMessages.MULTIPART_FILE_EMPTY.getMessage());

        Document document = this.documentRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(() -> new DocumentNotFoundException(DocumentMessages.DOCUMENT_NOT_FOUND.getMessage()));

        document.setName(multipartFile.getOriginalFilename());
        document.setContentType(multipartFile.getContentType());
        document.setFormat(Objects.requireNonNull(multipartFile.getContentType()).split("/")[1]);
        document.setSize(multipartFile.getSize());
        document.setData(multipartFile.getBytes());

        return this.documentRepository.save(document);
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public void delete(UUID uuid) {
        Document document = this.documentRepository.findById(uuid == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : uuid)
                .orElseThrow(() -> new DocumentNotFoundException(DocumentMessages.DOCUMENT_NOT_FOUND.getMessage()));
        this.documentRepository.delete(document);
    }
}
