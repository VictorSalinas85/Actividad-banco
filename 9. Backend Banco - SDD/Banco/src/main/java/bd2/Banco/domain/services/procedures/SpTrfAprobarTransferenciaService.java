package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.AprobarTransferenciaRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpTrfAprobarTransferenciaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(AprobarTransferenciaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_transferencia_id", request.getTransferenciaId());
        params.put("p_aprobador_usuario_id", request.getAprobadorUsuarioId());

        return procedureCallHelper.ejecutar("sp_trf_aprobar_transferencia", params);
    }
}
