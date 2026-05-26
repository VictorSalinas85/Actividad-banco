package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.TrfTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrfTransferenciaRepository extends JpaRepository<TrfTransferencia, Long> {
    List<TrfTransferencia> findByCuentaOrigenId(Long cuentaOrigenId);
    List<TrfTransferencia> findByCuentaDestinoId(Long cuentaDestinoId);
    List<TrfTransferencia> findByEmpresaId(Long empresaId);
    List<TrfTransferencia> findByEstadoId(Long estadoId);
    List<TrfTransferencia> findByCreadorUsuarioId(Long creadorUsuarioId);
}
