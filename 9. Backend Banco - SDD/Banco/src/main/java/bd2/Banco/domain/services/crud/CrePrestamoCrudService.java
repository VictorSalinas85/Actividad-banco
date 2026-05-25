package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CrePrestamoCreateRequest;
import bd2.Banco.domain.dto.request.CrePrestamoUpdateRequest;
import bd2.Banco.domain.dto.response.CrePrestamoResponse;
import bd2.Banco.domain.entities.CatEstadoPrestamo;
import bd2.Banco.domain.entities.CatTipoPrestamo;
import bd2.Banco.domain.entities.CrePrestamo;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CatEstadoPrestamoRepository;
import bd2.Banco.domain.repositories.CatTipoPrestamoRepository;
import bd2.Banco.domain.repositories.CrePrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrePrestamoCrudService {

    private final CrePrestamoRepository repository;
    private final CatTipoPrestamoRepository catTipoPrestamoRepository;
    private final CatEstadoPrestamoRepository catEstadoPrestamoRepository;

    @Transactional
    public CrePrestamoResponse crear(CrePrestamoCreateRequest request) {
        CatTipoPrestamo tipoPrestamo = catTipoPrestamoRepository.findById(request.getTipoPrestamoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoPrestamo", request.getTipoPrestamoId()));
        CatEstadoPrestamo estado = catEstadoPrestamoRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoPrestamo", request.getEstadoId()));
        CrePrestamo entity = CrePrestamo.builder()
                .tipoPrestamo(tipoPrestamo)
                .clienteTipo(request.getClienteTipo())
                .clientePersonaId(request.getClientePersonaId())
                .clienteEmpresaId(request.getClienteEmpresaId())
                .montoSolicitado(request.getMontoSolicitado())
                .plazoMeses(request.getPlazoMeses())
                .estado(estado)
                .cuentaDestinoDesembolsoId(request.getCuentaDestinoDesembolsoId())
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CrePrestamoResponse actualizar(Long id, CrePrestamoUpdateRequest request) {
        CrePrestamo entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamo", id));
        CatEstadoPrestamo estado = catEstadoPrestamoRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoPrestamo", request.getEstadoId()));
        entity.setMontoAprobado(request.getMontoAprobado());
        entity.setTasaInteres(request.getTasaInteres());
        entity.setEstado(estado);
        entity.setFechaAprobacion(request.getFechaAprobacion());
        entity.setFechaDesembolso(request.getFechaDesembolso());
        entity.setCuentaDestinoDesembolsoId(request.getCuentaDestinoDesembolsoId());
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamo", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CrePrestamoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamo", id)));
    }

    @Transactional(readOnly = true)
    public List<CrePrestamoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CrePrestamoResponse toResponse(CrePrestamo entity) {
        return CrePrestamoResponse.builder()
                .id(entity.getId())
                .tipoPrestamoId(entity.getTipoPrestamo().getId())
                .clienteTipo(entity.getClienteTipo())
                .clientePersonaId(entity.getClientePersonaId())
                .clienteEmpresaId(entity.getClienteEmpresaId())
                .montoSolicitado(entity.getMontoSolicitado())
                .montoAprobado(entity.getMontoAprobado())
                .tasaInteres(entity.getTasaInteres())
                .plazoMeses(entity.getPlazoMeses())
                .estadoId(entity.getEstado().getId())
                .fechaAprobacion(entity.getFechaAprobacion())
                .fechaDesembolso(entity.getFechaDesembolso())
                .cuentaDestinoDesembolsoId(entity.getCuentaDestinoDesembolsoId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
