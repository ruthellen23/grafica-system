package com.grafica.system;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityFullTest {
    @Autowired private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {"/usuarios", "/relatorios/vendas-mensais", "/clientes", "/pedidos"})
    void endpointsPrivadosDevemRetornar4xxSemToken(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().is4xxClientError());
    }
}