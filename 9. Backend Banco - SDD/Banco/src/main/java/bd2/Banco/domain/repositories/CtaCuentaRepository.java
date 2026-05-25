package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.enums.TipoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtaCuentaRepository extends JpaRepository<CtaCuenta, Long> {
    Optional<CtaCuenta> findByNumeroCuenta(String numeroCuenta);
    List<CtaCuenta> findByTitularPersonaId(Long titularPersonaId);
    List<CtaCuenta> findByTitularEmpresaId(Long titularEmpresaId);
    List<CtaCuenta> findByTitularTipoAndTitularPersonaId(TipoParticipante titularTipo, Long titularPersonaId);
    List<CtaCuenta> findByTitularTipoAndTitularEmpresaId(TipoParticipante titularTipo, Long titularEmpresaId);
    boolean existsByNumeroCuenta(String numeroCuenta);
}
