package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.AsociarUsuarioEmpresaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCliAsociarUsuarioEmpresaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(AsociarUsuarioEmpresaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_empresa_id", request.getEmpresaId());
        params.put("p_usuario_id", request.getUsuarioId());
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_cli_asociar_usuario_empresa", params);
    }
}
