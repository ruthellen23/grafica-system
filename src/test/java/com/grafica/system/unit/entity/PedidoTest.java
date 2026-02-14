package com.grafica.system.unit.entity;

import com.grafica.system.entity.ItemPedido;
import com.grafica.system.entity.Pedido;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PedidoTest {

    @Test
    @DisplayName("Deve calcular o valor total do pedido baseado nos subtotais dos itens")
    void deveCalcularValorTotalCorretamente() {
        Pedido pedido = new Pedido();
        List<ItemPedido> itens = new ArrayList<>();

        ItemPedido item1 = new ItemPedido();
        item1.setSubtotal(100.0);
        itens.add(item1);

        ItemPedido item2 = new ItemPedido();
        item2.setSubtotal(50.50);
        itens.add(item2);

        pedido.setItens(itens);

        pedido.calcularValorTotal();

        assertEquals(150.50, pedido.getValorTotal(), "O valor total deve ser a soma dos subtotais");
    }
}