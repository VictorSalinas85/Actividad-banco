package bd2.Banco.domain.dto.response;

import bd2.Banco.domain.enums.AccionAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudCambioDatoResponse {
    private Long id;
    private String tabla;
    private String registroId;
    private AccionAuditoria accion;
    private String oldData;
    private String newData;
    private String sqlUser;
    private String hostName;
    private String trxRef;
    private LocalDateTime changedAt;
}
