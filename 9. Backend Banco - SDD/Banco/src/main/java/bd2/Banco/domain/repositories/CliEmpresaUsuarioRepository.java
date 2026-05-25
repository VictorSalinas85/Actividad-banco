package bd2.Banco.domain.repositories;

import bd2.Banco.domain.entities.CliEmpresaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CliEmpresaUsuarioRepository extends JpaRepository<CliEmpresaUsuario, Long> {
    List<CliEmpresaUsuario> findByEmpresaId(Long empresaId);
    List<CliEmpresaUsuario> findByUsuarioId(Long usuarioId);
    Optional<CliEmpresaUsuario> findByEmpresaIdAndUsuarioId(Long empresaId, Long usuarioId);
    boolean existsByEmpresaIdAndUsuarioId(Long empresaId, Long usuarioId);
}
