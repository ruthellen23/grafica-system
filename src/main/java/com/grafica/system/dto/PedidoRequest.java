package com.grafica.system.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequest {
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    @NotEmpty(message = "Deve haver pelo menos um item no pedido")
    private List<ItemPedidoRequest> itens;
    
    private String observacoes;
}
