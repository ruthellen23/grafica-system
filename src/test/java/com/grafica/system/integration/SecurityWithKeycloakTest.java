package com.grafica.system.integration;

import com.grafica.system.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)

class SecurityWithKeycloakTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void semAutenticacaoDeveRetornar4xx() throws Exception {
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().is4xxClientError());
    }
}