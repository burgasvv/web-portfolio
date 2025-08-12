package org.burgas.portfolioservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.portfolioservice.dto.profession.ProfessionRequest;
import org.burgas.portfolioservice.dto.profession.ProfessionResponse;
import org.burgas.portfolioservice.exception.ProfessionNotFoundException;
import org.burgas.portfolioservice.mapper.ProfessionMapper;
import org.burgas.portfolioservice.message.ProfessionMessages;
import org.burgas.portfolioservice.repository.ProfessionRepository;
import org.burgas.portfolioservice.service.contract.EntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ProfessionService implements EntityService<ProfessionRequest, ProfessionResponse> {

    private final ProfessionRepository professionRepository;
    private final ProfessionMapper professionMapper;

    @Override
    public List<ProfessionResponse> findAll() {
        return this.professionRepository.findAll()
                .stream()
                .map(this.professionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProfessionResponse findById(UUID professionId) {
        return this.professionRepository.findById(
                        professionId == null ? UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : professionId
                )
                .map(this.professionMapper::toResponse)
                .orElseThrow(
                        () -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage())
                );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public ProfessionResponse createOrUpdate(ProfessionRequest professionRequest) {
        return this.professionMapper.toResponse(
                this.professionRepository.save(this.professionMapper.toEntity(professionRequest))
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(UUID professionId) {
        return this.professionRepository.findById(professionId == null ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : professionId)
                .map(
                        profession -> {
                            this.professionRepository.delete(profession);
                            return ProfessionMessages.PROFESSION_DELETED.getMessage();
                        }
                )
                .orElseThrow(
                        () -> new ProfessionNotFoundException(ProfessionMessages.PROFESSION_NOT_FOUND.getMessage())
                );
    }
}
