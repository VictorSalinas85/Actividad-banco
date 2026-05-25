package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatMonedaCreateRequest;
import bd2.Banco.domain.dto.request.CatMonedaUpdateRequest;
import bd2.Banco.domain.dto.response.CatMonedaResponse;
import bd2.Banco.domain.entities.CatMoneda;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatMonedaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatMonedaCrudService {

    private final CatMonedaRepository repository;

    @Transactional
    public CatMonedaResponse crear(CatMonedaCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatMoneda entity = CatMoneda.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatMonedaResponse actualizar(Long id, CatMonedaUpdateRequest request) {
        CatMoneda entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMoneda", id));
        entity.setNombre(request.getNombre());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMoneda", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatMonedaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMoneda", id)));
    }

    @Transactional(readOnly = true)
    public List<CatMonedaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatMonedaResponse toResponse(CatMoneda entity) {
        return CatMonedaResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
