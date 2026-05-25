package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatEstadoUsuarioCreateRequest;
import bd2.Banco.domain.dto.request.CatEstadoUsuarioUpdateRequest;
import bd2.Banco.domain.dto.response.CatEstadoUsuarioResponse;
import bd2.Banco.domain.entities.CatEstadoUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatEstadoUsuarioCrudService {

    private final CatEstadoUsuarioRepository repository;

    @Transactional
    public CatEstadoUsuarioResponse crear(CatEstadoUsuarioCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatEstadoUsuario entity = CatEstadoUsuario.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .ordenVisual(request.getOrdenVisual())
                .vigentDesde(request.getVigentDesde())
                .vigentHasta(request.getVigentHasta())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatEstadoUsuarioResponse actualizar(Long id, CatEstadoUsuarioUpdateRequest request) {
        CatEstadoUsuario entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        entity.setOrdenVisual(request.getOrdenVisual());
        entity.setVigentDesde(request.getVigentDesde());
        entity.setVigentHasta(request.getVigentHasta());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatEstadoUsuarioResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", id)));
    }

    @Transactional(readOnly = true)
    public List<CatEstadoUsuarioResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatEstadoUsuarioResponse toResponse(CatEstadoUsuario entity) {
        return CatEstadoUsuarioResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .ordenVisual(entity.getOrdenVisual())
                .vigentDesde(entity.getVigentDesde())
                .vigentHasta(entity.getVigentHasta())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
