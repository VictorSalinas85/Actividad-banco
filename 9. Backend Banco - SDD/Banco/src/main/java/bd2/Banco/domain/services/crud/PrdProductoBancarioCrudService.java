package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.PrdProductoBancarioCreateRequest;
import bd2.Banco.domain.dto.request.PrdProductoBancarioUpdateRequest;
import bd2.Banco.domain.dto.response.PrdProductoBancarioResponse;
import bd2.Banco.domain.entities.PrdProductoBancario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.PrdProductoBancarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrdProductoBancarioCrudService {

    private final PrdProductoBancarioRepository repository;

    @Transactional
    public PrdProductoBancarioResponse crear(PrdProductoBancarioCreateRequest request) {
        repository.findByCodigoProducto(request.getCodigoProducto()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigoProducto", request.getCodigoProducto());
        });
        PrdProductoBancario entity = PrdProductoBancario.builder()
                .codigoProducto(request.getCodigoProducto())
                .nombreProducto(request.getNombreProducto())
                .categoria(request.getCategoria())
                .requiereAprobacion(request.getRequiereAprobacion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public PrdProductoBancarioResponse actualizar(Long id, PrdProductoBancarioUpdateRequest request) {
        PrdProductoBancario entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("PrdProductoBancario", id));
        entity.setNombreProducto(request.getNombreProducto());
        entity.setCategoria(request.getCategoria());
        entity.setRequiereAprobacion(request.getRequiereAprobacion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("PrdProductoBancario", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PrdProductoBancarioResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("PrdProductoBancario", id)));
    }

    @Transactional(readOnly = true)
    public List<PrdProductoBancarioResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private PrdProductoBancarioResponse toResponse(PrdProductoBancario entity) {
        return PrdProductoBancarioResponse.builder()
                .id(entity.getId())
                .codigoProducto(entity.getCodigoProducto())
                .nombreProducto(entity.getNombreProducto())
                .categoria(entity.getCategoria())
                .requiereAprobacion(entity.getRequiereAprobacion())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
