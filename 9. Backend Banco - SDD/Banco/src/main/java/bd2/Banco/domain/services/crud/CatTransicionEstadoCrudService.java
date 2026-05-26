package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatTransicionEstadoCreateRequest;
import bd2.Banco.domain.dto.request.CatTransicionEstadoUpdateRequest;
import bd2.Banco.domain.dto.response.CatTransicionEstadoResponse;
import bd2.Banco.domain.entities.CatTransicionEstado;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatTransicionEstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatTransicionEstadoCrudService {

    private final CatTransicionEstadoRepository repository;

    @Transactional
    public CatTransicionEstadoResponse crear(CatTransicionEstadoCreateRequest request) {
        if (repository.existsByEntidadAndEstadoOrigenCodigoAndEstadoDestinoCodigo(
                request.getEntidad(), request.getEstadoOrigenCodigo(), request.getEstadoDestinoCodigo())) {
            throw new RegistroDuplicadoException(
                    "entidad-estadoOrigen-estadoDestino",
                    request.getEntidad() + "-" + request.getEstadoOrigenCodigo() + "-" + request.getEstadoDestinoCodigo());
        }
        CatTransicionEstado entity = CatTransicionEstado.builder()
                .entidad(request.getEntidad())
                .estadoOrigenCodigo(request.getEstadoOrigenCodigo())
                .estadoDestinoCodigo(request.getEstadoDestinoCodigo())
                .rolRequeridoCodigo(request.getRolRequeridoCodigo())
                .requiereMotivo(request.getRequiereMotivo())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatTransicionEstadoResponse actualizar(Long id, CatTransicionEstadoUpdateRequest request) {
        CatTransicionEstado entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTransicionEstado", id));
        entity.setRolRequeridoCodigo(request.getRolRequeridoCodigo());
        entity.setRequiereMotivo(request.getRequiereMotivo());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTransicionEstado", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatTransicionEstadoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTransicionEstado", id)));
    }

    @Transactional(readOnly = true)
    public List<CatTransicionEstadoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatTransicionEstadoResponse toResponse(CatTransicionEstado entity) {
        return CatTransicionEstadoResponse.builder()
                .id(entity.getId())
                .entidad(entity.getEntidad())
                .estadoOrigenCodigo(entity.getEstadoOrigenCodigo())
                .estadoDestinoCodigo(entity.getEstadoDestinoCodigo())
                .rolRequeridoCodigo(entity.getRolRequeridoCodigo())
                .requiereMotivo(entity.getRequiereMotivo())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
