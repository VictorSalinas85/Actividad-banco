package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.AudErrorOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudErrorOperacionRepository extends JpaRepository<AudErrorOperacion, Long> {
    List<AudErrorOperacion> findByOperacionOrderByFechaErrorDesc(String operacion);
    List<AudErrorOperacion> findByUsuarioIdOrderByFechaErrorDesc(Long usuarioId);
}
