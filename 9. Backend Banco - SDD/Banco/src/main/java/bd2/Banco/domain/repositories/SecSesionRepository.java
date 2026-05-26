package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.SecSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecSesionRepository extends JpaRepository<SecSesion, Long> {
    Optional<SecSesion> findByToken(String token);
    List<SecSesion> findByUsuarioId(Long usuarioId);
    List<SecSesion> findByUsuarioIdAndEstadoSesionCodigo(Long usuarioId, String codigoEstado);
    Optional<SecSesion> findByTokenAndEstadoSesionCodigo(String token, String codigoEstado);
}
