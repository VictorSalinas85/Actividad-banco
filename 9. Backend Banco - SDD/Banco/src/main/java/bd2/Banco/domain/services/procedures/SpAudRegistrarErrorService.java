package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.RegistrarErrorRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpAudRegistrarErrorService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public void ejecutar(RegistrarErrorRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_codigo_error", request.getCodigoError());
        params.put("p_modulo", request.getModulo());
        params.put("p_mensaje", request.getMensaje());
        params.put("p_referencia", request.getReferencia());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());
        params.put("p_payload", request.getPayload());

        procedureCallHelper.ejecutarConResultado("sp_aud_registrar_error", params);
    }
}
