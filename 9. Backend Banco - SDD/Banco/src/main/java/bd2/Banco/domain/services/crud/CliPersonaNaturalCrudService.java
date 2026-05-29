package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.CliPersonaNaturalCreateRequest;
import bd2.Banco.domain.dto.request.CliPersonaNaturalUpdateRequest;
import bd2.Banco.domain.dto.response.CliPersonaNaturalResponse;
import bd2.Banco.domain.entities.CatEstadoUsuario;
import bd2.Banco.domain.entities.CatTipoIdentificacion;
import bd2.Banco.domain.entities.CliPersonaNatural;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.RegistroDuplicadoException;
import bd2.Banco.domain.repositories.CatEstadoUsuarioRepository;
import bd2.Banco.domain.repositories.CatTipoIdentificacionRepository;
import bd2.Banco.domain.repositories.CliPersonaNaturalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CliPersonaNaturalCrudService {

    private final CliPersonaNaturalRepository repository;
    private final CatTipoIdentificacionRepository catTipoIdentificacionRepository;
    private final CatEstadoUsuarioRepository catEstadoUsuarioRepository;
    private final CrudContextResolver contextResolver;

    @Transactional
    public CliPersonaNaturalResponse crear(CliPersonaNaturalCreateRequest request) {
        if (repository.existsByIdentificacion(request.getIdentificacion())) {
            throw new RegistroDuplicadoException("identificacion", request.getIdentificacion());
        }
        CatTipoIdentificacion tipoIdentificacion = catTipoIdentificacionRepository.findById(request.getTipoIdentificacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoIdentificacion", request.getTipoIdentificacionId()));
        Long estadoId = contextResolver.resolveEstadoUsuarioId(request.getEstadoId());
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(estadoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", estadoId));
        Long actor = contextResolver.resolveActor(request.getCreatedBy());
        CliPersonaNatural entity = CliPersonaNatural.builder()
                .tipoIdentificacion(tipoIdentificacion)
                .identificacion(request.getIdentificacion())
                .nombreCompleto(request.getNombreCompleto())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .fechaNacimiento(request.getFechaNacimiento())
                .direccion(request.getDireccion())
                .estado(estado)
                .createdBy(actor)
                .updatedBy(actor)
                .rowVersion(1L)
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public CliPersonaNaturalResponse actualizar(Long id, CliPersonaNaturalUpdateRequest request) {
        CliPersonaNatural entity = repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliPersonaNatural", id));

        // tipo_identificacion_id e identificacion son inmutables por trigger
        // en MySQL ('identificacion es inmutable'). Los ignoramos aunque vengan
        // en el body para no romper el UPDATE entero.

        Long estadoId = contextResolver.resolveEstadoUsuarioId(request.getEstadoId());
        CatEstadoUsuario estado = catEstadoUsuarioRepository.findById(estadoId)
                .orElseThrow(() -> new EntidadNoEncontradaException("CatEstadoUsuario", estadoId));
        if (request.getNombreCompleto() != null)  entity.setNombreCompleto(request.getNombreCompleto());
        if (request.getEmail() != null)           entity.setEmail(request.getEmail());
        if (request.getTelefono() != null)        entity.setTelefono(request.getTelefono());
        if (request.getFechaNacimiento() != null) entity.setFechaNacimiento(request.getFechaNacimiento());
        if (request.getDireccion() != null)       entity.setDireccion(request.getDireccion());
        entity.setEstado(estado);
        entity.setUpdatedBy(contextResolver.resolveActor(request.getUpdatedBy()));
        return toResponse(repository.save(entity));
    }

    @Transactional
    public void eliminarPorId(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliPersonaNatural", id));
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public CliPersonaNaturalResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("CliPersonaNatural", id)));
    }

    @Transactional(readOnly = true)
    public List<CliPersonaNaturalResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CliPersonaNaturalResponse toResponse(CliPersonaNatural entity) {
        return CliPersonaNaturalResponse.builder()
                .id(entity.getId())
                .tipoIdentificacionId(entity.getTipoIdentificacion().getId())
                .identificacion(entity.getIdentificacion())
                .nombreCompleto(entity.getNombreCompleto())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .fechaNacimiento(entity.getFechaNacimiento())
                .direccion(entity.getDireccion())
                .estadoId(entity.getEstado().getId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
