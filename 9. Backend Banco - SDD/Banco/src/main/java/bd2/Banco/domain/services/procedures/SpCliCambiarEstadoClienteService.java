package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.CambiarEstadoClienteRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCliCambiarEstadoClienteService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(CambiarEstadoClienteRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_cliente_tipo", request.getClienteTipo());
        params.put("p_cliente_id", request.getClienteId());
        params.put("p_estado_codigo", request.getEstadoCodigo());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cli_cambiar_estado_cliente", params);
    }
}
