package bd2.Banco.api.controllers.crud;

import bd2.Banco.api.dto.common.ApiResponse;
import bd2.Banco.domain.dto.request.*;
import bd2.Banco.domain.dto.response.*;
import bd2.Banco.domain.services.crud.PrdProductoBancarioCrudService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
@Tag(name = "Productos Bancarios", description = "CRUD de productos bancarios del catálogo")
public class ProductoController {

    private final PrdProductoBancarioCrudService productoService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<List<PrdProductoBancarioResponse>>> listar() {
        return ResponseEntity.ok(ApiResponse.ok(productoService.buscarTodos()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<PrdProductoBancarioResponse>> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<PrdProductoBancarioResponse>> crear(
            @Valid @RequestBody PrdProductoBancarioCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(productoService.crear(request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<PrdProductoBancarioResponse>> actualizar(
            @PathVariable Long id, @Valid @RequestBody PrdProductoBancarioUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(productoService.actualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ANALISTA_INTERNO')")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        productoService.eliminarPorId(id);
        return ResponseEntity.ok(ApiResponse.noContent("Producto bancario eliminado"));
    }
}
