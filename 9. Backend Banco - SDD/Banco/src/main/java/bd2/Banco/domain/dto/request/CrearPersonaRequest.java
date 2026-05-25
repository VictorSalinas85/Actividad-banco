package bd2.Banco.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class CrearPersonaRequest {
    private String tipoIdentCodigo;
    private String identificacion;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private Long actorUsuarioId;
}
