package bd2.Banco.security;

import bd2.Banco.domain.entities.SecUsuario;
import bd2.Banco.domain.repositories.SecUsuarioRepository;
import bd2.Banco.domain.repositories.SecUsuarioRolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SecUsuarioRepository usuarioRepository;
    private final SecUsuarioRolRepository usuarioRolRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecUsuario usuario = usuarioRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        List<GrantedAuthority> authorities = usuarioRolRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(ur -> new SimpleGrantedAuthority(ur.getRol().getCodigo()))
                .collect(Collectors.toList());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getHashPassword())
                .authorities(authorities)
                .build();
    }

    @Transactional(readOnly = true)
    public SecUsuario loadSecUsuario(String username) {
        return usuarioRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
