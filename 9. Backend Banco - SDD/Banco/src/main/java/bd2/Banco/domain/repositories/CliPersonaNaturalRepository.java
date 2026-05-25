package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CliPersonaNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CliPersonaNaturalRepository extends JpaRepository<CliPersonaNatural, Long> {
    Optional<CliPersonaNatural> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<CliPersonaNatural> findByEmail(String email);
    Optional<CliPersonaNatural> findBySecUsuarioId(Long usuarioId);
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByEmail(String email);
}
