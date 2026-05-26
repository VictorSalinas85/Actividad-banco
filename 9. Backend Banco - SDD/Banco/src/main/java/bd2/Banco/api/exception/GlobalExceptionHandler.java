package bd2.Banco.api.exception;

import bd2.Banco.api.dto.common.ApiError;
import bd2.Banco.domain.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntidadNoEncontradaException.class)
    public ResponseEntity<ApiError> handleEntidadNoEncontrada(EntidadNoEncontradaException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, "Not Found", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler({ClienteNoEncontradoException.class, CuentaNoEncontradaException.class,
                       EmpresaNoEncontradaException.class, PrestamoNoEncontradoException.class})
    public ResponseEntity<ApiError> handleDomainNotFound(DomainException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.of(404, "Not Found", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler({RegistroDuplicadoException.class, IdentificacionDuplicadaException.class})
    public ResponseEntity<ApiError> handleDuplicado(DomainException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "Conflict", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler({SaldoInsuficienteException.class, CuentaInactivaException.class,
                       TransferenciaInvalidaException.class, PrestamoNoAprobableException.class,
                       OperacionCrudNoPermitidaException.class})
    public ResponseEntity<ApiError> handleBusinessRule(DomainException ex, HttpServletRequest req) {
        return ResponseEntity.status(422)
                .body(ApiError.of(422, "Unprocessable Entity", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler(ProcedimientoBancoException.class)
    public ResponseEntity<ApiError> handleProcedimiento(ProcedimientoBancoException ex, HttpServletRequest req) {
        log.error("SP error: {} — {}", ex.getCodigoError(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Stored Procedure Error", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler(PermisoInsuficienteException.class)
    public ResponseEntity<ApiError> handlePermiso(PermisoInsuficienteException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(403, "Forbidden", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiError.of(403, "Forbidden", "No tiene permisos para realizar esta operación", req.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(401, "Unauthorized", "Credenciales incorrectas", req.getRequestURI()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiError.of(401, "Unauthorized", "Autenticación fallida", req.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.withDetails(400, "Bad Request", "Error de validación", req.getRequestURI(), details));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiError.of(409, "Conflict", "Violación de restricción de integridad de datos", req.getRequestURI()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex, HttpServletRequest req) {
        log.warn("Domain: {} — {}", ex.getCodigoError(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiError.of(400, "Bad Request", ex.getMessage(), ex.getCodigoError(), req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest req) {
        log.error("Error inesperado en {}: {}", req.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.of(500, "Internal Server Error", "Error interno del servidor", req.getRequestURI()));
    }
}
