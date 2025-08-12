package org.burgas.portfolioservice.router;

import org.burgas.portfolioservice.entity.Document;
import org.burgas.portfolioservice.service.DocumentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Configuration
public class DocumentRouter {

    @Bean
    public RouterFunction<ServerResponse> documentRoutes(final DocumentService documentService) {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/documents/by-id", request -> {
                            Document document = documentService.findById(UUID.fromString(request.param("documentId").orElseThrow()));
                            return ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.parseMediaType(document.getContentType()))
                                    .body(
                                            new InputStreamResource(
                                                    new ByteArrayInputStream(document.getData())
                                            )
                                    );
                        }
                )
                .build();
    }
}
