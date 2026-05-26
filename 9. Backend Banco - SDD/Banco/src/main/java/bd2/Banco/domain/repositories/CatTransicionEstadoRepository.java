package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatTransicionEstado;
import bd2.Banco.domain.enums.EntidadTransicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatTransicionEstadoRepository extends JpaRepository<CatTransicionEstado, Long> {
    List<CatTransicionEstado> findByEntidad(EntidadTransicion entidad);
    Optional<CatTransicionEstado> findByEntidadAndEstadoOrigenCodigoAndEstadoDestinoCodigo(
            EntidadTransicion entidad, String estadoOrigenCodigo, String estadoDestinoCodigo);
    List<CatTransicionEstado> findByEntidadAndEstadoOrigenCodigo(EntidadTransicion entidad, String estadoOrigenCodigo);
    boolean existsByEntidadAndEstadoOrigenCodigoAndEstadoDestinoCodigo(
            EntidadTransicion entidad, String estadoOrigenCodigo, String estadoDestinoCodigo);
}
