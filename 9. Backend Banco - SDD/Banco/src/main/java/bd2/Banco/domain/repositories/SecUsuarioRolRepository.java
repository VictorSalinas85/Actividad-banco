package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.SecUsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecUsuarioRolRepository extends JpaRepository<SecUsuarioRol, Long> {
    List<SecUsuarioRol> findByUsuarioId(Long usuarioId);
    List<SecUsuarioRol> findByRolId(Long rolId);
    Optional<SecUsuarioRol> findByUsuarioIdAndRolId(Long usuarioId, Long rolId);
    boolean existsByUsuarioIdAndRolId(Long usuarioId, Long rolId);
}
