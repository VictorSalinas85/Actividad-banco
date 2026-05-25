package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CtaMovimientoCreateRequest;
import bd2.Banco.domain.dto.request.CtaMovimientoUpdateRequest;
import bd2.Banco.domain.dto.response.CtaMovimientoResponse;
import bd2.Banco.domain.entities.CatCanalOperacion;
import bd2.Banco.domain.entities.CatTipoMovimiento;
import bd2.Banco.domain.entities.CatTipoOperacion;
import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.entities.CtaMovimiento;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatCanalOperacionRepository;
import bd2.Banco.domain.repositories.CatTipoMovimientoRepository;
import bd2.Banco.domain.repositories.CatTipoOperacionRepository;
import bd2.Banco.domain.repositories.CtaCuentaRepository;
import bd2.Banco.domain.repositories.CtaMovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtaMovimientoCrudService {

    private final CtaMovimientoRepository repository;
    private final CtaCuentaRepository ctaCuentaRepository;
    private final CatTipoMovimientoRepository catTipoMovimientoRepository;
    private final CatTipoOperacionRepository catTipoOperacionRepository;
    private final CatCanalOperacionRepository catCanalOperacionRepository;

    @Transactional
    public CtaMovimientoResponse crear(CtaMovimientoCreateRequest request) {
        CtaCuenta cuenta = ctaCuentaRepository.findById(request.getCuentaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", request.getCuentaId()));
        CatTipoMovimiento tipoMovimiento = catTipoMovimientoRepository.findById(request.getTipoMovimientoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoMovimiento", request.getTipoMovimientoId()));
        CatTipoOperacion tipoOperacion = catTipoOperacionRepository.findById(request.getTipoOperacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoOperacion", request.getTipoOperacionId()));
        CatCanalOperacion canal = catCanalOperacionRepository.findById(request.getCanalId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatCanalOperacion", request.getCanalId()));
        CtaMovimiento entity = CtaMovimiento.builder()
                .cuenta(cuenta)
                .tipoMovimiento(tipoMovimiento)
                .tipoOperacion(tipoOperacion)
                .canal(canal)
                .referenciaExterna(request.getReferenciaExterna())
                .idempotencyKey(request.getIdempotencyKey())
                .monto(request.getMonto())
                .saldoAntes(request.getSaldoAntes())
                .saldoDespues(request.getSaldoDespues())
                .fechaMovimiento(request.getFechaMovimiento())
                .createdBy(request.getCreatedBy())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CtaMovimientoResponse actualizar(Long id, CtaMovimientoUpdateRequest request) {
        CtaMovimiento entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaMovimiento", id));
        entity.setReferenciaExterna(request.getReferenciaExterna());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaMovimiento", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CtaMovimientoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaMovimiento", id)));
    }

    @Transactional(readOnly = true)
    public List<CtaMovimientoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CtaMovimientoResponse toResponse(CtaMovimiento entity) {
        return CtaMovimientoResponse.builder()
                .id(entity.getId())
                .cuentaId(entity.getCuenta().getId())
                .tipoMovimientoId(entity.getTipoMovimiento().getId())
                .tipoOperacionId(entity.getTipoOperacion().getId())
                .canalId(entity.getCanal().getId())
                .referenciaExterna(entity.getReferenciaExterna())
                .idempotencyKey(entity.getIdempotencyKey())
                .monto(entity.getMonto())
                .saldoAntes(entity.getSaldoAntes())
                .saldoDespues(entity.getSaldoDespues())
                .fechaMovimiento(entity.getFechaMovimiento())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
