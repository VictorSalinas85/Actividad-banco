package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatEstadoSesionCreateRequest;
import bd2.Banco.domain.dto.request.CatEstadoSesionUpdateRequest;
import bd2.Banco.domain.dto.response.CatEstadoSesionResponse;
import bd2.Banco.domain.entities.CatEstadoSesion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoSesionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatEstadoSesionCrudService {

    private final CatEstadoSesionRepository repository;

    @Transactional
    public CatEstadoSesionResponse crear(CatEstadoSesionCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatEstadoSesion entity = CatEstadoSesion.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatEstadoSesionResponse actualizar(Long id, CatEstadoSesionUpdateRequest request) {
        CatEstadoSesion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoSesion", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoSesion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatEstadoSesionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoSesion", id)));
    }

    @Transactional(readOnly = true)
    public List<CatEstadoSesionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatEstadoSesionResponse toResponse(CatEstadoSesion entity) {
        return CatEstadoSesionResponse.builder()
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
