package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.VencerTransferenciasPendientesRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpTrfVencerTransferenciasPendientesService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(VencerTransferenciasPendientesRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_actor_usuario_id", request.getActorUsuarioId());

        return procedureCallHelper.ejecutar("sp_trf_vencer_transferencias_pendientes", params);
    }
}
