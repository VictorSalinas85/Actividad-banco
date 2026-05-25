package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.AprobarPrestamoRequest;
import bd2.Banco.domain.dto.response.SpResultado;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SpCreAprobarPrestamoService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional
    public SpResultado ejecutar(AprobarPrestamoRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_prestamo_id", request.getPrestamoId());
        params.put("p_monto_aprobado", request.getMontoAprobado());
        params.put("p_tasa_interes", request.getTasaInteres());
        params.put("p_analista_usuario_id", request.getAnalistaUsuarioId());

        return procedureCallHelper.ejecutar("sp_cre_aprobar_prestamo", params);
    }
}
