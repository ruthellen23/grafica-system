package com.grafica.system.integration;

import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PedidoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setUsername("admin");
        admin.setEmail("admin@grafica.com");
        admin.setSenha("123456");
        admin.setTipo(TipoUsuario.ADMIN);
        admin.setAtivo(true);
        admin.setDataCadastro(LocalDateTime.now());

        usuarioRepository.save(admin);
    }

    @Test
    @DisplayName("GET /pedidos deve retornar 200 OK")
    void deveRetornarListaDePedidos() throws Exception {
        mockMvc.perform(get("/pedidos")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder.claim("preferred_username", "admin"))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /pedidos/1 deve retornar 404 se não existir")
    void deveBuscarPedidoPorId() throws Exception {
        mockMvc.perform(get("/pedidos/1")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder.claim("preferred_username", "admin"))))
                .andExpect(status().isNotFound());
    }
}