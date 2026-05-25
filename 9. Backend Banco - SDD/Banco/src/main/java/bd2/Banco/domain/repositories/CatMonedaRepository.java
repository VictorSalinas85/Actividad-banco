package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatMoneda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatMonedaRepository extends JpaRepository<CatMoneda, Long> {
    Optional<CatMoneda> findByCodigo(String codigo);
    List<CatMoneda> findByActivoTrue();
}
