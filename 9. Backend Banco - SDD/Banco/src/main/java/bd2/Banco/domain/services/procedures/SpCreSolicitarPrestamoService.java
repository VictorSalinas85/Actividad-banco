package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.SolicitarPrestamoRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCreSolicitarPrestamoService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(SolicitarPrestamoRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_cliente_tipo", request.getClienteTipo());
        params.put("p_cliente_id", request.getClienteId());
        params.put("p_tipo_prestamo_codigo", request.getTipoPrestamoCodigo());
        params.put("p_monto_solicitado", request.getMontoSolicitado());
        params.put("p_plazo_meses", request.getPlazoMeses());
        params.put("p_cuenta_destino_id", request.getCuentaDestinoId());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cre_solicitar_prestamo", params);
    }
}
