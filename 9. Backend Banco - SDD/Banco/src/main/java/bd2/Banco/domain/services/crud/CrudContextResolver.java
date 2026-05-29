package bd2.Banco.domain.services.crud;

import bd2.Banco.domain.entities.CatEstadoUsuario;
import bd2.Banco.domain.repositories.CatEstadoUsuarioRepository;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Resuelve los campos que el frontend no envia explicitamente al crear
 * registros: el actor autenticado (created_by/updated_by) y el estado por
 * defecto ACTIVO. Mantiene los formularios alineados con la regla de UX
 * "no se piden IDs internos al usuario".
 */
@Component
@RequiredArgsConstructor
public class CrudContextResolver {

    private final SecUsuarioRepository secUsuarioRepository;
    private final CatEstadoUsuarioRepository catEstadoUsuarioRepository;

    /** Id del usuario autenticado; si no se puede resolver, retorna 0 (sistema). */
    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return 0L;
        }
        Object principal = auth.getPrincipal();
        String username = (principal instanceof UserDetails ud) ? ud.getUsername() : principal.toString();
        if (username == null || username.isBlank() || "anonymousUser".equals(username)) {
            return 0L;
        }
        return secUsuarioRepository.findByUsernameOrEmail(username, username)
                .map(u -> u.getId())
                .orElse(0L);
    }

    /** Mantiene el valor recibido o cae al actor autenticado. */
    public Long resolveActor(Long explicitId) {
        return explicitId != null ? explicitId : currentUserId();
    }

    /** Devuelve el id del estado ACTIVO de cat_estado_usuario para defaults. */
    public Long defaultEstadoUsuarioId() {
        return catEstadoUsuarioRepository.findByCodigoAndActivoTrue("ACTIVO")
                .or(() -> catEstadoUsuarioRepository.findByCodigo("ACTIVO"))
                .map(CatEstadoUsuario::getId)
                .orElseThrow(() -> new IllegalStateException(
                        "Catalogo cat_estado_usuario sin codigo 'ACTIVO' activo"));
    }

    /** Mantiene el valor recibido o cae al estado ACTIVO. */
    public Long resolveEstadoUsuarioId(Long explicitId) {
        return explicitId != null ? explicitId : defaultEstadoUsuarioId();
    }
}
