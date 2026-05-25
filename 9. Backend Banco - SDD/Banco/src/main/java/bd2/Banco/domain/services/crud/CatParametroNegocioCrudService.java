package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CatParametroNegocioCreateRequest;
import bd2.Banco.domain.dto.request.CatParametroNegocioUpdateRequest;
import bd2.Banco.domain.dto.response.CatParametroNegocioResponse;
import bd2.Banco.domain.entities.CatParametroNegocio;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatParametroNegocioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatParametroNegocioCrudService {

    private final CatParametroNegocioRepository repository;

    @Transactional
    public CatParametroNegocioResponse crear(CatParametroNegocioCreateRequest request) {
        repository.findByCodigo(request.getCodigo()).ifPresent(e -> {
            throw new RegistroDuplicadoException("codigo", request.getCodigo());
        });
        CatParametroNegocio entity = CatParametroNegocio.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .valorTexto(request.getValorTexto())
                .valorNumerico(request.getValorNumerico())
                .valorBooleano(request.getValorBooleano())
                .activo(request.getActivo())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CatParametroNegocioResponse actualizar(Long id, CatParametroNegocioUpdateRequest request) {
        CatParametroNegocio entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatParametroNegocio", id));
        entity.setNombre(request.getNombre());
        entity.setDescripcion(request.getDescripcion());
        entity.setValorTexto(request.getValorTexto());
        entity.setValorNumerico(request.getValorNumerico());
        entity.setValorBooleano(request.getValorBooleano());
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatParametroNegocio", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CatParametroNegocioResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatParametroNegocio", id)));
    }

    @Transactional(readOnly = true)
    public List<CatParametroNegocioResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CatParametroNegocioResponse toResponse(CatParametroNegocio entity) {
        return CatParametroNegocioResponse.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .valorTexto(entity.getValorTexto())
                .valorNumerico(entity.getValorNumerico())
                .valorBooleano(entity.getValorBooleano())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
