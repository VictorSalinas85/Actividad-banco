package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.ValidarSesionRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpSecValidarSesionService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional(readOnly = true)
    public SpResultado ejecutar(ValidarSesionRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_token", request.getToken());

        return procedureCallHelper.ejecutar("sp_sec_validar_sesion", params);
    }
}
