package bd2.Banco.api.controllers.crud;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.CliPersonaNaturalCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personas")
@RequiredArgsConstructor
@Tag(name = "Personas Naturales", description = "CRUD de clientes persona natural")
@PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
public class ClientePersonaNaturalController {

    private final CliPersonaNaturalCrudService personaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CliPersonaNaturalResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(personaService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CliPersonaNaturalResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(personaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliPersonaNaturalResponse>> crear(
            @Valid @RequestBody CliPersonaNaturalCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(personaService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ANALISTA_INTERNO','EMPLEADO_VENTANILLA','EMPLEADO_COMERCIAL')")
    public ResponseEntity<ApiResponse<CliPersonaNaturalResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody CliPersonaNaturalUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(personaService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        personaService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Persona natural eliminada"));
    }
}
