package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatCanalOperacionCreateRequest;
import bd2.Banco.domain.dto.request.CatCanalOperacionUpdateRequest;
import bd2.Banco.domain.dto.response.CatCanalOperacionResponse;
import bd2.Banco.domain.entities.CatCanalOperacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatCanalOperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatCanalOperacionCrudService {

    private final CatCanalOperacionRepository repository;

    @Transactional
    public CatCanalOperacionResponse crear(CatCanalOperacionCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatCanalOperacion entity = CatCanalOperacion.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatCanalOperacionResponse actualizar(Long id, CatCanalOperacionUpdateRequest request) {
        CatCanalOperacion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatCanalOperacion", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatCanalOperacion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatCanalOperacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatCanalOperacion", id)));
    }

    @Transactional(readOnly = true)
    public List<CatCanalOperacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatCanalOperacionResponse toResponse(CatCanalOperacion entity) {
        return CatCanalOperacionResponse.builder()
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
