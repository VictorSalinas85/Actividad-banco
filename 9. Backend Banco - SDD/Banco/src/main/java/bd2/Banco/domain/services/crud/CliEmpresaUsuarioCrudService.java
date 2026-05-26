package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CliEmpresaUsuarioCreateRequest;
import bd2.Banco.domain.dto.request.CliEmpresaUsuarioUpdateRequest;
import bd2.Banco.domain.dto.response.CliEmpresaUsuarioResponse;
import bd2.Banco.domain.entities.CliEmpresa;
import bd2.Banco.domain.entities.CliEmpresaUsuario;
import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CliEmpresaRepository;
import bd2.Banco.domain.repositories.CliEmpresaUsuarioRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CliEmpresaUsuarioCrudService {

    private final CliEmpresaUsuarioRepository repository;
    private final CliEmpresaRepository cliEmpresaRepository;
    private final SecUsuarioRepository secUsuarioRepository;

    @Transactional
    public CliEmpresaUsuarioResponse crear(CliEmpresaUsuarioCreateRequest request) {
        CliEmpresa empresa = cliEmpresaRepository.findById(request.getEmpresaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresa", request.getEmpresaId()));
        SecUsuario usuario = secUsuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntidadNoEncontradaException("SecUsuario", request.getUsuarioId()));
        if (repository.existsByEmpresaIdAndUsuarioId(request.getEmpresaId(), request.getUsuarioId())) {
            throw new RegistroDuplicadoException("empresa-usuario", request.getEmpresaId() + "-" + request.getUsuarioId());
        }
        CliEmpresaUsuario entity = CliEmpresaUsuario.builder()
                .empresa(empresa)
                .usuario(usuario)
                .activo(request.getActivo())
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CliEmpresaUsuarioResponse actualizar(Long id, CliEmpresaUsuarioUpdateRequest request) {
        CliEmpresaUsuario entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuario", id));
        entity.setActivo(request.getActivo());
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuario", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CliEmpresaUsuarioResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresaUsuario", id)));
    }

    @Transactional(readOnly = true)
    public List<CliEmpresaUsuarioResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CliEmpresaUsuarioResponse toResponse(CliEmpresaUsuario entity) {
        return CliEmpresaUsuarioResponse.builder()
                .id(entity.getId())
                .empresaId(entity.getEmpresa().getId())
                .usuarioId(entity.getUsuario().getId())
                .activo(entity.getActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
