package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CliEmpresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CliEmpresaRepository extends JpaRepository<CliEmpresa, Long> {
    Optional<CliEmpresa> findByRuc(String ruc);
    Optional<CliEmpresa> findBySecUsuarioId(Long usuarioId);
    boolean existsByRuc(String ruc);
}
