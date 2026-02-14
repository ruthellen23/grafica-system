package com.grafica.system.repository;

import com.grafica.system.entity.Cliente;
import com.grafica.system.entity.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomRepositoryTest {

    @Mock
    private ClienteRepository clienteRepo;

    @Mock
    private ProdutoRepository produtoRepo;

    @Test
    @DisplayName("Cliente")
    void deveBuscarTopClientesPorCompras() {
        Cliente c1 = new Cliente();
        c1.setNome("Mock Cliente");
        c1.setTotalCompras(50);

        when(clienteRepo.findTopByTotalCompras()).thenReturn(List.of(c1));

        List<Cliente> resultado = clienteRepo.findTopByTotalCompras();
        assertEquals("Mock Cliente", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Produtos populares")
    void deveBuscarProdutosPopulares() {
        Produto p1 = new Produto();
        p1.setNome("Mock Produto");

        when(produtoRepo.findTopByTotalVendas()).thenReturn(List.of(p1));

        List<Produto> resultado = produtoRepo.findTopByTotalVendas();
        assertEquals("Mock Produto", resultado.get(0).getNome());
    }
}