package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CrePrestamoDesembolsoCreateRequest;
import bd2.Banco.domain.dto.request.CrePrestamoDesembolsoUpdateRequest;
import bd2.Banco.domain.dto.response.CrePrestamoDesembolsoResponse;
import bd2.Banco.domain.entities.CrePrestamo;
import bd2.Banco.domain.entities.CrePrestamoDesembolso;
import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.repositories.CrePrestamoDesembolsoRepository;
import bd2.Banco.domain.repositories.CrePrestamoRepository;
import bd2.Banco.domain.repositories.CtaCuentaRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrePrestamoDesembolsoCrudService {

    private final CrePrestamoDesembolsoRepository repository;
    private final CrePrestamoRepository crePrestamoRepository;
    private final SecUsuarioRepository secUsuarioRepository;
    private final CtaCuentaRepository ctaCuentaRepository;

    @Transactional
    public CrePrestamoDesembolsoResponse crear(CrePrestamoDesembolsoCreateRequest request) {
        CrePrestamo prestamo = crePrestamoRepository.findById(request.getPrestamoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamo", request.getPrestamoId()));
        SecUsuario analista = secUsuarioRepository.findById(request.getAnalistaUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getAnalistaUsuarioId()));
        CtaCuenta cuentaDestino = ctaCuentaRepository.findById(request.getCuentaDestinoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CtaCuenta", request.getCuentaDestinoId()));
        CrePrestamoDesembolso entity = CrePrestamoDesembolso.builder()
                .prestamo(prestamo)
                .analistaUsuario(analista)
                .cuentaDestino(cuentaDestino)
                .monto(request.getMonto())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CrePrestamoDesembolsoResponse actualizar(Long id, CrePrestamoDesembolsoUpdateRequest request) {
        CrePrestamoDesembolso entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoDesembolso", id));
        entity.setMonto(request.getMonto());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoDesembolso", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CrePrestamoDesembolsoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CrePrestamoDesembolso", id)));
    }

    @Transactional(readOnly = true)
    public List<CrePrestamoDesembolsoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CrePrestamoDesembolsoResponse toResponse(CrePrestamoDesembolso entity) {
        return CrePrestamoDesembolsoResponse.builder()
                .id(entity.getId())
                .prestamoId(entity.getPrestamo().getId())
                .analistaUsuarioId(entity.getAnalistaUsuario().getId())
                .cuentaDestinoId(entity.getCuentaDestino().getId())
                .monto(entity.getMonto())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
