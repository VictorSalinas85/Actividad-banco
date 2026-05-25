package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatEstadoTransferenciaCreateRequest;
import bd2.Banco.domain.dto.request.CatEstadoTransferenciaUpdateRequest;
import bd2.Banco.domain.dto.response.CatEstadoTransferenciaResponse;
import bd2.Banco.domain.entities.CatEstadoTransferencia;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoTransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatEstadoTransferenciaCrudService {

    private final CatEstadoTransferenciaRepository repository;

    @Transactional
    public CatEstadoTransferenciaResponse crear(CatEstadoTransferenciaCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatEstadoTransferencia entity = CatEstadoTransferencia.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatEstadoTransferenciaResponse actualizar(Long id, CatEstadoTransferenciaUpdateRequest request) {
        CatEstadoTransferencia entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoTransferencia", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoTransferencia", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatEstadoTransferenciaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoTransferencia", id)));
    }

    @Transactional(readOnly = true)
    public List<CatEstadoTransferenciaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatEstadoTransferenciaResponse toResponse(CatEstadoTransferencia entity) {
        return CatEstadoTransferenciaResponse.builder()
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
