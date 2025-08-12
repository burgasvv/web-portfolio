package org.burgas.portfolioservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.portfolio.PortfolioRequest;
import org.burgas.portfolioservice.dto.portfolio.PortfolioResponse;
import org.burgas.portfolioservice.service.PortfolioService;
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
@RequestMapping(value = "/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<PortfolioResponse>> getAllPortfolios() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.portfolioService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<PortfolioResponse> getPortfolioById(@RequestParam UUID portfolioId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.portfolioService.findById(portfolioId));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<PortfolioResponse> createPortfolio(@RequestBody PortfolioRequest portfolioRequest, @RequestParam UUID identityId) {
        portfolioRequest.setIdentityId(identityId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(this.portfolioService.createOrUpdate(portfolioRequest));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@RequestBody PortfolioRequest portfolioRequest, @RequestParam UUID identityId) {
        portfolioRequest.setIdentityId(identityId);
        PortfolioResponse portfolioResponse = this.portfolioService.createOrUpdate(portfolioRequest);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .location(URI.create("/api/v1/portfolios/by-id?portfolioId=" + portfolioResponse.getId()))
                .body(portfolioResponse);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deletePortfolio(@RequestParam UUID portfolioId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8))
                .body(this.portfolioService.deleteById(portfolioId));
    }
}
