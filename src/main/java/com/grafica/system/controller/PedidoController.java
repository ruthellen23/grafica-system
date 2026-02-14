package com.grafica.system.controller;

import com.grafica.system.dto.PedidoRequest;
import com.grafica.system.entity.Pedido;
import com.grafica.system.enums.StatusPedido;
import com.grafica.system.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Pedidos", description = "Gerenciamento de pedidos")
@SecurityRequirement(name = "keycloak")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Pedido> criar(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaimAsString("email");
        return ResponseEntity.ok(pedidoService.criarPedido(request, email));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<Pedido>> listarTodos(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaimAsString("preferred_username");
        return ResponseEntity.ok(pedidoService.listarPedidos(username));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<Pedido> buscarPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String username = jwt.getClaimAsString("preferred_username");
        return ResponseEntity.ok(pedidoService.buscarPorId(id, username));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        StatusPedido novoStatus = StatusPedido.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }
}