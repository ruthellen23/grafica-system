package com.grafica.system.service;

import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioSincronizacaoService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario sincronizarUsuario(Jwt jwt) {
        String keycloakId = jwt.getSubject();

        return usuarioRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setKeycloakId(keycloakId);
                    novoUsuario.setNome(jwt.getClaimAsString("name"));
                    novoUsuario.setEmail(jwt.getClaimAsString("email"));

                    String username = jwt.getClaimAsString("preferred_username");
                    novoUsuario.setUsername(username != null ? username : jwt.getClaimAsString("email"));

                    novoUsuario.setAtivo(true);

                    Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
                    if (realmAccess != null && realmAccess.containsKey("roles")) {
                        List<String> roles = (List<String>) realmAccess.get("roles");
                        if (roles.contains("ADMIN")) {
                            novoUsuario.setTipo(TipoUsuario.ADMIN);
                        } else {
                            novoUsuario.setTipo(TipoUsuario.CLIENTE);
                        }
                    } else {
                        novoUsuario.setTipo(TipoUsuario.CLIENTE);
                    }

                    return usuarioRepository.save(novoUsuario);
                });
    }
}