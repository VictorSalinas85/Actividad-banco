package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.AudErrorOperacionCreateRequest;
import bd2.Banco.domain.dto.request.AudErrorOperacionUpdateRequest;
import bd2.Banco.domain.dto.response.AudErrorOperacionResponse;
import bd2.Banco.domain.entities.AudErrorOperacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.OperacionCrudNoPermitidaException;
import bd2.Banco.domain.repositories.AudErrorOperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudErrorOperacionCrudService {

    private final AudErrorOperacionRepository repository;

    @Transactional
    public AudErrorOperacionResponse crear(AudErrorOperacionCreateRequest request) {
        AudErrorOperacion entity = AudErrorOperacion.builder()
                .codigoError(request.getCodigoError())
                .modulo(request.getModulo())
                .mensaje(request.getMensaje())
                .referencia(request.getReferencia())
                .actorUsuarioId(request.getActorUsuarioId())
                .payload(request.getPayload())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public AudErrorOperacionResponse actualizar(Long id, AudErrorOperacionUpdateRequest request) {
        throw new OperacionCrudNoPermitidaException("AudErrorOperacion", "actualizar");
    }

    @Transactional
    public void eliminarPorId(Long id) {
        throw new OperacionCrudNoPermitidaException("AudErrorOperacion", "eliminar");
    }

    @Transactional(readOnly = true)
    public AudErrorOperacionResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("AudErrorOperacion", id)));
    }

    @Transactional(readOnly = true)
    public List<AudErrorOperacionResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AudErrorOperacionResponse toResponse(AudErrorOperacion entity) {
        return AudErrorOperacionResponse.builder()
                .id(entity.getId())
                .codigoError(entity.getCodigoError())
                .modulo(entity.getModulo())
                .mensaje(entity.getMensaje())
                .referencia(entity.getReferencia())
                .actorUsuarioId(entity.getActorUsuarioId())
                .payload(entity.getPayload())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
