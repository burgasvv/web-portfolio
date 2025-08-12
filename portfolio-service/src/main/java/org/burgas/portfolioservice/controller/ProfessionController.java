package org.burgas.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.profession.ProfessionRequest;
import org.burgas.portfolioservice.dto.profession.ProfessionResponse;
import org.burgas.portfolioservice.service.ProfessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/professions")
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping
    public ResponseEntity<List<ProfessionResponse>> getAllProfessions() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.professionService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<ProfessionResponse> getProfessionById(@RequestParam UUID professionId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.professionService.findById(professionId));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<ProfessionResponse> createProfession(@RequestBody ProfessionRequest professionRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.professionService.createOrUpdate(professionRequest));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<ProfessionResponse> updateProfession(@RequestBody ProfessionRequest professionRequest) {
        ProfessionResponse professionResponse = this.professionService.createOrUpdate(professionRequest);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/api/v1/professions/by-id?professionId=" + professionResponse.getId()))
                .body(professionResponse);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteProfession(@RequestParam UUID professionId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.professionService.deleteById(professionId));
    }
}
