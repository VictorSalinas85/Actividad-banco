package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.TrfTransferenciaCreateRequest;
import bd2.Banco.domain.dto.request.TrfTransferenciaUpdateRequest;
import bd2.Banco.domain.dto.response.TrfTransferenciaResponse;
import bd2.Banco.domain.entities.CatCanalOperacion;
import bd2.Banco.domain.entities.CatEstadoTransferencia;
import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.entities.TrfTransferencia;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatCanalOperacionRepository;
import bd2.Banco.domain.repositories.CatEstadoTransferenciaRepository;
import bd2.Banco.domain.repositories.CtaCuentaRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import bd2.Banco.domain.repositories.TrfTransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrfTransferenciaCrudService {

    private final TrfTransferenciaRepository repository;
    private final CtaCuentaRepository ctaCuentaRepository;
    private final CatEstadoTransferenciaRepository catEstadoTransferenciaRepository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final CatCanalOperacionRepository catCanalOperacionRepository;

    @Transactional
    public TrfTransferenciaResponse crear(TrfTransferenciaCreateRequest request) {
        CtaCuenta cuentaOrigen = ctaCuentaRepository.findById(request.getCuentaOrigenId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", request.getCuentaOrigenId()));
        CtaCuenta cuentaDestino = ctaCuentaRepository.findById(request.getCuentaDestinoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", request.getCuentaDestinoId()));
        CatEstadoTransferencia estado = catEstadoTransferenciaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoTransferencia", request.getEstadoId()));
        SecUsuario creadorUsuario = secUsuarioRepository.findById(request.getCreadorUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getCreadorUsuarioId()));
        CatCanalOperacion canal = catCanalOperacionRepository.findById(request.getCanalId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatCanalOperacion", request.getCanalId()));
        TrfTransferencia entity = TrfTransferencia.builder()
                .cuentaOrigen(cuentaOrigen)
                .cuentaDestino(cuentaDestino)
                .empresaId(request.getEmpresaId())
                .monto(request.getMonto())
                .fechaCreacion(request.getFechaCreacion())
                .estado(estado)
                .creadorUsuario(creadorUsuario)
                .canal(canal)
                .idempotencyKey(request.getIdempotencyKey())
                .referenciaExterna(request.getReferenciaExterna())
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public TrfTransferenciaResponse actualizar(Long id, TrfTransferenciaUpdateRequest request) {
        TrfTransferencia entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferencia", id));
        CatEstadoTransferencia estado = catEstadoTransferenciaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoTransferencia", request.getEstadoId()));
        entity.setEstado(estado);
        if (request.getAprobadorUsuarioId() != null) {
            SecUsuario aprobador = secUsuarioRepository.findById(request.getAprobadorUsuarioId())
                    .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getAprobadorUsuarioId()));
            entity.setAprobadorUsuario(aprobador);
        }
        entity.setFechaAprobacion(request.getFechaAprobacion());
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferencia", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public TrfTransferenciaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("TrfTransferencia", id)));
    }

    @Transactional(readOnly = true)
    public List<TrfTransferenciaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private TrfTransferenciaResponse toResponse(TrfTransferencia entity) {
        return TrfTransferenciaResponse.builder()
                .id(entity.getId())
                .cuentaOrigenId(entity.getCuentaOrigen().getId())
                .cuentaDestinoId(entity.getCuentaDestino().getId())
                .empresaId(entity.getEmpresaId())
                .monto(entity.getMonto())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaAprobacion(entity.getFechaAprobacion())
                .estadoId(entity.getEstado().getId())
                .creadorUsuarioId(entity.getCreadorUsuario().getId())
                .aprobadorUsuarioId(entity.getAprobadorUsuario() != null ? entity.getAprobadorUsuario().getId() : null)
                .canalId(entity.getCanal().getId())
                .idempotencyKey(entity.getIdempotencyKey())
                .referenciaExterna(entity.getReferenciaExterna())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
