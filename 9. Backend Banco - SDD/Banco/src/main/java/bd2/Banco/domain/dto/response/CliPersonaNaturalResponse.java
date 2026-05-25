package bd2.Banco.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CliPersonaNaturalResponse {
    private Long id;
    private Long tipoIdentificacionId;
    private String identificacion;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private Long estadoId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
