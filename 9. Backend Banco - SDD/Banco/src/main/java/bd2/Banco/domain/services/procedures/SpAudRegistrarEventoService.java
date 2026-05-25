package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.RegistrarEventoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpAudRegistrarEventoService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public void ejecutar(RegistrarEventoRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_tipo_operacion_codigo", request.getTipoOperacionCodigo());
        params.put("p_id_usuario", request.getIdUsuario());
        params.put("p_rol_usuario_id", request.getRolUsuarioId());
        params.put("p_producto_tipo", request.getProductoTipo());
        params.put("p_producto_id", request.getProductoId());
        params.put("p_datos_detalle", request.getDatosDetalle());

        procedureCallHelper.ejecutarConResultado("sp_aud_registrar_evento", params);
    }
}
