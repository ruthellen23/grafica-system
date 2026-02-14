package com.grafica.system.service;

import com.grafica.system.entity.Pedido;
import com.grafica.system.enums.StatusPedido;
import com.grafica.system.repository.PedidoRepository;
import com.grafica.system.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private RelatorioService relatorioService;

    @Test
    @DisplayName("Deve calcular relatorio mensal corretamente quando usuario for ADMIN")
    void deveCalcularRelatorioMensalCorretamente() {
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        doReturn(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .when(auth).getAuthorities();

        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        Pedido p1 = new Pedido();
        p1.setValorTotal(200.0);
        p1.setStatus(StatusPedido.PENDENTE);

        when(pedidoRepository.findByDataPedidoBetween(any(), any())).thenReturn(List.of(p1));

        Map<String, Object> resultado = relatorioService.relatorioVendasMensais();

        assertEquals(200.0, resultado.get("totalVendas"));
        assertEquals(1L, resultado.get("quantidadePedidos"));
        assertEquals(200.0, resultado.get("ticketMedio"));
    }
}