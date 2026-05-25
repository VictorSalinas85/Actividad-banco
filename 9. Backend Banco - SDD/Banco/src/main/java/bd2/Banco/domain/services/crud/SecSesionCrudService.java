package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.SecSesionCreateRequest;
import bd2.Banco.domain.dto.request.SecSesionUpdateRequest;
import bd2.Banco.domain.dto.response.SecSesionResponse;
import bd2.Banco.domain.entities.CatEstadoSesion;
import bd2.Banco.domain.entities.SecSesion;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatEstadoSesionRepository;
import bd2.Banco.domain.repositories.SecSesionRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SecSesionCrudService {

    private final SecSesionRepository repository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final CatEstadoSesionRepository catEstadoSesionRepository;

    @Transactional
    public SecSesionResponse crear(SecSesionCreateRequest request) {
        SecUsuario usuario = secUsuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getUsuarioId()));
        CatEstadoSesion estadoSesion = catEstadoSesionRepository.findById(request.getEstadoSesionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoSesion", request.getEstadoSesionId()));
        SecSesion entity = SecSesion.builder()
                .token(request.getToken())
                .usuario(usuario)
                .estadoSesion(estadoSesion)
                .expiraAt(request.getExpiraAt())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public SecSesionResponse actualizar(Long id, SecSesionUpdateRequest request) {
        SecSesion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecSesion", id));
        CatEstadoSesion estadoSesion = catEstadoSesionRepository.findById(request.getEstadoSesionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoSesion", request.getEstadoSesionId()));
        entity.setEstadoSesion(estadoSesion);
        entity.setExpiraAt(request.getExpiraAt());
        entity.setRevocadaAt(request.getRevocadaAt());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecSesion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public SecSesionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("SecSesion", id)));
    }

    @Transactional(readOnly = true)
    public List<SecSesionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SecSesionResponse toResponse(SecSesion entity) {
        return SecSesionResponse.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .usuarioId(entity.getUsuario().getId())
                .estadoSesionId(entity.getEstadoSesion().getId())
                .expiraAt(entity.getExpiraAt())
                .revocadaAt(entity.getRevocadaAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
