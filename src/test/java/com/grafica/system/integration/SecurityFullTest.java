package com.grafica.system.integration;

import com.grafica.system.config.TestSecurityConfig;
import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Adicionado
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SecurityFullTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNome("Admin Teste");
        admin.setUsername("admin");
        admin.setEmail("admin@teste.com");
        admin.setKeycloakId("uuid-admin-teste");
        admin.setTipo(TipoUsuario.ADMIN);
        admin.setAtivo(true);
        admin.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(admin);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/usuarios", "/clientes", "/pedidos"})
    void endpointsProtegidosDevemEstarAcessiveisComUsuarioNoBanco(String url) throws Exception {
        mockMvc.perform(get(url)
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder
                                        .claim("sub", "uuid-admin-teste")
                                        .claim("name", "Admin Teste")
                                        .claim("preferred_username", "admin")
                                        .claim("email", "admin@teste.com")
                                        .claim("realm_access", Map.of("roles", List.of("ADMIN")))
                                )))
                .andExpect(status().isOk());
    }

    @Test
    void endpointsPublicosDevemEstarAcessiveis() throws Exception {
        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveListarUsuariosComoAdmin() throws Exception {
        mockMvc.perform(get("/usuarios")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(j -> j
                                        .claim("sub", "uuid-admin-teste")
                                        .claim("name", "Admin User")
                                        .claim("preferred_username", "admin"))))
                .andExpect(status().isOk());
    }
}