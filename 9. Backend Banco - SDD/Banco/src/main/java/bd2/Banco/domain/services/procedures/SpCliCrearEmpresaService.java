package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.CrearEmpresaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCliCrearEmpresaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(CrearEmpresaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_tipo_ident_codigo", request.getTipoIdentCodigo());
        params.put("p_nit", request.getNit());
        params.put("p_razon_social", request.getRazonSocial());
        params.put("p_email", request.getEmail());
        params.put("p_telefono", request.getTelefono());
        params.put("p_direccion", request.getDireccion());
        params.put("p_representante_persona_id", request.getRepresentantePersonaId());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cli_crear_empresa", params);
    }
}
