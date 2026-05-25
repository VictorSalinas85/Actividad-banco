package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.SecUsuarioRolCreateRequest;
import bd2.Banco.domain.dto.request.SecUsuarioRolUpdateRequest;
import bd2.Banco.domain.dto.response.SecUsuarioRolResponse;
import bd2.Banco.domain.entities.SecRol;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.entities.SecUsuarioRol;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.SecRolRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import bd2.Banco.domain.repositories.SecUsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecUsuarioRolCrudService {

    private final SecUsuarioRolRepository repository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final SecRolRepository secRolRepository;

    @Transactional
    public SecUsuarioRolResponse crear(SecUsuarioRolCreateRequest request) {
        SecUsuario usuario = secUsuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getUsuarioId()));
        SecRol rol = secRolRepository.findById(request.getRolId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecRol", request.getRolId()));
        SecUsuarioRol entity = SecUsuarioRol.builder()
                .usuario(usuario)
                .rol(rol)
                .createdBy(request.getCreatedBy())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public SecUsuarioRolResponse actualizar(Long id, SecUsuarioRolUpdateRequest request) {
        SecUsuarioRol entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuarioRol", id));
        entity.setCreatedBy(request.getCreatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuarioRol", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SecUsuarioRolResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuarioRol", id)));
    }

    @Transactional(readOnly = true)
    public List<SecUsuarioRolResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SecUsuarioRolResponse toResponse(SecUsuarioRol entity) {
        return SecUsuarioRolResponse.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuario().getId())
                .rolId(entity.getRol().getId())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
