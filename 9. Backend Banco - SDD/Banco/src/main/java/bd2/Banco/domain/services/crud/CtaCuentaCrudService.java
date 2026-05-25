package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CtaCuentaCreateRequest;
import bd2.Banco.domain.dto.request.CtaCuentaUpdateRequest;
import bd2.Banco.domain.dto.response.CtaCuentaResponse;
import bd2.Banco.domain.entities.CatEstadoCuenta;
import bd2.Banco.domain.entities.CatMoneda;
import bd2.Banco.domain.entities.CatTipoCuenta;
import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoCuentaRepository;
import bd2.Banco.domain.repositories.CatMonedaRepository;
import bd2.Banco.domain.repositories.CatTipoCuentaRepository;
import bd2.Banco.domain.repositories.CtaCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtaCuentaCrudService {

    private final CtaCuentaRepository repository;
    private final CatTipoCuentaRepository catTipoCuentaRepository;
    private final CatMonedaRepository catMonedaRepository;
    private final CatEstadoCuentaRepository catEstadoCuentaRepository;

    @Transactional
    public CtaCuentaResponse crear(CtaCuentaCreateRequest request) {
        repository.findByNumeroCuenta(request.getNumeroCuenta()).ifPresent(e -> {
            throw new RegistroDuplicadoException("numeroCuenta", request.getNumeroCuenta());
        });
        CatTipoCuenta tipoCuenta = catTipoCuentaRepository.findById(request.getTipoCuentaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoCuenta", request.getTipoCuentaId()));
        CatMoneda moneda = catMonedaRepository.findById(request.getMonedaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMoneda", request.getMonedaId()));
        CatEstadoCuenta estado = catEstadoCuentaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", request.getEstadoId()));
        CtaCuenta entity = CtaCuenta.builder()
                .numeroCuenta(request.getNumeroCuenta())
                .tipoCuenta(tipoCuenta)
                .titularTipo(request.getTitularTipo())
                .titularPersonaId(request.getTitularPersonaId())
                .titularEmpresaId(request.getTitularEmpresaId())
                .saldoActual(request.getSaldoActual())
                .limiteSobregirosAutorizado(request.getLimiteSobregirosAutorizado())
                .moneda(moneda)
                .estado(estado)
                .fechaApertura(request.getFechaApertura())
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CtaCuentaResponse actualizar(Long id, CtaCuentaUpdateRequest request) {
        CtaCuenta entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", id));
        CatEstadoCuenta estado = catEstadoCuentaRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", request.getEstadoId()));
        entity.setSaldoActual(request.getSaldoActual());
        entity.setLimiteSobregirosAutorizado(request.getLimiteSobregirosAutorizado());
        entity.setEstado(estado);
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CtaCuentaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", id)));
    }

    @Transactional(readOnly = true)
    public List<CtaCuentaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CtaCuentaResponse toResponse(CtaCuenta entity) {
        return CtaCuentaResponse.builder()
                .id(entity.getId())
                .numeroCuenta(entity.getNumeroCuenta())
                .tipoCuentaId(entity.getTipoCuenta().getId())
                .titularTipo(entity.getTitularTipo())
                .titularPersonaId(entity.getTitularPersonaId())
                .titularEmpresaId(entity.getTitularEmpresaId())
                .saldoActual(entity.getSaldoActual())
                .limiteSobregirosAutorizado(entity.getLimiteSobregirosAutorizado())
                .monedaId(entity.getMoneda().getId())
                .estadoId(entity.getEstado().getId())
                .fechaApertura(entity.getFechaApertura())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
