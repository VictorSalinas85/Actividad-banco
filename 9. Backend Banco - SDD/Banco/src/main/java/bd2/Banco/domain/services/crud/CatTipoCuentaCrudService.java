package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTipoCuentaCreateRequest;
import bd2.Banco.domain.dto.request.CatTipoCuentaUpdateRequest;
import bd2.Banco.domain.dto.response.CatTipoCuentaResponse;
import bd2.Banco.domain.entities.CatTipoCuenta;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTipoCuentaCrudService {

    private final CatTipoCuentaRepository repository;

    @Transactional
    public CatTipoCuentaResponse crear(CatTipoCuentaCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatTipoCuenta entity = CatTipoCuenta.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTipoCuentaResponse actualizar(Long id, CatTipoCuentaUpdateRequest request) {
        CatTipoCuenta entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoCuenta", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoCuenta", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTipoCuentaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoCuenta", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTipoCuentaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTipoCuentaResponse toResponse(CatTipoCuenta entity) {
        return CatTipoCuentaResponse.builder()
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
