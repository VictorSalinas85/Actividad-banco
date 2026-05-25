package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatRolEmpresaCreateRequest;
import bd2.Banco.domain.dto.request.CatRolEmpresaUpdateRequest;
import bd2.Banco.domain.dto.response.CatRolEmpresaResponse;
import bd2.Banco.domain.entities.CatRolEmpresa;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatRolEmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatRolEmpresaCrudService {

    private final CatRolEmpresaRepository repository;

    @Transactional
    public CatRolEmpresaResponse crear(CatRolEmpresaCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatRolEmpresa entity = CatRolEmpresa.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatRolEmpresaResponse actualizar(Long id, CatRolEmpresaUpdateRequest request) {
        CatRolEmpresa entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatRolEmpresa", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatRolEmpresa", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatRolEmpresaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatRolEmpresa", id)));
    }

    @Transactional(readOnly = true)
    public List<CatRolEmpresaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatRolEmpresaResponse toResponse(CatRolEmpresa entity) {
        return CatRolEmpresaResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
