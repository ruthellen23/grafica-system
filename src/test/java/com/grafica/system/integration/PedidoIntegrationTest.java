package com.grafica.system.integration;

import com.grafica.system.config.TestSecurityConfig;
import com.grafica.system.entity.Cliente;
import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.TipoUsuario;
import com.grafica.system.repository.ClienteRepository; // Importante
import com.grafica.system.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
class PedidoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setup() {
        clienteRepository.deleteAll();
        usuarioRepository.deleteAll();

        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setUsername("admin");
        admin.setEmail("admin@grafica.com");
        admin.setKeycloakId("admin-uuid-test");
        admin.setAtivo(true);
        admin.setTipo(TipoUsuario.ADMIN);
        admin.setDataCadastro(LocalDateTime.now());
        usuarioRepository.save(admin);

        Cliente cliente = new Cliente();
        cliente.setNome("Ruth Ellen");
        cliente.setEmail("ruth@example.com");
        cliente.setCpfCnpj("12345678901");
        cliente.setDataCadastro(LocalDateTime.now());
        clienteRepository.save(cliente);
    }

    @Test
    void deveRetornarListaDePedidos() throws Exception {
        mockMvc.perform(get("/pedidos")
                        .with(jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))
                                .jwt(builder -> builder
                                        .claim("sub", "ID-DO-KEYCLOAK-AQUI")
                                        .claim("name", "Ruth Ellen")
                                        .claim("preferred_username", "ruth.ellen")
                                        .claim("email", "ruth@example.com")
                                        .claim("realm_access", Map.of("roles", List.of("ADMIN")))
                                )))
                .andExpect(status().isOk());
    }
}