package bd2.Banco.domain.dto.request;

import bd2.Banco.domain.enums.AccionAuditoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudCambioDatoCreateRequest {
    private String tabla;
    private String registroId;
    private AccionAuditoria accion;
    private String oldData;
    private String newData;
    private String sqlUser;
    private String hostName;
    private String trxRef;
}
