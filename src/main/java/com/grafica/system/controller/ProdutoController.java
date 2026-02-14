package com.grafica.system.controller;

import com.grafica.system.dto.ProdutoRequest;
import com.grafica.system.entity.Categoria;
import com.grafica.system.entity.Produto;
import com.grafica.system.service.ProdutoService;
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
@RequestMapping("/produtos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Endpoint público")
    public ResponseEntity<List<Produto>> listarTodos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Endpoint público")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoService.buscarPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Criar novo produto", description = "Apenas ADMIN")
    public ResponseEntity<Produto> criar(@Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(produtoService.criar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Atualizar produto", description = "Apenas ADMIN")
    public ResponseEntity<Produto> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        return ResponseEntity.ok(produtoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "keycloak")
    @Operation(summary = "Deletar produto", description = "Apenas ADMIN")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Categorias", description = "Gerenciamento de categorias")
class CategoriaController {

    private final ProdutoService produtoService;

    @GetMapping
    @Operation(summary = "Listar todas as categorias", description = "Endpoint público")
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(produtoService.listarCategorias());
    }
}