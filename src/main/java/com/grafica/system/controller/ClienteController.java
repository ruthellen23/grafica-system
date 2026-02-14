package com.grafica.system.controller;

import com.grafica.system.dto.ClienteRequest;
import com.grafica.system.entity.Cliente;
import com.grafica.system.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "keycloak")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Apenas ADMIN")
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Apenas ADMIN")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo cliente", description = "Apenas ADMIN")
    public ResponseEntity<Cliente> criar(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.criar(request));
    }

    @GetMapping("/mais-compras")
    @Operation(summary = "Listar clientes com mais compras", description = "Apenas ADMIN")
    public ResponseEntity<List<Cliente>> listarMaisCompras() {
        return ResponseEntity.ok(clienteService.listarClientesMaisCompras());
    }
}