package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CliEmpresaUsuarioRolCreateRequest;
import bd2.Banco.domain.dto.request.CliEmpresaUsuarioRolUpdateRequest;
import bd2.Banco.domain.dto.response.CliEmpresaUsuarioRolResponse;
import bd2.Banco.domain.entities.CatRolEmpresa;
import bd2.Banco.domain.entities.CliEmpresaUsuario;
import bd2.Banco.domain.entities.CliEmpresaUsuarioRol;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatRolEmpresaRepository;
import bd2.Banco.domain.repositories.CliEmpresaUsuarioRepository;
import bd2.Banco.domain.repositories.CliEmpresaUsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CliEmpresaUsuarioRolCrudService {

    private final CliEmpresaUsuarioRolRepository repository;
    private final CliEmpresaUsuarioRepository cliEmpresaUsuarioRepository;
    private final CatRolEmpresaRepository catRolEmpresaRepository;

    @Transactional
    public CliEmpresaUsuarioRolResponse crear(CliEmpresaUsuarioRolCreateRequest request) {
        CliEmpresaUsuario empresaUsuario = cliEmpresaUsuarioRepository.findById(request.getEmpresaUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuario", request.getEmpresaUsuarioId()));
        CatRolEmpresa rolEmpresa = catRolEmpresaRepository.findById(request.getRolEmpresaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatRolEmpresa", request.getRolEmpresaId()));
        if (repository.existsByEmpresaUsuarioIdAndRolEmpresaId(request.getEmpresaUsuarioId(), request.getRolEmpresaId())) {
            throw new RegistroDuplicadoException("empresaUsuario-rolEmpresa", request.getEmpresaUsuarioId() + "-" + request.getRolEmpresaId());
        }
        CliEmpresaUsuarioRol entity = CliEmpresaUsuarioRol.builder()
                .empresaUsuario(empresaUsuario)
                .rolEmpresa(rolEmpresa)
                .activo(request.getActivo())
                .createdBy(request.getCreatedBy())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CliEmpresaUsuarioRolResponse actualizar(Long id, CliEmpresaUsuarioRolUpdateRequest request) {
        CliEmpresaUsuarioRol entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuarioRol", id));
        entity.setActivo(request.getActivo());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuarioRol", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CliEmpresaUsuarioRolResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuarioRol", id)));
    }

    @Transactional(readOnly = true)
    public List<CliEmpresaUsuarioRolResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CliEmpresaUsuarioRolResponse toResponse(CliEmpresaUsuarioRol entity) {
        return CliEmpresaUsuarioRolResponse.builder()
                .id(entity.getId())
                .empresaUsuarioId(entity.getEmpresaUsuario().getId())
                .rolEmpresaId(entity.getRolEmpresa().getId())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
