package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.CrearPersonaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCliCrearPersonaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(CrearPersonaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_tipo_ident_codigo", request.getTipoIdentCodigo());
        params.put("p_identificacion", request.getIdentificacion());
        params.put("p_nombre_completo", request.getNombreCompleto());
        params.put("p_email", request.getEmail());
        params.put("p_telefono", request.getTelefono());
        params.put("p_fecha_nacimiento", request.getFechaNacimiento());
        params.put("p_direccion", request.getDireccion());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cli_crear_persona", params);
    }
}
