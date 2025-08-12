package org.burgas.portfolioservice.service.contract;

import org.burgas.portfolioservice.dto.Request;
import org.burgas.portfolioservice.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface EntityService<T extends Request, V extends Response> {

    List<V> findAll();

    V findById(final UUID uuid);

    V createOrUpdate(final T t);

    String deleteById(final UUID uuid);
}
