package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTipoIdentificacionCreateRequest;
import bd2.Banco.domain.dto.request.CatTipoIdentificacionUpdateRequest;
import bd2.Banco.domain.dto.response.CatTipoIdentificacionResponse;
import bd2.Banco.domain.entities.CatTipoIdentificacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTipoIdentificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTipoIdentificacionCrudService {

    private final CatTipoIdentificacionRepository repository;

    @Transactional
    public CatTipoIdentificacionResponse crear(CatTipoIdentificacionCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatTipoIdentificacion entity = CatTipoIdentificacion.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .aplicaA(request.getAplicaA())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTipoIdentificacionResponse actualizar(Long id, CatTipoIdentificacionUpdateRequest request) {
        CatTipoIdentificacion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoIdentificacion", id));
        entity.setNombre(request.getNombre());
        entity.setAplicaA(request.getAplicaA());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoIdentificacion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTipoIdentificacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoIdentificacion", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTipoIdentificacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTipoIdentificacionResponse toResponse(CatTipoIdentificacion entity) {
        return CatTipoIdentificacionResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .aplicaA(entity.getAplicaA())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
