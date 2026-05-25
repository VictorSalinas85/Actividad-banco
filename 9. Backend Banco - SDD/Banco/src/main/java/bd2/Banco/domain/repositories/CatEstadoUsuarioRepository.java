package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatEstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatEstadoUsuarioRepository extends JpaRepository<CatEstadoUsuario, Long> {
    Optional<CatEstadoUsuario> findByCodigo(String codigo);
    Optional<CatEstadoUsuario> findByCodigoAndActivoTrue(String codigo);
}
