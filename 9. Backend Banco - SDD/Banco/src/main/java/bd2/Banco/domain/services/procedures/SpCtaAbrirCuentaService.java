package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.AbrirCuentaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCtaAbrirCuentaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(AbrirCuentaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_titular_tipo", request.getTitularTipo());
        params.put("p_titular_id", request.getTitularId());
        params.put("p_tipo_cuenta_codigo", request.getTipoCuentaCodigo());
        params.put("p_moneda_codigo", request.getMonedaCodigo());
        params.put("p_limite_sobregiro", request.getLimiteSobregiro());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cta_abrir_cuenta", params);
    }
}
