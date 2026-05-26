package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CliPersonaNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CliPersonaNaturalRepository extends JpaRepository<CliPersonaNatural, Long> {
    Optional<CliPersonaNatural> findByIdentificacion(String identificacion);
    Optional<CliPersonaNatural> findByEmail(String email);
    boolean existsByIdentificacion(String identificacion);
    boolean existsByEmail(String email);
}
