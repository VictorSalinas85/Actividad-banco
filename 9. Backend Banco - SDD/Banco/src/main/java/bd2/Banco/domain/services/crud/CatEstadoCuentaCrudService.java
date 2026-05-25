package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatEstadoCuentaCreateRequest;
import bd2.Banco.domain.dto.request.CatEstadoCuentaUpdateRequest;
import bd2.Banco.domain.dto.response.CatEstadoCuentaResponse;
import bd2.Banco.domain.entities.CatEstadoCuenta;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatEstadoCuentaCrudService {

    private final CatEstadoCuentaRepository repository;

    @Transactional
    public CatEstadoCuentaResponse crear(CatEstadoCuentaCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatEstadoCuenta entity = CatEstadoCuenta.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatEstadoCuentaResponse actualizar(Long id, CatEstadoCuentaUpdateRequest request) {
        CatEstadoCuenta entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatEstadoCuentaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", id)));
    }

    @Transactional(readOnly = true)
    public List<CatEstadoCuentaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatEstadoCuentaResponse toResponse(CatEstadoCuenta entity) {
        return CatEstadoCuentaResponse.builder()
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
