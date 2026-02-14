package com.grafica.system.service;

import com.grafica.system.entity.Pedido;
import com.grafica.system.entity.Produto;
import com.grafica.system.enums.StatusPedido;
import com.grafica.system.repository.PedidoRepository;
import com.grafica.system.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    private void validarAcesso() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean autorizado = auth.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN") ||
                                a.getAuthority().equals("ROLE_VENDEDOR")
                );

        if (!autorizado) {
            throw new AccessDeniedException("Acesso negado");
        }
    }

    public Map<String, Object> relatorioVendasMensais() {
        validarAcesso();

        YearMonth mesAtual = YearMonth.now();
        LocalDateTime inicioMes = mesAtual.atDay(1).atStartOfDay();
        LocalDateTime fimMes = mesAtual.atEndOfMonth().atTime(23, 59, 59);

        List<Pedido> pedidos =
                pedidoRepository.findByDataPedidoBetween(inicioMes, fimMes);

        double totalVendas = pedidos.stream()
                .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
                .mapToDouble(Pedido::getValorTotal)
                .sum();

        long quantidadePedidos = pedidos.stream()
                .filter(p -> p.getStatus() != StatusPedido.CANCELADO)
                .count();

        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("mes", mesAtual.toString());
        relatorio.put("totalVendas", totalVendas);
        relatorio.put("quantidadePedidos", quantidadePedidos);
        relatorio.put(
                "ticketMedio",
                quantidadePedidos > 0 ? totalVendas / quantidadePedidos : 0
        );

        return relatorio;
    }

    public List<Produto> produtosPopulares() {
        validarAcesso();
        return produtoRepository.findTopByTotalVendas();
    }

    public Map<String, Long> pedidosPorStatus() {
        validarAcesso();

        Map<String, Long> resultado = new HashMap<>();

        for (StatusPedido status : StatusPedido.values()) {
            Long count = pedidoRepository.countByStatus(status);
            resultado.put(status.name(), count);
        }

        return resultado;
    }
}
