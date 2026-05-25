package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.TrfTransferenciaAprobacionCreateRequest;
import bd2.Banco.domain.dto.request.TrfTransferenciaAprobacionUpdateRequest;
import bd2.Banco.domain.dto.response.TrfTransferenciaAprobacionResponse;
import bd2.Banco.domain.entities.CatMotivoRechazo;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.entities.TrfTransferencia;
import bd2.Banco.domain.entities.TrfTransferenciaAprobacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatMotivoRechazoRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import bd2.Banco.domain.repositories.TrfTransferenciaAprobacionRepository;
import bd2.Banco.domain.repositories.TrfTransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrfTransferenciaAprobacionCrudService {

    private final TrfTransferenciaAprobacionRepository repository;
    private final TrfTransferenciaRepository trfTransferenciaRepository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final CatMotivoRechazoRepository catMotivoRechazoRepository;

    @Transactional
    public TrfTransferenciaAprobacionResponse crear(TrfTransferenciaAprobacionCreateRequest request) {
        TrfTransferencia transferencia = trfTransferenciaRepository.findById(request.getTransferenciaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferencia", request.getTransferenciaId()));
        SecUsuario aprobador = secUsuarioRepository.findById(request.getAprobadorUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getAprobadorUsuarioId()));
        CatMotivoRechazo motivoRechazo = null;
        if (request.getMotivoRechazoId() != null) {
            motivoRechazo = catMotivoRechazoRepository.findById(request.getMotivoRechazoId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("CatMotivoRechazo", request.getMotivoRechazoId()));
        }
        TrfTransferenciaAprobacion entity = TrfTransferenciaAprobacion.builder()
                .transferencia(transferencia)
                .aprobadorUsuario(aprobador)
                .decision(request.getDecision())
                .motivoRechazo(motivoRechazo)
                .comentario(request.getComentario())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public TrfTransferenciaAprobacionResponse actualizar(Long id, TrfTransferenciaAprobacionUpdateRequest request) {
        TrfTransferenciaAprobacion entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferenciaAprobacion", id));
        entity.setComentario(request.getComentario());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferenciaAprobacion", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TrfTransferenciaAprobacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferenciaAprobacion", id)));
    }

    @Transactional(readOnly = true)
    public List<TrfTransferenciaAprobacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TrfTransferenciaAprobacionResponse toResponse(TrfTransferenciaAprobacion entity) {
        return TrfTransferenciaAprobacionResponse.builder()
                .id(entity.getId())
                .transferenciaId(entity.getTransferencia().getId())
                .aprobadorUsuarioId(entity.getAprobadorUsuario().getId())
                .decision(entity.getDecision())
                .motivoRechazoId(entity.getMotivoRechazo() != null ? entity.getMotivoRechazo().getId() : null)
                .comentario(entity.getComentario())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
