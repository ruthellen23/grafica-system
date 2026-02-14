package com.grafica.system.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemPedidoTest {
    @Test
    void deveCalcularSubtotalAutomaticamente() {
        ItemPedido item = new ItemPedido();
        item.setPrecoUnitario(25.0);
        item.setQuantidade(4);

        item.calcularSubtotal();

        assertEquals(100.0, item.getSubtotal());
    }
}