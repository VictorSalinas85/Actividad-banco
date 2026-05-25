package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.SecUsuarioCreateRequest;
import bd2.Banco.domain.dto.request.SecUsuarioUpdateRequest;
import bd2.Banco.domain.dto.response.SecUsuarioResponse;
import bd2.Banco.domain.entities.CatEstadoUsuario;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoUsuarioRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecUsuarioCrudService {

    private final SecUsuarioRepository repository;
    private final CatEstadoUsuarioRepository catEstadoUsuarioRepository;

    @Transactional
    public SecUsuarioResponse crear(SecUsuarioCreateRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            throw new RegistroDuplicadoException("username", request.getUsername());
        }
        if (repository.existsByEmail(request.getEmail())) {
            throw new RegistroDuplicadoException("email", request.getEmail());
        }
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", request.getEstadoId()));
        SecUsuario entity = SecUsuario.builder()
                .username(request.getUsername())
                .hashPassword(request.getHashPassword())
                .nombreCompleto(request.getNombreCompleto())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .estado(estado)
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public SecUsuarioResponse actualizar(Long id, SecUsuarioUpdateRequest request) {
        SecUsuario entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", id));
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", request.getEstadoId()));
        entity.setNombreCompleto(request.getNombreCompleto());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
        entity.setEstado(estado);
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SecUsuarioResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", id)));
    }

    @Transactional(readOnly = true)
    public List<SecUsuarioResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SecUsuarioResponse toResponse(SecUsuario entity) {
        return SecUsuarioResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .nombreCompleto(entity.getNombreCompleto())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .estadoId(entity.getEstado().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
