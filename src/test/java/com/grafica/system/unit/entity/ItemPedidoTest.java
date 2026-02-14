package com.grafica.system.unit.entity;

import com.grafica.system.entity.ItemPedido;
import com.grafica.system.entity.Pedido;
import com.grafica.system.entity.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemPedidoTest {

    @Test
    @DisplayName("Deve calcular subtotal corretamente")
    void deveCalcularSubtotal() {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Produto Teste");
        produto.setPreco(10.0);

        Pedido pedido = new Pedido();
        pedido.setId(1L);

        ItemPedido item = new ItemPedido();
        item.setProduto(produto);
        item.setPedido(pedido);
        item.setQuantidade(5);
        item.setPrecoUnitario(10.0);

        assertEquals(50.0, item.getSubtotal());
        assertEquals(5, item.getQuantidade());
        assertEquals(10.0, item.getPrecoUnitario());
    }

    @Test
    @DisplayName("Deve atualizar subtotal ao mudar quantidade")
    void deveAtualizarSubtotalAoMudarQuantidade() {
        ItemPedido item = new ItemPedido();
        item.setQuantidade(2);
        item.setPrecoUnitario(15.0);

        assertEquals(30.0, item.getSubtotal());

        item.setQuantidade(3);

        assertEquals(45.0, item.getSubtotal());
    }

    @Test
    @DisplayName("Deve atualizar subtotal ao mudar preço unitário")
    void deveAtualizarSubtotalAoMudarPreco() {
        ItemPedido item = new ItemPedido();
        item.setQuantidade(4);
        item.setPrecoUnitario(10.0);

        assertEquals(40.0, item.getSubtotal());

        item.setPrecoUnitario(12.0);

        assertEquals(48.0, item.getSubtotal());
    }
}