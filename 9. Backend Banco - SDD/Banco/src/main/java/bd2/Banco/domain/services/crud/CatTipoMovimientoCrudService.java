package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTipoMovimientoCreateRequest;
import bd2.Banco.domain.dto.request.CatTipoMovimientoUpdateRequest;
import bd2.Banco.domain.dto.response.CatTipoMovimientoResponse;
import bd2.Banco.domain.entities.CatTipoMovimiento;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTipoMovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTipoMovimientoCrudService {

    private final CatTipoMovimientoRepository repository;

    @Transactional
    public CatTipoMovimientoResponse crear(CatTipoMovimientoCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatTipoMovimiento entity = CatTipoMovimiento.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .naturaleza(request.getNaturaleza())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTipoMovimientoResponse actualizar(Long id, CatTipoMovimientoUpdateRequest request) {
        CatTipoMovimiento entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoMovimiento", id));
        entity.setNombre(request.getNombre());
        entity.setNaturaleza(request.getNaturaleza());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoMovimiento", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTipoMovimientoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoMovimiento", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTipoMovimientoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTipoMovimientoResponse toResponse(CatTipoMovimiento entity) {
        return CatTipoMovimientoResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .naturaleza(entity.getNaturaleza())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
