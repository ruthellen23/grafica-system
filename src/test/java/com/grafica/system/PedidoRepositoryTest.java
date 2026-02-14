package com.grafica.system.repository;

import com.grafica.system.entity.Pedido;
import com.grafica.system.enums.StatusPedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoRepositoryTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Test
    @DisplayName("Busca de pedidos por status")
    void deveBuscarPedidosPorStatus() {
        Pedido p1 = new Pedido();
        p1.setStatus(StatusPedido.PENDENTE);

        when(pedidoRepository.findByStatus(StatusPedido.PENDENTE))
                .thenReturn(List.of(p1));

        List<Pedido> resultado = pedidoRepository.findByStatus(StatusPedido.PENDENTE);

        assertEquals(1, resultado.size());
        assertEquals(StatusPedido.PENDENTE, resultado.get(0).getStatus());
    }
}