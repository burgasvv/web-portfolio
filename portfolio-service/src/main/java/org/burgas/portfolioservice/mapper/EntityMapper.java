package org.burgas.portfolioservice.mapper;

import org.burgas.portfolioservice.dto.Request;
import org.burgas.portfolioservice.dto.Response;
import org.burgas.portfolioservice.entity.AbstractEntity;
import org.burgas.portfolioservice.exception.EmptyFieldException;
import org.springframework.stereotype.Component;

@Component
public interface EntityMapper<T extends Request, S extends AbstractEntity, V extends Response> {

    default <D> D handleData(final D requestData, final D entityData) {
        return requestData == null || requestData == "" ? entityData : requestData;
    }

    default <D> D handleDataThrowable(final D requestData, final String message) {
        if (requestData == null || requestData == "")
            throw new EmptyFieldException(message);
        return requestData;
    }

    S toEntity(T t);

    V toResponse(S s);
}
