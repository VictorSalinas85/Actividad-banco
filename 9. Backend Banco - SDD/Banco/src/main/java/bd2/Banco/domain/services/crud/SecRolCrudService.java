package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.SecRolCreateRequest;
import bd2.Banco.domain.dto.request.SecRolUpdateRequest;
import bd2.Banco.domain.dto.response.SecRolResponse;
import bd2.Banco.domain.entities.SecRol;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.SecRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecRolCrudService {

    private final SecRolRepository repository;

    @Transactional
    public SecRolResponse crear(SecRolCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        SecRol entity = SecRol.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public SecRolResponse actualizar(Long id, SecRolUpdateRequest request) {
        SecRol entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecRol", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecRol", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SecRolResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecRol", id)));
    }

    @Transactional(readOnly = true)
    public List<SecRolResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SecRolResponse toResponse(SecRol entity) {
        return SecRolResponse.builder()
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
