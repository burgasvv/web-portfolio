package org.burgas.portfolioservice.router;

import org.burgas.portfolioservice.entity.Image;
import org.burgas.portfolioservice.service.ImageService;
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
public class ImageRouter {

    @Bean
    public RouterFunction<ServerResponse> imageRoutes(final ImageService imageService) {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/images/by-id", request -> {
                            Image image = imageService.findById(UUID.fromString(request.param("imageId").orElseThrow()));
                            return ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.parseMediaType(image.getContentType()))
                                    .body(
                                            new InputStreamResource(
                                                    new ByteArrayInputStream(image.getData())
                                            )
                                    );
                        }
                )
                .build();
    }
}
