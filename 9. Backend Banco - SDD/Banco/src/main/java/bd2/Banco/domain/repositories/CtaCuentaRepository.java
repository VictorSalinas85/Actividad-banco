package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CtaCuenta;
import bd2.Banco.domain.enums.TipoParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtaCuentaRepository extends JpaRepository<CtaCuenta, Long> {
    Optional<CtaCuenta> findByNumeroCuenta(String numeroCuenta);

    /**
     * Mayor numero de cuenta (como entero) entre las cuentas de 11 caracteres cuyo
     * numero inicia con el prefijo del anio dado. Devuelve null si no existe ninguna.
     * Se usa para asignar el consecutivo anual al abrir cuentas.
     */
    @Query(value = "SELECT MAX(CAST(numero_cuenta AS UNSIGNED)) FROM cta_cuenta " +
            "WHERE CHAR_LENGTH(numero_cuenta) = 11 AND numero_cuenta LIKE :prefijo",
            nativeQuery = true)
    Long findMaxNumeroCuentaByPrefijoAnio(@Param("prefijo") String prefijo);
    List<CtaCuenta> findByTitularPersonaId(Long titularPersonaId);
    List<CtaCuenta> findByTitularEmpresaId(Long titularEmpresaId);
    List<CtaCuenta> findByTitularTipoAndTitularPersonaId(TipoParticipante titularTipo, Long titularPersonaId);
    List<CtaCuenta> findByTitularTipoAndTitularEmpresaId(TipoParticipante titularTipo, Long titularEmpresaId);
    boolean existsByNumeroCuenta(String numeroCuenta);
}
