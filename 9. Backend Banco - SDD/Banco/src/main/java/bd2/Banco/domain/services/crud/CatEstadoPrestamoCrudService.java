package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatEstadoPrestamoCreateRequest;
import bd2.Banco.domain.dto.request.CatEstadoPrestamoUpdateRequest;
import bd2.Banco.domain.dto.response.CatEstadoPrestamoResponse;
import bd2.Banco.domain.entities.CatEstadoPrestamo;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatEstadoPrestamoCrudService {

    private final CatEstadoPrestamoRepository repository;

    @Transactional
    public CatEstadoPrestamoResponse crear(CatEstadoPrestamoCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatEstadoPrestamo entity = CatEstadoPrestamo.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatEstadoPrestamoResponse actualizar(Long id, CatEstadoPrestamoUpdateRequest request) {
        CatEstadoPrestamo entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoPrestamo", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoPrestamo", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatEstadoPrestamoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoPrestamo", id)));
    }

    @Transactional(readOnly = true)
    public List<CatEstadoPrestamoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatEstadoPrestamoResponse toResponse(CatEstadoPrestamo entity) {
        return CatEstadoPrestamoResponse.builder()
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
