package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.RechazarTransferenciaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpTrfRechazarTransferenciaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(RechazarTransferenciaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_transferencia_id", request.getTransferenciaId());
        params.put("p_aprobador_usuario_id", request.getAprobadorUsuarioId());
        params.put("p_motivo_rechazo_codigo", request.getMotivoRechazoCodigo());
        params.put("p_comentario", request.getComentario());

        return procedureCallHelper.ejecutar("sp_trf_rechazar_transferencia", params);
    }
}
