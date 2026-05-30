package bd2.Banco.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

/**
 * Devuelve 403 (no 401) cuando un usuario YA autenticado intenta una acción para
 * la que no tiene autoridad. Sin este handler, las denegaciones a nivel de URL
 * (authorizeHttpRequests) caían al entry point de autenticación y respondían 401,
 * lo que el frontend interpretaba como "sesión expirada".
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        mapper.writeValue(response.getWriter(), Map.of(
                "status", 403,
                "error", "Forbidden",
                "message", "No tienes permisos para esta acción.",
                "path", request.getRequestURI(),
                "timestamp", Instant.now().toString()
        ));
    }
}
