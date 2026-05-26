package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.AudCambioDato;
import bd2.Banco.domain.enums.AccionAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudCambioDatoRepository extends JpaRepository<AudCambioDato, Long> {
    List<AudCambioDato> findByTablaAndRegistroIdOrderByChangedAtDesc(String tabla, String registroId);
    List<AudCambioDato> findByAccionAndTablaOrderByChangedAtDesc(AccionAuditoria accion, String tabla);
    List<AudCambioDato> findByTablaOrderByChangedAtDesc(String tabla);
}
