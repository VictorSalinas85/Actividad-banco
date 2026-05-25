package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CatRolEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CatRolEmpresaRepository extends JpaRepository<CatRolEmpresa, Long> {
    Optional<CatRolEmpresa> findByCodigo(String codigo);
    List<CatRolEmpresa> findByActivoTrue();
}
