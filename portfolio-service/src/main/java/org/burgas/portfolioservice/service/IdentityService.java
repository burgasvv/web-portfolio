package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.identity.IdentityRequest;
import org.burgas.portfolioservice.dto.identity.IdentityResponse;
import org.burgas.portfolioservice.entity.Identity;
import org.burgas.portfolioservice.entity.Image;
import org.burgas.portfolioservice.exception.*;
import org.burgas.portfolioservice.mapper.IdentityMapper;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.message.ImageMessages;
import org.burgas.portfolioservice.repository.IdentityRepository;
import org.burgas.portfolioservice.service.contract.EntityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class IdentityService implements EntityService<IdentityRequest, IdentityResponse> {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .stream()
                .map(this.identityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public IdentityResponse findById(UUID identityId) {
        return this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .map(this.identityMapper::toResponse)
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(IdentityRequest identityRequest) {
        return this.identityMapper.toResponse(
                this.identityRepository.save(this.identityMapper.toEntity(identityRequest))
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(UUID identityId) {
        return this.identityRepository.findById(identityId == null ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId)
                .map(
                        identity -> {
                            this.identityRepository.delete(identity);
                            return IdentityMessages.IDENTITY_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String changePassword(UUID identityId, Map<String, String> password) {
        String newPassword = password.get("password");
        if (newPassword == null || newPassword.isBlank())
            throw new PasswordIsEmptyException(IdentityMessages.PASSWORD_IS_EMPTY.getMessage());

        Identity identity = this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
        if (this.passwordEncoder.matches(newPassword, identity.getPassword()))
            throw new IdentityPasswordMatchesException(IdentityMessages.IDENTITY_PASSWORD_MATCHES.getMessage());

        identity.setPassword(this.passwordEncoder.encode(newPassword));
        this.identityRepository.save(identity);
        return IdentityMessages.IDENTITY_PASSWORD_CHANGED.getMessage();
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String enableDisable(UUID identityId, Boolean enable) {
        Identity identity = this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
        if (enable == null)
            throw new ActivationFieldIsEmptyException(IdentityMessages.ACTIVATION_FIELD_EMPTY.getMessage());

        if (identity.getEnabled().equals(enable))
            throw new ActivationFieldMatchesException(IdentityMessages.ACTIVATION_FIELD_MATCHES.getMessage());

        identity.setEnabled(enable);
        Identity save = this.identityRepository.save(identity);
        return save.getEnabled() ? IdentityMessages.IDENTITY_ENABLED.getMessage() : IdentityMessages.IDENTITY_DISABLED.getMessage();
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String uploadImage(UUID identityId, MultipartFile multipartFile) {
        Identity identity = this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
        Image image = this.imageService.upload(multipartFile);
        identity.setImage(image);
        this.identityRepository.save(identity);
        return IdentityMessages.IDENTITY_IMAGE_UPLOADED.getMessage();
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String changeImage(UUID identityId, MultipartFile multipartFile) {
        Identity identity = this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );
        if (identity.getImage() != null) {
            this.imageService.change(identity.getImage().getId(), multipartFile);
            return IdentityMessages.IDENTITY_IMAGE_CHANGED.getMessage();

        } else {
            throw new ImageNotFoundException(ImageMessages.IMAGE_NOT_FOUND.getMessage());
        }
    }

    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteImage(UUID identityId) {
        Identity identity = this.identityRepository.findById(
                        identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : identityId
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IdentityMessages.IDENTITY_NOT_FOUND.getMessage())
                );

        Image image = identity.getImage();
        identity.setImage(null);
        this.identityRepository.save(identity);
        this.imageService.delete(image.getId());
        return IdentityMessages.IDENTITY_IMAGE_DELETED.getMessage();
    }
}
