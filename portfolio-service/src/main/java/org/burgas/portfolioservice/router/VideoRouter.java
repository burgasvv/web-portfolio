package org.burgas.portfolioservice.router;

import org.burgas.portfolioservice.entity.Video;
import org.burgas.portfolioservice.service.VideoService;
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
public class VideoRouter {

    @Bean
    public RouterFunction<ServerResponse> videoRoutes(final VideoService videoService) {
        return RouterFunctions.route()
                .GET(
                        "/api/v1/videos/by-id", request -> {
                            Video video = videoService.findById(UUID.fromString(request.param("videoId").orElseThrow()));
                            return ServerResponse
                                    .status(HttpStatus.OK)
                                    .contentType(MediaType.parseMediaType(video.getContentType()))
                                    .body(
                                            new InputStreamResource(
                                                    new ByteArrayInputStream(video.getData())
                                            )
                                    );
                        }
                )
                .build();
    }
}
