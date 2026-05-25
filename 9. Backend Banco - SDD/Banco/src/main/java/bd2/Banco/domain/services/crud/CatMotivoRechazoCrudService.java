package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatMotivoRechazoCreateRequest;
import bd2.Banco.domain.dto.request.CatMotivoRechazoUpdateRequest;
import bd2.Banco.domain.dto.response.CatMotivoRechazoResponse;
import bd2.Banco.domain.entities.CatMotivoRechazo;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatMotivoRechazoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatMotivoRechazoCrudService {

    private final CatMotivoRechazoRepository repository;

    @Transactional
    public CatMotivoRechazoResponse crear(CatMotivoRechazoCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatMotivoRechazo entity = CatMotivoRechazo.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatMotivoRechazoResponse actualizar(Long id, CatMotivoRechazoUpdateRequest request) {
        CatMotivoRechazo entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoRechazo", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoRechazo", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatMotivoRechazoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoRechazo", id)));
    }

    @Transactional(readOnly = true)
    public List<CatMotivoRechazoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatMotivoRechazoResponse toResponse(CatMotivoRechazo entity) {
        return CatMotivoRechazoResponse.builder()
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
