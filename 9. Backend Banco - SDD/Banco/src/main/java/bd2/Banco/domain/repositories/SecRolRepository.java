package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.SecRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecRolRepository extends JpaRepository<SecRol, Long> {
    Optional<SecRol> findByCodigo(String codigo);
    Optional<SecRol> findByCodigoAndActivoTrue(String codigo);
    List<SecRol> findByActivoTrue();
}
