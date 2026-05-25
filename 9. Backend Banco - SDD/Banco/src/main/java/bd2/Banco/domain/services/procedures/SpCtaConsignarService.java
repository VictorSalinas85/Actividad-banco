package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.ConsignarRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCtaConsignarService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(ConsignarRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_cuenta_id", request.getCuentaId());
        params.put("p_monto", request.getMonto());
        params.put("p_canal_codigo", request.getCanalCodigo());
        params.put("p_idempotency_key", request.getIdempotencyKey());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cta_consignar", params);
    }
}
