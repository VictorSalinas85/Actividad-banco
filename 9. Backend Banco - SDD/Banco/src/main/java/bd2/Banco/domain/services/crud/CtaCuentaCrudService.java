package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CtaCuentaCreateRequest;
import bd2.Banco.domain.dto.request.CtaCuentaUpdateRequest;
import bd2.Banco.domain.dto.response.CtaCuentaResponse;
import bd2.Banco.domain.entities.CatEstadoCuenta;
import bd2.Banco.domain.entities.CatMoneda;
import bd2.Banco.domain.entities.CatTipoCuenta;
import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.enums.TipoParticipante;
import bd2.Banco.domain.exceptions.DomainException;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatEstadoCuentaRepository;
import bd2.Banco.domain.repositories.CatMonedaRepository;
import bd2.Banco.domain.repositories.CatTipoCuentaRepository;
import bd2.Banco.domain.repositories.CtaCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CtaCuentaCrudService {

    private final CtaCuentaRepository repository;
    private final CatTipoCuentaRepository catTipoCuentaRepository;
    private final CatMonedaRepository catMonedaRepository;
    private final CatEstadoCuentaRepository catEstadoCuentaRepository;
    private final CrudContextResolver contextResolver;

    @Transactional
    public CtaCuentaResponse crear(CtaCuentaCreateRequest request) {
        String numeroCuenta = generarNumeroCuenta();
        if (request.getTipoCuentaId() == null) {
            throw new DomainException("DOM-CTA-001", "Debe indicar el tipo de cuenta");
        }
        if (request.getMonedaId() == null) {
            throw new DomainException("DOM-CTA-002", "Debe indicar la moneda");
        }
        CatTipoCuenta tipoCuenta = catTipoCuentaRepository.findById(request.getTipoCuentaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoCuenta", request.getTipoCuentaId()));
        CatMoneda moneda = catMonedaRepository.findById(request.getMonedaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatMoneda", request.getMonedaId()));

        // Estado: el frontend no lo pide; por defecto una cuenta nueva queda ACTIVA.
        CatEstadoCuenta estado = (request.getEstadoId() != null)
                ? catEstadoCuentaRepository.findById(request.getEstadoId())
                        .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", request.getEstadoId()))
                : catEstadoCuentaRepository.findByCodigoAndActivoTrue("ACTIVA")
                        .or(() -> catEstadoCuentaRepository.findByCodigo("ACTIVA"))
                        .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoCuenta", "ACTIVA"));

        // Tipo de titular: se deduce de cuál id viene (persona o empresa) si no se envía.
        TipoParticipante titularTipo = resolverTitularTipo(request);

        CtaCuenta entity = CtaCuenta.builder()
                .numeroCuenta(numeroCuenta)
                .tipoCuenta(tipoCuenta)
                .titularTipo(titularTipo)
                .titularPersonaId(titularTipo == TipoParticipante.PERSONA ? request.getTitularPersonaId() : null)
                .titularEmpresaId(titularTipo == TipoParticipante.EMPRESA ? request.getTitularEmpresaId() : null)
                .saldoActual(request.getSaldoActual() != null ? request.getSaldoActual() : BigDecimal.ZERO)
                .limiteSobregirosAutorizado(request.getLimiteSobregirosAutorizado() != null
                        ? request.getLimiteSobregirosAutorizado() : BigDecimal.ZERO)
                .moneda(moneda)
                .estado(estado)
                .fechaApertura(request.getFechaApertura() != null ? request.getFechaApertura() : LocalDateTime.now())
                .createdBy(contextResolver.resolveActor(request.getCreatedBy()))
                .updatedBy(contextResolver.resolveActor(request.getCreatedBy()))
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    /** Deduce si el titular es PERSONA o EMPRESA. Usa el valor explícito si viene; si no, infiere por el id presente. */
    private TipoParticipante resolverTitularTipo(CtaCuentaCreateRequest request) {
        if (request.getTitularTipo() != null) {
            return request.getTitularTipo();
        }
        boolean tienePersona = request.getTitularPersonaId() != null;
        boolean tieneEmpresa = request.getTitularEmpresaId() != null;
        if (tienePersona && !tieneEmpresa) return TipoParticipante.PERSONA;
        if (tieneEmpresa && !tienePersona) return TipoParticipante.EMPRESA;
        throw new DomainException("DOM-CTA-003",
                "Debe indicar exactamente un titular: persona natural o empresa");
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

    /**
     * Devuelve, sin crear nada, el numero de cuenta que se asignaria a la proxima cuenta.
     * Util para previsualizarlo en los formularios de apertura. El valor real puede variar
     * si se crea otra cuenta entre la consulta y el guardado (la asignacion definitiva ocurre
     * al guardar y el indice UNIQUE protege la integridad).
     */
    @Transactional(readOnly = true)
    public String proximoNumeroCuenta() {
        return generarNumeroCuenta();
    }

    /**
     * Genera el numero de cuenta consecutivo por anio: AAAA + 7 digitos (11 caracteres).
     * El primero del anio es AAAA0000000 (p.ej. 20260000000) y a partir de alli incrementa
     * de uno en uno. Misma logica que el SP sp_cta_abrir_cuenta para mantener coherencia.
     */
    private String generarNumeroCuenta() {
        String anio = String.valueOf(Year.now(ZoneOffset.UTC).getValue());
        Long ultimo = repository.findMaxNumeroCuentaByPrefijoAnio(anio + "%");
        return (ultimo == null) ? anio + "0000000" : String.valueOf(ultimo + 1);
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
