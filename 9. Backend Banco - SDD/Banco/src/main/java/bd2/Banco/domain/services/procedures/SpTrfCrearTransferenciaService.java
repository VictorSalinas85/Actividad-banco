package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.CrearTransferenciaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpTrfCrearTransferenciaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(CrearTransferenciaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_cuenta_origen_id", request.getCuentaOrigenId());
        params.put("p_cuenta_destino_id", request.getCuentaDestinoId());
        params.put("p_empresa_id", request.getEmpresaId());
        params.put("p_monto", request.getMonto());
        params.put("p_canal_codigo", request.getCanalCodigo());
        params.put("p_idempotency_key", request.getIdempotencyKey());
        params.put("p_creador_usuario_id", request.getCreadorUsuarioId());

        return procedureCallHelper.ejecutar("sp_trf_crear_transferencia", params);
    }
}
