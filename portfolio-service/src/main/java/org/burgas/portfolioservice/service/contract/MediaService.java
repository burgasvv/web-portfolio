package org.burgas.portfolioservice.service.contract;

import org.burgas.portfolioservice.entity.Media;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public interface MediaService<T extends Media> {

    T findById(final UUID uuid);

    T upload(final MultipartFile multipartFile);

    @SuppressWarnings("UnusedReturnValue")
    T change(final UUID uuid, final MultipartFile multipartFile);

    void delete(final UUID uuid);
}
