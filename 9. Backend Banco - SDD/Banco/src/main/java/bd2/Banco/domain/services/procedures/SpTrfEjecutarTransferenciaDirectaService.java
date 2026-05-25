package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.EjecutarTransferenciaDirectaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpTrfEjecutarTransferenciaDirectaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(EjecutarTransferenciaDirectaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_cuenta_origen_id", request.getCuentaOrigenId());
        params.put("p_cuenta_destino_id", request.getCuentaDestinoId());
        params.put("p_monto", request.getMonto());
        params.put("p_canal_codigo", request.getCanalCodigo());
        params.put("p_idempotency_key", request.getIdempotencyKey());
        params.put("p_usuario_id", request.getUsuarioId());

        return procedureCallHelper.ejecutar("sp_trf_ejecutar_transferencia_directa", params);
    }
}
