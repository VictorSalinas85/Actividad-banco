package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.dto.request.AudBitacoraEventoCreateRequest;
import bd2.Banco.domain.dto.request.AudBitacoraEventoUpdateRequest;
import bd2.Banco.domain.dto.response.AudBitacoraEventoResponse;
import bd2.Banco.domain.entities.AudBitacoraEvento;
import bd2.Banco.domain.entities.CatTipoOperacion;
import bd2.Banco.domain.exceptions.EntidadNoEncontradaException;
import bd2.Banco.domain.exceptions.OperacionCrudNoPermitidaException;
import bd2.Banco.domain.repositories.AudBitacoraEventoRepository;
import bd2.Banco.domain.repositories.CatTipoOperacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudBitacoraEventoCrudService {

    private final AudBitacoraEventoRepository repository;
    private final CatTipoOperacionRepository catTipoOperacionRepository;

    @Transactional
    public AudBitacoraEventoResponse crear(AudBitacoraEventoCreateRequest request) {
        CatTipoOperacion tipoOperacion = catTipoOperacionRepository.findById(request.getTipoOperacionId())
                .orElseThrow(() -> new EntidadNoEncontradaException("CatTipoOperacion", request.getTipoOperacionId()));
        AudBitacoraEvento entity = AudBitacoraEvento.builder()
                .tipoOperacion(tipoOperacion)
                .fechaHoraOperacion(request.getFechaHoraOperacion())
                .idUsuario(request.getIdUsuario())
                .rolUsuarioId(request.getRolUsuarioId())
                .productoTipo(request.getProductoTipo())
                .productoId(request.getProductoId())
                .datosDetalle(request.getDatosDetalle())
                .hashIntegridad(request.getHashIntegridad())
                .build();
        return toResponse(repository.save(entity));
    }

    @Transactional
    public AudBitacoraEventoResponse actualizar(Long id, AudBitacoraEventoUpdateRequest request) {
        throw new OperacionCrudNoPermitidaException("AudBitacoraEvento", "actualizar");
    }

    @Transactional
    public void eliminarPorId(Long id) {
        throw new OperacionCrudNoPermitidaException("AudBitacoraEvento", "eliminar");
    }

    @Transactional(readOnly = true)
    public AudBitacoraEventoResponse buscarPorId(Long id) {
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("AudBitacoraEvento", id)));
    }

    @Transactional(readOnly = true)
    public List<AudBitacoraEventoResponse> buscarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private AudBitacoraEventoResponse toResponse(AudBitacoraEvento entity) {
        return AudBitacoraEventoResponse.builder()
                .id(entity.getId())
                .tipoOperacionId(entity.getTipoOperacion().getId())
                .fechaHoraOperacion(entity.getFechaHoraOperacion())
                .idUsuario(entity.getIdUsuario())
                .rolUsuarioId(entity.getRolUsuarioId())
                .productoTipo(entity.getProductoTipo())
                .productoId(entity.getProductoId())
                .datosDetalle(entity.getDatosDetalle())
                .hashIntegridad(entity.getHashIntegridad())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
