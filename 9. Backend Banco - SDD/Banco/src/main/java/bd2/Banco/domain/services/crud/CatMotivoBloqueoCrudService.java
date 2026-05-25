package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatMotivoBloqueoCreateRequest;
import bd2.Banco.domain.dto.request.CatMotivoBloqueoUpdateRequest;
import bd2.Banco.domain.dto.response.CatMotivoBloqueoResponse;
import bd2.Banco.domain.entities.CatMotivoBloqueo;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatMotivoBloqueoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatMotivoBloqueoCrudService {

    private final CatMotivoBloqueoRepository repository;

    @Transactional
    public CatMotivoBloqueoResponse crear(CatMotivoBloqueoCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatMotivoBloqueo entity = CatMotivoBloqueo.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatMotivoBloqueoResponse actualizar(Long id, CatMotivoBloqueoUpdateRequest request) {
        CatMotivoBloqueo entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoBloqueo", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoBloqueo", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatMotivoBloqueoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoBloqueo", id)));
    }

    @Transactional(readOnly = true)
    public List<CatMotivoBloqueoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatMotivoBloqueoResponse toResponse(CatMotivoBloqueo entity) {
        return CatMotivoBloqueoResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
