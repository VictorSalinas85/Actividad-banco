package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTipoPrestamoCreateRequest;
import bd2.Banco.domain.dto.request.CatTipoPrestamoUpdateRequest;
import bd2.Banco.domain.dto.response.CatTipoPrestamoResponse;
import bd2.Banco.domain.entities.CatTipoPrestamo;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTipoPrestamoCrudService {

    private final CatTipoPrestamoRepository repository;

    @Transactional
    public CatTipoPrestamoResponse crear(CatTipoPrestamoCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatTipoPrestamo entity = CatTipoPrestamo.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTipoPrestamoResponse actualizar(Long id, CatTipoPrestamoUpdateRequest request) {
        CatTipoPrestamo entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoPrestamo", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoPrestamo", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTipoPrestamoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoPrestamo", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTipoPrestamoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTipoPrestamoResponse toResponse(CatTipoPrestamo entity) {
        return CatTipoPrestamoResponse.builder()
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
