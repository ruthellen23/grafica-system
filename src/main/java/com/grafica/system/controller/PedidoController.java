package com.grafica.system.controller;

import com.grafica.system.dto.PedidoRequest;
import com.grafica.system.entity.Pedido;
import com.grafica.system.entity.Usuario;
import com.grafica.system.enums.StatusPedido;
import com.grafica.system.service.PedidoService;
import com.grafica.system.service.UsuarioSincronizacaoService;
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
    private final UsuarioSincronizacaoService usuarioSincronizacaoService;

    @PostMapping
    public ResponseEntity<Pedido> criar(
            @Valid @RequestBody PedidoRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        Usuario usuario = usuarioSincronizacaoService.sincronizarUsuario(jwt);
        return ResponseEntity.ok(pedidoService.criarPedido(request, usuario.getEmail()));
    }

    @GetMapping
    @Operation(summary = "Listar pedidos")
    public ResponseEntity<List<Pedido>> listarTodos(@AuthenticationPrincipal Jwt jwt) {
        Usuario usuario = usuarioSincronizacaoService.sincronizarUsuario(jwt);

        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        boolean isAdmin = realmAccess != null && realmAccess.get("roles").toString().contains("ADMIN");

        if (isAdmin) {
            return ResponseEntity.ok(pedidoService.listarTodos());
        }

        return ResponseEntity.ok(pedidoService.listarPedidos(usuario.getEmail()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        Usuario usuario = usuarioSincronizacaoService.sincronizarUsuario(jwt);
        return ResponseEntity.ok(pedidoService.buscarPorId(id, usuario.getEmail()));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        StatusPedido novoStatus = StatusPedido.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, novoStatus));
    }
}