package org.burgas.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.identity.IdentityRequest;
import org.burgas.portfolioservice.dto.identity.IdentityResponse;
import org.burgas.portfolioservice.exception.WrongFileTypeException;
import org.burgas.portfolioservice.message.IdentityMessages;
import org.burgas.portfolioservice.service.IdentityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/identities")
public class IdentityController {

    private final IdentityService identityService;

    @GetMapping
    public ResponseEntity<List<IdentityResponse>> getAllIdentities() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.identityService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<IdentityResponse> getIdentityById(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.identityService.findById(identityId));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.identityService.createOrUpdate(identityRequest));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<IdentityResponse> updateIdentity(@RequestBody IdentityRequest identityRequest, @RequestParam UUID identityId) {
        identityRequest.setId(identityId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.identityService.createOrUpdate(identityRequest));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteIdentity(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.identityService.deleteById(identityId));
    }

    @PatchMapping(value = "/change-password")
    public ResponseEntity<String> changeIdentityPassword(@RequestParam UUID identityId, @RequestBody Map<String, String> password) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.identityService.changePassword(identityId, password));
    }

    @PatchMapping(value = "/enable-disable")
    public ResponseEntity<String> enableDisableIdentity(@RequestParam UUID identityId, @RequestParam Boolean enable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.identityService.enableDisable(identityId, enable));
    }

    @PostMapping(value = "/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam UUID identityId, @RequestPart MultipartFile file) {
        if (Objects.requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(this.identityService.uploadImage(identityId, file));
        } else {
            throw new WrongFileTypeException(IdentityMessages.WRONG_FILE_TYPE.getMessage());
        }
    }

    @PutMapping(value = "/change-image")
    public ResponseEntity<String> changeImage(@RequestParam UUID identityId, @RequestPart MultipartFile file) {
        if (Objects.requireNonNull(file.getContentType()).split("/")[0].equals("image")) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                    .body(this.identityService.changeImage(identityId, file));
        } else {
            throw new WrongFileTypeException(IdentityMessages.WRONG_FILE_TYPE.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image")
    public ResponseEntity<String> deleteImage(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.identityService.deleteImage(identityId));
    }
}
