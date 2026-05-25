package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CliEmpresaUsuarioRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CliEmpresaUsuarioRolRepository extends JpaRepository<CliEmpresaUsuarioRol, Long> {
    List<CliEmpresaUsuarioRol> findByEmpresaUsuarioId(Long empresaUsuarioId);
    List<CliEmpresaUsuarioRol> findByRolEmpresaId(Long rolEmpresaId);
    Optional<CliEmpresaUsuarioRol> findByEmpresaUsuarioIdAndRolEmpresaId(Long empresaUsuarioId, Long rolEmpresaId);
    boolean existsByEmpresaUsuarioIdAndRolEmpresaId(Long empresaUsuarioId, Long rolEmpresaId);
}
