package org.burgas.portfolioservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.profession.ProfessionRequest;
import org.burgas.portfolioservice.dto.profession.ProfessionResponse;
import org.burgas.portfolioservice.entity.Profession;
import org.burgas.portfolioservice.repository.ProfessionRepository;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public final class ProfessionMapper implements EntityMapper<ProfessionRequest, Profession, ProfessionResponse> {

    private final ProfessionRepository professionRepository;

    @Override
    public Profession toEntity(ProfessionRequest professionRequest) {
        UUID professionId = this.handleData(professionRequest.getId(), UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)));
        return this.professionRepository.findById(professionId)
                .map(
                        profession -> Profession.builder()
                                .id(profession.getId())
                                .name(this.handleData(professionRequest.getName(), profession.getName()))
                                .description(this.handleData(professionRequest.getDescription(), profession.getDescription()))
                                .build()
                )
                .orElseGet(
                        () -> Profession.builder()
                                .name(this.handleDataThrowable(professionRequest.getName(), ""))
                                .description(this.handleDataThrowable(professionRequest.getDescription(), ""))
                                .build()
                );
    }

    @Override
    public ProfessionResponse toResponse(Profession profession) {
        return ProfessionResponse.builder()
                .id(profession.getId())
                .name(profession.getName())
                .description(profession.getDescription())
                .build();
    }
}
