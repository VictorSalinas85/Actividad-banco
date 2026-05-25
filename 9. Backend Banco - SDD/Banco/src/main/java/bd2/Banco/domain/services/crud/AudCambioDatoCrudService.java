package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.AudCambioDatoCreateRequest;
import bd2.Banco.domain.dto.request.AudCambioDatoUpdateRequest;
import bd2.Banco.domain.dto.response.AudCambioDatoResponse;
import bd2.Banco.domain.entities.AudCambioDato;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.OperacionCrudNoPermitidaException;
import bd2.Banco.domain.repositories.AudCambioDatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudCambioDatoCrudService {

    private final AudCambioDatoRepository repository;

    @Transactional
    public AudCambioDatoResponse crear(AudCambioDatoCreateRequest request) {
        AudCambioDato entity = AudCambioDato.builder()
                .tabla(request.getTabla())
                .registroId(request.getRegistroId())
                .accion(request.getAccion())
                .oldData(request.getOldData())
                .newData(request.getNewData())
                .sqlUser(request.getSqlUser())
                .hostName(request.getHostName())
                .trxRef(request.getTrxRef())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public AudCambioDatoResponse actualizar(Long id, AudCambioDatoUpdateRequest request) {
        throw new OperacionCrudNoPermitidaException("AudCambioDato", "actualizar");
    }

    @Transactional
    public void eliminarPorId(Long id) {
        throw new OperacionCrudNoPermitidaException("AudCambioDato", "eliminar");
    }

    @Transactional(readOnly = true)
    public AudCambioDatoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("AudCambioDato", id)));
    }

    @Transactional(readOnly = true)
    public List<AudCambioDatoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AudCambioDatoResponse toResponse(AudCambioDato entity) {
        return AudCambioDatoResponse.builder()
                .id(entity.getId())
                .tabla(entity.getTabla())
                .registroId(entity.getRegistroId())
                .accion(entity.getAccion())
                .oldData(entity.getOldData())
                .newData(entity.getNewData())
                .sqlUser(entity.getSqlUser())
                .hostName(entity.getHostName())
                .trxRef(entity.getTrxRef())
                .changedAt(entity.getChangedAt())
                .build();
    }
}
