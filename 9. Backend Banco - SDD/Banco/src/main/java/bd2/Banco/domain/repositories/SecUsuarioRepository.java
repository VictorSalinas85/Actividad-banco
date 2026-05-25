package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.SecUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecUsuarioRepository extends JpaRepository<SecUsuario, Long> {
    Optional<SecUsuario> findByUsername(String username);
    Optional<SecUsuario> findByEmail(String email);
    Optional<SecUsuario> findByUsernameOrEmail(String username, String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
