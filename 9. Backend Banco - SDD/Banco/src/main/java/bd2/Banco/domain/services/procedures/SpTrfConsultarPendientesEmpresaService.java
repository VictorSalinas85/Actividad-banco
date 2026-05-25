package bd2.Banco.domain.services.procedures;

import bd2.Banco.config.ProcedureCallHelper;
import bd2.Banco.domain.dto.request.ConsultarPendientesEmpresaRequest;
import bd2.Banco.domain.dto.response.TransferenciaPendienteResponse;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpTrfConsultarPendientesEmpresaService {

    private final ProcedureCallHelper procedureCallHelper;

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<TransferenciaPendienteResponse> ejecutar(ConsultarPendientesEmpresaRequest request) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("p_empresa_id", request.getEmpresaId());

        StoredProcedureQuery query = procedureCallHelper.ejecutarConResultado(
                "sp_trf_consultar_pendientes_aprobacion_empresa", params);

        List<Object[]> rows = query.getResultList();

        return rows.stream().map(row -> TransferenciaPendienteResponse.builder()
                .id(toLong(row[0]))
                .cuentaOrigenId(toLong(row[1]))
                .cuentaDestinoId(toLong(row[2]))
                .monto(row[3] != null ? (BigDecimal) row[3] : null)
                .fechaCreacion(row[4] != null ? ((java.sql.Timestamp) row[4]).toLocalDateTime() : null)
                .creadoPor(row[5] != null ? row[5].toString() : null)
                .build()
        ).collect(Collectors.toList());
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long l) return l;
        if (value instanceof Number n) return n.longValue();
        return Long.parseLong(value.toString());
    }
}
