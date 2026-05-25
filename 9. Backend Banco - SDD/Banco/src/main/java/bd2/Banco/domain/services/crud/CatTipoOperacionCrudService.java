package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTipoOperacionCreateRequest;
import bd2.Banco.domain.dto.request.CatTipoOperacionUpdateRequest;
import bd2.Banco.domain.dto.response.CatTipoOperacionResponse;
import bd2.Banco.domain.entities.CatTipoOperacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTipoOperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTipoOperacionCrudService {

    private final CatTipoOperacionRepository repository;

    @Transactional
    public CatTipoOperacionResponse crear(CatTipoOperacionCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatTipoOperacion entity = CatTipoOperacion.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTipoOperacionResponse actualizar(Long id, CatTipoOperacionUpdateRequest request) {
        CatTipoOperacion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoOperacion", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoOperacion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTipoOperacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoOperacion", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTipoOperacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTipoOperacionResponse toResponse(CatTipoOperacion entity) {
        return CatTipoOperacionResponse.builder()
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
