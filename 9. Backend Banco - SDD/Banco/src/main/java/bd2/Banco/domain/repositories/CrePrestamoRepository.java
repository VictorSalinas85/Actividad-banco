package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CrePrestamo;
import bd2.Banco.domain.enums.TipoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrePrestamoRepository extends JpaRepository<CrePrestamo, Long> {
    List<CrePrestamo> findByClientePersonaId(Long clientePersonaId);
    List<CrePrestamo> findByClienteEmpresaId(Long clienteEmpresaId);
    List<CrePrestamo> findByClienteTipoAndClientePersonaId(TipoParticipante clienteTipo, Long clientePersonaId);
    List<CrePrestamo> findByClienteTipoAndClienteEmpresaId(TipoParticipante clienteTipo, Long clienteEmpresaId);
    List<CrePrestamo> findByCatEstadoPrestamoId(Long estadoPrestamoId);
}
