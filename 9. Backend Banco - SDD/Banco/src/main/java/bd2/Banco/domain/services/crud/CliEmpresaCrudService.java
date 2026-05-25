package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CliEmpresaCreateRequest;
import bd2.Banco.domain.dto.request.CliEmpresaUpdateRequest;
import bd2.Banco.domain.dto.response.CliEmpresaResponse;
import bd2.Banco.domain.entities.CatEstadoUsuario;
import bd2.Banco.domain.entities.CatTipoIdentificacion;
import bd2.Banco.domain.entities.CliEmpresa;
import bd2.Banco.domain.entities.CliPersonaNatural;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoUsuarioRepository;
import bd2.Banco.domain.repositories.CatTipoIdentificacionRepository;
import bd2.Banco.domain.repositories.CliEmpresaRepository;
import bd2.Banco.domain.repositories.CliPersonaNaturalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CliEmpresaCrudService {

    private final CliEmpresaRepository repository;
    private final CatTipoIdentificacionRepository catTipoIdentificacionRepository;
    private final CliPersonaNaturalRepository cliPersonaNaturalRepository;
    private final CatEstadoUsuarioRepository catEstadoUsuarioRepository;

    @Transactional
    public CliEmpresaResponse crear(CliEmpresaCreateRequest request) {
        repository.findByRuc(request.getNit()).ifPresent(e -> {
            throw new RegistroDuplicadoException("nit", request.getNit());
        });
        CatTipoIdentificacion tipoIdentificacion = catTipoIdentificacionRepository.findById(request.getTipoIdentificacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoIdentificacion", request.getTipoIdentificacionId()));
        CliPersonaNatural representante = cliPersonaNaturalRepository.findById(request.getRepresentantePersonaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CliPersonaNatural", request.getRepresentantePersonaId()));
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", request.getEstadoId()));
        CliEmpresa entity = CliEmpresa.builder()
                .tipoIdentificacion(tipoIdentificacion)
                .nit(request.getNit())
                .razonSocial(request.getRazonSocial())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .representantePersona(representante)
                .estado(estado)
                .createdBy(request.getCreatedBy())
                .updatedBy(request.getCreatedBy())
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CliEmpresaResponse actualizar(Long id, CliEmpresaUpdateRequest request) {
        CliEmpresa entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresa", id));
        CliPersonaNatural representante = cliPersonaNaturalRepository.findById(request.getRepresentantePersonaId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CliPersonaNatural", request.getRepresentantePersonaId()));
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(request.getEstadoId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", request.getEstadoId()));
        entity.setRazonSocial(request.getRazonSocial());
        entity.setEmail(request.getEmail());
        entity.setTelefono(request.getTelefono());
        entity.setDireccion(request.getDireccion());
        entity.setRepresentantePersona(representante);
        entity.setEstado(estado);
        entity.setUpdatedBy(request.getUpdatedBy());
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresa", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CliEmpresaResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliEmpresa", id)));
    }

    @Transactional(readOnly = true)
    public List<CliEmpresaResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CliEmpresaResponse toResponse(CliEmpresa entity) {
        return CliEmpresaResponse.builder()
                .id(entity.getId())
                .tipoIdentificacionId(entity.getTipoIdentificacion().getId())
                .nit(entity.getNit())
                .razonSocial(entity.getRazonSocial())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .representantePersonaId(entity.getRepresentantePersona().getId())
                .estadoId(entity.getEstado().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
