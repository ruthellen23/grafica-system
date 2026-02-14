package com.grafica.system.controller;

import com.grafica.system.entity.Produto;
import com.grafica.system.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Relatórios", description = "Relatórios gerenciais")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "keycloak")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/vendas-mensais")
    @Operation(summary = "Relatório de vendas mensais", description = "Apenas ADMIN")
    public ResponseEntity<Map<String, Object>> relatorioVendasMensais() {
        return ResponseEntity.ok(relatorioService.relatorioVendasMensais());
    }

    @GetMapping("/populares")
    @Operation(summary = "Produtos mais populares", description = "Apenas ADMIN")
    public ResponseEntity<List<Produto>> produtosPopulares() {
        return ResponseEntity.ok(relatorioService.produtosPopulares());
    }

    @GetMapping("/pedidos-por-status")
    @Operation(summary = "Quantidade de pedidos por status", description = "Apenas ADMIN")
    public ResponseEntity<Map<String, Long>> pedidosPorStatus() {
        return ResponseEntity.ok(relatorioService.pedidosPorStatus());
    }
}