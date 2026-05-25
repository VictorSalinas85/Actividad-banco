package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CrePrestamoAprobacionCreateRequest;
import bd2.Banco.domain.dto.request.CrePrestamoAprobacionUpdateRequest;
import bd2.Banco.domain.dto.response.CrePrestamoAprobacionResponse;
import bd2.Banco.domain.entities.CatMotivoRechazo;
import bd2.Banco.domain.entities.CrePrestamo;
import bd2.Banco.domain.entities.CrePrestamoAprobacion;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatMotivoRechazoRepository;
import bd2.Banco.domain.repositories.CrePrestamoAprobacionRepository;
import bd2.Banco.domain.repositories.CrePrestamoRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrePrestamoAprobacionCrudService {

    private final CrePrestamoAprobacionRepository repository;
    private final CrePrestamoRepository crePrestamoRepository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final CatMotivoRechazoRepository catMotivoRechazoRepository;

    @Transactional
    public CrePrestamoAprobacionResponse crear(CrePrestamoAprobacionCreateRequest request) {
        CrePrestamo prestamo = crePrestamoRepository.findById(request.getPrestamoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamo", request.getPrestamoId()));
        SecUsuario analista = secUsuarioRepository.findById(request.getAnalistaUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getAnalistaUsuarioId()));
        CatMotivoRechazo motivoRechazo = null;
        if (request.getMotivoRechazoId() != null) {
            motivoRechazo = catMotivoRechazoRepository.findById(request.getMotivoRechazoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoRechazo", request.getMotivoRechazoId()));
        }
        CrePrestamoAprobacion entity = CrePrestamoAprobacion.builder()
                .prestamo(prestamo)
                .analistaUsuario(analista)
                .decision(request.getDecision())
                .motivoRechazo(motivoRechazo)
                .comentario(request.getComentario())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CrePrestamoAprobacionResponse actualizar(Long id, CrePrestamoAprobacionUpdateRequest request) {
        CrePrestamoAprobacion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoAprobacion", id));
        entity.setComentario(request.getComentario());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoAprobacion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CrePrestamoAprobacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoAprobacion", id)));
    }

    @Transactional(readOnly = true)
    public List<CrePrestamoAprobacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CrePrestamoAprobacionResponse toResponse(CrePrestamoAprobacion entity) {
        return CrePrestamoAprobacionResponse.builder()
                .id(entity.getId())
                .prestamoId(entity.getPrestamo().getId())
                .analistaUsuarioId(entity.getAnalistaUsuario().getId())
                .decision(entity.getDecision())
                .motivoRechazoId(entity.getMotivoRechazo() != null ? entity.getMotivoRechazo().getId() : null)
                .comentario(entity.getComentario())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
