package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.RechazarPrestamoRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCreRechazarPrestamoService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(RechazarPrestamoRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_prestamo_id", request.getPrestamoId());
        params.put("p_motivo_rechazo_codigo", request.getMotivoRechazoCodigo());
        params.put("p_comentario", request.getComentario());
        params.put("p_analista_usuario_id", request.getAnalistaUsuarioId());

        return procedureCallHelper.ejecutar("sp_cre_rechazar_prestamo", params);
    }
}
